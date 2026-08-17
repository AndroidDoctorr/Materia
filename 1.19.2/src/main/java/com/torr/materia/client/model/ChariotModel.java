package com.torr.materia.client.model;

import com.torr.materia.entity.ChariotEntity;
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
 * Open chariot hull (front, floor, sides — no top or back) plus two wheels.
 * Uses a 48×32 atlas: top row wraps front + sides; bottom row has wheel (left) and floor (center).
 */
public class ChariotModel extends EntityModel<ChariotEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation(materia.MOD_ID, "chariot"), "main");

    private static final int TEX_W = 48;
    private static final int TEX_H = 32;

    private static final float U = 16.0F;
    private static final float W = ChariotEntity.WIDTH * U;
    private static final float L = ChariotEntity.LENGTH * U;
    private static final float H = ChariotEntity.HEIGHT * U;
    private static final float HALF_W = W * 0.5F;
    private static final float HALF_L = L * 0.5F;
    /** Minimum 1 px thickness — direction-only faces unavailable before 1.20. */
    private static final float FLOOR_H = 1.0F;
    private static final float WALL_H = H;
    private static final float WALL_T = 1.0F;

    private static final float WHEEL_MESH_D = U;
    private static final float WHEEL_MESH_T = 2.0F;
    private static final float WHEEL_MESH_HALF = WHEEL_MESH_D * 0.5F;
    private static final float WHEEL_D = ChariotEntity.WHEEL_RADIUS * 2.0F * U;
    private static final float BODY_BASE = WHEEL_D * 0.5F;
    private static final float WHEEL_PART_SCALE = ChariotEntity.WHEEL_RADIUS * 2.0F;

    public final ModelPart root;
    public final ModelPart floor;
    public final ModelPart wallFront;
    public final ModelPart wallRight;
    public final ModelPart wallLeft;
    public final ModelPart wheelLeft;
    public final ModelPart wheelRight;
    public final ModelPart hitch;

    public ChariotModel(ModelPart root) {
        this.root = root;
        this.floor = root.getChild("floor");
        this.wallFront = root.getChild("wall_front");
        this.wallRight = root.getChild("wall_right");
        this.wallLeft = root.getChild("wall_left");
        this.wheelLeft = root.getChild("wheel_left");
        this.wheelRight = root.getChild("wheel_right");
        this.hitch = root.getChild("hitch");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "floor",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-HALF_W, BODY_BASE, -HALF_L, W, FLOOR_H, L),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_front",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L, W, WALL_H, WALL_T),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_right",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(HALF_W - WALL_T, BODY_BASE + FLOOR_H, -HALF_L, WALL_T, WALL_H, L),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_left",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L, WALL_T, WALL_H, L),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "hitch",
                CubeListBuilder.create()
                        .texOffs(15, 0)
                        .addBox(0.0F - 3.0F, BODY_BASE + FLOOR_H, -1.5F * L, 6.0F, 1.0F, 16.0F),
                PartPose.ZERO);

        float halfAxle = WHEEL_MESH_T * 0.5F;
        addWheel(root, "wheel_left", -HALF_W - halfAxle, 0.0F, false);
        addWheel(root, "wheel_right", HALF_W + halfAxle, 0.0F, true);

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }

    private static void addWheel(PartDefinition root, String name, float centerX, float centerZ, boolean faceOutward) {
        PartPose pose = faceOutward
                ? PartPose.offsetAndRotation(centerX, BODY_BASE, centerZ, 0.0F, (float) Math.PI, 0.0F)
                : PartPose.offset(centerX, BODY_BASE, centerZ);
        root.addOrReplaceChild(
                name,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-WHEEL_MESH_T * 0.5F, -WHEEL_MESH_HALF, -WHEEL_MESH_HALF, WHEEL_MESH_T, WHEEL_MESH_D,
                                WHEEL_MESH_D),
                pose);
    }

    @Override
    public void setupAnim(ChariotEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        scaleWheel(wheelLeft);
        scaleWheel(wheelRight);
        float roll = entity.wheelRotation;
        wheelLeft.xRot = roll;
        wheelRight.xRot = -roll;
    }

    private static void scaleWheel(ModelPart wheel) {
        wheel.xScale = WHEEL_PART_SCALE;
        wheel.yScale = WHEEL_PART_SCALE;
        wheel.zScale = WHEEL_PART_SCALE;
    }

    public void renderHull(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        floor.render(poseStack, buffer, packedLight, packedOverlay);
        wallFront.render(poseStack, buffer, packedLight, packedOverlay);
        wallRight.render(poseStack, buffer, packedLight, packedOverlay);
        wallLeft.render(poseStack, buffer, packedLight, packedOverlay);
        hitch.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderWheels(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        wheelLeft.render(poseStack, buffer, packedLight, packedOverlay);
        wheelRight.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, float red,
            float green, float blue, float alpha) {
        renderHull(poseStack, buffer, packedLight, packedOverlay);
        renderWheels(poseStack, buffer, packedLight, packedOverlay);
    }
}
