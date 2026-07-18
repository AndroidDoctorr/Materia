package com.torr.materia.item;

import com.torr.materia.block.RugBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RugItem extends BlockItem {
    public RugItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        BlockPos headPos = context.getClickedPos().relative(state.getValue(RugBlock.FACING));
        Level level = context.getLevel();
        if (!level.getBlockState(headPos).canBeReplaced(context) || !level.getWorldBorder().isWithinBounds(headPos)) {
            return false;
        }
        if (!level.setBlock(context.getClickedPos(), state, Block.UPDATE_ALL)) {
            return false;
        }
        return level.setBlock(headPos, state.setValue(RugBlock.PART, RugBlock.RugPart.HEAD), Block.UPDATE_ALL);
    }

    @Override
    public boolean canPlace(BlockPlaceContext context, BlockState state) {
        BlockPos headPos = context.getClickedPos().relative(state.getValue(RugBlock.FACING));
        Level level = context.getLevel();
        return super.canPlace(context, state)
                && level.getBlockState(headPos).canBeReplaced(context)
                && level.getWorldBorder().isWithinBounds(headPos);
    }
}
