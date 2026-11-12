package me.rexsystems.rexAfkZone.zone;

public final class IdleClock {

    private IdleClock() {
    }

    public static final class State {
        private boolean placed;
        private double x;
        private double y;
        private double z;
        private int idleSeconds;
    }

    public static boolean countsThisSecond(State state, double x, double y, double z, int requiredIdle, double moveBlocks) {
        if (requiredIdle <= 0) {
            return true;
        }
        if (!state.placed) {
            state.placed = true;
            state.x = x;
            state.y = y;
            state.z = z;
            state.idleSeconds = 0;
            return false;
        }
        double dx = x - state.x;
        double dy = y - state.y;
        double dz = z - state.z;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance > Math.max(0.0, moveBlocks)) {
            state.x = x;
            state.y = y;
            state.z = z;
            state.idleSeconds = 0;
            return false;
        }
        state.idleSeconds++;
        return state.idleSeconds >= requiredIdle;
    }
}
