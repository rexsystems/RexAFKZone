---
title: Permissions
description: Permission nodes shipped with RexAFKZone.
sidebar:
  order: 2
---

| Permission | Default | Description |
|---|---|---|
| `rexafkzone.use` | true | Count time and receive rewards inside a zone |
| `rexafkzone.select` | op | Receive the wand and set position 1 and position 2 |
| `rexafkzone.admin` | op | Create, delete, list, and inspect zones |
| `rexafkzone.reload` | op | Reload `config.yml` and `zones.yml` |

`rexafkzone.select` is checked when the wand is used, not only when it is given. A player who holds the wand without that permission does not set positions, and the click is left alone.

`rexafkzone.use` does not grant the admin commands. Operators receive the nodes that default to op.
