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
Automation commands are not available in this version of the mod. A separate version with full automation support via Baritone is available on the `automation` branch of this repository.

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

### Steps
1. Download the latest `AgingWell-1.0.0.jar` from the [Releases](../../releases) page
2. Place it in your `.minecraft/mods` folder
3. Launch Minecraft using your Forge profile

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
