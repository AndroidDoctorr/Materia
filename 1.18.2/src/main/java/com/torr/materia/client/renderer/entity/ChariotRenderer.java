package com.torr.materia.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.torr.materia.client.model.ChariotModel;
import com.torr.materia.entity.ChariotEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChariotRenderer extends EntityRenderer<ChariotEntity> {

    private final ChariotModel model;

    public ChariotRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ChariotModel(context.bakeLayer(ChariotModel.LAYER));
        this.shadowRadius = ChariotEntity.shadowRadius();
    }

    @Override
    public void render(ChariotEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        poseStack.translate(0.0D, ChariotEntity.RENDER_Y_OFFSET, 0.0D);

        float travel = (float) Math.hypot(entity.getX() - entity.xo, entity.getZ() - entity.zo);
        if (travel > 0.0F) {
            entity.wheelRotation -= travel / ChariotEntity.WHEEL_RADIUS * ChariotEntity.WHEEL_ROTATION_FACTOR;
        }

        this.model.setupAnim(entity, 0.0F, 0.0F, partialTicks, 0.0F, 0.0F);

        ResourceLocation texture = entity.getChariotType().getEntityTexture();
        var vertexBuffer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        this.model.renderToBuffer(poseStack, vertexBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ChariotEntity entity) {
        return entity.getChariotType().getEntityTexture();
    }
}
