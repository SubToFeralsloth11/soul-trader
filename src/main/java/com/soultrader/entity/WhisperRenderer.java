package com.soultrader.entity;

import com.soultrader.SoulTraderMod;
import com.soultrader.SoulTraderModClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;

public class WhisperRenderer extends MobEntityRenderer<WhisperEntity, BipedEntityModel<WhisperEntity>> {
    public WhisperRenderer(EntityRendererFactory.Context context) {
        super(context, new WhisperModel(context.getPart(SoulTraderModClient.WHISPER_LAYER)), 0.3f);
    }

    @Override
    public Identifier getTexture(WhisperEntity entity) {
        return Identifier.of(SoulTraderMod.MOD_ID, "textures/entity/whisper.png");
    }
}
