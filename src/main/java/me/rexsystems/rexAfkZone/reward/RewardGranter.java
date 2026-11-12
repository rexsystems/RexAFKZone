package me.rexsystems.rexAfkZone.reward;

import me.rexsystems.rexAfkZone.RexAFKZone;
import me.rexsystems.rexAfkZone.util.MessageService;
import me.rexsystems.rexAfkZone.util.Placeholders;
import me.rexsystems.rexAfkZone.util.SchedulerUtils;
import me.rexsystems.rexAfkZone.util.TextParser;
import me.rexsystems.rexAfkZone.zone.Reward;
import me.rexsystems.rexAfkZone.zone.RewardItem;
import me.rexsystems.rexAfkZone.zone.Zone;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RewardGranter {

    private static final int MAX_ITEMS = 2304;

    private final RexAFKZone plugin;

    public RewardGranter(RexAFKZone plugin) {
        this.plugin = plugin;
    }

    public void grant(Player player, Zone zone, List<Reward> rewards, Map<String, String> tokens) {
        if (rewards == null || rewards.isEmpty()) {
            return;
        }
        boolean gave = false;
        for (Reward reward : rewards) {
            if (giveItems(player, reward, tokens)) {
                gave = true;
            }
            if (runCommands(player, reward, tokens)) {
                gave = true;
            }
            if (zone.chat()) {
                String template = reward.message().isBlank()
                        ? plugin.getConfigManager().prefixed("reward")
                        : reward.message();
                template = template.replace("{prefix}", plugin.getConfigManager().prefix())
                        .replace("<prefix>", plugin.getConfigManager().prefix());
                if (!template.isBlank()) {
                    MessageService.send(player, Placeholders.apply(player, TextParser.apply(template, tokens)));
                    gave = true;
                }
            }
        }
        if (gave) {
            plugin.sounds().play(player, plugin.getConfigManager().rewardSound());
        }
    }

    private boolean giveItems(Player player, Reward reward, Map<String, String> tokens) {
        boolean gave = false;
        for (RewardItem spec : reward.items()) {
            Material material = Material.matchMaterial(spec.material());
            if (material == null || !material.isItem() || material.isAir()) {
                plugin.getLogger().warning("Skipped reward item with material " + spec.material());
                continue;
            }
            int amount = spec.amount() <= 0 ? 1 : Math.min(spec.amount(), MAX_ITEMS);
            int max = Math.max(1, material.getMaxStackSize());
            int left = amount;
            while (left > 0) {
                int size = Math.min(max, left);
                ItemStack stack = new ItemStack(material, size);
                ItemMeta meta = stack.getItemMeta();
                if (!spec.name().isBlank()) {
                    meta.displayName(component(player, spec.name(), tokens));
                }
                if (!spec.lore().isEmpty()) {
                    List<Component> lore = new ArrayList<>();
                    for (String line : spec.lore()) {
                        lore.add(component(player, line, tokens));
                    }
                    meta.lore(lore);
                }
                if (spec.customModelData() > 0) {
                    meta.setCustomModelData(spec.customModelData());
                }
                stack.setItemMeta(meta);
                for (ItemStack extra : player.getInventory().addItem(stack).values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), extra);
                }
                left -= size;
                gave = true;
            }
        }
        return gave;
    }

    private boolean runCommands(Player player, Reward reward, Map<String, String> tokens) {
        boolean ran = false;
        for (String command : reward.commands()) {
            if (command == null || command.isBlank()) {
                continue;
            }
            String parsed = command.startsWith("/") ? command.substring(1) : command;
            for (Map.Entry<String, String> entry : tokens.entrySet()) {
                String key = entry.getKey();
                if (key == null || key.isEmpty()) {
                    continue;
                }
                String value = entry.getValue() == null ? "" : entry.getValue();
                parsed = parsed.replace("{" + key + "}", value);
            }
            String dispatch = Placeholders.apply(player, parsed);
            SchedulerUtils.runSync(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), dispatch));
            ran = true;
        }
        return ran;
    }

    private Component component(Player player, String template, Map<String, String> tokens) {
        return TextParser.parse(Placeholders.apply(player, TextParser.apply(template, tokens)));
    }
}
