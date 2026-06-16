package com.soultrader.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.tag.ItemTags;

public class BlindingAuraEnchantment extends Enchantment {
    public BlindingAuraEnchantment() {
        super(properties(ItemTags.ARMOR_ENCHANTABLE, 1, 1, Enchantment.constantCost(30), Enchantment.constantCost(50), 0, EquipmentSlot.CHEST));
    }
    @Override public boolean isTreasure() { return true; }
}
