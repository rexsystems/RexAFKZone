---
title: Messages
description: Chat lines, the boss bar, and the action bar.
sidebar:
  order: 2
---

Every player-facing string accepts MiniMessage, legacy `&` codes, `&#RRGGBB`, and `#RRGGBB`.

`{prefix}` and `<prefix>` in command replies use `messages.prefix`. Put color in the template. Names inserted with `{player}` are written as plain text.

## Command replies

| Key | When it is sent |
|---|---|
| `prefix` | Inserted into the replies below. |
| `no-permission` | The sender lacks the permission for that subcommand. |
| `players-only` | A console ran a command that needs a player. |
| `reload` | Reload finished. |
| `save-failed` | `zones.yml` could not be written. |
| `help-header` | First help line. |
| `help` | The rest of the help list. |
| `wand-given` | The wand was added to the inventory. |
| `wand-invalid` | `wand.material` is not an item. |
| `pos1-set` | Left click stored position 1. |
| `pos2-set` | Right click stored position 2. |
| `selection-worlds` | The two positions are in different worlds. |
| `selection-missing` | Create was used before both positions were set. |
| `zone-created` | A zone was saved. |
| `zone-exists` | That name is already used. |
| `zone-deleted` | A zone was removed. |
| `zone-missing` | No zone has that name. |
| `name-invalid` | The name is not 1 to 32 letters, numbers, or underscores. |
| `usage-create` | Create was missing a name. |
| `usage-delete` | Delete was missing a name. |
| `usage-info` | Console ran info without a name. |
| `list-header` | Shown above the zone list. |
| `list-line` | One zone in the list. |
| `list-empty` | There are no zones. |
| `info-header` | Zone name at the top of info. |
| `info-line` | One info row. |
| `not-in-zone` | Info without a name, and you are not standing in a zone. |

## While a player is inside

| Key | When it is sent |
|---|---|
| `bossbar` | Boss bar title, if that zone has `bossbar: true`. |
| `actionbar` | Action bar while time is counting, if that zone has `actionbar: true`. |
| `idle` | Action bar while the player still has to stand still. |
| `reward` | Chat when a reward pays and that reward's own `message` is blank. The zone also needs `chat: true`. |

The boss bar fills with `progress / interval`. It is removed when the player leaves the zone, loses `rexafkzone.use`, enters a world that is turned off, or the plugin reloads.

## Placeholders

These tokens work in messages, reward messages, item names, and item lore:

| Token | Value |
|---|---|
| `{player}` | Account name |
| `{zone}` | Zone name |
| `{world}` | Zone world |
| `{x}` `{y}` `{z}` | Block the player is standing in |
| `{priority}` | Zone priority |
| `{progress}` | Seconds counted toward the current interval |
| `{interval}` | `interval-seconds` for that zone |
| `{left}` | Seconds left in the current interval |
| `{seconds}` | Same as `{left}` |
| `{prefix}` | `messages.prefix`, in command replies and reward chat |

Reward commands use the same `{player}`, `{zone}`, `{world}`, `{x}`, `{y}`, and `{z}` tokens. They are not color-parsed. The command runs as console.

If PlaceholderAPI is installed, its `%placeholders%` are applied after these tokens.
