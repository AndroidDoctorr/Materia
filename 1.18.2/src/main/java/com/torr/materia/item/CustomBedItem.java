package com.torr.materia.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A BedItem that provides a custom item renderer so the 3D bed icon can use a non-vanilla bed texture.
 *
 * Vanilla bed items derive their item-render texture from DyeColor; our custom beds need to key off the bed block
 * so we can use textures like "materia:textures/entity/bed/indigo.png".
 */
public class CustomBedItem extends BedItem {
    private final Supplier<Block> bedBlock;

    public CustomBedItem(Supplier<Block> bedBlock, Item.Properties properties) {
        super(bedBlock.get(), properties);
        this.bedBlock = bedBlock;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new CustomBedItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static class CustomBedItemRenderer extends BlockEntityWithoutLevelRenderer {
        private BedBlockEntity cachedBedEntity;
        private Block cachedBlock;

        CustomBedItemRenderer() {
            super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }

        @Override
        public void renderByItem(ItemStack stack,
                                 net.minecraft.client.renderer.block.model.ItemTransforms.TransformType transformType,
                                 com.mojang.blaze3d.vertex.PoseStack poseStack,
                                 net.minecraft.client.renderer.MultiBufferSource buffer,
                                 int packedLight,
                                 int packedOverlay) {
            // Create (or reuse) a bed BE whose blockstate is our custom bed block.
            // This ensures CustomBedRenderer can select the correct custom bed material.
            Block block = ((net.minecraft.world.item.BlockItem) stack.getItem()).getBlock();
            if (cachedBedEntity == null || cachedBlock != block) {
                cachedBlock = block;
                BlockState state = block.defaultBlockState()
                        .setValue(BedBlock.FACING, Direction.SOUTH)
                        .setValue(BedBlock.PART, BedPart.HEAD);
                cachedBedEntity = new BedBlockEntity(BlockPos.ZERO, state);
            }

            // Use the block entity render dispatcher; it will route BED entities to our CustomBedRenderer
            // Our custom-bed item path renders via a block entity. In GUI/ground contexts it can appear slightly
            // "floating" compared to vanilla bed items, so nudge it down a couple pixels.
            poseStack.pushPose();
            if (transformType == net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GUI
                    || transformType == net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND
                    || transformType == net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.FIXED) {
                poseStack.translate(0.0D, 0.125D, -1.0D);
            }
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(cachedBedEntity, poseStack, buffer, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }
}

