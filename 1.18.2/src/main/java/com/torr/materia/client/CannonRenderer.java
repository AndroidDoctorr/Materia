package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.torr.materia.ModBlocks;
import com.torr.materia.block.CannonBlock;
import com.torr.materia.blockentity.CannonBlockEntity;
import com.torr.materia.util.CannonMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CannonRenderer implements BlockEntityRenderer<CannonBlockEntity> {
    private static final double PIVOT_X = 0.5D;
    private static final double PIVOT_Y = 10.0D / 16.0D;
    private static final double PIVOT_Z = 0.5D;

    // Barrel mesh points backward on this port; +180° static correction only (aim sign is separate).
    private static final float MODEL_FORWARD_OFFSET_DEG = 180f;

    private final BlockRenderDispatcher blockRenderer;

    public CannonRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(CannonBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be.getLevel() == null) {
            return;
        }

        BlockState state = be.getBlockState();
        if (!state.hasProperty(CannonBlock.FACING)) {
            return;
        }

        Direction facing = state.getValue(CannonBlock.FACING);
        float yaw = be.getYawDeg();
        float pitch = be.getPitchDeg();

        poseStack.pushPose();
        poseStack.translate(PIVOT_X, PIVOT_Y, PIVOT_Z);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(
                CannonMath.barrelRenderYawDeg(facing, yaw, MODEL_FORWARD_OFFSET_DEG)));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-pitch));
        poseStack.translate(-PIVOT_X, -PIVOT_Y, -PIVOT_Z);

        blockRenderer.renderSingleBlock(
                ModBlocks.CANNON_BARREL.get().defaultBlockState(),
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        poseStack.popPose();
    }
}

