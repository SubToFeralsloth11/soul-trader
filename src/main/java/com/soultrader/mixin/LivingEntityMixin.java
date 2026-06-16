package com.soultrader.mixin;

import com.soultrader.enchantment.ModEnchantments;
import com.soultrader.enchantment.SoulEnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onSoulSiphon(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getWorld().isClient) return;

        if (source.getAttacker() instanceof PlayerEntity player) {
            if (SoulEnchantmentHelper.hasSoulEnchantment(player, ModEnchantments.SOUL_SIPHON)) {
                player.heal(2.0f);
            }
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamage(float amount, DamageSource source) {
        if (source.getAttacker() instanceof PlayerEntity player) {
            if (source.getSource() instanceof WardenEntity) {
                if (SoulEnchantmentHelper.hasSoulEnchantment(player, ModEnchantments.WARDENS_WRATH)) {
                    return amount * 1.5f;
                }
            }
        }

        return amount;
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamageHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (SoulEnchantmentHelper.hasSoulEnchantment(self, ModEnchantments.PHOENIX)) {
            if (source.isOf(DamageTypes.ON_FIRE)
                    || source.isOf(DamageTypes.IN_FIRE)
                    || source.isOf(DamageTypes.LAVA)
                    || source.isOf(DamageTypes.FIREBALL)
                    || source.isOf(DamageTypes.CAMPFIRE)
                    || source.isOf(DamageTypes.HOT_FLOOR)) {
                self.heal(1.0f);
                self.setFireTicks(0);
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamageReturn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getWorld().isClient) return;

        if (self instanceof PlayerEntity player) {
            if (SoulEnchantmentHelper.hasSoulEnchantment(player, ModEnchantments.SECOND_WIND)) {
                if (player.getHealth() <= 1.0f && !player.isDead()) {
                    ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
                    if (!player.getItemCooldownManager().isCoolingDown(totem)) {
                        player.setHealth(2.0f);
                        player.getItemCooldownManager().set(totem, 6000);
                        player.getWorld().sendEntityStatus(player, (byte) 35);
                    }
                }
            }
        }
    }

    @Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
    private void onTakeKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (SoulEnchantmentHelper.hasSoulEnchantment(self, ModEnchantments.GRAVITY)) {
            ci.cancel();
        }
    }
}
