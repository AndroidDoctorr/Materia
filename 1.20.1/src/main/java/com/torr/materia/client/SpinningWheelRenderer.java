package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.SpinningWheelBlock;
import com.torr.materia.blockentity.SpinningWheelBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class SpinningWheelRenderer implements BlockEntityRenderer<SpinningWheelBlockEntity> {
    private static final long FRAME_TICKS = 5L;

    private final BlockRenderDispatcher blockRenderer;

    public SpinningWheelRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(SpinningWheelBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be.getLevel() == null) return;

        long gameTime = be.getLevel().getGameTime();
        int frame = (int) ((gameTime / FRAME_TICKS) & 1L);

        BlockState state = be.getBlockState();
        if (state.hasProperty(SpinningWheelBlock.FRAME)) {
            state = state.setValue(SpinningWheelBlock.FRAME, frame);
        }

        blockRenderer.renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);
    }
}

