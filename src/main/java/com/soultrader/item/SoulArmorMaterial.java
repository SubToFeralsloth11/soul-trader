package com.soultrader.item;

import com.soultrader.SoulTraderMod;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SoulArmorMaterial {
    public static final int BASE_DURABILITY = 40;
    public static final RegistryEntry<ArmorMaterial> INSTANCE = Registry.register(
            Registries.ARMOR_MATERIAL,
            Identifier.of(SoulTraderMod.MOD_ID, "soul"),
            new ArmorMaterial(
                    createDefenseMap(4, 7, 9, 4),
                    25,
                    SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.ofItems(ModItems.SOUL),
                    List.of(new ArmorMaterial.Layer(Identifier.of(SoulTraderMod.MOD_ID, "soul"))),
                    4.0f,
                    0.15f
            )
    );

    private static Map<ArmorItem.Type, Integer> createDefenseMap(int boots, int leggings, int chestplate, int helmet) {
        Map<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.BODY, 0);
        return map;
    }
}
