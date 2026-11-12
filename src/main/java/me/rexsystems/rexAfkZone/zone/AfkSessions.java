package me.rexsystems.rexAfkZone.zone;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AfkSessions {

    private final Map<UUID, Session> sessions = new HashMap<>();

    public synchronized int add(UUID player, String zone, int seconds, int cap) {
        Session session = session(player);
        int current = session.progress.getOrDefault(zone, 0);
        int next = current + Math.max(0, seconds);
        if (next < 0) {
            next = Integer.MAX_VALUE;
        }
        if (cap > 0 && next > cap) {
            next = cap;
        }
        session.progress.put(zone, next);
        return next;
    }

    public synchronized int get(UUID player, String zone) {
        Session session = sessions.get(player);
        if (session == null) {
            return 0;
        }
        return session.progress.getOrDefault(zone, 0);
    }

    public synchronized void set(UUID player, String zone, int seconds) {
        session(player).progress.put(zone, Math.max(0, seconds));
    }

    public synchronized void setActive(UUID player, String zone) {
        session(player).active = zone;
    }

    public synchronized String active(UUID player) {
        Session session = sessions.get(player);
        return session == null ? null : session.active;
    }

    public synchronized boolean countSecond(UUID player, double x, double y, double z, int requiredIdle, double moveBlocks) {
        return IdleClock.countsThisSecond(session(player).idle, x, y, z, requiredIdle, moveBlocks);
    }

    public synchronized void resetForReload() {
        for (Session session : sessions.values()) {
            session.active = null;
        }
    }

    private Session session(UUID player) {
        return sessions.computeIfAbsent(player, id -> new Session());
    }

    private static final class Session {
        private final Map<String, Integer> progress = new HashMap<>();
        private final IdleClock.State idle = new IdleClock.State();
        private String active;
    }
}
