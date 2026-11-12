package me.rexsystems.rexAfkZone.zone;

import java.util.List;

public record Zone(
        String name,
        String world,
        int minX,
        int minY,
        int minZ,
        int maxX,
        int maxY,
        int maxZ,
        int priority,
        int createdOrder,
        int intervalSeconds,
        Integer rolls,
        boolean bossbar,
        boolean actionbar,
        boolean chat,
        List<Reward> rewards
) {

    public Zone {
        name = name == null ? "" : name;
        world = world == null ? "" : world;
        int x1 = Math.min(minX, maxX);
        int x2 = Math.max(minX, maxX);
        int y1 = Math.min(minY, maxY);
        int y2 = Math.max(minY, maxY);
        int z1 = Math.min(minZ, maxZ);
        int z2 = Math.max(minZ, maxZ);
        minX = x1;
        maxX = x2;
        minY = y1;
        maxY = y2;
        minZ = z1;
        maxZ = z2;
        rewards = rewards == null ? List.of() : List.copyOf(rewards);
    }

    public boolean contains(String worldName, int x, int y, int z) {
        if (worldName == null || !world.equalsIgnoreCase(worldName)) {
            return false;
        }
        return x >= minX && x <= maxX
                && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ;
    }

    public int rollCount(int settingsRolls) {
        int chosen = rolls == null ? settingsRolls : rolls;
        return Math.max(0, chosen);
    }
}
