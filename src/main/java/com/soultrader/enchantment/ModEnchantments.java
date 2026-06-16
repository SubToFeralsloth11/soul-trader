package com.soultrader.enchantment;

import com.soultrader.SoulTraderMod;
import com.soultrader.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static RegistryEntry<Enchantment> SOUL_SIPHON;
    public static RegistryEntry<Enchantment> VEIL;
    public static RegistryEntry<Enchantment> WARDENS_WRATH;
    public static RegistryEntry<Enchantment> SECOND_WIND;
    public static RegistryEntry<Enchantment> SHADOWSTEP;
    public static RegistryEntry<Enchantment> SOULBOUND;
    public static RegistryEntry<Enchantment> ENDER_MIND;
    public static RegistryEntry<Enchantment> BLINDING_AURA;
    public static RegistryEntry<Enchantment> PHOENIX;
    public static RegistryEntry<Enchantment> GRAVITY;

    private static final Item[] SOUL_ARMOR_ITEMS = {
            ModItems.SOUL_HELMET,
            ModItems.SOUL_CHESTPLATE,
            ModItems.SOUL_LEGGINGS,
            ModItems.SOUL_BOOTS
    };

    public static void register() {
        SOUL_SIPHON = register("soul_siphon", new SoulSiphonEnchantment());
        VEIL = register("veil", new VeilEnchantment());
        WARDENS_WRATH = register("wardens_wrath", new WardensWrathEnchantment());
        SECOND_WIND = register("second_wind", new SecondWindEnchantment());
        SHADOWSTEP = register("shadowstep", new ShadowstepEnchantment());
        SOULBOUND = register("soulbound", new SoulboundEnchantment());
        ENDER_MIND = register("ender_mind", new EnderMindEnchantment());
        BLINDING_AURA = register("blinding_aura", new BlindingAuraEnchantment());
        PHOENIX = register("phoenix", new PhoenixEnchantment());
        GRAVITY = register("gravity", new GravityEnchantment());
    }

    private static RegistryEntry<Enchantment> register(String id, Enchantment enchantment) {
        return Registry.registerReference(
                Registries.ENCHANTMENT,
                Identifier.of(SoulTraderMod.MOD_ID, id),
                enchantment
        );
    }
}
