package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ThreeSistersBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ThreeSistersSeedItem extends ItemNameBlockItem {
    private final Block originalCropBlock;

    public ThreeSistersSeedItem(Block originalCropBlock, Item.Properties properties) {
        super(originalCropBlock, properties);
        this.originalCropBlock = originalCropBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(pos);
        ItemStack held = context.getItemInHand();

        // Determine which crop this seed represents (based on held item id)
        IntegerProperty ageProp = null;
        if (held.getItem() == ModItems.CORN.get()) {
            ageProp = ThreeSistersBlock.CORN_AGE;
        } else if (held.getItem() == ModItems.BEANS.get()) {
            ageProp = ThreeSistersBlock.BEANS_AGE;
        } else if (held.getItem() == ModItems.SQUASH_SEEDS.get()) {
            ageProp = ThreeSistersBlock.SQUASH_AGE;
        }

        if (ageProp != null) {
            // Clicking directly on an existing Three Sisters block: plant into it.
            if (clickedState.getBlock() instanceof ThreeSistersBlock) {
                if (level.isClientSide) return InteractionResult.SUCCESS;
                if (clickedState.getValue(ageProp) > 0) return InteractionResult.FAIL;

                level.setBlock(pos, clickedState.setValue(ageProp, 1), 3);
                if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                    held.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // Clicking on farmland: create/extend a Three Sisters block above it.
            if (clickedState.getBlock() instanceof FarmBlock) {
                BlockPos abovePos = pos.above();
                BlockState aboveState = level.getBlockState(abovePos);

                if (aboveState.isAir()) {
                    if (level.isClientSide) return InteractionResult.SUCCESS;
                    BlockState threeSistersState = ModBlocks.THREE_SISTERS_CROP.get().defaultBlockState().setValue(ageProp, 1);
                    level.setBlock(abovePos, threeSistersState, 3);
                    if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                        held.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }

                if (aboveState.getBlock() instanceof ThreeSistersBlock) {
                    if (level.isClientSide) return InteractionResult.SUCCESS;
                    if (aboveState.getValue(ageProp) > 0) return InteractionResult.FAIL;
                    level.setBlock(abovePos, aboveState.setValue(ageProp, 1), 3);
                    if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                        held.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        
        // Fallback to normal behavior (place individual crop)
        return super.useOn(context);
    }
}
