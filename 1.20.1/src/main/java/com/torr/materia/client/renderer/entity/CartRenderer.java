package com.torr.materia.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.torr.materia.client.model.CartBodyModel;
import com.torr.materia.client.model.CartShieldModel;
import com.torr.materia.client.model.CartCoverModel;
import com.torr.materia.client.model.CartLanternModel;
import com.torr.materia.client.model.CartModel;
import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CartRenderer extends EntityRenderer<CartEntity> {

    private static final ResourceLocation WHEEL_TEXTURE =
            new ResourceLocation(materia.MOD_ID, "textures/entity/cart_wheel.png");
    private static final ResourceLocation CHEST_TEXTURE =
            new ResourceLocation(materia.MOD_ID, "textures/entity/cart_chest.png");
    private static final ResourceLocation LANTERN_TEXTURE =
            new ResourceLocation(materia.MOD_ID, "textures/entity/cart_lantern.png");
    private static final ResourceLocation SHIELD_TEXTURE =
            new ResourceLocation(materia.MOD_ID, "textures/entity/cart_shield.png");

    private final CartBodyModel bodyModel;
    private final CartModel model;
    private final CartCoverModel coverModel;
    private final CartLanternModel lanternModel;
    private final CartShieldModel shieldModel;

    public CartRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.bodyModel = new CartBodyModel(context.bakeLayer(CartBodyModel.LAYER));
        this.model = new CartModel(context.bakeLayer(CartModel.LAYER));
        this.coverModel = new CartCoverModel(context.bakeLayer(CartCoverModel.LAYER));
        this.lanternModel = new CartLanternModel(context.bakeLayer(CartLanternModel.LAYER));
        this.shieldModel = new CartShieldModel(context.bakeLayer(CartShieldModel.LAYER));
        this.shadowRadius = CartEntity.shadowRadius();
    }

    @Override
    public void render(CartEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        poseStack.translate(0.0D, CartEntity.RENDER_Y_OFFSET, 0.0D);

        float travel = (float) Math.hypot(entity.getX() - entity.xo, entity.getZ() - entity.zo);
        if (travel > 0.0F) {
            entity.wheelRotation -= travel / CartEntity.WHEEL_RADIUS * CartEntity.WHEEL_ROTATION_FACTOR;
        }

        ResourceLocation bodyTexture = entity.getWoodType().getEntityTexture();

        this.model.setupAnim(entity, 0.0F, 0.0F, partialTicks, 0.0F, 0.0F);

        VertexConsumer bodyBuffer = buffer.getBuffer(RenderType.entityCutoutNoCull(bodyTexture));
        this.bodyModel.renderHullBody(poseStack, bodyBuffer, packedLight, OverlayTexture.NO_OVERLAY);
        this.bodyModel.renderFront(poseStack, bodyBuffer, packedLight, OverlayTexture.NO_OVERLAY);
        this.bodyModel.renderBack(poseStack, bodyBuffer, packedLight, OverlayTexture.NO_OVERLAY);
        this.bodyModel.renderDraftArms(poseStack, bodyBuffer, packedLight, OverlayTexture.NO_OVERLAY);

        this.model.renderChest(poseStack,
                buffer.getBuffer(RenderType.entityCutoutNoCull(CHEST_TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY);
        this.model.renderWheels(poseStack,
                buffer.getBuffer(RenderType.entityCutoutNoCull(WHEEL_TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY);

        entity.getCoverColor().ifPresent(color -> this.coverModel.renderCover(poseStack,
                buffer.getBuffer(RenderType.entityCutoutNoCull(color.getEntityTexture())),
                packedLight, OverlayTexture.NO_OVERLAY));
        if (entity.hasLantern()) {
            int lanternLight = CartEntity.canSleepAt(entity.level()) ? 15728880 : packedLight;
            this.lanternModel.renderLantern(poseStack,
                    buffer.getBuffer(RenderType.entityCutoutNoCull(LANTERN_TEXTURE)),
                    lanternLight, OverlayTexture.NO_OVERLAY);
        }
        if (entity.hasAnyShield()) {
            this.shieldModel.renderShields(poseStack,
                    buffer.getBuffer(RenderType.entityCutoutNoCull(SHIELD_TEXTURE)),
                    entity, packedLight, OverlayTexture.NO_OVERLAY);
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CartEntity entity) {
        return entity.getWoodType().getEntityTexture();
    }
}
