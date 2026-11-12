package me.rexsystems.rexAfkZone.zone;

import me.rexsystems.rexAfkZone.RexAFKZone;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ZoneStore {

    private final RexAFKZone plugin;
    private final File file;
    private volatile List<Zone> zones = List.of();

    public ZoneStore(RexAFKZone plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "zones.yml");
    }

    public List<Zone> all() {
        return zones;
    }

    public Zone find(String name) {
        if (name == null) {
            return null;
        }
        for (Zone zone : zones) {
            if (zone.name().equalsIgnoreCase(name)) {
                return zone;
            }
        }
        return null;
    }

    public void load() {
        try {
            ensureFile();
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            this.zones = List.copyOf(parse(yaml));
        } catch (Exception failure) {
            plugin.getLogger().warning("Could not read zones.yml: " + failure.getMessage());
        }
    }

    public boolean add(Zone zone) {
        try {
            ensureFile();
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            String path = "zones." + zone.name();
            yaml.set(path + ".world", zone.world());
            yaml.set(path + ".min.x", zone.minX());
            yaml.set(path + ".min.y", zone.minY());
            yaml.set(path + ".min.z", zone.minZ());
            yaml.set(path + ".max.x", zone.maxX());
            yaml.set(path + ".max.y", zone.maxY());
            yaml.set(path + ".max.z", zone.maxZ());
            yaml.set(path + ".priority", zone.priority());
            yaml.set(path + ".created", zone.createdOrder());
            yaml.set(path + ".interval-seconds", zone.intervalSeconds());
            if (zone.rolls() != null) {
                yaml.set(path + ".rolls", zone.rolls());
            }
            yaml.set(path + ".bossbar", zone.bossbar());
            yaml.set(path + ".actionbar", zone.actionbar());
            yaml.set(path + ".chat", zone.chat());
            yaml.set(path + ".rewards", RewardLists.toMaps(zone.rewards()));
            yaml.save(file);
            this.zones = List.copyOf(parse(yaml));
            return true;
        } catch (Exception failure) {
            plugin.getLogger().warning("Could not save zones.yml: " + failure.getMessage());
            return false;
        }
    }

    public boolean remove(String name) {
        try {
            ensureFile();
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection section = yaml.getConfigurationSection("zones");
            if (section != null) {
                for (String key : List.copyOf(section.getKeys(false))) {
                    if (key.equalsIgnoreCase(name)) {
                        yaml.set("zones." + key, null);
                    }
                }
            }
            yaml.save(file);
            this.zones = List.copyOf(parse(yaml));
            return true;
        } catch (Exception failure) {
            plugin.getLogger().warning("Could not save zones.yml: " + failure.getMessage());
            return false;
        }
    }

    public int nextCreatedOrder() {
        int next = 1;
        for (Zone zone : zones) {
            if (zone.createdOrder() >= next) {
                next = zone.createdOrder() + 1;
            }
        }
        return next;
    }

    private void ensureFile() throws IOException {
        File folder = plugin.getDataFolder();
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create " + folder.getAbsolutePath());
        }
        if (file.exists()) {
            return;
        }
        String body = "# RexAFKZone zones. This file is not replaced on update.\nzones: {}\n";
        Files.writeString(file.toPath(), body, StandardCharsets.UTF_8);
    }

    private List<Zone> parse(YamlConfiguration yaml) {
        ConfigurationSection section = yaml.getConfigurationSection("zones");
        if (section == null) {
            return List.of();
        }
        List<Zone> parsed = new ArrayList<>();
        int fallback = 1;
        for (String key : section.getKeys(false)) {
            ConfigurationSection zone = section.getConfigurationSection(key);
            if (zone == null) {
                continue;
            }
            String world = zone.getString("world", "");
            if (world == null || world.isBlank()) {
                plugin.getLogger().warning("Skipped zone " + key + " because world is empty.");
                continue;
            }
            int created = zone.contains("created") ? zone.getInt("created") : fallback;
            fallback = Math.max(fallback, created + 1);
            Integer rolls = zone.contains("rolls") ? zone.getInt("rolls") : null;
            parsed.add(new Zone(
                    key,
                    world,
                    zone.getInt("min.x"),
                    zone.getInt("min.y"),
                    zone.getInt("min.z"),
                    zone.getInt("max.x"),
                    zone.getInt("max.y"),
                    zone.getInt("max.z"),
                    zone.getInt("priority", 0),
                    created,
                    Math.max(0, zone.getInt("interval-seconds", 60)),
                    rolls,
                    zone.getBoolean("bossbar", true),
                    zone.getBoolean("actionbar", true),
                    zone.getBoolean("chat", true),
                    RewardLists.fromMaps(zone.getMapList("rewards"))
            ));
        }
        parsed.sort(Comparator.comparingInt(Zone::createdOrder).thenComparing(Zone::name, String.CASE_INSENSITIVE_ORDER));
        return parsed;
    }
}
