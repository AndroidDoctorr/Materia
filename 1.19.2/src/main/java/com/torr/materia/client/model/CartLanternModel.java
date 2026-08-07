package com.torr.materia.client.model;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * Lantern prism between the draft arms. Full 6×7×6 box scaled to 67% via the lantern child.
 */
public class CartLanternModel extends EntityModel<CartEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation(materia.MOD_ID, "cart_lantern"), "main");

    private static final int TEX_W = 6;
    private static final int TEX_H = 13;

    private static final float U = 16.0F;
    private static final float L = CartEntity.LENGTH * U;
    private static final float HALF_L = L * 0.5F;
    private static final float H = CartEntity.HEIGHT * U;
    private static final float FLOOR_H = H * CartEntity.FLOOR_HEIGHT_FRACTION;
    private static final float WALL_H = H - FLOOR_H;
    private static final float WHEEL_D = CartEntity.WHEEL_RADIUS * 2.0F * U;
    private static final float BODY_BASE = WHEEL_D * 0.5F;

    private static final float DRAFT_ARM_LEN = 5.0F;
    private static final float DRAFT_ARM_H = 2.0F;
    private static final float DRAFT_ARM_Y = BODY_BASE + FLOOR_H + WALL_H * 0.35F;
    private static final float DRAFT_TIP_Z = -HALF_L - DRAFT_ARM_LEN;
    private static final float LANTERN_BACK_OFFSET = 5.5F;

    private static final float LANTERN_SCALE = 2.0F / 3.0F;
    private static final float LANTERN_W = 6.0F;
    private static final float LANTERN_H = 7.0F;
    private static final float LANTERN_D = 6.0F;
    private static final float HALF_LANTERN_W = LANTERN_W * 0.5F;
    private static final float HALF_LANTERN_D = LANTERN_D * 0.5F;

    private static final float MOUNT_X = 0.0F;
    private static final float MOUNT_Y = DRAFT_ARM_Y + DRAFT_ARM_H * 0.15F;
    private static final float MOUNT_Z = DRAFT_TIP_Z - DRAFT_ARM_LEN * 0.5F + LANTERN_BACK_OFFSET;

    private final ModelPart root;

    public CartLanternModel(ModelPart root) {
        this.root = root;
        ModelPart lantern = root.getChild("lantern");
        lantern.xScale = LANTERN_SCALE;
        lantern.yScale = LANTERN_SCALE;
        lantern.zScale = LANTERN_SCALE;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "lantern",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-HALF_LANTERN_W, 0.0F, -HALF_LANTERN_D, LANTERN_W, LANTERN_H, LANTERN_D),
                PartPose.offset(MOUNT_X, MOUNT_Y, MOUNT_Z));

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }

    @Override
    public void setupAnim(CartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
    }

    public void renderLantern(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, float red,
            float green, float blue, float alpha) {
        this.renderLantern(poseStack, buffer, packedLight, packedOverlay);
    }
}
