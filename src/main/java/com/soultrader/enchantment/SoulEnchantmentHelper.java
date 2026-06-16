package com.soultrader.enchantment;

import com.soultrader.item.SoulArmorItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class SoulEnchantmentHelper {
    private static final Identifier HEALTH_MODIFIER_ID = Identifier.of("soultrader", "full_set_health");
    private static final List<EquipmentSlot> ARMOR_SLOTS = List.of(
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    );

    public static boolean hasSoulEnchantment(LivingEntity entity, RegistryKey<Enchantment> key) {
        return getSoulEnchantmentLevel(entity, key) > 0;
    }

    public static int getSoulEnchantmentLevel(LivingEntity entity, RegistryKey<Enchantment> key) {
        Optional<Registry<Enchantment>> reg = entity.getWorld().getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
        if (reg.isEmpty()) return 0;

        Optional<RegistryEntry.Reference<Enchantment>> entry = reg.get().getOptional(key);
        if (entry.isEmpty()) return 0;

        int max = 0;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = entity.getEquippedStack(slot);
            if (stack.hasEnchantments()) {
                int level = stack.getEnchantments().getLevel(entry.get());
                if (level > max) max = level;
            }
        }
        return max;
    }

    public static boolean isWearingFullSoulArmor(LivingEntity entity) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (!(entity.getEquippedStack(slot).getItem() instanceof SoulArmorItem)) {
                return false;
            }
        }
        return true;
    }

    public static void applyFullSetBonus(PlayerEntity player) {
        EntityAttributeInstance healthAttr = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (healthAttr == null) return;

        EntityAttributeModifier existing = healthAttr.getModifier(HEALTH_MODIFIER_ID);

        if (!isWearingFullSoulArmor(player)) {
            if (existing != null) {
                healthAttr.removeModifier(HEALTH_MODIFIER_ID);
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
            return;
        }

        if (existing == null) {
            EntityAttributeModifier modifier = new EntityAttributeModifier(
                    HEALTH_MODIFIER_ID,
                    20.0,
                    EntityAttributeModifier.Operation.ADD_VALUE
            );
            healthAttr.addPersistentModifier(modifier);
        }
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, false, false, true));
    }

    public static List<EquipmentSlot> getArmorSlots() {
        return ARMOR_SLOTS;
    }
}
