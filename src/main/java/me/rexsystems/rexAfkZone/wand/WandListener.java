package me.rexsystems.rexAfkZone.wand;

import me.rexsystems.rexAfkZone.RexAFKZone;
import me.rexsystems.rexAfkZone.util.MessageService;
import me.rexsystems.rexAfkZone.zone.Selections;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

public final class WandListener implements Listener {

    private final RexAFKZone plugin;
    private final WandItem wandItem;

    public WandListener(RexAFKZone plugin) {
        this.plugin = plugin;
        this.wandItem = new WandItem(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        boolean left = action == Action.LEFT_CLICK_BLOCK;
        boolean right = action == Action.RIGHT_CLICK_BLOCK;
        if (!left && !right) {
            return;
        }
        Player player = event.getPlayer();
        if (!wandItem.isWand(player.getInventory().getItemInMainHand())) {
            return;
        }
        if (!player.hasPermission("rexafkzone.select")) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null || block.getWorld() == null) {
            return;
        }
        // Paper runs this event for each hand; leave the off-hand copy cancelled or the block still opens.
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        if (event.getHand() != null && event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Selections.Point point = new Selections.Point(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
        String path = left ? "pos1-set" : "pos2-set";
        if (left) {
            plugin.getSelections().setPos1(player.getUniqueId(), point);
        } else {
            plugin.getSelections().setPos2(player.getUniqueId(), point);
        }
        MessageService.send(player, plugin.getConfigManager().prefixed(path), Map.of(
                "x", Integer.toString(point.x()),
                "y", Integer.toString(point.y()),
                "z", Integer.toString(point.z()),
                "world", point.world()
        ));
        plugin.sounds().play(player, plugin.getConfigManager().selectSound());
    }
}
