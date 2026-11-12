---
title: Rewards
description: Chance, commands, items, and how many times a cycle rolls.
sidebar:
  order: 3
---

Rewards are a list on each zone in `zones.yml`. When a player's counted seconds reach `interval-seconds`, and they are still inside that zone, the plugin rolls the list and then subtracts one interval. Seconds past the interval are kept.

If they are no longer inside at the moment of the payout, nothing is granted and the counted seconds stay.

`chance` is a percent from 0 to 100. `100` always grants that reward. `0` never does. Each reward is rolled on its own, so one cycle can grant more than one reward.

`rolls` on the zone, when set, is how many times the whole list is rolled. When the key is absent, `settings.rolls` is used. `1` checks each reward once. `2` checks each reward twice.

A missed chance still ends the interval. The player waits again for the next roll.

## Item fields

| Field | What it does |
|---|---|
| `material` | Bukkit material name, such as `GOLD_NUGGET`. |
| `amount` | How many. Values under 1 are treated as 1. One reward gives at most 2304 of that item. |
| `name` | Display name. Blank keeps the vanilla name. |
| `lore` | Lore lines. |
| `custom-model-data` | Integer model data. `0` leaves it unset. |

Items that do not fit in the inventory drop at the player's feet.

## Commands

Each entry is a console command without a leading slash. `{player}` is the account name.

```yaml
commands:
  - "eco give {player} 10"
```

## Example

```yaml
zones:
  plaza:
    world: world
    min:
      x: 10
      y: 64
      z: 10
    max:
      x: 20
      y: 80
      z: 20
    priority: 1
    created: 1
    interval-seconds: 60
    bossbar: true
    actionbar: true
    chat: true
    rewards:
      - chance: 100
        message: "<green>You received an AFK reward."
        commands: []
        items:
          - material: GOLD_NUGGET
            amount: 1
            name: "<gold>Plaza token"
            lore:
              - "<gray>From {zone}"
            custom-model-data: 0
      - chance: 25
        message: "<aqua>Bonus."
        commands:
          - "say {player} found a bonus in {zone}"
        items: []
```

`created` is the order used when two overlapping zones share a priority. Lower numbers were created first. You can omit `rolls` to follow `settings.rolls`, or set `rolls: 2` on this zone only.

`/rexafkzone create` writes a zone in this shape and copies `defaults.rewards` from `config.yml`.
