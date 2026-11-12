---
title: RexAFKZone
description: AFK reward zones for Paper, Purpur, and Folia.
---

RexAFKZone pays players who stay inside a region you mark. Each zone has its own wait, rewards, and priority. A wand sets the two corners. WorldEdit is not required.

Messages use MiniMessage. Legacy `&` codes, `&#RRGGBB`, and `#RRGGBB` also work.

## Files

```
plugins/RexAFKZone/
  config.yml
  zones.yml
```

`config.yml` holds settings, the wand, sounds, and messages. A reload that changes the file writes `config.yml.bak.<timestamp>` first, then adds any keys that were missing. Values you already set stay as they are.

`zones.yml` is the zone list. The plugin creates it when the folder is new. It is never copied out of the jar over a file you already have.

Time a player has already counted is kept in memory. Reload keeps it. A server restart clears it.

## Next

- [Installation](/rexafkzone/getting-started/installation/)
- [Creating a zone](/rexafkzone/guides/creating-a-zone/)
- [Rewards](/rexafkzone/configuration/rewards/)
