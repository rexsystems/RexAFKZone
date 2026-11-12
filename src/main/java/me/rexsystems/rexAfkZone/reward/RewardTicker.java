package me.rexsystems.rexAfkZone.reward;

import me.rexsystems.rexAfkZone.RexAFKZone;
import me.rexsystems.rexAfkZone.util.Placeholders;
import me.rexsystems.rexAfkZone.util.SchedulerUtils;
import me.rexsystems.rexAfkZone.util.TextParser;
import me.rexsystems.rexAfkZone.zone.PayoutGate;
import me.rexsystems.rexAfkZone.zone.Reward;
import me.rexsystems.rexAfkZone.zone.RewardChance;
import me.rexsystems.rexAfkZone.zone.Zone;
import me.rexsystems.rexAfkZone.zone.ZonePicker;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public final class RewardTicker implements Listener {

    private final RexAFKZone plugin;
    private final RewardGranter granter;
    private final Map<UUID, BossBar> bars = new ConcurrentHashMap<>();
    private io.papermc.paper.threadedregions.scheduler.ScheduledTask task;

    public RewardTicker(RexAFKZone plugin) {
        this.plugin = plugin;
        this.granter = new RewardGranter(plugin);
    }

    public void start() {
        this.task = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduled -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                SchedulerUtils.runForPlayer(plugin, player, () -> tick(player));
            }
        }, 20L, 20L);
    }

    public void shutdown() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            hide(player);
        }
    }

    public void hideAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            SchedulerUtils.runForPlayer(plugin, player, () -> hide(player));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        hide(event.getPlayer());
    }

    private void tick(Player player) {
        if (!player.isOnline() || player.isDead() || !plugin.getConfigManager().enabled()) {
            hide(player);
            return;
        }
        if (!player.hasPermission("rexafkzone.use")) {
            hide(player);
            return;
        }
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world == null || !plugin.getConfigManager().worldAllowed(world.getName())) {
            plugin.getSessions().setActive(player.getUniqueId(), null);
            hide(player);
            return;
        }
        Zone zone = ZonePicker.pick(
                plugin.getZoneStore().all(),
                world.getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());
        if (zone == null) {
            plugin.getSessions().setActive(player.getUniqueId(), null);
            hide(player);
            return;
        }
        String previous = plugin.getSessions().active(player.getUniqueId());
        if (previous != null && !previous.equals(zone.name())) {
            hide(player);
        }
        plugin.getSessions().setActive(player.getUniqueId(), zone.name());

        boolean counting = plugin.getSessions().countSecond(
                player.getUniqueId(),
                location.getX(),
                location.getY(),
                location.getZ(),
                plugin.getConfigManager().idleSeconds(),
                plugin.getConfigManager().idleMoveBlocks());
        if (counting && zone.intervalSeconds() > 0) {
            plugin.getSessions().add(player.getUniqueId(), zone.name(), 1, zone.intervalSeconds());
        }

        int progress = plugin.getSessions().get(player.getUniqueId(), zone.name());
        Location now = player.getLocation();
        World nowWorld = now.getWorld();
        if (nowWorld != null && PayoutGate.allow(zone, progress, nowWorld.getName(), now.getBlockX(), now.getBlockY(), now.getBlockZ())) {
            progress = progress - zone.intervalSeconds();
            plugin.getSessions().set(player.getUniqueId(), zone.name(), progress);
            granter.grant(player, zone, roll(zone), tokens(player, zone, progress));
        }

        Map<String, String> tokens = tokens(player, zone, progress);
        if (zone.bossbar()) {
            showBar(player, progress, tokens);
        } else {
            hide(player);
        }
        if (zone.actionbar()) {
            boolean showIdle = plugin.getConfigManager().idleSeconds() > 0 && !counting;
            String template = plugin.getConfigManager().message(showIdle ? "idle" : "actionbar");
            String text = Placeholders.apply(player, TextParser.apply(template, tokens));
            if (!text.isBlank()) {
                player.sendActionBar(TextParser.parse(text));
            }
        }
    }

    private List<Reward> roll(Zone zone) {
        List<Reward> won = new ArrayList<>();
        int times = zone.rollCount(plugin.getConfigManager().rolls());
        for (int i = 0; i < times; i++) {
            for (Reward reward : zone.rewards()) {
                if (RewardChance.pays(reward.chance(), ThreadLocalRandom.current())) {
                    won.add(reward);
                }
            }
        }
        return won;
    }

    private void showBar(Player player, int progress, Map<String, String> tokens) {
        int interval = Math.max(1, Integer.parseInt(tokens.getOrDefault("interval", "1")));
        float fraction = Math.min(1f, Math.max(0f, progress / (float) interval));
        Component name = TextParser.parse(Placeholders.apply(player, TextParser.apply(plugin.getConfigManager().message("bossbar"), tokens)));
        BossBar bar = bars.get(player.getUniqueId());
        if (bar == null) {
            bar = BossBar.bossBar(name, fraction, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            bars.put(player.getUniqueId(), bar);
            player.showBossBar(bar);
            return;
        }
        bar.name(name);
        bar.progress(fraction);
    }

    public void hide(Player player) {
        if (player == null) {
            return;
        }
        BossBar bar = bars.remove(player.getUniqueId());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }

    private Map<String, String> tokens(Player player, Zone zone, int progress) {
        int interval = zone.intervalSeconds();
        int left = Math.max(0, interval - progress);
        Location location = player.getLocation();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("player", player.getName());
        tokens.put("zone", zone.name());
        tokens.put("world", zone.world());
        tokens.put("x", Integer.toString(location.getBlockX()));
        tokens.put("y", Integer.toString(location.getBlockY()));
        tokens.put("z", Integer.toString(location.getBlockZ()));
        tokens.put("priority", Integer.toString(zone.priority()));
        tokens.put("progress", Integer.toString(Math.max(0, progress)));
        tokens.put("interval", Integer.toString(interval));
        tokens.put("left", Integer.toString(left));
        tokens.put("seconds", Integer.toString(left));
        return tokens;
    }
}
