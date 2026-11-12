---
title: Commands
description: RexAFKZone commands and their permissions.
sidebar:
  order: 1
---

| Command | Description | Permission |
|---|---|---|
| `/rexafkzone` | Show help | everyone |
| `/rexafkzone help` | Show help | everyone |
| `/rexafkzone wand` | Give yourself the selection wand | `rexafkzone.select` |
| `/rexafkzone create <name>` | Create a zone from position 1 and position 2 | `rexafkzone.admin` |
| `/rexafkzone delete <name>` | Delete a zone | `rexafkzone.admin` |
| `/rexafkzone list` | List zone names, worlds, and priorities | `rexafkzone.admin` |
| `/rexafkzone info [name]` | Show one zone. With no name, show the zone you are standing in | `rexafkzone.admin` |
| `/rexafkzone reload` | Reload `config.yml` and `zones.yml` | `rexafkzone.reload` |

Aliases: `/rafk`, `/afkzone`

Names are 1 to 32 characters: letters, numbers, and underscores. Lookup ignores case.

Reload keeps the seconds each player has already counted. It reads zone edits from disk, hides boss bars, and the next second inside a zone shows the bar again.

Help text comes from `messages.help-header` and `messages.help`.

Selection positions are remembered until the player sets them again or the server stops. Reload does not clear them.
