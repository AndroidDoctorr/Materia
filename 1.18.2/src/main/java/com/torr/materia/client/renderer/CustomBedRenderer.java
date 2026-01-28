package com.torr.materia.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class CustomBedRenderer implements BlockEntityRenderer<BedBlockEntity> {
    private final BedRenderer vanillaBedRenderer;
    private final ModelPart headRoot;
    private final ModelPart footRoot;

    public CustomBedRenderer(BlockEntityRendererProvider.Context context) {
        this.vanillaBedRenderer = new BedRenderer(context);
        this.headRoot = context.bakeLayer(ModelLayers.BED_HEAD);
        this.footRoot = context.bakeLayer(ModelLayers.BED_FOOT);
    }

    @Override
    public void render(BedBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        // For vanilla beds, delegate fully to Mojang's BedRenderer so item transforms match exactly
        // (fixes the "floating" look caused by our slightly different math).
        if (!isCustomBed(blockEntity.getBlockState().getBlock())) {
            vanillaBedRenderer.render(blockEntity, partialTick, poseStack, buffer, combinedLight, combinedOverlay);
            return;
        }

        Material material = getCustomBedMaterial(blockEntity);
        
        Level level = blockEntity.getLevel();
        if (level != null) {
            BlockState blockState = blockEntity.getBlockState();
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> combineResult = DoubleBlockCombiner.combineWithNeigbour(
                    BlockEntityType.BED,
                    BedBlock::getBlockType,
                    BedBlock::getConnectedDirection,
                    ChestBlock.FACING,
                    blockState,
                    level,
                    blockEntity.getBlockPos(),
                    (world, pos) -> false
            );
            int light = combineResult.apply(new BrightnessCombiner<>()).get(combinedLight);
            this.renderPiece(poseStack, buffer, blockState.getValue(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot, blockState.getValue(BedBlock.FACING), material, light, combinedOverlay, false);
        } else {
            // Item/inventory rendering: render BOTH halves so the item looks like a full bed.
            // Vanilla BedRenderer does this via a no-level path as well.
            Direction facing = Direction.SOUTH;
            poseStack.pushPose();
            this.renderPiece(poseStack, buffer, this.footRoot, facing, material, combinedLight, combinedOverlay, true);
            poseStack.translate(facing.getStepX(), 0.0D, facing.getStepZ());
            this.renderPiece(poseStack, buffer, this.headRoot, facing, material, combinedLight, combinedOverlay, false);
            poseStack.popPose();
        }
    }

    private void renderPiece(PoseStack poseStack, MultiBufferSource buffer, ModelPart model, Direction direction, Material material, int light, int overlay, boolean foot) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.5625D, 0.0D);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F + direction.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        VertexConsumer vertexConsumer = material.buffer(buffer, RenderType::entitySolid);
        model.render(poseStack, vertexConsumer, light, overlay);
        poseStack.popPose();
    }

    private Material getCustomBedMaterial(BedBlockEntity blockEntity) {
        Block block = blockEntity.getBlockState().getBlock();
        
        if (block == ModBlocks.OCHRE_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/ochre"));
        } else if (block == ModBlocks.RED_OCHRE_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/red_ochre"));
        } else if (block == ModBlocks.INDIGO_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/indigo"));
        } else if (block == ModBlocks.OLIVE_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/olive"));
        } else if (block == ModBlocks.TYRIAN_PURPLE_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/tyrian_purple"));
        } else if (block == ModBlocks.LAVENDER_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/lavender"));
        } else if (block == ModBlocks.CHARCOAL_GRAY_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/charcoal_gray"));
        } else if (block == ModBlocks.TAUPE_BED.get()) {
            return new Material(Sheets.BED_SHEET, new ResourceLocation(materia.MOD_ID, "entity/bed/taupe"));
        }
        
        // For vanilla beds (and bed-item rendering), use the BedBlockEntity's color
        // Bed items in 1.18.2 rely on BedBlockEntity#getColor, not the block instance
        DyeColor color = blockEntity.getColor();
        ResourceLocation texture = new ResourceLocation("minecraft", "entity/bed/" + color.getName());
        return new Material(Sheets.BED_SHEET, texture);
    }

    private static boolean isCustomBed(Block block) {
        return block == ModBlocks.OCHRE_BED.get()
                || block == ModBlocks.RED_OCHRE_BED.get()
                || block == ModBlocks.INDIGO_BED.get()
                || block == ModBlocks.OLIVE_BED.get()
                || block == ModBlocks.TYRIAN_PURPLE_BED.get()
                || block == ModBlocks.LAVENDER_BED.get()
                || block == ModBlocks.CHARCOAL_GRAY_BED.get()
                || block == ModBlocks.TAUPE_BED.get();
    }
}
