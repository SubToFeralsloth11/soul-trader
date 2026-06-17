package com.soultrader.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createConfigScreen;
    }

    private Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.soultrader.title"));

        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory spawning = builder.getOrCreateCategory(Text.translatable("config.soultrader.spawning"));
        spawning.addEntry(eb.startDoubleField(Text.translatable("config.soultrader.spawn_chance"), ModConfig.spawnChance)
                .setDefaultValue(0.5)
                .setMin(0.01).setMax(2.0)
                .setTooltip(Text.translatable("config.soultrader.spawn_chance.tooltip"))
                .setSaveConsumer(v -> ModConfig.spawnChance = v)
                .build());
        spawning.addEntry(eb.startIntField(Text.translatable("config.soultrader.despawn_minutes"), ModConfig.despawnMinutes)
                .setDefaultValue(25)
                .setMin(5).setMax(60)
                .setSaveConsumer(v -> ModConfig.despawnMinutes = v)
                .build());

        ConfigCategory trading = builder.getOrCreateCategory(Text.translatable("config.soultrader.trading"));
        trading.addEntry(eb.startIntField(Text.translatable("config.soultrader.buy_cost_type"), ModConfig.buyCostType)
                .setDefaultValue(-1)
                .setMin(-1).setMax(2)
                .setTooltip(Text.translatable("config.soultrader.buy_cost_type.tooltip"))
                .setSaveConsumer(v -> ModConfig.buyCostType = v)
                .build());

        ConfigCategory debug = builder.getOrCreateCategory(Text.translatable("config.soultrader.debug"));
        debug.addEntry(eb.startBooleanToggle(Text.translatable("config.soultrader.debug_logging"), ModConfig.debugLogging)
                .setDefaultValue(false)
                .setSaveConsumer(v -> ModConfig.debugLogging = v)
                .build());

        builder.setSavingRunnable(ModConfig::save);
        return builder.build();
    }
}
