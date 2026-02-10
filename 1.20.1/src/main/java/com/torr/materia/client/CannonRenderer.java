package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.torr.materia.ModBlocks;
import com.torr.materia.block.CannonBlock;
import com.torr.materia.blockentity.CannonBlockEntity;
import com.torr.materia.materia;
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

    // 1.20.1: barrel model aligns with the yawWorld convention used by aiming + firing.
    // Keep any model-specific tweaks here (render-only).
    private static final float MODEL_YAW_OFFSET_DEG = 0f;

    private final BlockRenderDispatcher blockRenderer;
    private static Boolean lastLoggedIntegrated = null;

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

        // Match the blockstate y-rotations used elsewhere (north=0, east=90, south=180, west=270)
        float baseModelY = CannonMath.facingToModelY(facing);

        float yaw = be.getYawDeg();
        float pitch = be.getPitchDeg();

        Boolean integrated = null;
        try {
            integrated = Minecraft.getInstance().hasSingleplayerServer();
        } catch (Throwable ignored) {
            integrated = null;
        }

        // Log once per "integrated vs dedicated-server connection" mode.
        // This lets us compare singleplayer vs multiplayer within the same client session.
        if (integrated == null ? lastLoggedIntegrated != null : !integrated.equals(lastLoggedIntegrated)) {
            lastLoggedIntegrated = integrated;
            String source = "unknown";
            try {
                var cs = CannonRenderer.class.getProtectionDomain().getCodeSource();
                if (cs != null && cs.getLocation() != null) source = cs.getLocation().toString();
            } catch (Throwable ignored) {
            }
            float yawWorld = baseModelY + yaw;
            float yawVisual = baseModelY + yaw + MODEL_YAW_OFFSET_DEG;
            materia.LOGGER.info(
                    "CannonRenderer init: integrated={} source={} pos={} facing={} baseModelY={} yaw={} pitch={} yawWorld={} yawVisual={} offset={}",
                    integrated, source, be.getBlockPos(), facing, baseModelY, yaw, pitch, yawWorld, yawVisual, MODEL_YAW_OFFSET_DEG
            );
        }

        poseStack.pushPose();

        // Rotate barrel around a pivot point above the base
        poseStack.translate(PIVOT_X, PIVOT_Y, PIVOT_Z);
        // IMPORTANT: PoseStack rotations are applied in local space.
        // We want yaw to pick the heading, then pitch to tilt towards that heading.
        // Use the same yaw convention as aiming/firing:
        // yawWorld = baseModelY + yaw
        poseStack.mulPose(Axis.YP.rotationDegrees(baseModelY + yaw + MODEL_YAW_OFFSET_DEG));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
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

