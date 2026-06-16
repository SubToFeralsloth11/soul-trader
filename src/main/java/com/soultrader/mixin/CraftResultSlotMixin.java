package com.soultrader.mixin;

import com.soultrader.item.SoulArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public class CraftResultSlotMixin {

    @Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    private void onCrafted(ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() instanceof SoulArmorItem) {
        }
    }
}
