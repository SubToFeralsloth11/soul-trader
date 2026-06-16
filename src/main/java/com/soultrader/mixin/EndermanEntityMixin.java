package com.soultrader.mixin;

import com.soultrader.enchantment.ModEnchantments;
import com.soultrader.enchantment.SoulEnchantmentHelper;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    @Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
    private void onIsPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (SoulEnchantmentHelper.hasSoulEnchantment(player, ModEnchantments.ENDER_MIND)) {
            cir.setReturnValue(false);
        }
    }
}
