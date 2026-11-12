package me.rexsystems.rexAfkZone;

import me.rexsystems.rexAfkZone.command.AfkZoneCommand;
import me.rexsystems.rexAfkZone.config.ConfigManager;
import me.rexsystems.rexAfkZone.reward.GameSounds;
import me.rexsystems.rexAfkZone.reward.RewardTicker;
import me.rexsystems.rexAfkZone.wand.WandListener;
import me.rexsystems.rexAfkZone.zone.AfkSessions;
import me.rexsystems.rexAfkZone.zone.Selections;
import me.rexsystems.rexAfkZone.zone.ZoneStore;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class RexAFKZone extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 35265;

    private ConfigManager configManager;
    private ZoneStore zoneStore;
    private AfkSessions sessions;
    private Selections selections;
    private RewardTicker ticker;
    private GameSounds sounds;
    private NamespacedKey wandKey;

    @Override
    public void onEnable() {
        this.wandKey = new NamespacedKey(this, "wand");
        this.configManager = new ConfigManager(this);
        this.configManager.load();
        this.zoneStore = new ZoneStore(this);
        this.zoneStore.load();
        this.sessions = new AfkSessions();
        this.selections = new Selections();
        this.sounds = new GameSounds(this);
        this.ticker = new RewardTicker(this);

        getServer().getPluginManager().registerEvents(new WandListener(this), this);
        getServer().getPluginManager().registerEvents(ticker, this);

        AfkZoneCommand command = new AfkZoneCommand(this);
        PluginCommand pluginCommand = getCommand("rexafkzone");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
        }

        ticker.start();
        new Metrics(this, BSTATS_PLUGIN_ID);
        getLogger().info("RexAFKZone enabled.");
    }

    @Override
    public void onDisable() {
        if (ticker != null) {
            ticker.shutdown();
        }
        getLogger().info("RexAFKZone disabled.");
    }

    public void reloadPlugin() {
        configManager.reload();
        zoneStore.load();
        sessions.resetForReload();
        if (ticker != null) {
            ticker.hideAll();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ZoneStore getZoneStore() {
        return zoneStore;
    }

    public AfkSessions getSessions() {
        return sessions;
    }

    public Selections getSelections() {
        return selections;
    }

    public GameSounds sounds() {
        return sounds;
    }

    public NamespacedKey wandKey() {
        return wandKey;
    }
}
