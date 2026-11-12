package me.rexsystems.rexAfkZone.zone;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ZonePickerTest {

    @Test
    void priorityPickWhenZonesOverlap() {
        Zone camp = zone("camp", 1, 1);
        Zone vip = zone("vip", 8, 2);
        Zone picked = ZonePicker.pick(List.of(camp, vip), "world", 4, 70, 4);
        assertNotNull(picked);
        assertEquals("vip", picked.name());

        Zone earlier = zone("alpha", 4, 1);
        Zone later = zone("beta", 4, 2);
        Zone tied = ZonePicker.pick(List.of(later, earlier), "world", 4, 70, 4);
        assertNotNull(tied);
        assertEquals("alpha", tied.name());
    }

    private static Zone zone(String name, int priority, int created) {
        return new Zone(name, "world", 0, 64, 0, 10, 80, 10, priority, created, 60, null, true, false, true, List.of());
    }
}
