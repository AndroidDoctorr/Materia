package com.torr.materia.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class LotusBlockItem extends BlockItem {
    public LotusBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos[] candidates = {
                context.getClickedPos().relative(context.getClickedFace()),
                context.getClickedPos().above(),
                context.getClickedPos()
        };

        Level level = context.getLevel();
        BlockState lotusState = getBlock().defaultBlockState();

        for (BlockPos candidate : candidates) {
            if (lotusState.canSurvive(level, candidate)) {
                return BlockPlaceContext.at(context, candidate, Direction.DOWN);
            }
        }

        if (level.getFluidState(context.getClickedPos()).is(Fluids.WATER)) {
            BlockPos aboveWater = context.getClickedPos().above();
            if (lotusState.canSurvive(level, aboveWater)) {
                return BlockPlaceContext.at(context, aboveWater, Direction.DOWN);
            }
        }

        return null;
    }
}
