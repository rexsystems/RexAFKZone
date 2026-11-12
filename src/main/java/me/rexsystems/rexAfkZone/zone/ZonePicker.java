package me.rexsystems.rexAfkZone.zone;

import java.util.List;

public final class ZonePicker {

    private ZonePicker() {
    }

    public static Zone pick(List<Zone> zones, String world, int x, int y, int z) {
        Zone best = null;
        if (zones == null) {
            return null;
        }
        for (Zone zone : zones) {
            if (zone == null || !zone.contains(world, x, y, z)) {
                continue;
            }
            if (best == null || outranks(zone, best)) {
                best = zone;
            }
        }
        return best;
    }

    static boolean outranks(Zone candidate, Zone current) {
        if (candidate.priority() != current.priority()) {
            return candidate.priority() > current.priority();
        }
        if (candidate.createdOrder() != current.createdOrder()) {
            return candidate.createdOrder() < current.createdOrder();
        }
        return candidate.name().compareToIgnoreCase(current.name()) < 0;
    }
}
