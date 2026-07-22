package com.torr.materia.client.renderer.entity;



import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Vector3f;

import com.torr.materia.entity.CartEntity;

import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;

import net.minecraft.client.renderer.entity.EntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.minecraft.client.renderer.texture.OverlayTexture;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;



@OnlyIn(Dist.CLIENT)

public class CartRenderer extends EntityRenderer<CartEntity> {



    private static final BlockState PLANKS = Blocks.OAK_PLANKS.defaultBlockState();



    private final BlockRenderDispatcher blockRenderer;



    public CartRenderer(EntityRendererProvider.Context context) {

        super(context);

        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();

        this.shadowRadius = CartEntity.shadowRadius();

    }



    @Override

    public void render(CartEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,

            MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));

        poseStack.translate(0.0D, CartEntity.RENDER_Y_OFFSET, 0.0D);

        poseStack.scale(CartEntity.WIDTH, CartEntity.HEIGHT, CartEntity.LENGTH);

        poseStack.translate(-0.5D, 0.0D, -0.5D);



        float floorH = CartEntity.FLOOR_HEIGHT_FRACTION;

        float wallH = 1.0F - floorH;

        float wallT = CartEntity.WALL_THICKNESS_FRACTION;

        float innerSpan = 1.0F - 2.0F * wallT;



        renderUnitBlock(poseStack, buffer, packedLight, 0.0F, 0.0F, 0.0F, 1.0F, floorH, 1.0F);

        renderUnitBlock(poseStack, buffer, packedLight, 0.0F, floorH, 0.0F, 1.0F, wallH, wallT);

        renderUnitBlock(poseStack, buffer, packedLight, 0.0F, floorH, 1.0F - wallT, 1.0F, wallH, wallT);

        renderUnitBlock(poseStack, buffer, packedLight, 0.0F, floorH, wallT, wallT, wallH, innerSpan);

        renderUnitBlock(poseStack, buffer, packedLight, 1.0F - wallT, floorH, wallT, wallT, wallH, innerSpan);



        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    }



    private void renderUnitBlock(PoseStack poseStack, MultiBufferSource buffer, int packedLight,

            float x, float y, float z, float sx, float sy, float sz) {

        poseStack.pushPose();

        poseStack.translate(x, y, z);

        poseStack.scale(sx, sy, sz);

        this.blockRenderer.renderSingleBlock(PLANKS, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

    }



    @Override

    public ResourceLocation getTextureLocation(CartEntity entity) {

        return null;

    }

}


