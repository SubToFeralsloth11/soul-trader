package com.soultrader.enchantment;

import com.soultrader.item.ModItems;
import com.soultrader.item.SoulArmorItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class SoulEnchantmentHelper {
    public static boolean hasSoulEnchantment(LivingEntity entity, RegistryEntry<Enchantment> enchantment) {
        return getSoulEnchantmentLevel(entity, enchantment) > 0;
    }

    public static int getSoulEnchantmentLevel(LivingEntity entity, RegistryEntry<Enchantment> enchantment) {
        int max = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack stack = entity.getEquippedStack(slot);
            if (stack.hasEnchantments()) {
                int level = stack.getEnchantments().getLevel(enchantment);
                if (level > max) max = level;
            }
        }
        return max;
    }

    public static boolean isWearingFullSoulArmor(LivingEntity entity) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            if (!(entity.getEquippedStack(slot).getItem() instanceof SoulArmorItem)) {
                return false;
            }
        }
        return true;
    }

    public static void applyFullSetBonus(PlayerEntity player) {
        if (!isWearingFullSoulArmor(player)) {
            player.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
            return;
        }

        player.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, false, false, true));
    }
}
