package com.torr.materia.block;

import com.torr.materia.blockentity.FishTrapBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class FishTrapBlock extends Block implements EntityBlock {

    public FishTrapBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.core.Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.core.Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.core.Direction direction) {
        return 5;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FishTrapBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof FishTrapBlockEntity trap) {
                FishTrapBlockEntity.tick(lvl, pos, st, trap);
            }
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return isValidPlacement(level, pos);
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
        return isValidPlacement(context.getLevel(), context.getClickedPos())
                ? super.getStateForPlacement(context)
                : null;
    }

    private static boolean isValidPlacement(LevelReader level, BlockPos pos) {
        int sideWaterCount = 0;
        boolean nsOpen = false;
        boolean ewOpen = false;

        boolean northWater = isWater(level.getFluidState(pos.relative(Direction.NORTH)));
        boolean southWater = isWater(level.getFluidState(pos.relative(Direction.SOUTH)));
        boolean westWater = isWater(level.getFluidState(pos.relative(Direction.WEST)));
        boolean eastWater = isWater(level.getFluidState(pos.relative(Direction.EAST)));

        if (northWater) sideWaterCount++;
        if (southWater) sideWaterCount++;
        if (westWater) sideWaterCount++;
        if (eastWater) sideWaterCount++;

        nsOpen = northWater && southWater;
        ewOpen = westWater && eastWater;

        if (sideWaterCount < 3) return false;
        if (!(nsOpen || ewOpen)) return false; // require at least one opposite pair open

        // Shallow water: determine depth at any adjacent water column (min depth among water sides)
        int minDepth = Integer.MAX_VALUE;
        if (northWater) minDepth = Math.min(minDepth, measureWaterColumnDepth(level, pos.relative(Direction.NORTH)));
        if (southWater) minDepth = Math.min(minDepth, measureWaterColumnDepth(level, pos.relative(Direction.SOUTH)));
        if (westWater)  minDepth = Math.min(minDepth, measureWaterColumnDepth(level, pos.relative(Direction.WEST)));
        if (eastWater)  minDepth = Math.min(minDepth, measureWaterColumnDepth(level, pos.relative(Direction.EAST)));

        if (minDepth == Integer.MAX_VALUE) return false; // no water adjacent somehow
        return minDepth <= 3; // 0-3 blocks deep allowed
    }

    private static boolean isWater(FluidState fluid) {
        return fluid.getType() == Fluids.WATER || fluid.getType() == Fluids.FLOWING_WATER;
    }

    // Measures the full continuous water column height that includes the given position's column
    private static int measureWaterColumnDepth(LevelReader level, BlockPos columnPos) {
        // Find the top of the water column
        BlockPos cursor = columnPos;
        int guard = 0;
        while (guard++ < 16 && isWater(level.getFluidState(cursor))) {
            BlockPos above = cursor.above();
            if (!isWater(level.getFluidState(above))) break;
            cursor = above;
        }
        // Now walk downward counting consecutive water blocks
        int depth = 0;
        BlockPos down = cursor;
        guard = 0;
        while (guard++ < 16 && isWater(level.getFluidState(down))) {
            depth++;
            BlockPos next = down.below();
            if (!isWater(level.getFluidState(next))) break;
            down = next;
        }
        return depth;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FishTrapBlockEntity trap) {
            int count = trap.extractAll();
            if (count > 0) {
                player.getInventory().placeItemBackInInventory(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.COD, count));
                level.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.7F, 1.0F);
            } else {
                level.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 0.4F, 0.9F);
            }
        }
        return InteractionResult.CONSUME;
    }
} 