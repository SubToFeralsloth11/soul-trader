package com.soultrader.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.tag.ItemTags;

public class SecondWindEnchantment extends Enchantment {
    public SecondWindEnchantment() {
        super(properties(ItemTags.ARMOR_ENCHANTABLE, 1, 1,
                Enchantment.constantCost(30), Enchantment.constantCost(50), 0,
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
    }
    @Override public boolean isTreasure() { return true; }
}
