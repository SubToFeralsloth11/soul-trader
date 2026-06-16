package com.soultrader.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.util.Identifier;

public class WhisperModel extends BipedEntityModel<WhisperEntity> {
    public WhisperModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0f);
        ModelPartData root = modelData.getRoot();

        root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation.NONE), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create()
                .uv(32, 0).cuboid(-5.0f, -9.0f, -5.0f, 10.0f, 12.0f, 10.0f, Dilation.NONE), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
                .uv(16, 16).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create()
                .uv(40, 16).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(-5.0f, 2.0f, 0.0f));

        root.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create()
                .uv(40, 16).mirrored().cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(5.0f, 2.0f, 0.0f));

        root.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create()
                .uv(0, 16).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(-1.9f, 12.0f, 0.0f));

        root.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create()
                .uv(0, 16).mirrored().cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(1.9f, 12.0f, 0.0f));

        return TexturedModelData.of(modelData, 64, 32);
    }
}
