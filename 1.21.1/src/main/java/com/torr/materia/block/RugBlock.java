package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Two-block floor rug (1 wide x 2 long). Only the foot half drops the rug item.
 */
public class RugBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<RugPart> PART = EnumProperty.create("part", RugPart.class);

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public enum RugPart implements StringRepresentable {
        FOOT("foot"),
        HEAD("head");

        private final String name;

        RugPart(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public RugBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, RugPart.FOOT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos headPos = context.getClickedPos().relative(facing);
        Level level = context.getLevel();
        if (!level.getBlockState(headPos).canBeReplaced(context) || !level.getWorldBorder().isWithinBounds(headPos)) {
            return null;
        }
        return defaultBlockState().setValue(FACING, facing).setValue(PART, RugPart.FOOT);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            BlockPos headPos = pos.relative(state.getValue(FACING));
            level.setBlock(headPos, state.setValue(PART, RugPart.HEAD), Block.UPDATE_ALL);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        if (state.getValue(PART) == RugPart.FOOT) {
            if (direction != facing) {
                return state;
            }
            return neighborState.is(this)
                    && neighborState.getValue(PART) == RugPart.HEAD
                    && neighborState.getValue(FACING) == facing
                    ? state
                    : Blocks.AIR.defaultBlockState();
        }
        if (direction != facing.getOpposite()) {
            return state;
        }
        return neighborState.is(this)
                && neighborState.getValue(PART) == RugPart.FOOT
                && neighborState.getValue(FACING) == facing
                ? state
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (state.getValue(PART) == RugPart.HEAD) {
                BlockPos footPos = pos.relative(state.getValue(FACING).getOpposite());
                BlockState footState = level.getBlockState(footPos);
                if (footState.is(this) && footState.getValue(PART) == RugPart.FOOT) {
                    Block.dropResources(footState, level, footPos, null, player, player.getMainHandItem());
                    level.setBlock(footPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                }
            } else if (player.isCreative()) {
                BlockPos headPos = pos.relative(state.getValue(FACING));
                BlockState headState = level.getBlockState(headPos);
                if (headState.is(this)) {
                    level.setBlock(headPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock() && !level.isClientSide && state.getValue(PART) == RugPart.HEAD) {
            BlockPos footPos = pos.relative(state.getValue(FACING).getOpposite());
            BlockState footState = level.getBlockState(footPos);
            if (footState.is(this) && footState.getValue(PART) == RugPart.FOOT) {
                level.setBlock(footPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (state.getValue(PART) == RugPart.HEAD) {
            return Collections.emptyList();
        }
        return super.getDrops(state, builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
