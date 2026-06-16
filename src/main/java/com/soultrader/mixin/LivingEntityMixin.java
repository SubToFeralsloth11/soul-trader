package com.soultrader.mixin;

import com.soultrader.enchantment.ModEnchantments;
import com.soultrader.enchantment.SoulEnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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

        LivingEntity self = (LivingEntity) (Object) this;
        if (SoulEnchantmentHelper.hasSoulEnchantment(self, ModEnchantments.PHOENIX)) {
            if (source.isOf(net.minecraft.entity.damage.DamageTypes.ON_FIRE)
                    || source.isOf(net.minecraft.entity.damage.DamageTypes.IN_FIRE)
                    || source.isOf(net.minecraft.entity.damage.DamageTypes.LAVA)
                    || source.isOf(net.minecraft.entity.damage.DamageTypes.FIREBALL)
                    || source.isOf(net.minecraft.entity.damage.DamageTypes.CAMPFIRE)
                    || source.isOf(net.minecraft.entity.damage.DamageTypes.HOT_FLOOR)) {
                self.heal(amount);
                return 0f;
            }
        }

        if (self instanceof PlayerEntity player) {
            if (SoulEnchantmentHelper.hasSoulEnchantment(player, ModEnchantments.SECOND_WIND)) {
                if (player.getHealth() <= amount && !player.getItemCooldownManager().isCoolingDown(net.minecraft.item.Items.TOTEM_OF_UNDYING)) {
                    player.getItemCooldownManager().set(net.minecraft.item.Items.TOTEM_OF_UNDYING, 6000);
                    player.setHealth(1.0f);
                    player.getWorld().sendEntityStatus(player, (byte) 35);
                    return 0f;
                }
            }
        }

        return amount;
    }

    @Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
    private void onTakeKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (SoulEnchantmentHelper.hasSoulEnchantment(self, ModEnchantments.GRAVITY)) {
            ci.cancel();
        }
    }
}
