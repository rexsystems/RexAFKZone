package me.rexsystems.rexAfkZone.zone;

import java.util.List;

public record RewardItem(String material, int amount, String name, List<String> lore, int customModelData) {

    public RewardItem {
        material = material == null ? "" : material;
        name = name == null ? "" : name;
        lore = lore == null ? List.of() : List.copyOf(lore);
        amount = Math.max(0, amount);
    }
}
