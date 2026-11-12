package me.rexsystems.rexAfkZone.zone;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RewardChanceTest {

    @Test
    void seededRollDoesNotPayWhenItMisses() {
        long seed = 12345L;
        double ceiling = new Random(seed).nextDouble() * 100.0;
        assertTrue(ceiling > 0.0);
        double chance = ceiling / 2.0;
        assertFalse(RewardChance.pays(chance, new Random(seed)));
    }
}
