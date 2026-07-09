# AgingWell Assistance Mod — Automation Version

A Minecraft mod designed to make the game more accessible for older or less experienced players. Provides in-game help, Baritone-powered automation, guidance, and mob targeting features to support players as they learn the game.

This is the **automation-enabled** version of the mod. It requires Baritone to be installed for automation features to work. A version without Baritone dependency is available on the `main` branch of this repository.

---

## Features

### /info Command
Get plain-language descriptions of items, blocks, and entities directly in chat.

- `/info` — describes the item currently held in your main hand
- `/info block` — describes the block you are looking at
- `/info entity` — describes the nearest entity
- `/info <keyword>` — look up a concept by keyword (e.g. `/info hunger`, `/info crafting`)

### /guidance Command
Suggests what to do next based on your current inventory and progress.

- `/guidance` — prints your current goal in chat
- `/guidance toggle` — pins the goal display to the top-left of your screen
- `/guidance next` — skips to the next suggested goal
- `/guidance help` — shows guidance command help

The guidance system tracks progress from your first moments in the game through to defeating the Ender Dragon, automatically advancing as you complete goals.

### /automate Command
Automates common tasks using Baritone pathfinding. Commands are entered in plain English.

- `/automate go to <x> <y> <z>` — walk to specific coordinates
- `/automate go home` — navigate to world spawn
- `/automate mine <material>` — find and mine a resource (e.g. `/automate mine diamonds`)
- `/automate gather <material>` — collect surface resources (e.g. `/automate gather wood`)
- `/automate surface` — find a path back to the surface
- `/automate stop` — cancel whatever automation is running
- `/automate status` — check what automation is currently active

Supported mining targets include: diamonds, iron, gold, coal, redstone, lapis, emerald,
copper, netherite, and all wood types. Plain English names are accepted
(e.g. "diamonds" instead of "minecraft:diamond_ore").

If Baritone is not installed, the mod will still load and all features except automation
will work normally. Automation commands will print an install reminder instead of crashing.

### Target Lock
Press `R` (default) to lock your camera onto the nearest hostile mob. Your view will smoothly track the target until it is defeated, moves out of range, or you press `R` again.

### Guidance Overlay
Press `G` (default) to toggle a small HUD panel in the top-left corner of your screen showing your current suggested goal. Also toggled by `/guidance toggle`.

Both keybindings can be remapped in `Options → Controls → AgingWell`.

---

## Requirements
- Minecraft 1.21.1
- Forge 52.1.14 or higher
- Java 21 or higher
- Baritone `baritone-api-1.11.2.jar` — required for automation features

---

## Installation

1. Download the latest `AgingWell-Automation-1.0.0.jar` from the [Releases](../../releases) page
2. Download `baritone-api-1.11.2.jar` from:
   `https://github.com/cabaletta/baritone/releases`
3. Place **both** jars in your `.minecraft/mods` folder
4. Launch Minecraft using your Forge 1.21.1 profile

If Baritone is not installed, the mod will still load and all features except automation
will work normally. Automation commands will print a friendly install message instead of crashing.

---

## For Developers

### Building from Source
Clone the repository, switch to the `Automation` branch, place `baritone-api-1.11.2.jar`
in the `libs/` folder, then run:

```powershell
.\gradlew build
```

The compiled jar will be at `build/libs/AgingWell-Automation-1.0.0.jar`.

### Running in Development
```powershell
.\gradlew runClient
```

### Project Structure
```
src/main/java/com/AgingWell/
├── AgingWell.java              — mod entry point and event registration
├── Config.java                 — mod configuration
├── ResourceAliases.java        — plain-English to block ID mapping
├── automation/
│   └── BaritoneAdapter.java    — Baritone integration via reflection
├── client/
│   ├── GuidanceOverlay.java    — HUD overlay renderer
│   ├── KeyBindings.java        — hotkey registration
│   └── TargetingSystem.java    — mob camera lock
├── commands/
│   ├── AutomationCommand.java  — /automate command
│   ├── GuidanceCommand.java    — /guidance command
│   └── InfoCommand.java        — /info command
├── guidance/
│   └── GoalAdvisor.java        — goal progression system
└── info/
    ├── BlockInfoDatabase.java  — block descriptions and tips
    ├── EntityInfoDatabase.java — entity descriptions and tips
    ├── InfoLookup.java         — keyword search
    └── ItemInfoDatabase.java   — item descriptions and tips
```

### Adding Content
- **New item descriptions** — add entries to `ItemInfoDatabase.java`
- **New block descriptions** — add entries to `BlockInfoDatabase.java`
- **New entity descriptions** — add entries to `EntityInfoDatabase.java`
- **New goals** — add `Goal` entries to the static block in `GoalAdvisor.java`
- **New automation aliases** — add entries to `ResourceAliases.java`

### Baritone Integration
Baritone is integrated via Java reflection in `BaritoneAdapter.java`. This means the mod
compiles and runs without Baritone present — it only attempts to use it at runtime.
All Baritone calls are wrapped in try/catch blocks so failures are reported to the player
as chat messages rather than crashes.

### No-Automation Version
The `main` branch of this repository contains a version of the mod without Baritone
dependency. All other features are identical. Bug fixes and content additions should be
made on `main` first, then merged into `Automation`.

---

## Planned Features
- Persistent goal progress saved between sessions
- Extended automation commands (farming, smelting queues)
- Automatic resource gathering based on current guidance goal
- Combat automation assistance for hostile mob encounters
- Server-side guidance for multiplayer sessions

---

## Licence
This project is for educational purposes. See `LICENSE` for details.
