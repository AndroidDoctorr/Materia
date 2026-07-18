package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AwningBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    private static VoxelShape buildRidgeWedge(Direction ridge) {
        VoxelShape shape = Shapes.empty();
        for (int i = 0; i < 16; i++) {
            int height = i + 1;
            switch (ridge) {
                case SOUTH -> shape = Shapes.or(shape, Block.box(0, 0, i, 16, height, i + 1));
                case NORTH -> shape = Shapes.or(shape, Block.box(0, 0, 15 - i, 16, height, 15 - i + 1));
                case EAST -> shape = Shapes.or(shape, Block.box(i, 0, 0, i + 1, height, 16));
                case WEST -> shape = Shapes.or(shape, Block.box(15 - i, 0, 0, 16 - i, height, 16));
                default -> {
                }
            }
        }
        return shape;
    }

    private static VoxelShape shapeForState(BlockState state) {
        Direction ridge = state.getValue(FACING).getOpposite();
        StairsShape shape = state.getValue(SHAPE);
        VoxelShape result = buildRidgeWedge(ridge);
        if (shape == StairsShape.STRAIGHT) {
            return result;
        }

        Direction crossRidge = switch (shape) {
            case INNER_LEFT, OUTER_LEFT -> ridge.getClockWise();
            case INNER_RIGHT, OUTER_RIGHT -> ridge.getCounterClockWise();
            default -> null;
        };
        if (crossRidge == null) {
            return result;
        }
        VoxelShape cross = buildRidgeWedge(crossRidge);
        return switch (shape) {
            case INNER_LEFT, INNER_RIGHT -> Shapes.or(result, cross);
            case OUTER_LEFT, OUTER_RIGHT -> Shapes.join(result, cross, BooleanOp.AND);
            default -> result;
        };
    }

    public AwningBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SHAPE, StairsShape.STRAIGHT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        BlockState state = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(SHAPE, StairsShape.STRAIGHT);
        return orientFromNeighbors(state, context.getLevel(), pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(SHAPE, computeShape(state, level, pos));
        }
        return state;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            refreshShapeNeighbors(level, pos);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeForState(state);
    }

    private static StairsShape computeShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction ridge = state.getValue(FACING);
        Direction descent = ridge.getOpposite();

        BlockState ridgeNeighbor = level.getBlockState(pos.relative(ridge));
        if (isCompatibleAwning(ridgeNeighbor)) {
            StairsShape shape = shapeForNeighbor(ridge, ridge, ridgeNeighbor.getValue(FACING));
            if (shape != StairsShape.STRAIGHT) {
                return shape;
            }
        }

        BlockState descentNeighbor = level.getBlockState(pos.relative(descent));
        if (isCompatibleAwning(descentNeighbor)) {
            StairsShape shape = shapeForNeighbor(ridge, descent, descentNeighbor.getValue(FACING));
            if (shape != StairsShape.STRAIGHT) {
                return shape;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static StairsShape shapeForNeighbor(Direction ridge, Direction side, Direction neighborRidge) {
        if (neighborRidge.getAxis() == ridge.getAxis()) {
            return StairsShape.STRAIGHT;
        }

        if (side == ridge) {
            return neighborRidge == ridge.getCounterClockWise()
                    ? StairsShape.INNER_RIGHT
                    : StairsShape.INNER_LEFT;
        }

        if (side == ridge.getOpposite()) {
            return neighborRidge == ridge.getCounterClockWise()
                    ? StairsShape.OUTER_RIGHT
                    : StairsShape.OUTER_LEFT;
        }

        return StairsShape.STRAIGHT;
    }

    private static BlockState orientFromNeighbors(BlockState state, BlockGetter level, BlockPos pos) {
        return state.setValue(SHAPE, computeShape(state, level, pos));
    }

    private static boolean isCompatibleAwning(BlockState other) {
        return other.getBlock() instanceof AwningBlock;
    }

    private static void refreshShapeNeighbors(Level level, BlockPos pos) {
        refreshShapeAt(level, pos);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockState(neighborPos).getBlock() instanceof AwningBlock) {
                refreshShapeAt(level, neighborPos);
            }
        }
    }

    private static void refreshShapeAt(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof AwningBlock)) {
            return;
        }
        StairsShape shape = computeShape(state, level, pos);
        if (state.getValue(SHAPE) != shape) {
            level.setBlock(pos, state.setValue(SHAPE, shape), 3);
        }
    }
}
