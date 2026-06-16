package com.soultrader.entity;

import com.soultrader.SoulTraderMod;
import com.soultrader.item.ModItems;
import com.soultrader.screen.WhisperScreenHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WhisperEntity extends PathAwareEntity {
    private static final TrackedData<Boolean> BUY_OFFER_USED = DataTracker.registerData(WhisperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SELL_OFFER_USED = DataTracker.registerData(WhisperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> COST_TYPE = DataTracker.registerData(WhisperEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static List<ItemStack> getSellRewards() {
        return com.soultrader.config.ModConfig.sellRewards.isEmpty()
                ? List.of(
                    new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1),
                    new ItemStack(Items.NETHERITE_SCRAP, 2),
                    new ItemStack(Items.ECHO_SHARD, 3),
                    new ItemStack(Items.ANCIENT_DEBRIS, 2),
                    new ItemStack(Items.TOTEM_OF_UNDYING, 1),
                    new ItemStack(Items.DIAMOND, 5),
                    new ItemStack(Items.NETHERITE_INGOT, 1),
                    new ItemStack(Items.ENDER_PEARL, 8),
                    new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 2))
                : com.soultrader.config.ModConfig.sellRewards;
    }

    private ItemStack sellReward = ItemStack.EMPTY;
    private int disappearTicks = -1;

    public WhisperEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistent();
        if (!world.isClient) {
            int costType = com.soultrader.config.ModConfig.buyCostType;
            this.dataTracker.set(COST_TYPE, costType >= 0 ? Math.min(costType, 2) : this.random.nextInt(3));
            List<ItemStack> rewards = getSellRewards();
            this.sellReward = rewards.get(this.random.nextInt(rewards.size())).copy();
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BUY_OFFER_USED, false);
        builder.add(SELL_OFFER_USED, false);
        builder.add(COST_TYPE, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createWhisperAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.FOLLOW_RANGE, 16.0);
    }

    public int getCostType() {
        return this.dataTracker.get(COST_TYPE);
    }

    public ItemStack getBuyOfferCost() {
        return switch (getCostType()) {
            case 0 -> new ItemStack(Items.DIAMOND_BLOCK, 1);
            case 1 -> new ItemStack(Items.EMERALD_BLOCK, 1);
            default -> new ItemStack(Items.GOLDEN_APPLE, 5);
        };
    }

    public boolean isBuyOfferUsed() {
        return this.dataTracker.get(BUY_OFFER_USED);
    }

    public boolean isSellOfferUsed() {
        return this.dataTracker.get(SELL_OFFER_USED);
    }

    public ItemStack getSellReward() {
        return this.sellReward.copy();
    }

    public void markBuyOfferUsed() {
        this.dataTracker.set(BUY_OFFER_USED, true);
        if (isSellOfferUsed()) scheduleDisappear();
    }

    public void markSellOfferUsed() {
        this.dataTracker.set(SELL_OFFER_USED, true);
        if (isBuyOfferUsed()) scheduleDisappear();
    }

    private void scheduleDisappear() {
        if (this.getWorld().isClient) return;
        this.disappearTicks = 30;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && this.disappearTicks >= 0) {
            this.disappearTicks--;
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                for (int i = 0; i < 3; i++) {
                    serverWorld.spawnParticles(
                            SoulTraderMod.SOUL_TEAL,
                            this.getX() + (this.random.nextDouble() - 0.5) * 0.5,
                            this.getY() + this.random.nextDouble() * 1.0,
                            this.getZ() + (this.random.nextDouble() - 0.5) * 0.5,
                            1, 0.0, 0.05, 0.0, 0.0
                    );
                }
                if (this.disappearTicks <= 0) {
                    for (int i = 0; i < 30; i++) {
                        serverWorld.spawnParticles(
                                SoulTraderMod.SOUL_TEAL,
                                this.getX() + (this.random.nextDouble() - 0.5) * 0.8,
                                this.getY() + this.random.nextDouble() * 1.2,
                                this.getZ() + (this.random.nextDouble() - 0.5) * 0.8,
                                1, (this.random.nextDouble() - 0.5) * 0.1, 0.05, (this.random.nextDouble() - 0.5) * 0.1, 0.0
                        );
                    }
                    this.discard();
                }
            }
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) return ActionResult.SUCCESS;

        if (this.getWorld() instanceof ServerWorld serverWorld) {
            player.openHandledScreen(new net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory<WhisperScreenHandler.WhisperData>() {
                @Override
                public WhisperScreenHandler.WhisperData getScreenOpeningData(net.minecraft.server.network.ServerPlayerEntity player) {
                    return new WhisperScreenHandler.WhisperData(
                            WhisperEntity.this.getId(),
                            getCostType(),
                            isBuyOfferUsed(),
                            isSellOfferUsed(),
                            getSellReward()
                    );
                }

                @Override
                public net.minecraft.text.Text getDisplayName() {
                    return net.minecraft.text.Text.translatable("entity.soultrader.whisper");
                }

                @Nullable
                @Override
                public net.minecraft.screen.ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory playerInventory, net.minecraft.entity.player.PlayerEntity player) {
                    return new WhisperScreenHandler(syncId, playerInventory, WhisperEntity.this);
                }
            });
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("BuyOfferUsed", isBuyOfferUsed());
        nbt.putBoolean("SellOfferUsed", isSellOfferUsed());
        nbt.putInt("CostType", getCostType());
        if (!sellReward.isEmpty()) {
            NbtElement encoded = ItemStack.CODEC.encodeStart(this.getRegistryManager().getOps(NbtOps.INSTANCE), sellReward).getOrThrow();
            nbt.put("SellReward", encoded);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(BUY_OFFER_USED, nbt.getBoolean("BuyOfferUsed").orElse(false));
        this.dataTracker.set(SELL_OFFER_USED, nbt.getBoolean("SellOfferUsed").orElse(false));
        this.dataTracker.set(COST_TYPE, nbt.getInt("CostType").orElse(0));
        if (nbt.contains("SellReward")) {
            this.sellReward = ItemStack.CODEC.parse(this.getRegistryManager().getOps(NbtOps.INSTANCE), nbt.get("SellReward")).result().orElse(ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoulTraderMod.WHISPER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoulTraderMod.WHISPER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoulTraderMod.WHISPER_DEATH;
    }
}
