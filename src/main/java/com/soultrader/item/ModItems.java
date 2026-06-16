package com.soultrader.item;

import com.soultrader.SoulTraderMod;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item SOUL = register("soul", new SoulItem(new Item.Settings().maxCount(64)));

    public static final Item SOUL_HELMET = register("soul_helmet",
            new SoulArmorItem(new Item.Settings().armor(SoulArmorMaterial.INSTANCE, EquipmentType.HELMET).maxDamage(EquipmentType.HELMET.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));
    public static final Item SOUL_CHESTPLATE = register("soul_chestplate",
            new SoulArmorItem(new Item.Settings().armor(SoulArmorMaterial.INSTANCE, EquipmentType.CHESTPLATE).maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));
    public static final Item SOUL_LEGGINGS = register("soul_leggings",
            new SoulArmorItem(new Item.Settings().armor(SoulArmorMaterial.INSTANCE, EquipmentType.LEGGINGS).maxDamage(EquipmentType.LEGGINGS.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));
    public static final Item SOUL_BOOTS = register("soul_boots",
            new SoulArmorItem(new Item.Settings().armor(SoulArmorMaterial.INSTANCE, EquipmentType.BOOTS).maxDamage(EquipmentType.BOOTS.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SoulTraderMod.MOD_ID, id), item);
    }

    public static void register() {
    }
}
