# Tasks: Bug Fix & Polish Pass

**Feature**: `001-bug-fix-and-polish` | **Date**: 2026-06-16

## Dependencies & Execution Order

Each phase depends on the previous phase completing. Tasks marked `[P]` within a phase can run in parallel. Tasks marked `[US1]` through `[US5]` map to user stories.

```
Setup → US1 (Critical) → US2 (High) → US3 (Medium/Low) → US4 (Assets) → US5 (Config) → Polish
```

---

## Phase 0: Setup & Dependencies

- [ ] T001 Add GeckoLib dependency to build.gradle
- [ ] T002 Add Cloth Config API dependency to build.gradle
- [ ] T003 Add ModMenu dependency to build.gradle
- [ ] T004 Update fabric.mod.json with new dependency entries and GeckoLib entrypoint

---

## Phase 1: US1 - Critical Bug Fixes

### Soulbound (Prevent Item Drop)

- [ ] T005 [US1] In `src/main/java/com/soultrader/mixin/PlayerEntityMixin.java`: change `dropInventory` injection from `@Inject` to `@Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)`. Instead of repairing armor, iterate armor slots, remove Soulbound pieces from inventory BEFORE the super call drops them, and store them in a list. After super call returns, re-add the saved pieces to the player's inventory.
- [ ] T006 [P] [US1] Test: Run `/kill` while wearing Soulbound armor, verify armor stays in inventory after respawn.

### Full-Set Health (Attribute Modifier)

- [ ] T007 [US1] In `src/main/java/com/soultrader/enchantment/SoulEnchantmentHelper.java`: replace `setBaseValue(20.0/40.0)` calls in `applyFullSetBonus()` with `addPersistentModifier` / `removeModifier` pattern. Create a `UUID` constant for the modifier. Apply when full set is worn, remove when not. Clamp current health to new max when modifier is removed.
- [ ] T008 [P] [US1] In `src/main/java/com/soultrader/SoulTraderMod.java`: change `applyFullSetBonus` call site to only run on equipment change events (`EntityEquipmentChangeCallback` from Fabric API), not every tick.
- [ ] T009 [P] [US1] Test: Equip full Soul Armor while another health-modifying mod/effect is active. Verify health is additive, not overwritten.

### Veil (Synchronized Position)

- [ ] T010 [US1] In `src/main/java/com/soultrader/SoulTraderMod.java`: change Veil stillness check from `player.lastRenderX/Y/Z` to `player.prevX/prevY/prevZ` (or manually track last position in a `HashMap<UUID, Vec3d>` within the mod class).
- [ ] T011 [P] [US1] Test: On dedicated server, equip Veil armor and stand still for 3 seconds. Verify invisibility applies.

### Multi-Slot Buy Trade

- [ ] T012 [US1] In `src/main/java/com/soultrader/screen/WhisperScreenHandler.java`: rewrite `processBuySoul()` to accumulate total matching items across all inventory slots, check if total >= required, then decrement from multiple slots starting from the end of inventory.
- [ ] T013 [P] [US1] Test: Place 3 golden apples in slot 0 and 3 in slot 7. Buy Soul (cost: 5). Verify trade succeeds and 2+3 are consumed.

### Server-Confirmed Trade Sync

- [ ] T014 [US1] In `src/main/java/com/soultrader/screen/WhisperScreenHandler.java`: implement server-to-client sync of `WhisperData` after trade. On `onButtonClick` success, update syncedData and call `world.playSound` or add a property delegate.
- [ ] T015 [US1] In `src/main/java/com/soultrader/screen/WhisperScreen.java`: remove optimistic button disable from button callbacks. Instead, disable buttons based on `handler.isBuyOfferUsed()` / `handler.isSellOfferUsed()` state. Add `tick()` override to check handler state and update button active flags.
- [ ] T016 [P] [US1] Test: Two clients connect. One trades with Whisper. Verify second client's screen does not show SOLD OUT prematurely.

---

## Phase 2: US2 - High-Priority Bug Fixes

### Second Wind (Post-Reduction Check)

- [ ] T017 [US2] In `src/main/java/com/soultrader/mixin/LivingEntityMixin.java`: move Second Wind check from `@ModifyVariable` at HEAD to `@Inject` at `damage()` tail. Use `@Local` to capture the final post-reduction damage value. Check `getHealth() - finalDamage <= 0` for the kill threshold.
- [ ] T018 [P] [US2] Test: Give player diamond armor (high protection) and 10 HP. Apply damage that raw = 12 but reduced = 6. Verify Second Wind does NOT trigger. Reduce armor to none. Apply damage that kills. Verify Second Wind triggers.

### Shadowstep (Collision Validation)

- [ ] T019 [US2] In `src/main/java/com/soultrader/mixin/PlayerEntityMixin.java`: add destination validation in `onShadowstep`. Check: target block is passable (non-solid), block below target is solid (no void drop), target block is not lava. If any check fails, cancel teleport and play a quiet failure sound.
- [ ] T020 [P] [US2] Test: Face wall at 1 block distance, sneak+jump. Verify no teleport. Face cliff, sneak+jump. Verify no teleport. Face open ground, sneak+jump. Verify teleport succeeds.

### Custom Sound Events

- [ ] T021 [US2] In `src/main/java/com/soultrader/SoulTraderMod.java` or a new `ModSounds.java`: register 3 `SoundEvent` instances (`WHISPER_AMBIENT`, `WHISPER_HURT`, `WHISPER_DEATH`) in the `soultrader` namespace.
- [ ] T022 [P] [US2] In `src/main/java/com/soultrader/entity/WhisperEntity.java`: update `getAmbientSound()`, `getHurtSound()`, `getDeathSound()` to return the new custom sound events.
- [ ] T023 [P] [US2] Update `src/main/resources/assets/soultrader/sounds.json` to point to the correct `.ogg` filenames.
- [ ] T024 [P] [US2] Generate placeholder `.ogg` sound files in `src/main/resources/assets/soultrader/sounds/`.

### CraftResultSlotMixin (Apply Enchantment)

- [ ] T025 [US2] In `src/main/java/com/soultrader/mixin/CraftResultSlotMixin.java`: add call to `SoulArmorItem.applyRandomSoulEnchantment(stack)` inside the existing `if` block.
- [ ] T026 [P] [US2] Test: Craft Soul Helmet in crafting table. Verify it has exactly one unique random enchantment.

### Batched Per-Tick Checks

- [ ] T027 [US2] In `src/main/java/com/soultrader/SoulTraderMod.java`: create a single `END_WORLD_TICK` handler that iterates players ONCE, checking all enchantments (Blinding Aura, Veil, full-set) in one pass, caching registry lookups.
- [ ] T028 [P] [US2] In `src/main/java/com/soultrader/enchantment/SoulEnchantmentHelper.java`: predefine `ARMOR_SLOTS` list, add batched-check utility method that returns a bitmask of active enchantments per player per tick.
- [ ] T029 [P] [US2] Test: Join server with 10 players wearing various enchantments. Verify no tick lag.

### Phoenix (Full Fire Cancel)

- [ ] T030 [US2] In `src/main/java/com/soultrader/mixin/LivingEntityMixin.java`: rewrite Phoenix handler to use `@Inject(method = "damage", at = @At("HEAD"), cancellable = true)` for fire damage types. Cancel the entire damage call (not just zeroing amount), heal the entity, and also clear fire ticks on the entity.
- [ ] T031 [P] [US2] Test: Stand in fire with Phoenix armor. Verify zero damage, zero knockback, burning effect immediately extinguished, health instead increases slightly.

### Wrong Base Class & FOLLOW_RANGE

- [ ] T032 [US2] In `src/main/java/com/soultrader/entity/WhisperEntity.java`: change `extends PassiveEntity` to `extends PathAwareEntity`. Remove `isBaby()` and `createChild()` overrides. Update constructor and generics in `FabricEntityTypeBuilder`.
- [ ] T033 [P] [US2] In `src/main/java/com/soultrader/entity/WhisperEntity.java`: change `FOLLOW_RANGE` from `35.0` to `16.0`.
- [ ] T034 [P] [US2] Test: Spawn Whisper, walk away to 20 blocks. Verify it no longer detects/tracks the player.

---

## Phase 3: US3 - Medium/Low Bugs & Polish

### Whisper AI (Don't Flee)

- [ ] T035 [US3] In `src/main/java/com/soultrader/entity/WhisperEntity.java`: remove `FleeEntityGoal` from goal selector. Add a `StayCloseToPlayerGoal` or keep `WanderAroundFarGoal` only. Ensure right-click interaction still works without chasing.
- [ ] T036 [P] [US3] Test: Approach a Whisper within 3 blocks. Verify it stays in place and the trade GUI opens on right-click.

### Enchantment Fallback Strings

- [ ] T037 [P] [US3] In all 10 files under `src/main/resources/data/soultrader/enchantment/`: standardize the `description` fallback string to use proper English capitalization (e.g., "Soul Siphon", "Ender Mind").

### Blinding Aura Throttle

- [ ] T038 [US3] In `src/main/java/com/soultrader/SoulTraderMod.java`: add a `playerTickCounter` map. Only run Blinding Aura entity scan every 10 ticks per player.
- [ ] T039 [P] [US3] Test: Verify mobs around a Blinding Aura player still get slowness applied (every ~0.5 seconds is barely noticeable).

### Soul Stack Size

- [ ] T040 [US3] In `src/main/java/com/soultrader/item/ModItems.java`: change Soul item `maxCount(1)` to `maxCount(64)`.

### Sell Reward Lazy Generation

- [ ] T041 [US3] In `src/main/java/com/soultrader/entity/WhisperEntity.java`: move sell reward generation from lazy getter to `onInitialSpawn` or constructor. Remove side effect from `getSellReward()`.

### Duplicate Cost Logic

- [ ] T042 [US3] In `src/main/java/com/soultrader/screen/WhisperScreenHandler.java`: delegate `getBuyOfferCost()` to `whisper.getBuyOfferCost()` instead of duplicating the switch statement.

### Screen Premature Close

- [ ] T043 [US3] In `src/main/java/com/soultrader/screen/WhisperScreen.java`: remove the `if (!otherButton.active) this.close()` lines from button callbacks. The screen should only close when BOTH offers are used, checked via handler state.

### Cooldown API Fix

- [ ] T044 [P] [US3] In `src/main/java/com/soultrader/mixin/LivingEntityMixin.java` and `PlayerEntityMixin.java`: verify `isCoolingDown()` and `set()` calls use `ItemStack` parameter type (not `Item`). Fix if needed.

---

## Phase 4: US4 - Custom Visual Assets

### Pixel Art Textures

- [ ] T045 [US4] Generate `src/main/resources/assets/soultrader/textures/item/soul.png` (16×16 glowing teal orb with radial gradient and highlight).
- [ ] T046 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/item/soul_helmet.png` (16×16 dark navy helmet with cyan trim).
- [ ] T047 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/item/soul_chestplate.png` (16×16 dark navy chestplate with cyan trim).
- [ ] T048 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/item/soul_leggings.png` (16×16 dark navy leggings with cyan trim).
- [ ] T049 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/item/soul_boots.png` (16×16 dark navy boots with cyan trim).
- [ ] T050 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/entity/whisper.png` (64×64 dark hooded cloak texture for GeckoLib model, with faint cyan eye glow).
- [ ] T051 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/gui/whisper_trade.png` (256×256 dark mystical themed GUI background).
- [ ] T052 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/models/armor/soul_layer_1.png` (64×32 dark armor layer 1 with cyan trim).
- [ ] T053 [P] [US4] Generate `src/main/resources/assets/soultrader/textures/models/armor/soul_layer_2.png` (64×32 dark armor layer 2 with cyan trim).
- [ ] T054 [P] [US4] Add `.mcmeta` animation file for Soul item texture to give it a subtle pulse glow.

### GeckoLib Model

- [ ] T055 [US4] Create `src/main/resources/assets/soultrader/geo/whisper.geo.json` — GeckoLib model with bone structure: head (with hood overlay), body, left_arm, right_arm, left_leg, right_leg. All cubes ~1 block total height. Hood/hat bone as separate layer for cyan eye glow.
- [ ] T056 [US4] Create `src/main/resources/assets/soultrader/animations/whisper.animation.json` — idle floating bobbing animation (gentle Y-axis oscillation, slight arm sway).
- [ ] T057 [US4] In `src/main/java/com/soultrader/entity/WhisperModel.java`: replace `BipedEntityModel` with GeckoLib's `GeoModel` implementation. Wire `.geo.json` and texture.
- [ ] T058 [US4] In `src/main/java/com/soultrader/entity/WhisperRenderer.java`: extend `GeoEntityRenderer<WhisperEntity>`. Register with GeckoLib's `GeoRenderLayer`.
- [ ] T059 [P] [US4] In `src/main/java/com/soultrader/SoulTraderModClient.java`: update model layer registration to GeckoLib pattern.

### Custom Particles

- [ ] T060 [US4] Register custom `SimpleParticleType` `SOUL_TEAL` in `SoulTraderMod.java`.
- [ ] T061 [US4] Create or reference a custom particle texture at `assets/soultrader/textures/particle/soul_teal.png` (teal/cyan gradient circle, 8×8 or 16×16).
- [ ] T062 [US4] In `src/main/java/com/soultrader/entity/WhisperEntity.java`: replace `ParticleTypes.SOUL` with the custom teal particle in the disappear effect.

### Custom Sounds

- [ ] T063 [US4] Generate `whisper_ambient.ogg` — soft eerie ambient tone, 2-3 seconds.
- [ ] T064 [P] [US4] Generate `whisper_hurt.ogg` — short discordant tone, 0.5 seconds.
- [ ] T065 [P] [US4] Generate `whisper_death.ogg` — fading echo tone, 1 second.

---

## Phase 5: US5 - Config & ModMenu

### Cloth Config

- [ ] T066 [US5] Create `src/main/java/com/soultrader/config/ModConfig.java` — Cloth Config class with categories:
  - Spawning: `spawnChance` (double, 0.0-2.0, default 0.5), `despawnMinutes` (int, 5-60, default 25)
  - Trading: `buyCostTypeWeight` (int list for random weights), `sellRewardList` (string list)
  - Debug: `debugLogging` (boolean, default false)
- [ ] T067 [US5] In `src/main/java/com/soultrader/SoulTraderMod.java`: load config on init, use config values in spawn handler and trade logic.

### ModMenu Integration

- [ ] T068 [US5] Create `src/main/java/com/soultrader/config/ModMenuIntegration.java` — implements `ModMenuApi`, returns the Cloth Config screen factory.
- [ ] T069 [US5] Update `fabric.mod.json` entrypoints to include `"modmenu": ["com.soultrader.config.ModMenuIntegration"]`.
- [ ] T070 [P] [US5] Test: Install ModMenu, open mod list, click Soul Trader, verify config screen opens with all categories.

---

## Phase 6: Polish & Final Verification

- [ ] T071 Build mod with `JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./gradlew build`, verify 0 errors.
- [ ] T072 Run `runClient` and visually verify: Whisper model renders with animation, textures are custom (not placeholder solid colors), GUI has dark background, particles are teal.
- [ ] T073 Test full set bonus: equip 4/4 Soul Armor, verify double health and Resistance I, un-equip one piece, verify bonus removed.
- [ ] T074 Test all 10 enchantments individually (or verify mixins compile correctly for each).
- [ ] T075 Commit and push to GitHub.

---

## Summary

| Phase | Tasks | Story |
|-------|-------|-------|
| Setup | T001-T004 | — |
| US1 Critical | T005-T016 | Soulbound, Health, Veil, Multi-slot, Trade Sync |
| US2 High | T017-T034 | Second Wind, Shadowstep, Sounds, Mixin, Batched, Phoenix, Base Class, Range |
| US3 Medium/Low | T035-T044 | AI, Strings, Aura, Stack Size, Lazy Gen, Duplicate, Close |
| US4 Assets | T045-T065 | Textures, GeckoLib Model, Particles, Sounds |
| US5 Config | T066-T070 | Cloth Config, ModMenu |
| Polish | T071-T075 | Build, Visual Check, Enchantment Test, Push |
