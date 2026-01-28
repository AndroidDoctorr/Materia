package com.torr.materia.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.entity.FallingAmphoraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FallingAmphoraRenderer extends EntityRenderer<FallingAmphoraEntity> {
    private final BlockRenderDispatcher dispatcher;

    public FallingAmphoraRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.dispatcher = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(FallingAmphoraEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        BlockState blockstate = entity.getBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();
            if (blockstate != level.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                poseStack.pushPose();
                poseStack.translate(-0.5D, 0.0D, -0.5D);
                this.dispatcher.renderSingleBlock(blockstate, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
                super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(FallingAmphoraEntity entity) {
        // We don't need a texture since we're rendering the block model
        return null;
    }
}
