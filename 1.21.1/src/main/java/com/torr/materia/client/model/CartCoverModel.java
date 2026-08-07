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
 * Fabric cover over the cart bed — 1.25×1.25×2 blocks with 0.125 overhang per side.
 * Atlas {@code textures/entity/taupe_cart_cover.png} (32×32):
 * upper-left 16×16 = front/back, right half = sides + top.
 */
public class CartCoverModel extends EntityModel<CartEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "cart_cover"), "main");

    private static final int COVER_TEX_W = 32;
    private static final int COVER_TEX_H = 32;

    private static final float U = 16.0F;
    private static final float COVER_OVERHANG = 0.125F;
    private static final float COVER_WIDTH = CartEntity.WIDTH + COVER_OVERHANG * 2.0F;
    private static final float COVER_HEIGHT = 1.25F;
    private static final float COVER_LENGTH = CartEntity.LENGTH;

    private static final float COVER_W = COVER_WIDTH * U;
    private static final float COVER_H = COVER_HEIGHT * U;
    private static final float COVER_L = COVER_LENGTH * U;
    private static final float HALF_COVER_W = COVER_W * 0.5F;
    private static final float HALF_COVER_L = COVER_L * 0.5F;
    private static final float SHELL = 0.01F;

    private static final float H = CartEntity.HEIGHT * U;
    private static final float FLOOR_H = H * CartEntity.FLOOR_HEIGHT_FRACTION;
    private static final float WALL_H = H - FLOOR_H;
    private static final float WHEEL_D = CartEntity.WHEEL_RADIUS * 2.0F * U;
    private static final float BODY_BASE = WHEEL_D * 0.5F;
    private static final float COVER_Y = BODY_BASE + FLOOR_H + WALL_H;

    private static final Set<Direction> FACE_NORTH = EnumSet.of(Direction.NORTH);
    private static final Set<Direction> FACE_SOUTH = EnumSet.of(Direction.SOUTH);
    private static final Set<Direction> FACE_WEST = EnumSet.of(Direction.WEST);
    private static final Set<Direction> FACE_EAST = EnumSet.of(Direction.EAST);
    private static final Set<Direction> FACE_UP = EnumSet.of(Direction.UP);

    private final ModelPart root;

    public CartCoverModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Front (−Z): upper-left quadrant, V flipped via texOffs(0, 16)
        root.addOrReplaceChild(
                "cover_front",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-HALF_COVER_W, COVER_Y, -HALF_COVER_L, COVER_W, COVER_H, SHELL, FACE_NORTH),
                PartPose.ZERO);

        // Back (+Z): same art as front
        root.addOrReplaceChild(
                "cover_back",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-HALF_COVER_W, COVER_Y, HALF_COVER_L - SHELL, COVER_W, COVER_H, SHELL, FACE_SOUTH),
                PartPose.ZERO);

        // Left / right sides: right half of atlas
        root.addOrReplaceChild(
                "cover_west",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-HALF_COVER_W, COVER_Y, -HALF_COVER_L, SHELL, COVER_H, COVER_L, FACE_WEST),
                PartPose.ZERO);
        root.addOrReplaceChild(
                "cover_east",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(HALF_COVER_W - SHELL, COVER_Y, -HALF_COVER_L, SHELL, COVER_H, COVER_L, FACE_EAST),
                PartPose.ZERO);

        // Top: UV rotated 90° via a Y-axis pivot (same footprint, different texture orientation)
        PartDefinition topPivot = root.addOrReplaceChild(
                "cover_top_pivot",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, COVER_Y + COVER_H - SHELL, 0.0F));
        topPivot.addOrReplaceChild(
                "cover_top",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-HALF_COVER_L, 0.0F, -HALF_COVER_W, COVER_L, SHELL, COVER_W, FACE_UP),
                PartPose.rotation(0.0F, (float) (-Math.PI / 2.0), 0.0F));

        return LayerDefinition.create(mesh, COVER_TEX_W, COVER_TEX_H);
    }

    @Override
    public void setupAnim(CartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
    }

    public void renderCover(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
            com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.renderCover(poseStack, buffer, packedLight, packedOverlay);
    }
}
