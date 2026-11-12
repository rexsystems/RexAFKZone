package me.rexsystems.rexAfkZone.zone;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AfkSessionsTest {

    @Test
    void reloadResetKeepsProgress() {
        AfkSessions sessions = new AfkSessions();
        UUID player = UUID.fromString("11111111-1111-1111-1111-111111111111");
        sessions.add(player, "plaza", 27, 60);
        sessions.setActive(player, "plaza");

        sessions.resetForReload();

        assertEquals(27, sessions.get(player, "plaza"));
        assertNull(sessions.active(player));
    }
}
