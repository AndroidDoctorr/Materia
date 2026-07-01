package com.torr.materia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class LotusBlock extends WaterlilyBlock {
    public LotusBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        if (!level.getFluidState(pos).is(Fluids.WATER) && !(state.getBlock() instanceof IceBlock)) {
            return false;
        }
        if (!level.getFluidState(pos.above()).isEmpty()) {
            return false;
        }
        return isShallowWater(level, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos support = pos.below();
        return mayPlaceOn(level.getBlockState(support), level, support);
    }

    private static boolean isShallowWater(BlockGetter level, BlockPos surfaceWaterPos) {
        int depth = 0;
        BlockPos check = surfaceWaterPos;
        while (depth < 16 && level.getFluidState(check).is(Fluids.WATER)) {
            depth++;
            check = check.below();
        }
        return depth >= 1 && depth <= 2;
    }
}
