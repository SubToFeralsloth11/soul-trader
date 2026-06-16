# Implementation Plan: Bug Fix & Polish Pass

**Feature**: `001-bug-fix-and-polish` | **Date**: 2026-06-16 | **Spec**: [spec.md](spec.md)

## Summary

Fix 28 bugs identified in a thorough code audit (5 critical, 8 high, 5 medium, 10 low), add custom animated GeckoLib model for the Whisper, create 7+ custom 16x16 pixel-art textures in a dark mystical palette, integrate Cloth Config + ModMenu for in-game settings, add custom sound events with custom audio files, and improve AI behavior. The mod currently compiles but core features (Soulbound, Veil, trade sync) are non-functional or buggy.

## Technical Context

**Language/Version**: Java 21, Minecraft 1.21.5
**Primary Dependencies**: Fabric API 0.114.4+, GeckoLib 4.x (model/animations), Cloth Config API 15.x (config), ModMenu 12.x (config GUI)
**Storage**: JSON config file (Cloth Config), NBT entity data (trade state), JSON enchantment definitions (data-driven)
**Testing**: Manual in-game testing, Fabric runClient gradle task
**Target Platform**: Fabric 1.21.5, dedicated server compatible
**Project Type**: Fabric Minecraft mod (single JAR)
**Performance Goals**: <0.1ms per-player tick overhead for enchantment checks
**Constraints**: Must not break with other mods (no setBaseValue), must work on dedicated server
**Scale/Scope**: ~20 Java source files modified, ~10 new resource files, 1 GeckoLib model, 7+ textures

## Constitution Check

_GATE: constitution from project CLAUDE.md_

Confirm the design honours the standing principles:

| Principle | Status | Notes |
|-----------|--------|-------|
| **I. Fabric-Only** | PASS | GeckoLib and Cloth Config are Fabric-compatible; no Forge/NeoForge code |
| **II. Vanilla-Adjacent Design** | PASS | Dark mystical palette fits Minecraft's soul-fire aesthetic; textures are 16x16 pixel art; GeckoLib model uses vanilla-like proportions |
| **III. Balanced Rarity** | PASS | Config system allows tuning spawn rate; fixes don't change balance — Soulbound now works as intended rather than being broken |
| **IV. Self-Contained** | PASS | Namespace `soultrader` preserved; no registry collisions; config file namespaced under mod ID |
| **V. Client-Server Safe** | PASS | Veil fix uses synchronized position; trade sync uses packet-based confirmation; GeckoLib models work on both sides; sounds registered properly |

| Violation | Why needed | Simpler alternative rejected because |
| --------- | ---------- | ------------------------------------ |
| GeckoLib dependency | Animated model requires a lib; no vanilla animation API | Hand-coded frame-by-frame animation would be 10x the code for worse results |
| Cloth Config + ModMenu deps | In-game config GUI is a stated requirement | Manual JSON editing was rejected by user (P5 user story requires GUI) |

## Project Structure

### Documentation (this feature)

```text
.local/specs/001-bug-fix-and-polish/
├── spec.md
├── plan.md
├── research.md
├── quickstart.md
├── checklists/
│   └── requirements.md
└── tasks.md
```

### Source code (modified files)

```text
src/main/java/com/soultrader/
├── SoulTraderMod.java              (config init, batched tick handler, particle color)
├── SoulTraderModClient.java        (GeckoLib renderer registration)
├── entity/
│   ├── WhisperEntity.java          (base class change, AI fix, custom sounds, FOLLOW_RANGE)
│   ├── WhisperModel.java           (replaced by GeckoLib model)
│   └── WhisperRenderer.java        (GeckoLib renderer)
├── item/
│   ├── ModItems.java               (stack size change)
│   ├── SoulArmorItem.java          (onCraft verify, world.random)
│   └── SoulArmorMaterial.java      (attribute-based health)
├── screen/
│   ├── WhisperScreenHandler.java   (server-confirmed trade sync, multi-slot buy)
│   └── WhisperScreen.java          (button sync fix, new GUI texture)
├── enchantment/
│   ├── ModEnchantments.java        (check registry call)
│   └── SoulEnchantmentHelper.java  (attribute modifier health, batched checks, armor slot predefine)
└── mixin/
    ├── LivingEntityMixin.java      (Second Wind post-reduction, Phoenix full cancel, Gravity)
    ├── PlayerEntityMixin.java      (Soulbound prevent drop, Shadowstep collision check)
    └── CraftResultSlotMixin.java   (actually call applyRandomSoulEnchantment)
```

## Phase 0: Research

All open technical decisions resolved in [research.md](research.md).

Key decisions:
1. Attribute modifier vs setBaseValue for health bonus → `EntityAttributeInstance.addPersistentModifier`
2. Server-to-client trade confirmation → `ScreenHandler.sendContentUpdates()`
3. GeckoLib model format → `.geo.json` hand-authored (no Blockbench access)
4. Custom particle tinting → Register custom `SimpleParticleType` with teal texture
5. Sound asset creation → Programmatic `.ogg` synthesis via Python `pydub` or tone generation
6. Texture creation → Programmatic 16x16 PNG generation with palette algorithm
7. Post-reduction damage check for Second Wind → `@Inject` at method tail with `@Local` capture

## Phase 1: Design & Contracts

- **Data model** (`data-model.md`): Config schema, NBT trade state, enchantment JSON structure
- **Quickstart** (`quickstart.md`): End-to-end build and test instructions
