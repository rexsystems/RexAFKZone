package me.rexsystems.rexAfkZone.wand;

import me.rexsystems.rexAfkZone.RexAFKZone;
import me.rexsystems.rexAfkZone.util.MessageService;
import me.rexsystems.rexAfkZone.util.Placeholders;
import me.rexsystems.rexAfkZone.util.TextParser;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class WandItem {

    private final RexAFKZone plugin;

    public WandItem(RexAFKZone plugin) {
        this.plugin = plugin;
    }

    public ItemStack create(Player player) {
        Material material = Material.matchMaterial(plugin.getConfigManager().wandMaterial());
        if (material == null || !material.isItem() || material.isAir()) {
            return null;
        }
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String name = plugin.getConfigManager().wandName();
        if (name != null && !name.isBlank()) {
            meta.displayName(TextParser.parse(Placeholders.apply(player, name)));
        }
        List<Component> lore = new ArrayList<>();
        for (String line : plugin.getConfigManager().wandLore()) {
            if (line == null || line.isBlank()) {
                continue;
            }
            lore.add(TextParser.parse(Placeholders.apply(player, line)));
        }
        if (!lore.isEmpty()) {
            meta.lore(lore);
        }
        meta.getPersistentDataContainer().set(key(), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isWand(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return false;
        }
        Byte mark = item.getItemMeta().getPersistentDataContainer().get(key(), PersistentDataType.BYTE);
        return mark != null && mark == (byte) 1;
    }

    public void give(Player player) {
        ItemStack item = create(player);
        if (item == null) {
            MessageService.send(player, plugin.getConfigManager().prefixed("wand-invalid"));
            return;
        }
        PlayerInventory inventory = player.getInventory();
        Map<Integer, ItemStack> leftover = inventory.addItem(item);
        for (ItemStack extra : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), extra);
        }
        MessageService.send(player, plugin.getConfigManager().prefixed("wand-given"));
    }

    private NamespacedKey key() {
        return plugin.wandKey();
    }
}
