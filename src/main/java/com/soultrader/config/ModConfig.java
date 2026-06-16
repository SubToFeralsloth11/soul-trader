package com.soultrader.config;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ModConfig {
    public static double spawnChance = 0.5;
    public static int despawnMinutes = 25;
    public static boolean debugLogging = false;
    public static int buyCostType = -1;

    public static List<ItemStack> sellRewards = List.of(
            new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1),
            new ItemStack(Items.NETHERITE_SCRAP, 2),
            new ItemStack(Items.ECHO_SHARD, 3),
            new ItemStack(Items.ANCIENT_DEBRIS, 2),
            new ItemStack(Items.TOTEM_OF_UNDYING, 1),
            new ItemStack(Items.DIAMOND, 5),
            new ItemStack(Items.NETHERITE_INGOT, 1),
            new ItemStack(Items.ENDER_PEARL, 8),
            new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 2)
    );
}
