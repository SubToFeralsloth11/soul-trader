package com.soultrader.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.entity.EquipmentSlot;

public class SoulSiphonEnchantment extends Enchantment {
    public SoulSiphonEnchantment() {
        super(properties(
                ItemTags.ARMOR_ENCHANTABLE,
                1, 1,
                Enchantment.constantCost(30),
                Enchantment.constantCost(50),
                0,
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
        ));
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
