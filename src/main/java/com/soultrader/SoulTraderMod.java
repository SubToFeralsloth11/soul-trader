package com.soultrader;

import com.soultrader.enchantment.ModEnchantments;
import com.soultrader.enchantment.SoulEnchantmentHelper;
import com.soultrader.entity.WhisperEntity;
import com.soultrader.item.ModItems;
import com.soultrader.screen.WhisperScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SoulTraderMod implements ModInitializer {
    public static final String MOD_ID = "soultrader";

    public static final RegistryKey<EntityType<?>> WHISPER_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, "whisper"));
    public static final EntityType<WhisperEntity> WHISPER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "whisper"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WhisperEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.0f))
                    .trackRangeChunks(8)
                    .build(WHISPER_KEY)
    );

    public static final ExtendedScreenHandlerType<WhisperScreenHandler, WhisperScreenHandler.WhisperData> WHISPER_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(MOD_ID, "whisper_trade"),
            new ExtendedScreenHandlerType<>(WhisperScreenHandler::new, WhisperScreenHandler.WhisperData.PACKET_CODEC)
    );

    public static final RegistryKey<ItemGroup> SOUL_TRADER_GROUP_KEY = RegistryKey.of(
            RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "soul_trader_group")
    );

    public static final ItemGroup SOUL_TRADER_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.SOUL))
            .displayName(Text.translatable("itemGroup.soultrader.soul_trader_group"))
            .build();

    @Override
    public void onInitialize() {
        ModItems.register();
        ModEnchantments.register();
        Registry.register(Registries.ITEM_GROUP, SOUL_TRADER_GROUP_KEY, SOUL_TRADER_GROUP);

        ItemGroupEvents.modifyEntriesEvent(SOUL_TRADER_GROUP_KEY).register(entries -> {
            entries.add(ModItems.SOUL);
            entries.add(ModItems.SOUL_HELMET);
            entries.add(ModItems.SOUL_CHESTPLATE);
            entries.add(ModItems.SOUL_LEGGINGS);
            entries.add(ModItems.SOUL_BOOTS);
        });

        FabricDefaultAttributeRegistry.register(WHISPER, WhisperEntity.createWhisperAttributes());

        registerEventHandlers();
    }

    private void registerEventHandlers() {
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                applyFullSetBonus(player);

                if (SoulEnchantmentHelper.hasSoulEnchantment(player, ModEnchantments.BLINDING_AURA)) {
                    Box box = player.getBoundingBox().expand(4.0);
                    List<LivingEntity> nearby = world.getEntitiesByClass(LivingEntity.class, box, e -> e != player && e instanceof HostileEntity);
                    for (LivingEntity entity : nearby) {
                        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0, false, false));
                    }
                }
            }

            trySpawnWhisper(world);
        });
    }

    private void applyFullSetBonus(ServerPlayerEntity player) {
        boolean hasFullSet = SoulEnchantmentHelper.isWearingFullSoulArmor(player);
        SoulEnchantmentHelper.applyFullSetBonus(player);

        if (hasFullSet && SoulEnchantmentHelper.hasSoulEnchantment(player, ModEnchantments.VEIL)) {
            Vec3d currentPos = player.getPos();
            Vec3d lastPos = new Vec3d(player.lastRenderX, player.lastRenderY, player.lastRenderZ);
            if (currentPos.squaredDistanceTo(lastPos) < 0.01) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 40, 0, false, false));
            }
        }
    }

    private void trySpawnWhisper(ServerWorld world) {
        if (world.random.nextInt(4800) == 0) {
            List<ServerPlayerEntity> players = world.getPlayers();
            if (players.isEmpty()) return;

            ServerPlayerEntity target = players.get(world.random.nextInt(players.size()));
            BlockPos spawnPos = findSpawnPos(world, target.getBlockPos());

            if (spawnPos != null) {
                WhisperEntity whisper = WHISPER.create(world, SpawnReason.NATURAL);
                if (whisper != null) {
                    whisper.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, world.random.nextFloat() * 360f, 0f);
                    world.spawnEntity(whisper);
                }
            }
        }
    }

    private BlockPos findSpawnPos(ServerWorld world, BlockPos near) {
        for (int attempt = 0; attempt < 10; attempt++) {
            int x = near.getX() + world.random.nextInt(64) - 32;
            int z = near.getZ() + world.random.nextInt(64) - 32;
            int y = world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);

            BlockPos pos = new BlockPos(x, y, z);
            if (world.getBlockState(pos.down()).isOpaque() && world.getBlockState(pos).isAir() && world.getBlockState(pos.up()).isAir()) {
                return pos;
            }
        }
        return null;
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
