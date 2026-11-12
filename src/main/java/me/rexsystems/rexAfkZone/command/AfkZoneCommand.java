package me.rexsystems.rexAfkZone.command;

import me.rexsystems.rexAfkZone.RexAFKZone;
import me.rexsystems.rexAfkZone.util.MessageService;
import me.rexsystems.rexAfkZone.wand.WandItem;
import me.rexsystems.rexAfkZone.zone.Selections;
import me.rexsystems.rexAfkZone.zone.Zone;
import me.rexsystems.rexAfkZone.zone.ZonePicker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class AfkZoneCommand implements CommandExecutor, TabCompleter {

    private final RexAFKZone plugin;

    public AfkZoneCommand(RexAFKZone plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            return help(sender);
        }
        return switch (args[0].toLowerCase(Locale.ROOT)) {
            case "wand" -> wand(sender);
            case "create" -> create(sender, args);
            case "delete" -> delete(sender, args);
            case "list" -> list(sender);
            case "info" -> info(sender, args);
            case "reload" -> reload(sender);
            default -> help(sender);
        };
    }

    private boolean help(CommandSender sender) {
        MessageService.send(sender, plugin.getConfigManager().prefixed("help-header"));
        for (String line : plugin.getConfigManager().helpLines()) {
            MessageService.send(sender, line);
        }
        return true;
    }

    private boolean wand(CommandSender sender) {
        if (!sender.hasPermission("rexafkzone.select")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        if (!(sender instanceof Player player)) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("players-only"));
            return true;
        }
        new WandItem(plugin).give(player);
        return true;
    }

    private boolean create(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rexafkzone.admin")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        if (!(sender instanceof Player player)) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("players-only"));
            return true;
        }
        if (args.length < 2) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("usage-create"));
            return true;
        }
        String name = args[1];
        if (!name.matches("[A-Za-z0-9_]{1,32}")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("name-invalid"));
            return true;
        }
        if (plugin.getZoneStore().find(name) != null) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("zone-exists"), Map.of("zone", name));
            return true;
        }
        Selections.Point first = plugin.getSelections().pos1(player.getUniqueId());
        Selections.Point second = plugin.getSelections().pos2(player.getUniqueId());
        if (first == null || second == null) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("selection-missing"));
            return true;
        }
        if (!first.world().equalsIgnoreCase(second.world())) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("selection-worlds"));
            return true;
        }
        Zone zone = new Zone(
                name,
                first.world(),
                first.x(),
                first.y(),
                first.z(),
                second.x(),
                second.y(),
                second.z(),
                plugin.getConfigManager().defaultPriority(),
                plugin.getZoneStore().nextCreatedOrder(),
                plugin.getConfigManager().defaultInterval(),
                null,
                plugin.getConfigManager().defaultBossbar(),
                plugin.getConfigManager().defaultActionbar(),
                plugin.getConfigManager().defaultChat(),
                plugin.getConfigManager().defaultRewards()
        );
        if (!plugin.getZoneStore().add(zone)) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("save-failed"));
            return true;
        }
        MessageService.send(sender, plugin.getConfigManager().prefixed("zone-created"), Map.of(
                "zone", zone.name(),
                "world", zone.world(),
                "min", zone.minX() + " " + zone.minY() + " " + zone.minZ(),
                "max", zone.maxX() + " " + zone.maxY() + " " + zone.maxZ()
        ));
        return true;
    }

    private boolean delete(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rexafkzone.admin")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        if (args.length < 2) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("usage-delete"));
            return true;
        }
        Zone zone = plugin.getZoneStore().find(args[1]);
        if (zone == null) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("zone-missing"), Map.of("zone", args[1]));
            return true;
        }
        if (!plugin.getZoneStore().remove(zone.name())) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("save-failed"));
            return true;
        }
        MessageService.send(sender, plugin.getConfigManager().prefixed("zone-deleted"), Map.of("zone", zone.name()));
        return true;
    }

    private boolean list(CommandSender sender) {
        if (!sender.hasPermission("rexafkzone.admin")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        List<Zone> zones = plugin.getZoneStore().all();
        if (zones.isEmpty()) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("list-empty"));
            return true;
        }
        MessageService.send(sender, plugin.getConfigManager().prefixed("list-header"), Map.of("count", Integer.toString(zones.size())));
        for (Zone zone : zones) {
            MessageService.send(sender, plugin.getConfigManager().message("list-line"), Map.of(
                    "zone", zone.name(),
                    "world", zone.world(),
                    "priority", Integer.toString(zone.priority())
            ));
        }
        return true;
    }

    private boolean info(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rexafkzone.admin")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        Zone zone;
        if (args.length >= 2) {
            zone = plugin.getZoneStore().find(args[1]);
            if (zone == null) {
                MessageService.send(sender, plugin.getConfigManager().prefixed("zone-missing"), Map.of("zone", args[1]));
                return true;
            }
        } else if (sender instanceof Player player) {
            zone = zoneAt(player);
            if (zone == null) {
                MessageService.send(sender, plugin.getConfigManager().prefixed("not-in-zone"));
                return true;
            }
        } else {
            MessageService.send(sender, plugin.getConfigManager().prefixed("usage-info"));
            return true;
        }
        MessageService.send(sender, plugin.getConfigManager().prefixed("info-header"), Map.of("zone", zone.name()));
        line(sender, "World", zone.world());
        line(sender, "Bounds", zone.minX() + " " + zone.minY() + " " + zone.minZ()
                + " to " + zone.maxX() + " " + zone.maxY() + " " + zone.maxZ());
        line(sender, "Priority", Integer.toString(zone.priority()));
        line(sender, "Interval", zone.intervalSeconds() + "s");
        line(sender, "Rolls", zone.rolls() == null
                ? "settings (" + plugin.getConfigManager().rolls() + ")"
                : Integer.toString(zone.rolls()));
        line(sender, "Boss bar", Boolean.toString(zone.bossbar()));
        line(sender, "Action bar", Boolean.toString(zone.actionbar()));
        line(sender, "Chat", Boolean.toString(zone.chat()));
        line(sender, "Rewards", Integer.toString(zone.rewards().size()));
        return true;
    }

    private boolean reload(CommandSender sender) {
        if (!sender.hasPermission("rexafkzone.reload")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        plugin.reloadPlugin();
        MessageService.send(sender, plugin.getConfigManager().prefixed("reload"));
        return true;
    }

    private void line(CommandSender sender, String key, String value) {
        MessageService.send(sender, plugin.getConfigManager().message("info-line"), Map.of("key", key, "value", value));
    }

    private Zone zoneAt(Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world == null) {
            return null;
        }
        return ZonePicker.pick(plugin.getZoneStore().all(), world.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("help");
            if (sender.hasPermission("rexafkzone.select")) {
                options.add("wand");
            }
            if (sender.hasPermission("rexafkzone.admin")) {
                options.add("create");
                options.add("delete");
                options.add("list");
                options.add("info");
            }
            if (sender.hasPermission("rexafkzone.reload")) {
                options.add("reload");
            }
            return filter(options, args[0]);
        }
        if (args.length == 2 && sender.hasPermission("rexafkzone.admin")
                && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("info"))) {
            List<String> names = new ArrayList<>();
            for (Zone zone : plugin.getZoneStore().all()) {
                names.add(zone.name());
            }
            return filter(names, args[1]);
        }
        return List.of();
    }

    private static List<String> filter(List<String> options, String prefix) {
        String needle = prefix == null ? "" : prefix.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(needle)) {
                matches.add(option);
            }
        }
        return matches;
    }
}
