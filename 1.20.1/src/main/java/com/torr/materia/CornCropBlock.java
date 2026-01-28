package com.torr.materia;

import com.torr.materia.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.util.RandomSource;

public class CornCropBlock extends CropBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final int TALL_AGE = 4;

    public CornCropBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(AGE, 0)
            .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(AGE, HALF);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (half == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());
            return below.getBlock() == this
                && below.getValue(HALF) == DoubleBlockHalf.LOWER
                && below.getValue(AGE) >= TALL_AGE;
        } else {
            return super.canSurvive(state, level, pos); // farmland check
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            int old = state.getValue(AGE);
            super.randomTick(state, level, pos, rand); // may advance age
            int now = level.getBlockState(pos).getValue(AGE);
            syncTopAfterGrowth(level, pos, now);
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            int newAge = Math.min(getMaxAge(), state.getValue(AGE) + getBonemealAgeIncrease(level));
            level.setBlock(pos, state.setValue(AGE, newAge), 2);
            syncTopAfterGrowth(level, pos, newAge);
        }
    }

    private void syncTopAfterGrowth(Level level, BlockPos lowerPos, int ageNow) {
        BlockPos up = lowerPos.above();
        BlockState upState = level.getBlockState(up);
        boolean shouldBeTall = ageNow >= TALL_AGE;
        if (shouldBeTall) {
            if (upState.isAir()) {
                level.setBlock(up, this.defaultBlockState()
                    .setValue(AGE, ageNow)
                    .setValue(HALF, DoubleBlockHalf.UPPER), 3);
            } else if (upState.getBlock() == this && upState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                if (upState.getValue(AGE) != ageNow) {
                    level.setBlock(up, upState.setValue(AGE, ageNow), 2);
                }
            }
        } else {
            if (upState.getBlock() == this && upState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                level.removeBlock(up, false);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (half == DoubleBlockHalf.UPPER && dir == Direction.DOWN) {
            if (neighbor.getBlock() != this || neighbor.getValue(HALF) != DoubleBlockHalf.LOWER) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        if (half == DoubleBlockHalf.LOWER && dir == Direction.UP) {
            // If tall and missing top, try to recreate it (if air)
            if (state.getValue(AGE) >= TALL_AGE) {
                BlockPos up = pos.above();
                if (level.getBlockState(up).isAir()) {
                    level.setBlock(up, this.defaultBlockState()
                        .setValue(AGE, state.getValue(AGE))
                        .setValue(HALF, DoubleBlockHalf.UPPER), 3);
                }
            }
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockPos up = pos.above();
                BlockState upState = level.getBlockState(up);
                if (upState.getBlock() == this && upState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    level.destroyBlock(up, false, player);
                }
            } else { // UPPER
                BlockPos down = pos.below();
                BlockState low = level.getBlockState(down);
                if (low.getBlock() == this && low.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    // Donâ€™t drop from the upper; let the lower handle loot.
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CORN.get();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        // Only lower half randomly ticks; avoids double growth calls
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state);
    }
}
