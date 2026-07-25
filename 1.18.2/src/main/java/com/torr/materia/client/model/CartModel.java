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
 * Cart hull + wheels in 1/16-block units (16 units = 1 block). Matches {@link CartEntity} footprint.
 */
public class CartModel extends EntityModel<CartEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation(materia.MOD_ID, "cart"), "main");

    private static final int TEX_W = 16;
    private static final int TEX_H = 16;

    private static final float U = 16.0F;
    private static final float W = CartEntity.WIDTH * U;
    private static final float H = CartEntity.HEIGHT * U;
    private static final float L = CartEntity.LENGTH * U;
    private static final float HALF_W = W * 0.5F;
    private static final float HALF_L = L * 0.5F;
    private static final float FLOOR_H = H * CartEntity.FLOOR_HEIGHT_FRACTION;
    private static final float WALL_H = H - FLOOR_H;
    private static final float WALL_T = W * CartEntity.WALL_THICKNESS_FRACTION;
    private static final float INNER_L = L - 2.0F * WALL_T;

    private static final float WHEEL_D = CartEntity.WHEEL_RADIUS * 2.0F * U;
    private static final float WHEEL_T = CartEntity.WHEEL_THICKNESS * U;
    private static final float WHEEL_HALF = WHEEL_D * 0.5F;
    private static final float BODY_BASE = WHEEL_D * 0.5F;
    private static final float WHEEL_Z_CENTER_FRONT = -HALF_L + WHEEL_D * 0.5F + 2.0F;
    private static final float WHEEL_Z_CENTER_BACK = HALF_L - WHEEL_D * 0.5F - 2.0F;

    public final ModelPart root;
    public final ModelPart floor;
    public final ModelPart wallNorth;
    public final ModelPart wallSouth;
    public final ModelPart wallWest;
    public final ModelPart wallEast;
    public final ModelPart wheelLeftFront;
    public final ModelPart wheelLeftBack;
    public final ModelPart wheelRightFront;
    public final ModelPart wheelRightBack;

    public CartModel(ModelPart root) {
        this.root = root;
        this.floor = root.getChild("floor");
        this.wallNorth = root.getChild("wall_north");
        this.wallSouth = root.getChild("wall_south");
        this.wallWest = root.getChild("wall_west");
        this.wallEast = root.getChild("wall_east");
        this.wheelLeftFront = root.getChild("wheel_left_front");
        this.wheelLeftBack = root.getChild("wheel_left_back");
        this.wheelRightFront = root.getChild("wheel_right_front");
        this.wheelRightBack = root.getChild("wheel_right_back");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "floor",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-HALF_W, BODY_BASE, -HALF_L, W, FLOOR_H, L),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_north",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L, W, WALL_H, WALL_T),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_south",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, HALF_L - WALL_T, W, WALL_H, WALL_T),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_west",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L + WALL_T, WALL_T, WALL_H, INNER_L),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_east",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(HALF_W - WALL_T, BODY_BASE + FLOOR_H, -HALF_L + WALL_T, WALL_T, WALL_H, INNER_L),
                PartPose.ZERO);

        float halfAxle = WHEEL_T * 0.5F;
        addWheel(root, "wheel_left_front", -HALF_W - halfAxle, WHEEL_Z_CENTER_FRONT, false);
        addWheel(root, "wheel_left_back", -HALF_W - halfAxle, WHEEL_Z_CENTER_BACK, false);
        addWheel(root, "wheel_right_front", HALF_W + halfAxle, WHEEL_Z_CENTER_FRONT, true);
        addWheel(root, "wheel_right_back", HALF_W + halfAxle, WHEEL_Z_CENTER_BACK, true);

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }

    private static void addWheel(PartDefinition root, String name, float centerX, float centerZ, boolean faceOutward) {
        PartPose pose = faceOutward
                ? PartPose.offsetAndRotation(centerX, 0.0F, centerZ, 0.0F, (float) Math.PI, 0.0F)
                : PartPose.offset(centerX, 0.0F, centerZ);
        root.addOrReplaceChild(
                name,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-WHEEL_T * 0.5F, 0.0F, -WHEEL_HALF, WHEEL_T, WHEEL_D, WHEEL_D),
                pose);
    }

    @Override
    public void setupAnim(CartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
    }

    public void renderHull(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        floor.render(poseStack, buffer, packedLight, packedOverlay);
        wallNorth.render(poseStack, buffer, packedLight, packedOverlay);
        wallSouth.render(poseStack, buffer, packedLight, packedOverlay);
        wallWest.render(poseStack, buffer, packedLight, packedOverlay);
        wallEast.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderWheels(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        wheelLeftFront.render(poseStack, buffer, packedLight, packedOverlay);
        wheelLeftBack.render(poseStack, buffer, packedLight, packedOverlay);
        wheelRightFront.render(poseStack, buffer, packedLight, packedOverlay);
        wheelRightBack.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, float red,
            float green, float blue, float alpha) {
        renderHull(poseStack, buffer, packedLight, packedOverlay);
        renderWheels(poseStack, buffer, packedLight, packedOverlay);
    }
}
