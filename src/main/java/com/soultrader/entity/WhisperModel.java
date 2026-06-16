package com.soultrader.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

public class WhisperModel extends BipedEntityModel<BipedEntityRenderState> {
    public WhisperModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        root.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation.NONE),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.HAT,
                ModelPartBuilder.create().uv(32, 0).cuboid(-5.0f, -9.0f, -5.0f, 10.0f, 12.0f, 10.0f, Dilation.NONE),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create().uv(16, 28).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 10.0f, 4.0f, Dilation.NONE),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create().uv(40, 24).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 10.0f, 4.0f, Dilation.NONE),
                ModelTransform.of(-5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create().uv(40, 24).mirrored().cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 10.0f, 4.0f, Dilation.NONE),
                ModelTransform.of(5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create().uv(0, 20).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, Dilation.NONE),
                ModelTransform.of(-1.9f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        root.addChild(EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create().uv(0, 20).mirrored().cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, Dilation.NONE),
                ModelTransform.of(1.9f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        return TexturedModelData.of(modelData, 64, 64);
    }
}
