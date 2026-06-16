package com.soultrader.enchantment;

import com.soultrader.SoulTraderMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> SOUL_SIPHON = key("soul_siphon");
    public static final RegistryKey<Enchantment> VEIL = key("veil");
    public static final RegistryKey<Enchantment> WARDENS_WRATH = key("wardens_wrath");
    public static final RegistryKey<Enchantment> SECOND_WIND = key("second_wind");
    public static final RegistryKey<Enchantment> SHADOWSTEP = key("shadowstep");
    public static final RegistryKey<Enchantment> SOULBOUND = key("soulbound");
    public static final RegistryKey<Enchantment> ENDER_MIND = key("ender_mind");
    public static final RegistryKey<Enchantment> BLINDING_AURA = key("blinding_aura");
    public static final RegistryKey<Enchantment> PHOENIX = key("phoenix");
    public static final RegistryKey<Enchantment> GRAVITY = key("gravity");

    private static RegistryKey<Enchantment> key(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulTraderMod.MOD_ID, id));
    }

    public static void register() {
    }
}
