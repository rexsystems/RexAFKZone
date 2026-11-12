package me.rexsystems.rexAfkZone.zone;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Selections {

    private final Map<UUID, Point> pos1 = new ConcurrentHashMap<>();
    private final Map<UUID, Point> pos2 = new ConcurrentHashMap<>();

    public void setPos1(UUID player, Point point) {
        pos1.put(player, point);
    }

    public void setPos2(UUID player, Point point) {
        pos2.put(player, point);
    }

    public Point pos1(UUID player) {
        return pos1.get(player);
    }

    public Point pos2(UUID player) {
        return pos2.get(player);
    }

    public record Point(String world, int x, int y, int z) {
    }
}
