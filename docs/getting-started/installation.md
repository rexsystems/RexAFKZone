---
title: Installation
description: Put RexAFKZone on a Paper, Purpur, or Folia server.
sidebar:
  order: 1
---

1. Download the jar.
2. Put it in the server `plugins/` folder.
3. Start the server once so `plugins/RexAFKZone/config.yml` and `zones.yml` are written.
4. Edit `config.yml`. Add zones with the wand or by editing `zones.yml`.
5. Run `/rexafkzone reload`, or restart.

PlaceholderAPI is not required. If it is installed, `%placeholders%` in messages, item names, item lore, and reward commands are filled for that player.

Players need `rexafkzone.use` to count time and receive rewards. That permission defaults to true.
