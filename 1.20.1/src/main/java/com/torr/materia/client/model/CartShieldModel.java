package com.torr.materia.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.torr.materia.entity.CartEntity;
import com.torr.materia.entity.CartWallSide;
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
 * Side-mounted pavise shields ({@value #SHIELD_DEPTH}×{@value #SHIELD_HEIGHT} model units,
 * 1:1 with {@value #TEX_W}×{@value #TEX_H} {@code cart_shield.png}).
 */
public class CartShieldModel extends EntityModel<CartEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation(materia.MOD_ID, "cart_shield"), "main");

    private static final int TEX_W = 23;
    private static final int TEX_H = 13;

    private static final float U = 16.0F;
    private static final float W = CartEntity.WIDTH * U;
    private static final float H = CartEntity.HEIGHT * U;
    private static final float FLOOR_H = H * CartEntity.FLOOR_HEIGHT_FRACTION;
    private static final float WALL_H = H - FLOOR_H;
    private static final float WHEEL_D = CartEntity.WHEEL_RADIUS * 2.0F * U;
    private static final float BODY_BASE = WHEEL_D * 0.5F;
    private static final float HALF_W = W * 0.5F;
    private static final float WALL_T = W * CartEntity.WALL_THICKNESS_FRACTION;

    /** Depth along cart length (Z). */
    private static final float SHIELD_DEPTH = 22.0F;
    /** Height on the wall (Y). */
    private static final float SHIELD_HEIGHT = 12.0F;
    private static final float HALF_SHIELD_DEPTH = SHIELD_DEPTH * 0.5F;
    /** Match cart wall plank thickness so the shield reads as solid wood, not a plane. */
    private static final float SHIELD_THICK = WALL_T;
    private static final float SHIELD_Y = BODY_BASE + FLOOR_H + (WALL_H - SHIELD_HEIGHT) * 0.5F;

    private static final Set<Direction> LEFT_OUTER = EnumSet.of(
            Direction.WEST, Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN);
    private static final Set<Direction> RIGHT_OUTER = EnumSet.of(
            Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN);

    private final ModelPart shieldLeft;
    private final ModelPart shieldRight;

    public CartShieldModel(ModelPart root) {
        this.shieldLeft = root.getChild("shield_left");
        this.shieldRight = root.getChild("shield_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "shield_left",
                CubeListBuilder.create()
                        .texOffs(0, 5)
                        .addBox(0.0F, 0.0F, -HALF_SHIELD_DEPTH, SHIELD_THICK, SHIELD_HEIGHT, SHIELD_DEPTH,
                                LEFT_OUTER),
                PartPose.offset(-HALF_W - SHIELD_THICK, SHIELD_Y, 0.0F));

        root.addOrReplaceChild(
                "shield_right",
                CubeListBuilder.create()
                        .texOffs(0, 5)
                        .addBox(0.0F, 0.0F, -HALF_SHIELD_DEPTH, SHIELD_THICK, SHIELD_HEIGHT, SHIELD_DEPTH,
                                RIGHT_OUTER),
                PartPose.offset(HALF_W, SHIELD_Y, 0.0F));

        return LayerDefinition.create(mesh, TEX_W, TEX_H);
    }

    @Override
    public void setupAnim(CartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
    }

    public void renderShields(PoseStack poseStack, VertexConsumer buffer, CartEntity entity, int packedLight,
            int packedOverlay) {
        if (entity.hasShield(CartWallSide.LEFT)) {
            this.shieldLeft.render(poseStack, buffer, packedLight, packedOverlay);
        }
        if (entity.hasShield(CartWallSide.RIGHT)) {
            this.shieldRight.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha) {
    }
}
