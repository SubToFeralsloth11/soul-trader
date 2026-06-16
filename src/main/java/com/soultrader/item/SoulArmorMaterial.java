package com.soultrader.item;

import com.soultrader.SoulTraderMod;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Map;

public class SoulArmorMaterial {
    public static final int BASE_DURABILITY = 40;
    public static final TagKey<Item> REPAIR_MATERIAL = TagKey.of(RegistryKeys.ITEM, Identifier.of(SoulTraderMod.MOD_ID, "repairs_soul_armor"));
    public static final RegistryKey<EquipmentAsset> SOUL_ASSET_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(SoulTraderMod.MOD_ID, "soul"));
    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            createDefenseMap(4, 7, 9, 4),
            25,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            4.0f,
            0.15f,
            REPAIR_MATERIAL,
            SOUL_ASSET_KEY
    );

    private static Map<EquipmentType, Integer> createDefenseMap(int boots, int leggings, int chestplate, int helmet) {
        return Map.of(
                EquipmentType.BOOTS, boots,
                EquipmentType.LEGGINGS, leggings,
                EquipmentType.CHESTPLATE, chestplate,
                EquipmentType.HELMET, helmet
        );
    }
}
