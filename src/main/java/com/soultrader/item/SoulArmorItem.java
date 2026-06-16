package com.soultrader.item;

import com.soultrader.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SoulArmorItem extends Item {
    public SoulArmorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onCraft(ItemStack stack, World world) {
        applyRandomSoulEnchantment(stack, world);
    }

    public static void applyRandomSoulEnchantment(ItemStack stack, World world) {
        if (stack.hasEnchantments()) return;
        if (world.isClient) return;

        world.getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT).ifPresent(reg -> {
            Random random = Random.create();
            List<RegistryKey<Enchantment>> soulEnchantKeys = new ArrayList<>();
            soulEnchantKeys.add(ModEnchantments.SOUL_SIPHON);
            soulEnchantKeys.add(ModEnchantments.VEIL);
            soulEnchantKeys.add(ModEnchantments.WARDENS_WRATH);
            soulEnchantKeys.add(ModEnchantments.SECOND_WIND);
            soulEnchantKeys.add(ModEnchantments.SHADOWSTEP);
            soulEnchantKeys.add(ModEnchantments.SOULBOUND);
            soulEnchantKeys.add(ModEnchantments.ENDER_MIND);
            soulEnchantKeys.add(ModEnchantments.BLINDING_AURA);
            soulEnchantKeys.add(ModEnchantments.PHOENIX);
            soulEnchantKeys.add(ModEnchantments.GRAVITY);

            RegistryKey<Enchantment> chosen = soulEnchantKeys.get(random.nextInt(soulEnchantKeys.size()));
            reg.getOptional(chosen).ifPresent(entry -> stack.addEnchantment(entry, 1));
        });
    }
}
