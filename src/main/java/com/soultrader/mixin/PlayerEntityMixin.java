package com.soultrader.mixin;

import com.soultrader.enchantment.ModEnchantments;
import com.soultrader.enchantment.SoulEnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "dropInventory", at = @At("HEAD"))
    private void onDropInventory(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self.getWorld().isClient) return;

        if (SoulEnchantmentHelper.hasSoulEnchantment(self, ModEnchantments.SOULBOUND)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
                ItemStack armor = self.getEquippedStack(slot);
                armor.setDamage(0);
            }
        }
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void onShadowstep(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self.getWorld().isClient) return;

        if (self.isSneaking() && SoulEnchantmentHelper.hasSoulEnchantment(self, ModEnchantments.SHADOWSTEP)) {
            Vec3d lookVec = self.getRotationVector();
            Vec3d newPos = self.getPos().add(lookVec.x * 5, 0, lookVec.z * 5);
            self.requestTeleport(newPos.x, newPos.y, newPos.z);
            self.addExhaustion(0.5f);
            self.getItemCooldownManager().set(new ItemStack(Items.ENDER_PEARL), 600);
            ci.cancel();
        }
    }
}
