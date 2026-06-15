# SpireMod — Lightweight Slay the Spire Mod Design Spec

**Date**: 2026-06-15  
**Status**: Draft  
**Approach**: SpirePatch only (no BaseMod)

## Overview

A minimal Slay the Spire content mod. On game start (new run), the player receives:

- +200 gold
- Membership Card (原版 Boss Relic)
- The Courier (原版 Shop Relic)
- Black Star (原版 Boss Relic)

All three relics are vanilla — no custom effects or new assets.

## Project Structure

```
SpireMod/
├── src/main/java/spiremod/
│   ├── SpireMod.java              # @ModAnnotation entry point
│   └── patches/
│       ├── GoldPatch.java         # Adds +200 gold at game start
│       └── RelicPatch.java        # Grants 3 relics at game start
├── src/main/resources/
│   └── ModTheSpire.json           # MTS metadata manifest
└── build.gradle                   # Gradle build (ModTheSpire only)
```

## Dependencies

- **ModTheSpire** (required) — patching framework
- **desktop-1.0.jar** (compile-only) — Slay the Spire game classes
- No BaseMod dependency

## Component Design

### SpireMod.java — Entry Point

Standard `@SpireInitializer` class. Calls `BaseModInit` and registers the mod with ModTheSpire. No subscriptions or BaseMod hooks.

### GoldPatch.java

- **Hook target**: Character initialization phase (exact class/method TBD from decompiled source)
- **Behavior**: Increments the player's gold counter by 200
- **Guard**: Only applies at game start (not on reload / continue)

### RelicPatch.java

- **Hook target**: Relic distribution phase (exact class/method TBD from decompiled source)
- **Behavior**: Appends three relic instances to the player's relic list:
  - `Membership Card`
  - `The Courier`
  - `Black Star`
- **Guard**: Only applies at game start; does not fire on boss relic selection screens

### ModTheSpire.json

Standard MTS manifest with mod ID, name, author, description, and dependency list (empty or MTS only).

## Hook Point Candidates

Exact class names and method signatures will be determined during implementation by referencing decompiled Slay the Spire source. Likely candidates:

- **Gold**: `AbstractPlayer.gainGold(int)` or `AbstractPlayer.initializeGold()` or character constructor
- **Relics**: `AbstractDungeon.initializeRelics()` or `AbstractPlayer.obtainPotion` area

## Testing

- Launch game with ModTheSpire, start a new run
- Verify: gold starts at 299 (Ironclad base 99 + 200) or equivalent
- Verify: all 3 relics are present in the relic bar
- Verify: relics do NOT appear when loading a saved game

## build.gradle

Standard ModTheSpire mod template:
- Apply `java` plugin
- `desktop-1.0.jar` as compileOnly dependency
- Jar task that packs classes into the ModTheSpire `mods/` directory
