package me.rexsystems.rexAfkZone.config;

import me.rexsystems.rexAfkZone.RexAFKZone;
import me.rexsystems.rexAfkZone.zone.Reward;
import me.rexsystems.rexAfkZone.zone.RewardLists;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private final RexAFKZone plugin;
    private FileConfiguration config;

    public ConfigManager(RexAFKZone plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        new ConfigAutoUpdater(plugin).update();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public void reload() {
        load();
    }

    public boolean enabled() {
        return config.getBoolean("settings.enabled", true);
    }

    public int idleSeconds() {
        return Math.max(0, config.getInt("settings.idle-seconds", 0));
    }

    public double idleMoveBlocks() {
        return Math.max(0.0, config.getDouble("settings.idle-move-blocks", 0.35));
    }

    public int rolls() {
        return Math.max(0, config.getInt("settings.rolls", 1));
    }

    public int defaultInterval() {
        return Math.max(1, config.getInt("settings.default-interval-seconds", 60));
    }

    public int defaultPriority() {
        return config.getInt("settings.default-priority", 0);
    }

    public boolean defaultBossbar() {
        return config.getBoolean("settings.default-bossbar", true);
    }

    public boolean defaultActionbar() {
        return config.getBoolean("settings.default-actionbar", true);
    }

    public boolean defaultChat() {
        return config.getBoolean("settings.default-chat", true);
    }

    public boolean worldAllowed(String worldName) {
        if (worldName == null) {
            return false;
        }
        List<String> enabled = config.getStringList("worlds.enabled");
        if (enabled != null && !enabled.isEmpty()) {
            return enabled.stream().anyMatch(world -> world.equalsIgnoreCase(worldName));
        }
        List<String> disabled = config.getStringList("worlds.disabled");
        if (disabled == null || disabled.isEmpty()) {
            return true;
        }
        return disabled.stream().noneMatch(world -> world.equalsIgnoreCase(worldName));
    }

    public String wandMaterial() {
        return config.getString("wand.material", "BLAZE_ROD");
    }

    public String wandName() {
        return config.getString("wand.name", "");
    }

    public List<String> wandLore() {
        List<String> lore = config.getStringList("wand.lore");
        return lore == null ? List.of() : lore;
    }

    public boolean soundEnabled() {
        return config.getBoolean("sound.enabled", true);
    }

    public String selectSound() {
        return config.getString("sound.select", "");
    }

    public String rewardSound() {
        return config.getString("sound.reward", "");
    }

    public float soundVolume() {
        return (float) config.getDouble("sound.volume", 1.0);
    }

    public float soundPitch() {
        return (float) config.getDouble("sound.pitch", 1.0);
    }

    public List<Reward> defaultRewards() {
        return RewardLists.fromMaps(config.getMapList("defaults.rewards"));
    }

    public String message(String path) {
        return config.getString("messages." + path, "");
    }

    public String prefix() {
        String prefix = message("prefix");
        return prefix == null ? "" : prefix;
    }

    public String prefixed(String path) {
        String raw = message(path);
        if (raw == null) {
            return "";
        }
        return raw.replace("<prefix>", prefix()).replace("{prefix}", prefix());
    }

    public List<String> helpLines() {
        List<String> lines = config.getStringList("messages.help");
        return lines == null ? List.of() : lines;
    }
}
