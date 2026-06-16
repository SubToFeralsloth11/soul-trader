# Research: Bug Fix & Polish Pass

**Feature**: `001-bug-fix-and-polish` | **Date**: 2026-06-16

## R1: Health Bonus Implementation

**Decision**: Use `EntityAttributeInstance.addPersistentModifier()` with a UUID-keyed `EntityAttributeModifier` for the full-set health bonus instead of `setBaseValue()`.

**Rationale**: `setBaseValue(40.0)` overwrites the entire attribute base, destroying any health changes from other mods, vanilla equipment, or status effects. A modifier is additive, can be cleanly added/removed when equip/unequip events fire, and is the vanilla-idiomatic approach (used by vanilla armor and effects).

**Alternatives considered**:
- `setBaseValue()` — rejected: incompatible with other mods, causes flickering on every tick
- Capability/component system — rejected: overengineered for a simple health modifier; vanitla attribute system suffices
- Track with a boolean flag and only apply once — rejected: fragile, doesn't handle equip/unequip events

## R2: Server-to-Client Trade Confirmation

**Decision**: Use `ScreenHandler.sendContentUpdates()` to push updated `WhisperData` from server to client after a trade. The client listens via `onPropertyUpdate` and refreshes button states.

**Rationale**: The current implementation optimistically disables buttons on the client before the server confirms. In multiplayer, this means a failed trade (another player traded first) leaves the client in a broken state. `sendContentUpdates()` is the vanilla approach for merchant screens and works without additional networking code.

**Alternatives considered**:
- Custom network packet — rejected: adds unnecessary complexity; screen handler properties do the job
- PropertyDelegate with int-encoded state — considered: works but awkward for ItemStack data; WhisperData can be sent via extended screen handler
- Do nothing (optimistic update) — rejected: multiplayer race condition breaks the UI

## R3: GeckoLib Model Format

**Decision**: Hand-author a GeckoLib `.geo.json` model file programmatically. The model will be a simple bipedal cloaked figure (~1 block tall) with a head cube, body cube, arm/leg cubes, and a separate hood/cloak overlay group.

**Rationale**: No access to Blockbench in this environment. GeckoLib's `.geo.json` format is well-documented JSON with cube definitions, bone hierarchy, and texture UVs. A simple humanoid model is achievable with 7-8 bone groups. The `.animation.json` for idle floating bobbing is equally straightforward.

**Alternatives considered**:
- Java model (current `WhisperModel`) — rejected: no animation support, static pose only
- Keep current model and add cosmetic particle effects only — rejected: user explicitly chose GeckoLib model
- Use a villager/player model scaled down — rejected: doesn't achieve the hooded cloaked look

## R4: Custom Particle Tinting

**Decision**: Register a custom `SimpleParticleType` (`SOUL_TEAL`) with a custom 16x16 particle texture sheet. The texture will be a teal/cyan gradient circle matching the dark mystical palette.

**Rationale**: Vanilla `ParticleTypes.SOUL` uses a hardcoded blue/white texture in the vanilla particle sheet which cannot be tinted at runtime. A custom particle type with its own texture is the standard approach for colored particles in Minecraft mods.

**Alternatives considered**:
- Modify vanilla particle sheet via resource pack — rejected: conflicts with player resource packs and is fragile
- Spawn multiple colored dust particles — rejected: doesn't look like soul fire, performance hit
- Use `ParticleTypes.END_ROD` — considered: closest vanilla match but wrong shape/color

## R5: Sound Asset Creation

**Decision**: Generate simple ambient tone `.ogg` files programmatically using Python. Three sounds: `whisper_ambient.ogg` (soft, eerie wind-chime-like tone, 2-3s loop), `whisper_hurt.ogg` (short discordant tone, 0.5s), `whisper_death.ogg` (fading echo tone, 1s).

**Rationale**: No external sound libraries available. Simple synthesized tones can be generated with Python's `wave` module and converted to OGG with `ffmpeg` or kept as WAV (Minecraft supports WAV). The eerie/mystical tone fits the dark mystical theme. These are basic placeholder sounds that convey the right feel.

**Alternatives considered**:
- Use existing CC0 sound libraries — considered: requires download, may not match theme
- Silence (remove sounds.json) — rejected: user wants custom sounds
- Reuse Minecraft's own ambient cave sounds — rejected: doesn't fit the Whisper's identity

## R6: Programmatic Texture Generation

**Decision**: Generate all 7+ textures programmatically as PNG files at correct resolutions using Python's `struct` + `zlib` modules (already used for placeholders). Each texture follows a precise pixel-art algorithm with the dark mystical palette:
- Palette: `#1a1a2e` (deep navy), `#16213e` (dark blue), `#0f3460` (mid blue), `#e94560` (accent red), `#00d2ff` (cyan/teal highlight)
- Soul: 16×16 glowing orb with radial gradient and 2-frame pulse animation (soul.png + soul.png.mcmeta)
- Armor: Dark navy base with cyan trim following vanilla armor item convention, each piece distinctive shape
- Whisper entity: 64×64 texture for GeckoLib model — dark hooded robe, faint cyan eye dots, subtle fabric folds
- GUI: 256×256 dark panel with subtle border, matching Minecraft's UI convention

**Rationale**: Programmatic generation is the only approach available without image editing tools. The pixel-art resolution (16×16 for items, 64×64 for entity) is manageable with grid-based drawing algorithms.

**Alternatives considered**:
- Keep placeholder solid colors — rejected: user wants "really good assets"
- Download textures from mod resource sites — rejected: copyright concerns, may not match palette
- Use ASCII art descriptions — not a viable alternative

## R7: Second Wind Post-Reduction Damage Check

**Decision**: Move the Second Wind check from `@ModifyVariable` at `HEAD` (pre-reduction) to an `@Inject` at the method tail with `@Local` capture of the post-reduction damage value. The check compares actual health after reduction against the processed damage.

**Rationale**: The current implementation checks `player.getHealth() <= rawDamage` which fires on attacks that wouldn't actually kill due to armor. By checking after all reductions, Second Wind only triggers when the entity would genuinely die.

**Alternatives considered**:
- Keep pre-reduction but multiply by armor factor — rejected: fragile, doesn't account for enchantments, potions, absorption
- Inject at specific point in `damage()` method — considered: requires finding exact injection point in complex method; tail injection is simpler and safer
- Use Fabric `EntityDamageEvents` — considered: doesn't provide post-reduction values easily
