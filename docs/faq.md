---
title: FAQ
description: Reload, overlapping zones, and wand clicks.
sidebar:
  order: 1
---

## Does reload keep progress?

Yes. `/rexafkzone reload` reads `config.yml` and `zones.yml` again. Seconds already counted for each player and each zone stay in memory. The current zone highlight is cleared and boss bars are hidden. The next second inside a zone continues the same number.

A full server restart clears that memory. Progress is not written to disk.

Editing a zone's name in `zones.yml` starts a new counter, because the counter is stored under the name.

## Which zone pays when two zones overlap?

The zone with the highest `priority` wins. The other zones keep the seconds already stored for them, and those seconds do not increase while a higher priority zone is the one being counted.

If two overlapping zones have the same priority, the one with the lower `created` value is used. That is the zone that was created first. If `created` is missing, file order is used for that load. If those values are also equal, the name that comes first alphabetically is used, so the choice does not change between reloads.

## Does the wand cancel other clicks?

Only a left click or a right click on a block, while that player holds the wand and has `rexafkzone.select`. That click sets a corner and is cancelled, so the wand does not break the block or open a chest.

Any other interact is left alone. That includes clicks without the wand, clicks from a player who lacks `rexafkzone.select`, left or right clicks in the air, and physical actions such as stepping on a plate.

Paper delivers the block click once for each hand. Both copies of that wand click are cancelled. The corner is stored once.

## A player stood in the zone and got nothing

Check these in order:

1. `settings.enabled` is true.
2. The player has `rexafkzone.use`.
3. The world is allowed. A non-empty `worlds.enabled` list ignores every other world.
4. `interval-seconds` on the zone is at least 1.
5. `settings.idle-seconds` is 0, or the player has stood still long enough. Moving farther than `idle-move-blocks` starts that wait again.
6. The reward `chance` can miss. A miss still uses up the interval.
7. Another zone with a higher priority covers the same block.

## The boss bar stayed on screen

The bar is removed when the player leaves the winning zone, dies, quits, enters a world that is turned off, or when you reload. If a zone has `bossbar: false`, that zone does not show one.
