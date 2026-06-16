# Soul Trader Mod

A Fabric mod for Minecraft 1.21.x that adds a rare cloaked NPC called the Whisper. Whispers sell Souls (a powerful crafting material) and buy Souls in exchange for rare items. Souls craft into Soul Armor — the best armor tier, each piece gaining a random unique enchantment.

## Constitution

### Core Principles

#### I. Fabric-Only

All code MUST target the Fabric modloader (Minecraft 1.21.x). No Forge, NeoForge, or Quilt-specific code. The sole required dependency is Fabric API — no external mod dependencies beyond it.

#### II. Vanilla-Adjacent Design

All additions (mobs, items, enchantments, GUIs) MUST feel like they belong in vanilla Minecraft. Textures use the vanilla art palette. Mob AI follows vanilla patterns. No effect may trivialize core game mechanics without proportional cost.

#### III. Balanced Rarity

Spawn rates and trade costs MUST be tuned so content feels special but attainable. No item or mob may have a >1% encounter chance without explicit justification. Soul Armor's power is gated behind rarity, cost, and the one-trade-per-Whisper limitation.

#### IV. Self-Contained

The mod MUST NOT register duplicate items, entities, or enchantments from other mods. Use unique mod ID (`soultrader`) and namespace for all registry objects. Avoid naming collisions.

#### V. Client-Server Safe

All custom entities, enchantments, and GUI interactions MUST function correctly in multiplayer. Networking packets MUST be used where client-server synchronization is needed. Visual effects MUST degrade gracefully on server side.
