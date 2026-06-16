package com.soultrader.enchantment;

import com.soultrader.item.SoulArmorItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class SoulEnchantmentHelper {
    public static boolean hasSoulEnchantment(LivingEntity entity, RegistryKey<Enchantment> key) {
        return getSoulEnchantmentLevel(entity, key) > 0;
    }

    public static int getSoulEnchantmentLevel(LivingEntity entity, RegistryKey<Enchantment> key) {
        Optional<Registry<Enchantment>> reg = entity.getWorld().getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
        if (reg.isEmpty()) return 0;

        Optional<RegistryEntry.Reference<Enchantment>> entry = reg.get().getOptional(key);
        if (entry.isEmpty()) return 0;

        int max = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack stack = entity.getEquippedStack(slot);
            if (stack.hasEnchantments()) {
                int level = stack.getEnchantments().getLevel(entry.get());
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
