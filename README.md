# RexAFKZone

AFK reward zones for Paper, Purpur, and Folia. Mark a region with a wand, and players who stay there earn the rewards you put on that zone.

<p>
  <img src="https://img.shields.io/badge/Minecraft-1.21%2B-62B47A?style=for-the-badge" alt="Minecraft 1.21+">
  <img src="https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
</p>

## Features

- Unlimited zones, stored in `zones.yml`
- Selection wand. No WorldEdit
- Highest priority wins when regions overlap. An equal priority keeps the zone that was created first
- Time already counted stays through `/rexafkzone reload`
- Leaving a zone pauses that timer. Coming back continues it
- Optional idle delay. Movement resets the idle timer and leaves the saved zone time alone
- Each reward has a chance, commands, and items
- Boss bar, action bar, and chat can be turned off per zone
- MiniMessage, legacy `&` codes, `&#RRGGBB`, and `#RRGGBB`
- PlaceholderAPI in messages, item text, and reward commands

## Requirements

- Java 21
- Paper, Purpur, or Folia 1.21+
- PlaceholderAPI is optional

## Install

1. Put the jar in `plugins/`
2. Restart
3. Edit `plugins/RexAFKZone/config.yml` and `zones.yml`
4. Run `/rexafkzone reload`

## Commands

| Command | Permission |
|---|---|
| `/rexafkzone help` | everyone |
| `/rexafkzone wand` | `rexafkzone.select` |
| `/rexafkzone create <name>` | `rexafkzone.admin` |
| `/rexafkzone delete <name>` | `rexafkzone.admin` |
| `/rexafkzone list` | `rexafkzone.admin` |
| `/rexafkzone info [name]` | `rexafkzone.admin` |
| `/rexafkzone reload` | `rexafkzone.reload` |

Aliases: `/rafk`, `/afkzone`

## Documentation

The pages in [docs/](docs/index.md) drop into a Starlight site at `src/content/docs/rexafkzone/`. Paste `docs/sidebar.mjs` into the site sidebar.

Full docs: [https://rexsystems.me/docs/rexafkzone](https://rexsystems.me/docs/rexafkzone)

## License

Licensed under the [GNU Affero General Public License v3.0](LICENSE).
