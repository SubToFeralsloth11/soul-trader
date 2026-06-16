package com.soultrader;

import com.soultrader.entity.WhisperEntity;
import com.soultrader.entity.WhisperModel;
import com.soultrader.entity.WhisperRenderer;
import com.soultrader.screen.WhisperScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class SoulTraderModClient implements ClientModInitializer {
    public static final EntityModelLayer WHISPER_LAYER = new EntityModelLayer(
            Identifier.of(SoulTraderMod.MOD_ID, "whisper"), "main"
    );

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(SoulTraderMod.WHISPER, WhisperRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(WHISPER_LAYER, WhisperModel::getTexturedModelData);

        HandledScreens.register(SoulTraderMod.WHISPER_SCREEN_HANDLER, WhisperScreen::new);
    }
}
