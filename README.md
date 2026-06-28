# AgingWell Assistance Mod

A Minecraft mod designed to make the game more accessible for older or less experienced players. Provides in-game help, guidance, and mob targeting features to support players as they learn the game.

## Features

### /info Command
Get plain-language descriptions of items, blocks, and entities directly in chat.

- `/info` — describes the item currently held in your main hand
- `/info block` — describes the block you are looking at
- `/info entity` — describes the nearest entity
- `/info <name>` — look up a concept by keyword (e.g. `/info hunger`, `/info crafting`)

### /guidance Command
Shows suggested next steps based on your current inventory and progress.

- `/guidance` — prints your current goal in chat
- `/guidance toggle` — pins the goal display to the top-left of your screen
- `/guidance next` — skips to the next suggested goal
- `/guidance help` — shows guidance command help

The guidance system tracks progress from your first moments in the game through to defeating the Ender Dragon, automatically advancing as you complete goals.

### /automate Command
Automates common tasks using Baritone pathfinding. Commands are entered in plain English.

- `/automate go to <x> <y> <z>` — walk to specific coordinates
- `/automate go home` — navigate back to your spawn point
- `/automate mine <material>` — find and mine a resource (e.g. `/automate mine diamonds`)
- `/automate gather <material>` — collect surface resources (e.g. `/automate gather wood`)
- `/automate surface` — find a path back to the surface
- `/automate stop` — cancel whatever automation is running
- `/automate status` — check what automation is currently active

Supported mining targets include: diamonds, iron, gold, coal, redstone, lapis, emerald,
copper, netherite, and all wood types. Plain English names are accepted
(e.g. "diamonds" instead of "minecraft:diamond_ore").

Automation requires Baritone to be installed — see Installation below.

### Target Lock
Press `R` (default) to lock your camera onto the nearest hostile mob. Your view will smoothly track the target until it is defeated, moves out of range, or you press `R` again.

### Guidance Overlay
Press `G` (default) to toggle a small HUD panel in the top-left corner of your screen showing your current suggested goal. Also toggled by `/guidance toggle`.

Both keybindings can be remapped in `Options → Controls → AgingWell`.

---

## Installation

### Requirements
- Minecraft with Forge installed (version compatible with this build)
- Java 25 or higher
- Baritone — required for automation features

### Steps
1. Download the latest mod file from the [Releases](../../releases) page
2. Download baritone from
   `https://github.com/cabaletta/baritone/releases` or from the libs folder.
3. Place **both** jars in your `.minecraft/mods` folder
4. Launch Minecraft using your Forge profile

If Baritone is not installed, the mod will still load and all features except
automation will work normally. Automation commands will print an install reminder.

---

## For Developers

### Building from Source
Clone the repository and run:
```powershell
.\gradlew build
```
The compiled jar will be at `build/libs/AgingWell-1.0.0.jar`.

### Running in Development
```powershell
.\gradlew runClient
```

### Project Structure
```
src/main/java/com/AgingWell/
├── AgingWell.java              — mod entry point and event registration
├── Config.java                 — mod configuration
├── automation/
│   └── BaritoneAdapter.java    — automation stub (no-op in this version)
├── client/
│   ├── GuidanceOverlay.java    — HUD overlay renderer
│   ├── KeyBindings.java        — hotkey registration
│   └── TargetingSystem.java    — mob camera lock
├── commands/
│   ├── AutomationCommand.java  — /automate command
│   ├── BlockInfoDatabase.java  — block descriptions and tips
│   ├── EntityInfoDatabase.java — entity descriptions and tips
│   ├── GuidanceCommand.java    — /guidance command
│   ├── InfoCommand.java        — /info command
│   ├── InfoLookup.java         — keyword search
│   ├── ItemInfoDatabase.java   — item descriptions and tips
│   └── ResourceAliases.java    — plain-English to block ID mapping
└── guidance/
    └── GoalAdvisor.java        — goal progression system
```

### Adding Content
- **New item descriptions** — add entries to `ItemInfoDatabase.java`
- **New block descriptions** — add entries to `BlockInfoDatabase.java`
- **New entity descriptions** — add entries to `EntityInfoDatabase.java`
- **New goals** — add `Goal` entries to the static block in `GoalAdvisor.java`
- **New automation aliases** — add entries to `ResourceAliases.java`

### Automation Version
The `automation` branch of this repository contains a version of the mod with full Baritone pathfinding integration. To build it, clone that branch and place `baritone-api-1.11.2.jar` in the `libs/` folder before building.

---

## Planned Features
- Persistent goal progress saved between sessions
- Voice-friendly chat output formatting
- Larger entity and block description databases
- Server-side guidance for multiplayer sessions

---

## Licence
This project is for educational purposes. See `LICENCE` for details.
