package me.rexsystems.rexAfkZone.zone;

public final class PayoutGate {

    private PayoutGate() {
    }

    public static boolean allow(Zone zone, int progressSeconds, String world, int x, int y, int z) {
        if (zone == null || zone.intervalSeconds() <= 0) {
            return false;
        }
        if (progressSeconds < zone.intervalSeconds()) {
            return false;
        }
        return zone.contains(world, x, y, z);
    }
}
