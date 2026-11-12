package me.rexsystems.rexAfkZone.reward;

import me.rexsystems.rexAfkZone.RexAFKZone;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class GameSounds {

    private final RexAFKZone plugin;
    private final Set<String> invalid = ConcurrentHashMap.newKeySet();

    public GameSounds(RexAFKZone plugin) {
        this.plugin = plugin;
    }

    public void play(Player player, String name) {
        if (player == null || !plugin.getConfigManager().soundEnabled()) {
            return;
        }
        if (name == null || name.isBlank()) {
            return;
        }
        Sound sound;
        try {
            sound = Sound.valueOf(name.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException failure) {
            if (invalid.add(name)) {
                plugin.getLogger().warning("Unknown sound: " + name);
            }
            return;
        }
        player.playSound(player.getLocation(), sound, plugin.getConfigManager().soundVolume(), plugin.getConfigManager().soundPitch());
    }
}
