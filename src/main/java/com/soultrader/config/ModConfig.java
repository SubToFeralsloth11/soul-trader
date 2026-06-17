package com.soultrader.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("soultrader.json");

    public static double spawnChance = 0.5;
    public static int despawnMinutes = 25;
    public static boolean debugLogging = false;
    public static int buyCostType = -1;
    public static List<String> sellRewardIds = List.of(
            "minecraft:enchanted_golden_apple","minecraft:netherite_scrap","minecraft:netherite_scrap",
            "minecraft:echo_shard","minecraft:echo_shard","minecraft:echo_shard",
            "minecraft:ancient_debris","minecraft:ancient_debris",
            "minecraft:totem_of_undying","minecraft:diamond","minecraft:diamond",
            "minecraft:diamond","minecraft:diamond","minecraft:diamond",
            "minecraft:netherite_ingot","minecraft:ender_pearl","minecraft:ender_pearl",
            "minecraft:ender_pearl","minecraft:ender_pearl","minecraft:ender_pearl",
            "minecraft:ender_pearl","minecraft:ender_pearl","minecraft:ender_pearl",
            "minecraft:enchanted_golden_apple","minecraft:enchanted_golden_apple"
    );

    public static void load() {
        if (!CONFIG_PATH.toFile().exists()) {
            save();
            return;
        }
        try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            if (data != null) {
                spawnChance = data.spawnChance;
                despawnMinutes = data.despawnMinutes;
                debugLogging = data.debugLogging;
                buyCostType = data.buyCostType;
                if (data.sellRewardIds != null) sellRewardIds = data.sellRewardIds;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(new ConfigData(spawnChance, despawnMinutes, debugLogging, buyCostType, sellRewardIds), writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ItemStack> getSellRewards() {
        return sellRewardIds.stream().map(id -> {
            String[] parts = id.split(":");
            if (parts.length < 2) return ItemStack.EMPTY;
            var key = net.minecraft.registry.RegistryKey.of(
                    net.minecraft.registry.RegistryKeys.ITEM,
                    net.minecraft.util.Identifier.of(parts[0], parts[1])
            );
            var opt = net.minecraft.registry.Registries.ITEM.getOptional(key);
            return opt.map(item -> new ItemStack(item, 1)).orElse(ItemStack.EMPTY);
        }).filter(s -> !s.isEmpty()).toList();
    }

    private static class ConfigData {
        double spawnChance = 0.5;
        int despawnMinutes = 25;
        boolean debugLogging = false;
        int buyCostType = -1;
        List<String> sellRewardIds;

        ConfigData() {}
        ConfigData(double sc, int dm, boolean dl, int bct, List<String> sr) {
            this.spawnChance = sc;
            this.despawnMinutes = dm;
            this.debugLogging = dl;
            this.buyCostType = bct;
            this.sellRewardIds = sr;
        }
    }
}
