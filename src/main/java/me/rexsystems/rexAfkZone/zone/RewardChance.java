package me.rexsystems.rexAfkZone.zone;

import java.util.Random;

public final class RewardChance {

    private RewardChance() {
    }

    public static boolean pays(double chance, Random random) {
        if (chance <= 0.0 || Double.isNaN(chance)) {
            return false;
        }
        if (chance >= 100.0) {
            return true;
        }
        return random.nextDouble() * 100.0 < chance;
    }
}
