package me.rexsystems.rexAfkZone.zone;

import java.util.List;

public record Reward(double chance, String message, List<String> commands, List<RewardItem> items) {

    public Reward {
        message = message == null ? "" : message;
        commands = commands == null ? List.of() : List.copyOf(commands);
        items = items == null ? List.of() : List.copyOf(items);
    }
}
