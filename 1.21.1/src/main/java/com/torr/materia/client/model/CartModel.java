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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;
import java.util.Set;

/**
 * Cart hull + wheels in 1/16-block units (16 units = 1 block). Matches {@link CartEntity} footprint.
 * <p>
 * Wheels are authored at 1-block diameter so a 16×16 texture maps 1:1 to the disc face, then scaled
 * in {@link #setupAnim} to {@link CartEntity#WHEEL_RADIUS}.
 */
public class CartModel extends EntityModel<CartEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "cart"), "main");

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

    /** Template wheel diameter in model units — one block, matches a 16×16 wheel texture. */
    private static final float WHEEL_MESH_D = U;
    private static final float WHEEL_MESH_T = 2.0F;
    private static final float WHEEL_MESH_HALF = WHEEL_MESH_D * 0.5F;

    private static final float WHEEL_D = CartEntity.WHEEL_RADIUS * 2.0F * U;
    /** Hull floor sits at wheel axle height (center of the disc). */
    private static final float BODY_BASE = WHEEL_D * 0.5F;
    private static final float WHEEL_Z_CENTER_FRONT = -HALF_L + WHEEL_D * 0.5F + 2.0F;
    private static final float WHEEL_Z_CENTER_BACK = HALF_L - WHEEL_D * 0.5F - 2.0F;
    /** Outward wheel disc — left wheels use this face as-is; right wheels rotate 180° on Y. */
    private static final Set<Direction> WHEEL_DISC_FACE = EnumSet.of(Direction.WEST);
    /** Front/back wall shells omit the outward face so it can use a separate texture pass. */
    private static final Set<Direction> WALL_NORTH_OUTER = EnumSet.of(Direction.NORTH);
    private static final Set<Direction> WALL_NORTH_SHELL = EnumSet.of(Direction.SOUTH, Direction.EAST, Direction.WEST,
            Direction.UP, Direction.DOWN);
    private static final Set<Direction> WALL_SOUTH_OUTER = EnumSet.of(Direction.SOUTH);
    private static final Set<Direction> WALL_SOUTH_SHELL = EnumSet.of(Direction.NORTH, Direction.EAST, Direction.WEST,
            Direction.UP, Direction.DOWN);
    private static final float WHEEL_PART_SCALE = CartEntity.WHEEL_RADIUS * 2.0F;

    /** Draft arms extend from the front wall (−Z); matches {@link CartEntity#DRAFT_HOOK_FORWARD}. */
    private static final float DRAFT_ARM_LEN = 5.0F;
    private static final float DRAFT_ARM_W = 1.5F;
    private static final float DRAFT_ARM_H = 2.0F;
    private static final float DRAFT_ARM_Y = BODY_BASE + FLOOR_H + WALL_H * 0.35F;
    private static final float DRAFT_ARM_X = HALF_W * 0.55F;

    /** Storage chest in the cart bed — visual only (inventory is on the entity). */
    private static final float CHEST_W = 12.0F;
    private static final float CHEST_BASE_H = 4.0F;
    private static final float CHEST_LID_H = 2.0F;
    private static final float CHEST_D = 6.0F;
    private static final float CHEST_LID_OVERHANG = 0.25F;
    private static final float CHEST_X = -CHEST_W * 0.5F;
    private static final float CHEST_Y = BODY_BASE + FLOOR_H;
    private static final float CHEST_Z = HALF_L - WALL_T - CHEST_D - 1.0F;
    private static final float CHEST_LID_X = CHEST_X - CHEST_LID_OVERHANG;
    private static final float CHEST_LID_Y = CHEST_Y + CHEST_BASE_H;
    private static final float CHEST_LID_Z = CHEST_Z - CHEST_LID_OVERHANG;
    private static final float CHEST_LID_W = CHEST_W + CHEST_LID_OVERHANG * 2.0F;
    private static final float CHEST_LID_D = CHEST_D + CHEST_LID_OVERHANG * 2.0F;

    public final ModelPart root;
    public final ModelPart floor;
    /** Front wall outer face only (−Z, {@code cart_front.png}). */
    public final ModelPart wallNorthFace;
    /** Front wall without the outer face ({@code cart.png}). */
    public final ModelPart wallNorthShell;
    /** Back wall outer face only (+Z, {@code cart_back.png}). */
    public final ModelPart wallSouthFace;
    /** Back wall without the outer face ({@code cart.png}). */
    public final ModelPart wallSouthShell;
    public final ModelPart wallWest;
    public final ModelPart wallEast;
    public final ModelPart draftArmLeft;
    public final ModelPart draftArmRight;
    public final ModelPart draftCrossbar;
    public final ModelPart chestBase;
    public final ModelPart chestLid;
    public final ModelPart wheelLeftFront;
    public final ModelPart wheelLeftBack;
    public final ModelPart wheelRightFront;
    public final ModelPart wheelRightBack;

    public CartModel(ModelPart root) {
        this.root = root;
        this.floor = root.getChild("floor");
        this.wallNorthFace = root.getChild("wall_north_face");
        this.wallNorthShell = root.getChild("wall_north_shell");
        this.wallSouthFace = root.getChild("wall_south_face");
        this.wallSouthShell = root.getChild("wall_south_shell");
        this.wallWest = root.getChild("wall_west");
        this.wallEast = root.getChild("wall_east");
        this.draftArmLeft = root.getChild("draft_arm_left");
        this.draftArmRight = root.getChild("draft_arm_right");
        this.draftCrossbar = root.getChild("draft_crossbar");
        this.chestBase = root.getChild("chest_base");
        this.chestLid = root.getChild("chest_lid");
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
                "wall_north_shell",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L, W, WALL_H, WALL_T, WALL_NORTH_SHELL),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_north_face",
                CubeListBuilder.create()
                        .texOffs(6, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L, W, WALL_H, WALL_T, WALL_NORTH_OUTER),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_south_shell",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, HALF_L - WALL_T, W, WALL_H, WALL_T, WALL_SOUTH_SHELL),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_south_face",
                CubeListBuilder.create()
                        .texOffs(6, 0)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, HALF_L - WALL_T, W, WALL_H, WALL_T, WALL_SOUTH_OUTER),
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

        float draftTipZ = -HALF_L - DRAFT_ARM_LEN;
        root.addOrReplaceChild(
                "draft_arm_left",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-DRAFT_ARM_X - DRAFT_ARM_W * 0.5F, DRAFT_ARM_Y, draftTipZ, DRAFT_ARM_W, DRAFT_ARM_H,
                                DRAFT_ARM_LEN),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "draft_arm_right",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(DRAFT_ARM_X - DRAFT_ARM_W * 0.5F, DRAFT_ARM_Y, draftTipZ, DRAFT_ARM_W, DRAFT_ARM_H,
                                DRAFT_ARM_LEN),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "draft_crossbar",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-DRAFT_ARM_X, DRAFT_ARM_Y + DRAFT_ARM_H * 0.25F, draftTipZ - 1.5F, DRAFT_ARM_X * 2.0F,
                                DRAFT_ARM_H * 0.5F, 1.5F),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "chest_base",
                CubeListBuilder.create()
                        .texOffs(12, 4)
                        .addBox(CHEST_X, CHEST_Y, CHEST_Z, CHEST_W, CHEST_BASE_H, CHEST_D),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "chest_lid",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(CHEST_LID_X, CHEST_LID_Y, CHEST_LID_Z, CHEST_LID_W, CHEST_LID_H, CHEST_LID_D),
                PartPose.ZERO);

        float halfAxle = WHEEL_MESH_T * 0.5F;
        addWheel(root, "wheel_left_front", -HALF_W - halfAxle, WHEEL_Z_CENTER_FRONT, false);
        addWheel(root, "wheel_left_back", -HALF_W - halfAxle, WHEEL_Z_CENTER_BACK, false);
        addWheel(root, "wheel_right_front", HALF_W + halfAxle, WHEEL_Z_CENTER_FRONT, true);
        addWheel(root, "wheel_right_back", HALF_W + halfAxle, WHEEL_Z_CENTER_BACK, true);

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }

    /**
     * @param faceOutward when true, rotate 180° on Y so {@link #WHEEL_DISC_FACE} points away from the cart
     *                    (right side) instead of using UV mirror or a separate east face.
     */
    private static void addWheel(PartDefinition root, String name, float centerX, float centerZ, boolean faceOutward) {
        PartPose pose = faceOutward
                ? PartPose.offsetAndRotation(centerX, BODY_BASE, centerZ, 0.0F, (float) Math.PI, 0.0F)
                : PartPose.offset(centerX, BODY_BASE, centerZ);
        root.addOrReplaceChild(
                name,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-WHEEL_MESH_T * 0.5F, -WHEEL_MESH_HALF, -WHEEL_MESH_HALF, WHEEL_MESH_T, WHEEL_MESH_D,
                                WHEEL_MESH_D, WHEEL_DISC_FACE),
                pose);
    }

    @Override
    public void setupAnim(CartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        scaleWheel(wheelLeftFront);
        scaleWheel(wheelLeftBack);
        scaleWheel(wheelRightFront);
        scaleWheel(wheelRightBack);
        float roll = entity.wheelRotation;
        wheelLeftFront.xRot = roll;
        wheelLeftBack.xRot = roll;
        wheelRightFront.xRot = -roll;
        wheelRightBack.xRot = -roll;
    }

    private static void scaleWheel(ModelPart wheel) {
        wheel.xScale = WHEEL_PART_SCALE;
        wheel.yScale = WHEEL_PART_SCALE;
        wheel.zScale = WHEEL_PART_SCALE;
    }

    public void renderHullBody(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        floor.render(poseStack, buffer, packedLight, packedOverlay);
        wallNorthShell.render(poseStack, buffer, packedLight, packedOverlay);
        wallSouthShell.render(poseStack, buffer, packedLight, packedOverlay);
        wallWest.render(poseStack, buffer, packedLight, packedOverlay);
        wallEast.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderChest(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        chestBase.render(poseStack, buffer, packedLight, packedOverlay);
        chestLid.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderFront(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        wallNorthFace.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderBack(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        wallSouthFace.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderDraftArms(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        draftArmLeft.render(poseStack, buffer, packedLight, packedOverlay);
        draftArmRight.render(poseStack, buffer, packedLight, packedOverlay);
        draftCrossbar.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderHull(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        renderHullBody(poseStack, buffer, packedLight, packedOverlay);
        renderFront(poseStack, buffer, packedLight, packedOverlay);
        renderBack(poseStack, buffer, packedLight, packedOverlay);
        renderDraftArms(poseStack, buffer, packedLight, packedOverlay);
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
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        renderHull(poseStack, buffer, packedLight, packedOverlay);
        renderWheels(poseStack, buffer, packedLight, packedOverlay);
    }
}
