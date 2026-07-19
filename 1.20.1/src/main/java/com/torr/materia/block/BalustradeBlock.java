package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Stone balustrade segments that connect on all four horizontal sides to other balustrades.
 */
public class BalustradeBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final VoxelShape POST = Block.box(4, 0, 4, 12, 12, 12);
    private static final VoxelShape ARM_NORTH = Block.box(4, 0, 0, 12, 12, 12);
    private static final VoxelShape ARM_SOUTH = Block.box(4, 0, 4, 12, 12, 16);
    private static final VoxelShape ARM_WEST = Block.box(0, 0, 4, 12, 12, 12);
    private static final VoxelShape ARM_EAST = Block.box(4, 0, 4, 16, 12, 12);

    private static final VoxelShape STRAIGHT_EW = Shapes.or(
            Block.box(3, 0, 7, 5, 9, 9),
            Block.box(11, 0, 7, 13, 9, 9),
            Block.box(0, 9, 5, 16, 11, 11));
    private static final VoxelShape STRAIGHT_NS = Shapes.or(
            Block.box(7, 0, 3, 9, 9, 5),
            Block.box(7, 0, 11, 9, 9, 13),
            Block.box(5, 9, 0, 11, 11, 16));

    public BalustradeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
        return updateConnections(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return updateConnections(state, level, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            refreshNeighbors(level, pos);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            refreshNeighbors(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private static BlockState updateConnections(BlockState state, BlockGetter level, BlockPos pos) {
        Block self = state.getBlock();
        return state
                .setValue(NORTH, connectsTo(level, pos, Direction.NORTH, self))
                .setValue(SOUTH, connectsTo(level, pos, Direction.SOUTH, self))
                .setValue(EAST, connectsTo(level, pos, Direction.EAST, self))
                .setValue(WEST, connectsTo(level, pos, Direction.WEST, self));
    }

    private static boolean connectsTo(BlockGetter level, BlockPos pos, Direction direction, Block self) {
        return level.getBlockState(pos.relative(direction)).is(self);
    }

    private static void refreshNeighbors(Level level, BlockPos pos) {
        refreshAt(level, pos);
        Block self = level.getBlockState(pos).getBlock();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockState(neighborPos).is(self)) {
                refreshAt(level, neighborPos);
            }
        }
    }

    private static void refreshAt(Level level, BlockPos pos) {
        BlockState current = level.getBlockState(pos);
        if (!(current.getBlock() instanceof BalustradeBlock)) {
            return;
        }
        BlockState updated = updateConnections(current, level, pos);
        if (updated != current) {
            level.setBlock(pos, updated, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!state.getValue(NORTH) && !state.getValue(SOUTH) && !state.getValue(EAST) && !state.getValue(WEST)) {
            Direction facing = state.getValue(FACING);
            return facing.getAxis() == Direction.Axis.X ? STRAIGHT_NS : STRAIGHT_EW;
        }

        VoxelShape shape = POST;
        if (state.getValue(NORTH)) {
            shape = Shapes.or(shape, ARM_NORTH);
        }
        if (state.getValue(SOUTH)) {
            shape = Shapes.or(shape, ARM_SOUTH);
        }
        if (state.getValue(WEST)) {
            shape = Shapes.or(shape, ARM_WEST);
        }
        if (state.getValue(EAST)) {
            shape = Shapes.or(shape, ARM_EAST);
        }
        return shape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }
}
