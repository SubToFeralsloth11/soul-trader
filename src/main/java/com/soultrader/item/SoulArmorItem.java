package com.soultrader.item;

import com.soultrader.enchantment.ModEnchantments;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class SoulArmorItem extends ArmorItem {
    public SoulArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void onCraft(ItemStack stack, net.minecraft.world.World world) {
        applyRandomSoulEnchantment(stack);
    }

    public static void applyRandomSoulEnchantment(ItemStack stack) {
        if (stack.hasEnchantments()) return;

        Random random = Random.create();
        List<RegistryEntry<Enchantment>> soulEnchants = new ArrayList<>();
        soulEnchants.add(ModEnchantments.SOUL_SIPHON);
        soulEnchants.add(ModEnchantments.VEIL);
        soulEnchants.add(ModEnchantments.WARDENS_WRATH);
        soulEnchants.add(ModEnchantments.SECOND_WIND);
        soulEnchants.add(ModEnchantments.SHADOWSTEP);
        soulEnchants.add(ModEnchantments.SOULBOUND);
        soulEnchants.add(ModEnchantments.ENDER_MIND);
        soulEnchants.add(ModEnchantments.BLINDING_AURA);
        soulEnchants.add(ModEnchantments.PHOENIX);
        soulEnchants.add(ModEnchantments.GRAVITY);

        RegistryEntry<Enchantment> chosen = soulEnchants.get(random.nextInt(soulEnchants.size()));
        stack.addEnchantment(chosen, 1);
    }
}
