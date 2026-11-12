---
title: Creating a zone
description: Select two corners and save a named zone.
sidebar:
  order: 1
---

1. Run `/rexafkzone wand`.
2. Hold the wand. Left click a block for position 1. Right click a block for position 2.
3. Run `/rexafkzone create plaza`.

Both clicks have to be in the same world. The saved box includes every block from the lower corner through the higher corner, on all three axes.

The new zone uses `default-priority`, `default-interval-seconds`, and the boss bar, action bar, and chat defaults from `config.yml`. Its rewards start as a copy of `defaults.rewards`. `rolls` is left unset, so the zone follows `settings.rolls` until you add the key.

Open `plugins/RexAFKZone/zones.yml` to change the box, the wait, the priority, or the rewards. Then run `/rexafkzone reload`.

`/rexafkzone info plaza` prints the saved box. `/rexafkzone list` prints every zone. `/rexafkzone delete plaza` removes one.

A player inside the zone needs `rexafkzone.use`. The world has to be allowed. The plugin adds one second at a time, and only while the idle rule says this second counts. When the count reaches `interval-seconds`, rewards roll if the player is still inside.

If another zone covers the same block, the higher `priority` is the one that counts. See the [FAQ](/rexafkzone/faq/).
