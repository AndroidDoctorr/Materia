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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

/**
 * Open chariot hull (front, floor, sides — no top or back) plus two wheels.
 * Uses a 48×32 atlas: top row wraps front + sides; bottom row has wheel (left) and floor (center).
 * Geometry matches 1.20 (zero-thickness panels; wheels baked at {@link #WHEEL_PART_SCALE} with 1px depth).
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
    /** Zero-thickness panels (1.20); only the outward face is meaningful in the atlas. */
    private static final float FLOOR_H = 0.0F;
    private static final float WALL_H = H;
    private static final float WALL_T = 0.0F;

    private static final float WHEEL_MESH_D = U;
    private static final float WHEEL_MESH_T = 1.0F;
    private static final float WHEEL_MESH_HALF = WHEEL_MESH_D * 0.5F;
    private static final float WHEEL_PART_SCALE = ChariotEntity.WHEEL_RADIUS * 2.0F;
    private static final float WHEEL_CUBE_RADIUS = WHEEL_MESH_HALF * WHEEL_PART_SCALE;

    private static final float WHEEL_D = ChariotEntity.WHEEL_RADIUS * 2.0F * U;
    private static final float BODY_BASE = WHEEL_D * 0.5F;

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

        root.addOrReplaceChild("floor", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("wall_front", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("wall_right", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("wall_left", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hitch", CubeListBuilder.create(), PartPose.ZERO);

        float halfAxle = WHEEL_MESH_T * 0.5F;
        addWheel(root, "wheel_left", -HALF_W - halfAxle, 0.0F, false);
        addWheel(root, "wheel_right", HALF_W + halfAxle, 0.0F, true);

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }

    private static void addWheel(PartDefinition root, String name, float centerX, float centerZ, boolean faceOutward) {
        PartPose pose = faceOutward
                ? PartPose.offsetAndRotation(centerX, BODY_BASE, centerZ, 0.0F, (float) Math.PI, 0.0F)
                : PartPose.offset(centerX, BODY_BASE, centerZ);
        root.addOrReplaceChild(name, CubeListBuilder.create(), pose);
    }

    @Override
    public void setupAnim(ChariotEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        float roll = entity.wheelRotation;
        wheelLeft.xRot = roll;
        wheelRight.xRot = -roll;
    }

    public void renderHull(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        com.mojang.blaze3d.vertex.PoseStack.Pose pose = poseStack.last();
        float wallY = BODY_BASE + FLOOR_H;
        float panel = 1.0F;
        EntityPlaneRenderer.boxFace(pose, buffer, Direction.UP, -HALF_W, BODY_BASE, -HALF_L, W, panel, L, 16, 16,
                TEX_W, TEX_H, packedLight, packedOverlay);
        EntityPlaneRenderer.boxFace(pose, buffer, Direction.NORTH, -HALF_W, wallY, -HALF_L, W, WALL_H, panel, 16, 0,
                TEX_W, TEX_H, packedLight, packedOverlay);
        EntityPlaneRenderer.boxFace(pose, buffer, Direction.EAST, HALF_W - panel, wallY, -HALF_L, panel, WALL_H, L, 0,
                0, TEX_W, TEX_H, packedLight, packedOverlay);
        EntityPlaneRenderer.boxFace(pose, buffer, Direction.WEST, -HALF_W, wallY, -HALF_L, panel, WALL_H, L, 32, 0,
                TEX_W, TEX_H, packedLight, packedOverlay);
        EntityPlaneRenderer.boxFace(pose, buffer, Direction.UP, -3.0F, wallY, -1.5F * L, 6.0F, panel, 16.0F, 15, 0,
                TEX_W, TEX_H, packedLight, packedOverlay);
    }

    public void renderWheels(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        float radius = WHEEL_CUBE_RADIUS;
        EntityPlaneRenderer.westDiscOnPart(poseStack, buffer, wheelLeft, packedLight, packedOverlay, WHEEL_MESH_T,
                radius, 0, 16, TEX_W, TEX_H);
        EntityPlaneRenderer.westDiscOnPart(poseStack, buffer, wheelRight, packedLight, packedOverlay, WHEEL_MESH_T,
                radius, 0, 16, TEX_W, TEX_H);
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, float red,
            float green, float blue, float alpha) {
        renderHull(poseStack, buffer, packedLight, packedOverlay);
        renderWheels(poseStack, buffer, packedLight, packedOverlay);
    }
}
