# Quickstart: Bug Fix & Polish Pass

**Feature**: `001-bug-fix-and-polish` | **Date**: 2026-06-16

## Prerequisites

- Java 21 (`/opt/homebrew/opt/openjdk@21`)
- Python 3 (for texture generation)
- Internet access (for Gradle dependency download)

## Build & Run

```bash
cd /Users/lastminuteplays/Code/soul-trader
JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./gradlew build
```

To test in-game:
```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./gradlew runClient
```

## End-to-End Validation Scenarios

### 1. Soulbound Works

1. Craft or /give Soul Armor with Soulbound enchantment
2. Equip the armor
3. `/kill` the player
4. After respawn, verify Soulbound armor is in inventory (not on death pile)
5. Repeat 5+ times to verify consistency

### 2. Veil Works on Server

1. Open a dedicated server with the mod installed
2. Connect a client, equip Veil-enchanted armor
3. Stand perfectly still for 3 seconds
4. Verify Invisibility I appears
5. Move — verify it disappears
6. Stand still again — verify it reappears

### 3. Trade Validation

1. Fill inventory with Diamond Blocks split across multiple stacks
2. Find a Whisper offering Diamond Block trade
3. Click Buy Soul — trade succeeds even with split stacks
4. Verify only one button per Whisper; second click is blocked by server

### 4. Shadowstep Safety

1. Equip Shadowstep boots
2. Stand facing a solid wall 1 block away
3. Sneak + jump — verify teleport fails silently (wall blocked)
4. Stand near cliff edge
5. Sneak + jump — verify teleport fails silently (void blocked)

### 5. Visual Check

1. Spawn a Whisper — verify hooded cloak model with floating animation
2. Open creative menu — verify Soul is teal orb, armor pieces are dark with cyan trim
3. Open trade GUI — verify dark mystical background
4. Complete a trade — verify teal particles on Whisper disappearance

### 6. Config Check

1. Install ModMenu
2. Open ModMenu → find Soul Trader → click config
3. Change spawn rate to 1.0
4. Reload world — verify Whispers spawn as frequently as Wandering Traders
5. Change buy cost item type — verify Whisper offers new price
