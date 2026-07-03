package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.block.RoofTilesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RoofFrameItem extends Item {
    public RoofFrameItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        BlockState existing = level.getBlockState(pos);
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        if (!existing.canBeReplaced(placeContext)) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide) {
            BlockState placed = ModBlocks.ROOF_TILES.get().defaultBlockState()
                    .setValue(RoofTilesBlock.FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(RoofTilesBlock.STAGE, 0);
            level.setBlock(pos, placed, 3);
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
