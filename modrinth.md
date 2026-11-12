# RexAFKZone

AFK reward zones for Paper, Purpur, and Folia. Select a region with the built-in wand, then pay players who stay inside it.

**Java 21. Paper, Purpur, or Folia 1.21+.** PlaceholderAPI is optional.

## Features

- Unlimited named zones in `zones.yml`
- Wand selection for the two corners. WorldEdit is not required
- Overlapping zones: the highest `priority` pays. The same priority uses the zone created first
- Reload reads both files again and keeps the seconds a player has already counted
- Walking out pauses a zone. Walking back in continues that zone's timer
- `settings.idle-seconds` of `0` counts every second inside. A higher value waits until the player stands still
- Rewards roll by chance, and one cycle can roll more than one reward
- Items support name, lore, and custom model data. Commands run as console
- Boss bar, action bar, and chat are toggles on each zone
- The boss bar for a player is removed when that player leaves the zone

## Commands

| Command | Description | Permission |
|---|---|---|
| `/rexafkzone help` | Show help | everyone |
| `/rexafkzone wand` | Give yourself the selection wand | `rexafkzone.select` |
| `/rexafkzone create <name>` | Save the current selection as a zone | `rexafkzone.admin` |
| `/rexafkzone delete <name>` | Delete a zone | `rexafkzone.admin` |
| `/rexafkzone list` | List zones | `rexafkzone.admin` |
| `/rexafkzone info [name]` | Show one zone, or the zone you are standing in | `rexafkzone.admin` |
| `/rexafkzone reload` | Reload `config.yml` and `zones.yml` | `rexafkzone.reload` |

Aliases: `/rafk`, `/afkzone`

## Permissions

| Permission | Default | Description |
|---|---|---|
| `rexafkzone.use` | true | Receive rewards while inside a zone |
| `rexafkzone.select` | op | Use the selection wand |
| `rexafkzone.admin` | op | Create, delete, list, and inspect zones |
| `rexafkzone.reload` | op | Reload the plugin |

`zones.yml` is your zone data. An update does not overwrite it. `config.yml` gains missing keys and keeps a backup when that happens.

Documentation: https://rexsystems.me/docs/rexafkzone
