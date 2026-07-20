package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.block.PlanterBlock;
import com.torr.materia.block.PottedPlantRules;
import com.torr.materia.blockentity.PlanterBlockEntity;
import com.torr.materia.blockentity.UrnBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PottedPlantRenderer {
    private static final float PLANTER_SOIL_Y = 7f / 16f;
    private static final float PLANTER_LEFT_X = 4f / 16f;
    private static final float PLANTER_RIGHT_X = 12f / 16f;
    private static final float PLANTER_SLOT_Z = 4f / 16f;
    private static final float PLANTER_PLANT_SCALE = 0.45f;

    private static final float URN_SOIL_Y = 1f;
    private static final float URN_PLANT_SCALE = 0.55f;

    private PottedPlantRenderer() {
    }

    public static void renderBlock(PoseStack poseStack, Block block, float centerX, float soilY, float centerZ,
                                   float scale, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (block == net.minecraft.world.level.block.Blocks.AIR) {
            return;
        }
        BlockState lower = PottedPlantRules.displayState(block);
        poseStack.pushPose();
        poseStack.translate(centerX, soilY, centerZ);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                lower, poseStack, buffer, packedLight, packedOverlay);
        if (PottedPlantRules.isTallPlant(block)) {
            poseStack.translate(0.0D, 1.0D, 0.0D);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                    PottedPlantRules.tallUpperState(block), poseStack, buffer, packedLight, packedOverlay);
        }
        poseStack.popPose();
    }

    public static float[] mapPlanterPoint(Direction facing, float x, float z) {
        return switch (facing) {
            case SOUTH -> new float[]{1f - x, 1f - z};
            case EAST -> new float[]{1f - z, x};
            case WEST -> new float[]{z, 1f - x};
            default -> new float[]{x, z};
        };
    }

    public static class Planter implements BlockEntityRenderer<PlanterBlockEntity> {
        public Planter(BlockEntityRendererProvider.Context context) {
        }

        @Override
        public void render(PlanterBlockEntity planter, float partialTick, PoseStack poseStack,
                           MultiBufferSource buffer, int packedLight, int packedOverlay) {
            Direction facing = planter.getBlockState().getValue(PlanterBlock.FACING);
            renderSlot(planter.getLeftPlant(), PLANTER_LEFT_X, PLANTER_SLOT_Z, facing, poseStack, buffer, packedLight, packedOverlay);
            renderSlot(planter.getRightPlant(), PLANTER_RIGHT_X, PLANTER_SLOT_Z, facing, poseStack, buffer, packedLight, packedOverlay);
        }

        private static void renderSlot(Block plant, float localX, float localZ, Direction facing,
                                       PoseStack poseStack, MultiBufferSource buffer,
                                       int packedLight, int packedOverlay) {
            float[] mapped = mapPlanterPoint(facing, localX, localZ);
            renderBlock(poseStack, plant, mapped[0], PLANTER_SOIL_Y, mapped[1],
                    PLANTER_PLANT_SCALE, buffer, packedLight, packedOverlay);
        }
    }

    public static class Urn implements BlockEntityRenderer<UrnBlockEntity> {
        public Urn(BlockEntityRendererProvider.Context context) {
        }

        @Override
        public void render(UrnBlockEntity urn, float partialTick, PoseStack poseStack,
                           MultiBufferSource buffer, int packedLight, int packedOverlay) {
            renderBlock(poseStack, urn.getPlant(), 0.5F, URN_SOIL_Y, 0.5F,
                    URN_PLANT_SCALE, buffer, packedLight, packedOverlay);
        }
    }
}
