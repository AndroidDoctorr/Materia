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
 * Lantern prism between the draft arms. Atlas ({@link #TEX_W}×{@link #TEX_H}):
 * top 6×6 caps at (0,0), sides 6×7 at (0,6). Geometry is full 6×7×6 then scaled to 67%.
 */
public class CartLanternModel extends EntityModel<CartEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "cart_lantern"), "main");

    private static final int TEX_W = 6;
    private static final int TEX_H = 13;

    private static final int CAP_U = 0;
    private static final int CAP_V = 0;
    private static final int SIDE_U = 0;
    private static final int SIDE_V = 6;

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
    private static final float SHELL = 0.01F;

    private static final float MOUNT_X = 0.0F;
    private static final float MOUNT_Y = DRAFT_ARM_Y + DRAFT_ARM_H * 0.15F;
    private static final float MOUNT_Z = DRAFT_TIP_Z - DRAFT_ARM_LEN * 0.5F + LANTERN_BACK_OFFSET;

    private static final Set<Direction> FACE_NORTH = EnumSet.of(Direction.NORTH);
    private static final Set<Direction> FACE_SOUTH = EnumSet.of(Direction.SOUTH);
    private static final Set<Direction> FACE_WEST = EnumSet.of(Direction.WEST);
    private static final Set<Direction> FACE_EAST = EnumSet.of(Direction.EAST);
    private static final Set<Direction> FACE_UP = EnumSet.of(Direction.UP);
    private static final Set<Direction> FACE_DOWN = EnumSet.of(Direction.DOWN);

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

        PartDefinition lantern = root.addOrReplaceChild(
                "lantern",
                CubeListBuilder.create(),
                PartPose.offset(MOUNT_X, MOUNT_Y, MOUNT_Z));

        lantern.addOrReplaceChild(
                "face_north",
                CubeListBuilder.create()
                        .texOffs(SIDE_U, SIDE_V)
                        .addBox(-HALF_LANTERN_W, 0.0F, -HALF_LANTERN_D, LANTERN_W, LANTERN_H, SHELL, FACE_NORTH),
                PartPose.ZERO);
        lantern.addOrReplaceChild(
                "face_south",
                CubeListBuilder.create()
                        .texOffs(SIDE_U, SIDE_V)
                        .addBox(-HALF_LANTERN_W, 0.0F, HALF_LANTERN_D - SHELL, LANTERN_W, LANTERN_H, SHELL, FACE_SOUTH),
                PartPose.ZERO);
        lantern.addOrReplaceChild(
                "face_west",
                CubeListBuilder.create()
                        .texOffs(SIDE_U, SIDE_V)
                        .addBox(-HALF_LANTERN_W, 0.0F, -HALF_LANTERN_D, SHELL, LANTERN_H, LANTERN_D, FACE_WEST),
                PartPose.ZERO);
        lantern.addOrReplaceChild(
                "face_east",
                CubeListBuilder.create()
                        .texOffs(SIDE_U, SIDE_V)
                        .addBox(HALF_LANTERN_W - SHELL, 0.0F, -HALF_LANTERN_D, SHELL, LANTERN_H, LANTERN_D, FACE_EAST),
                PartPose.ZERO);
        lantern.addOrReplaceChild(
                "face_up",
                CubeListBuilder.create()
                        .texOffs(CAP_U, CAP_V)
                        .addBox(-HALF_LANTERN_W, LANTERN_H - SHELL, -HALF_LANTERN_D, LANTERN_W, SHELL, LANTERN_D, FACE_UP),
                PartPose.ZERO);
        lantern.addOrReplaceChild(
                "face_down",
                CubeListBuilder.create()
                        .texOffs(CAP_U, CAP_V)
                        .addBox(-HALF_LANTERN_W, 0.0F, -HALF_LANTERN_D, LANTERN_W, SHELL, LANTERN_D, FACE_DOWN),
                PartPose.ZERO);

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
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.renderLantern(poseStack, buffer, packedLight, packedOverlay);
    }
}
