# Feature Specification: Bug Fix & Polish Pass

**Feature**: `001-bug-fix-and-polish`
**Created**: 2026-06-16
**Status**: Draft

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Critical Bugs Are Fixed (Priority: P1)

A player wearing Soulbound armor dies — the armor stays in their inventory instead of dropping. A player with full Soul Armor does not have their base health overwritten every tick, remaining compatible with other mods. The Veil enchantment activates invisibility when standing still, even on a dedicated server. The trade GUI correctly validates items across all inventory slots (not just per-slot), and trade buttons do not disable prematurely before the server confirms the trade.

**Why this priority**: These are game-breaking bugs that make core features (Soulbound, Veil, trading) non-functional or actively harmful. Without fixes, the mod is broken.

**Independent Test**: Load the mod on a dedicated server. Kill a player with Soulbound armor — armor stays. Stand still with Veil — invisibility applies. Trade with a Whisper using split stacks — trade succeeds. Buttons disable only after server confirms.

**Acceptance Scenarios**:

1. **Given** a player wearing Soulbound-enchanted armor, **When** they die, **Then** the armor stays in their post-death inventory (not dropped on ground).
2. **Given** a player wearing full Soul Armor, **When** another mod applies a +10 health modifier, **Then** the player's health is not reset to 20.0 every tick.
3. **Given** a player with Veil enchantment on a dedicated server, **When** they stand still for 3 seconds, **Then** they gain Invisibility I.
4. **Given** a player with 3 golden apples in slot 0 and 3 in slot 7, **When** they click Buy Soul (cost: 5 golden apples), **Then** the trade succeeds, consuming 2 from one stack and 3 from the other.
5. **Given** two players interacting with the same Whisper, **When** Player A clicks Buy and the server accepts, **Then** Player B's GUI does not show a SOLD OUT button for a trade that never completed.

---

### User Story 2 - High-Priority Bugs Are Fixed (Priority: P2)

Second Wind prevents death at the right moment (after damage reduction, not before). Shadowstep does not teleport players into walls or the void. Custom sounds play when the Whisper is nearby instead of Wandering Trader sounds. The CraftResultSlot mixin actually applies random enchantments. Per-tick enchantment checks are efficient and batched.

**Why this priority**: These bugs make enchantment effects unreliable (Second Wind, Shadowstep, Phoenix) or degrade the player experience (wrong sounds, dead code). Fixing them makes the mod feel polished and functional.

**Independent Test**: Craft Soul Armor — it gets a random enchantment. Stand near a Whisper — custom ambient sounds play (not Wandering Trader). Teleport with Shadowstep — never ends up in a wall. Take near-fatal damage with Second Wind — triggers correctly at the kill threshold, not before.

**Acceptance Scenarios**:

1. **Given** a player with Second Wind at 10 HP taking 12 raw damage (reduced to 6 by armor), **When** the damage is processed, **Then** Second Wind does NOT trigger (the player would survive).
2. **Given** a player with Second Wind at 1 HP taking fatal damage, **When** the damage would kill them, **Then** Second Wind triggers, setting health to 1 HP with a 5-minute cooldown.
3. **Given** a player with Shadowstep sneaking and jumping, **When** they teleport, **Then** the destination is checked for solid blocks, lava, and void — if blocked, teleport silently fails.
4. **Given** a Whisper is within earshot, **When** the player approaches, **Then** custom `soultrader:entity.whisper.ambient` sounds play, not Wandering Trader sounds.
5. **Given** a player crafting Soul Armor in a crafting table, **When** they take the result, **Then** the armor piece has exactly one random unique enchantment applied.

---

### User Story 3 - Medium/Low Bugs & Polish Features (Priority: P3)

The Whisper no longer flees from players trying to trade but instead stays in place and becomes interactable when approached. All enchantment fallback text is consistently capitalized. The `PassiveEntity` base class is changed to `PathAwareEntity`. Configurable spawn rate and trade costs via a config file. Blinding Aura only scans every 10 ticks instead of every tick. Soul stack size increased to allow carrying multiple. Phoenix enchantment fully cancels fire damage including side effects.

**Why this priority**: These are quality-of-life fixes that improve the feel of the mod without changing core functionality. They remove friction from the player experience.

**Independent Test**: Approach a Whisper — it does not flee but waits. Open config — spawn rate and trade costs are modifiable. Craft Soul Armor — enchantment text is properly capitalized. Stack Souls — up to 64 per slot. Take fire damage with Phoenix — no knockback or side effects applied.

**Acceptance Scenarios**:

1. **Given** a player approaching a Whisper within 10 blocks, **When** they get close, **Then** the Whisper stops moving and becomes directly interactable (does not flee).
2. **Given** a Whisper with a FOLLOW_RANGE of 16 (not 35), **When** a player is 20 blocks away, **Then** the Whisper does not register the player for AI flee.
3. **Given** the mod config file, **When** a player changes `spawnRate` from 0.5 to 0.25, **Then** Whispers spawn at 25% of Wandering Trader rate.
4. **Given** a player wearing Phoenix-enchanted armor standing in fire, **When** fire damage ticks, **Then** the player takes zero damage, zero knockback, and is healed for the damage amount.

---

### User Story 4 - Custom Visual Assets (Priority: P4)

The Whisper has a proper GeckoLib animated model — a hooded, cloaked figure ~1 block tall with glowing cyan eyes and floating idle animation. All items (Soul, Soul Armor pieces) have custom 16x16 pixel-art textures in a dark mystical palette. The trade GUI has a dark-themed background with soul-fire styling. Soul flame particles are recolored to teal/cyan.

**Why this priority**: Assets define first impressions. Placeholder gray/purple squares make the mod look unfinished. Custom textures, an animated model, and a themed GUI make the mod feel complete and professional.

**Independent Test**: Spawn a Whisper — visible as a hooded cloaked figure with a gentle floating animation and cyan eye glow. Open creative inventory — Soul is a glowing teal orb icon, armor pieces are dark with cyan trim. Open trade GUI — dark mystical-themed background.

**Acceptance Scenarios**:

1. **Given** a Whisper spawned in the world, **When** a player looks at it, **Then** they see a small hooded cloaked figure (~1 block tall) with barely visible glowing cyan eyes, floating slightly with a bobbing idle animation.
2. **Given** a Soul item in inventory, **When** the player views it, **Then** it displays as a glowing teal/cyan orb with a subtle pulse animation.
3. **Given** Soul Armor equipped on a player, **When** viewed in 3rd person or inventory, **Then** each piece is dark with cyan trim in the vanilla-adjacent palette.
4. **Given** the Whisper trade GUI open, **When** the player looks at it, **Then** it has a dark mystical-themed background (not a solid gray rectangle).
5. **Given** a Whisper disappearing, **When** it vanishes, **Then** teal/cyan soul particles (not vanilla blue soul-fire) burst outward.

---

### User Story 5 - Config & ModMenu Integration (Priority: P5)

Players can edit mod settings through an in-game ModMenu config screen. Settings include Whisper spawn rate, Soul buy price ranges, trade reward lists, and debug toggles. Changes take effect on world reload.

**Why this priority**: Config is the final polish step. It lets server owners and players tune the mod to their balance preferences without editing files manually.

**Independent Test**: Install ModMenu. Navigate to Soul Trader config. Change spawn rate to 1.0. Verify Whispers spawn as often as Wandering Traders. Change buy cost to require Netherite Blocks. Verify Whisper offers the new price.

**Acceptance Scenarios**:

1. **Given** ModMenu installed, **When** the player opens the mod list, **Then** "Soul Trader" appears with a config button.
2. **Given** the config screen open, **When** the player changes `spawnChance` to 0.25, **Then** Whispers spawn at 25% of Wandering Trader rate after world reload.

---

### Edge Cases

- What happens if a player with Soulbound dies in the void (below Y=-64)? The armor should still be retained.
- What happens if two players click the same Whisper's buy button simultaneously on a server? Only the first trade processed should succeed; the second should silently fail without disabling the second player's button.
- What happens if a player unequips a piece of Soul Armor while the full-set bonus is active? The double health should be immediately removed (health clamped to 20 max).
- What happens if GeckoLib is not installed? The mod should fall back to the current biped model or gracefully fail with a clear dependency error.
- What happens if a player's Soul Armor runs out of durability? The random enchantment should persist through repair.
- What happens when the config file is missing or corrupted? Sensible defaults (current hardcoded values) should be used.
- What happens with Phoenix enchantment and the `/damage` command or modded fire damage? Only vanilla fire damage sources should be negated.

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: Soulbound-enchanted armor MUST remain in the player's inventory after death.
- **FR-002**: The full-set health bonus MUST use attribute modifiers (not `setBaseValue`) to avoid overwriting other mods' health changes.
- **FR-003**: Veil enchantment MUST use synchronized player position (`prevX/prevY/prevZ`) for the stillness check, not client-only `lastRenderX/Y/Z`.
- **FR-004**: The buy trade logic MUST check the player's entire inventory for the total required item count, not just per-slot counts.
- **FR-005**: Trade buttons MUST only disable after server confirmation of trade success.
- **FR-006**: Second Wind MUST trigger based on post-reduction damage, not raw pre-reduction damage.
- **FR-007**: Shadowstep teleport MUST validate the destination for solid blocks, lava, and void before executing.
- **FR-008**: The Whisper MUST use custom `soultrader` namespace sound events, not vanilla Wandering Trader sounds.
- **FR-009**: The CraftResultSlot mixin MUST call `SoulArmorItem.applyRandomSoulEnchantment()` on craft.
- **FR-010**: Per-tick enchantment checks (Veil, Blinding Aura, full set) MUST be batched into a single pass per player.
- **FR-011**: Phoenix enchantment MUST fully cancel all fire damage effects (damage, knockback, burn ticks).
- **FR-012**: The Whisper MUST NOT flee from players when within interaction range; it MUST stay and become interactable.
- **FR-013**: The Whisper entity base class MUST be `PathAwareEntity`, not `PassiveEntity`.
- **FR-014**: The FOLLOW_RANGE attribute MUST be 16.0 (not 35.0).
- **FR-015**: All enchantment fallback strings MUST use consistent English capitalization.
- **FR-016**: Blinding Aura entity scans MUST be throttled to every 10 ticks (not every tick).
- **FR-017**: Soul item max stack size MUST be configurable or raised to a reasonable default (e.g., 16 or 64).
- **FR-018**: The Whisper MUST have a GeckoLib animated model (hooded cloaked figure, ~1 block tall, cyan eyes, floating idle animation).
- **FR-019**: All items MUST have custom 16x16 pixel-art textures in the dark mystical (deep purple/blue + cyan/soul-fire) palette.
- **FR-020**: The trade GUI MUST have a custom dark-themed background texture.
- **FR-021**: Soul flame particles MUST be teal/cyan in color (not vanilla blue).
- **FR-022**: The mod MUST integrate with Cloth Config and ModMenu for in-game settings editing.
- **FR-023**: Configurable settings MUST include: Whisper spawn rate, Soul buy cost, trade reward pool, and debug toggles.
- **FR-024**: Soul Armor crafting MUST apply exactly one random unique enchantment per piece.
- **FR-025**: The Whisper screen MUST NOT close prematurely when one trade offer is still available.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: All 5 critical bugs and 8 high-priority bugs from the bug audit are resolved.
- **SC-002**: Soulbound armor is retained across 100 consecutive player deaths with zero items dropped.
- **SC-003**: The mod causes zero `setBaseValue` health overwrites per tick on players without full Soul Armor.
- **SC-004**: Veil activates within 3 seconds of standing still on both singleplayer and dedicated server.
- **SC-005**: Shadowstep teleports fail gracefully (no death) on 100 consecutive attempts against walls/lava/void.
- **SC-006**: The Whisper model renders with GeckoLib animations and custom textures visible from all angles.
- **SC-007**: All 6 item textures and 1 GUI texture are recognized as vanilla-adjacent in the dark mystical palette by a blind taste test.
- **SC-008**: Config changes made in ModMenu take effect after world reload without requiring client restart.
- **SC-009**: Per-tick performance overhead for enchantment checks is under 0.1ms per player.

## User Interface

- **Whisper trade screen**: Custom dark-themed GUI with soul-fire styling, buy/sell buttons, item render slots, "sold out" indicators. Wireframe: N/A (existing screen being re-skinned).
- **ModMenu config screen**: Standard Cloth Config generated screen with categories for Spawning, Trading, and Debug.

## Assumptions

- GeckoLib 4.x+ is compatible with Fabric 1.21.5 and will be added as a dependency.
- Cloth Config API and ModMenu are compatible with Fabric 1.21.5 and will be added as optional dependencies.
- The vanilla Minecraft soul-fire particle (`ParticleTypes.SOUL`) can be color-tinted or replaced with a custom particle type for teal/cyan coloring.
- Custom sounds will be sourced from existing free sound libraries (CC0-licensed) or synthesized programmatically.
- The Whispter's custom model will be hand-authored programmatically as a GeckoLib `.geo.json` file since we cannot use Blockbench in this environment.
- Textures will be generated programmatically at 16x16 pixel resolution with the dark mystical palette.
