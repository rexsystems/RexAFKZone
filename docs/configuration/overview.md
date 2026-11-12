---
title: Configuration
description: Settings, worlds, the wand, and sounds in config.yml.
sidebar:
  order: 1
---

`config.yml` is the shared setup. Zone bounds and rewards live in `zones.yml`. `config-version` is internal. Leave it alone.

The timer adds one second at a time.

## settings

| Key | What it does |
|---|---|
| `enabled` | Master switch. `false` stops counting and hides boss bars. |
| `idle-seconds` | Seconds a player must stand still before time counts. `0` counts every second they are inside. |
| `idle-move-blocks` | Distance, in blocks, from the spot where the idle timer last started. Crossing it restarts the idle timer. Zone progress already counted stays. |
| `rolls` | How many times each reward in a zone is rolled when an interval ends. A zone can set its own `rolls`. |
| `default-interval-seconds` | Wait copied onto a zone created with the command. Minimum 1. |
| `default-priority` | Priority copied onto a new zone. |
| `default-bossbar` | Boss bar toggle copied onto a new zone. |
| `default-actionbar` | Action bar toggle copied onto a new zone. |
| `default-chat` | Chat toggle copied onto a new zone. |

`idle-move-blocks` is measured from the last idle reset, not from the previous tick. A slow walk crosses the limit. A tiny shift that stays inside the limit does not.

Movement never clears the seconds already stored for a zone. Leaving the zone pauses that store. Entering it again continues the same number.

## worlds

If `worlds.enabled` has any names, time is counted only there. Otherwise `worlds.disabled` is a deny list. Both empty means every world.

A player who is in a disabled world keeps the progress they already had. The boss bar is removed until they are somewhere the plugin is allowed to count.

## wand

`wand.material` is a Bukkit material name, such as `BLAZE_ROD`. `wand.name` and `wand.lore` are the item text. The given item is marked as the wand. A normal item of the same material does not select positions.

## sound

`sound.enabled` turns both sounds off when it is false. `sound.select` plays when a position is set. `sound.reward` plays when a cycle grants something. Names are Bukkit sound enum names, such as `ENTITY_EXPERIENCE_ORB_PICKUP`. An unknown name is logged once. An empty name skips that sound.

`sound.volume` and `sound.pitch` are shared.

## defaults.rewards

`/rexafkzone create` copies this list into the new zone. After that, edit the zone in `zones.yml`. Changing the default does not rewrite zones that already exist.

See [Rewards](/rexafkzone/configuration/rewards/).
