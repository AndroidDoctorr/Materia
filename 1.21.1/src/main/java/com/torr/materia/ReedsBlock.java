package com.torr.materia;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ReedsBlock extends BushBlock {
    public static final MapCodec<ReedsBlock> CODEC = simpleCodec(ReedsBlock::new);

    public ReedsBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.SAND) || state.is(Blocks.RED_SAND) || state.is(Blocks.PODZOL);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (!mayPlaceOn(below, level, pos.below())) {
            return false;
        }
        return hasWaterAdjacent(level, pos);
    }

    private static boolean hasWaterAdjacent(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = level.getBlockState(pos.relative(direction));
            if (neighbor.getFluidState().is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }
}
