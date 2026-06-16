package com.soultrader.item;

import com.soultrader.SoulTraderMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item SOUL = register("soul", new SoulItem(new Item.Settings().maxCount(1)));

    public static final Item SOUL_HELMET = register("soul_helmet",
            new SoulArmorItem(SoulArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));
    public static final Item SOUL_CHESTPLATE = register("soul_chestplate",
            new SoulArmorItem(SoulArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));
    public static final Item SOUL_LEGGINGS = register("soul_leggings",
            new SoulArmorItem(SoulArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));
    public static final Item SOUL_BOOTS = register("soul_boots",
            new SoulArmorItem(SoulArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(SoulArmorMaterial.BASE_DURABILITY))));

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SoulTraderMod.MOD_ID, id), item);
    }

    public static void register() {
    }
}
