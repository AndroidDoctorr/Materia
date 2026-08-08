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
 * Cart wood hull (floor, walls, draft arms) using {@code textures/entity/oak_cart.png}
 * ({@value #TEX_W}×{@value #TEX_H}).
 * <p>
 * Atlas layout ({@value #TEX_W}×{@value #TEX_H}):
 * <ul>
 *   <li>Top row, cols 0–31: hull / sides / front (two plank tiles)</li>
 *   <li>Top row, cols 32–47: back wall (planks + tools)</li>
 *   <li>Bottom row: plain planks in all three slots (UV wrap margin)</li>
 *   <li>Col 48–49: padding so back-face cross-UV (u+16..u+32) does not wrap to column 0</li>
 * </ul>
 * Hull parts use {@code texOffs(0, 16)} so faces that span 32 texels horizontally
 * pick up plain wood from the bottom row instead of the tools tile.
 * Back wall uses {@code texOffs(16, 0)} — Minecraft maps SOUTH at {@code u+dx}, not {@code u}.
 * Chest and wheels stay on {@link CartModel} with their own 16×16 textures.
 */
public class CartBodyModel extends EntityModel<CartEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation(materia.MOD_ID, "cart_body"), "main");

    private static final int TILE = 16;
    /** 48 px of art + 2 px gutter so back-face cross-UV (u+dx..u+2dx) never wraps to column 0. */
    private static final int TEX_W = TILE * 3 + 2;
    private static final int TEX_H = TILE * 2;
    private static final int HULL_U = 0;
    /** Bottom row — plain planks for horizontal/vertical UV wrap. */
    private static final int HULL_V = TILE;
    private static final int FRONT_U = 6;
    private static final int FRONT_V = TILE;
    /** Cross-layout SOUTH starts at u+dx; 16+16=32 → third tile (tools). */
    private static final int BACK_U = TILE - 4;
    private static final int BACK_V = 0;

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
    private static final float BODY_BASE = WHEEL_D * 0.5F;

    private static final Set<Direction> WALL_NORTH_OUTER = EnumSet.of(Direction.NORTH);
    private static final Set<Direction> WALL_NORTH_SHELL = EnumSet.of(Direction.SOUTH, Direction.EAST, Direction.WEST,
            Direction.UP, Direction.DOWN);
    private static final Set<Direction> WALL_SOUTH_OUTER = EnumSet.of(Direction.SOUTH);
    private static final Set<Direction> WALL_SOUTH_SHELL = EnumSet.of(Direction.NORTH, Direction.EAST, Direction.WEST,
            Direction.UP, Direction.DOWN);

    private static final float DRAFT_ARM_LEN = 5.0F;
    private static final float DRAFT_ARM_W = 1.5F;
    private static final float DRAFT_ARM_H = 2.0F;
    private static final float DRAFT_ARM_Y = BODY_BASE + FLOOR_H + WALL_H * 0.35F;
    private static final float DRAFT_ARM_X = HALF_W * 0.55F;

    public final ModelPart floor;
    public final ModelPart wallNorthFace;
    public final ModelPart wallNorthShell;
    public final ModelPart wallSouthFace;
    public final ModelPart wallSouthShell;
    public final ModelPart wallWest;
    public final ModelPart wallEast;
    public final ModelPart draftArmLeft;
    public final ModelPart draftArmRight;
    public final ModelPart draftCrossbar;

    public CartBodyModel(ModelPart root) {
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
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "floor",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(-HALF_W, BODY_BASE, -HALF_L, W, FLOOR_H, L),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_north_shell",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L, W, WALL_H, WALL_T, WALL_NORTH_SHELL),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_north_face",
                CubeListBuilder.create()
                        .texOffs(FRONT_U, FRONT_V)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L, W, WALL_H, WALL_T, WALL_NORTH_OUTER),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_south_shell",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, HALF_L - WALL_T, W, WALL_H, WALL_T, WALL_SOUTH_SHELL),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_south_face",
                CubeListBuilder.create()
                        .texOffs(BACK_U, BACK_V)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, HALF_L - WALL_T, W, WALL_H, WALL_T, WALL_SOUTH_OUTER),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_west",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(-HALF_W, BODY_BASE + FLOOR_H, -HALF_L + WALL_T, WALL_T, WALL_H, INNER_L),
                PartPose.ZERO);

        root.addOrReplaceChild(
                "wall_east",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(HALF_W - WALL_T, BODY_BASE + FLOOR_H, -HALF_L + WALL_T, WALL_T, WALL_H, INNER_L),
                PartPose.ZERO);

        float draftTipZ = -HALF_L - DRAFT_ARM_LEN;
        root.addOrReplaceChild(
                "draft_arm_left",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(-DRAFT_ARM_X - DRAFT_ARM_W * 0.5F, DRAFT_ARM_Y, draftTipZ, DRAFT_ARM_W, DRAFT_ARM_H,
                                DRAFT_ARM_LEN),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "draft_arm_right",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(DRAFT_ARM_X - DRAFT_ARM_W * 0.5F, DRAFT_ARM_Y, draftTipZ, DRAFT_ARM_W, DRAFT_ARM_H,
                                DRAFT_ARM_LEN),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "draft_crossbar",
                CubeListBuilder.create()
                        .texOffs(HULL_U, HULL_V)
                        .addBox(-DRAFT_ARM_X, DRAFT_ARM_Y + DRAFT_ARM_H * 0.25F, draftTipZ - 1.5F, DRAFT_ARM_X * 2.0F,
                                DRAFT_ARM_H * 0.5F, 1.5F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }

    @Override
    public void setupAnim(CartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
    }

    public void renderHullBody(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        floor.render(poseStack, buffer, packedLight, packedOverlay);
        wallNorthShell.render(poseStack, buffer, packedLight, packedOverlay);
        wallSouthShell.render(poseStack, buffer, packedLight, packedOverlay);
        wallWest.render(poseStack, buffer, packedLight, packedOverlay);
        wallEast.render(poseStack, buffer, packedLight, packedOverlay);
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

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, float red,
            float green, float blue, float alpha) {
        renderHullBody(poseStack, buffer, packedLight, packedOverlay);
        renderFront(poseStack, buffer, packedLight, packedOverlay);
        renderBack(poseStack, buffer, packedLight, packedOverlay);
        renderDraftArms(poseStack, buffer, packedLight, packedOverlay);
    }
}
