package me.rexsystems.rexAfkZone.zone;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RewardLists {

    private RewardLists() {
    }

    public static List<Reward> fromMaps(List<Map<?, ?>> maps) {
        if (maps == null || maps.isEmpty()) {
            return List.of();
        }
        List<Reward> rewards = new ArrayList<>();
        for (Map<?, ?> map : maps) {
            if (map == null) {
                continue;
            }
            double chance = number(map.get("chance"), 100.0);
            String message = string(map.get("message"));
            List<String> commands = strings(map.get("commands"));
            List<RewardItem> items = items(map.get("items"));
            rewards.add(new Reward(chance, message, commands, items));
        }
        return List.copyOf(rewards);
    }

    public static List<Map<String, Object>> toMaps(List<Reward> rewards) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (rewards == null) {
            return maps;
        }
        for (Reward reward : rewards) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("chance", reward.chance());
            map.put("message", reward.message());
            map.put("commands", new ArrayList<>(reward.commands()));
            List<Map<String, Object>> items = new ArrayList<>();
            for (RewardItem item : reward.items()) {
                Map<String, Object> itemMap = new LinkedHashMap<>();
                itemMap.put("material", item.material());
                itemMap.put("amount", item.amount());
                itemMap.put("name", item.name());
                itemMap.put("lore", new ArrayList<>(item.lore()));
                itemMap.put("custom-model-data", item.customModelData());
                items.add(itemMap);
            }
            map.put("items", items);
            maps.add(map);
        }
        return maps;
    }

    private static List<RewardItem> items(Object raw) {
        if (!(raw instanceof List<?> list)) {
            return List.of();
        }
        List<RewardItem> items = new ArrayList<>();
        for (Object entry : list) {
            if (!(entry instanceof Map<?, ?> map)) {
                continue;
            }
            String material = string(map.get("material"));
            int amount = (int) number(map.get("amount"), 1.0);
            String name = string(map.get("name"));
            List<String> lore = strings(map.get("lore"));
            int model = (int) number(map.get("custom-model-data"), 0.0);
            items.add(new RewardItem(material, amount, name, lore, model));
        }
        return items;
    }

    private static List<String> strings(Object raw) {
        if (raw == null) {
            return List.of();
        }
        if (raw instanceof String text) {
            return text.isBlank() ? List.of() : List.of(text);
        }
        if (!(raw instanceof List<?> list)) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        for (Object entry : list) {
            if (entry != null) {
                values.add(String.valueOf(entry));
            }
        }
        return values;
    }

    private static String string(Object raw) {
        return raw == null ? "" : String.valueOf(raw);
    }

    private static double number(Object raw, double fallback) {
        if (raw instanceof Number number) {
            return number.doubleValue();
        }
        if (raw instanceof String text) {
            try {
                return Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                return fallback;
            }
        }
        return fallback;
    }
}
