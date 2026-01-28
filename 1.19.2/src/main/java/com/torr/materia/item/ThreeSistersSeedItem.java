package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.ThreeSistersBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

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
        
        if (!level.isClientSide) {
            // Check if clicking on a Three Sisters block
            if (clickedState.getBlock() instanceof ThreeSistersBlock) {
                // Let the Three Sisters block handle the interaction
                return clickedState.getBlock().use(clickedState, level, pos, context.getPlayer(), context.getHand(), null);
            }
            
            // Check if clicking on farmland
            if (clickedState.getBlock() instanceof FarmBlock) {
                BlockPos abovePos = pos.above();
                BlockState aboveState = level.getBlockState(abovePos);
                
                // If there's air above farmland, create a Three Sisters block
                if (aboveState.isAir()) {
                    BlockState threeSistersState = ModBlocks.THREE_SISTERS_CROP.get().defaultBlockState();
                    level.setBlock(abovePos, threeSistersState, 3);
                    
                    // Then let the Three Sisters block handle planting this specific seed
                    return ModBlocks.THREE_SISTERS_CROP.get().use(threeSistersState, level, abovePos, context.getPlayer(), context.getHand(), null);
                }
                
                // If there's already a crop above farmland, try to add to it
                if (aboveState.getBlock() instanceof ThreeSistersBlock) {
                    return aboveState.getBlock().use(aboveState, level, abovePos, context.getPlayer(), context.getHand(), null);
                }
            }
        }
        
        // Fallback to normal behavior (place individual crop)
        return super.useOn(context);
    }
}
