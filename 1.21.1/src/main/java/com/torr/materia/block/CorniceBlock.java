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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Wall-mounted cornice trim. {@link #FACING} matches the clicked horizontal face (outward from the wall).
 * The model back (z=0) sits against the opposite face of the support block.
 */
public class CorniceBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    private static final VoxelShape STRAIGHT_NORTH = Block.box(0, 0, 0, 16, 16, 8);
    private static final VoxelShape STRAIGHT_SOUTH = Block.box(0, 0, 8, 16, 16, 16);
    private static final VoxelShape STRAIGHT_WEST = Block.box(0, 0, 0, 8, 16, 16);
    private static final VoxelShape STRAIGHT_EAST = Block.box(8, 0, 0, 16, 16, 16);

    public CorniceBlock(Properties properties) {
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
        Direction clicked = context.getClickedFace();
        if (!clicked.getAxis().isHorizontal()) {
            return null;
        }
        BlockPos pos = WallAttachment.placementPos(context);
        BlockState state = defaultBlockState().setValue(FACING, clicked);
        return state.setValue(SHAPE, computeShape(state, context.getLevel(), pos));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        StairsShape shape = computeShape(state, level, pos);
        return state.getValue(SHAPE) == shape ? state : state.setValue(SHAPE, shape);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            refreshShapeNeighbors(level, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            refreshShapeNeighbors(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction outward = state.getValue(FACING);
        Direction wallSide = outward.getOpposite();
        StairsShape shape = state.getValue(SHAPE);
        VoxelShape base = straightShape(wallSide);
        if (shape == StairsShape.STRAIGHT) {
            return base;
        }

        Direction crossWallSide = switch (shape) {
            case INNER_LEFT -> outward.getCounterClockWise();
            case INNER_RIGHT -> outward.getClockWise();
            case OUTER_LEFT -> wallSide.getCounterClockWise();
            case OUTER_RIGHT -> wallSide.getClockWise();
            default -> null;
        };
        if (crossWallSide == null) {
            return base;
        }
        VoxelShape crossShape = straightShape(crossWallSide);
        return switch (shape) {
            case INNER_LEFT, INNER_RIGHT -> Shapes.or(base, crossShape);
            case OUTER_LEFT, OUTER_RIGHT -> Shapes.join(base, crossShape, BooleanOp.AND);
            default -> base;
        };
    }

    private static VoxelShape straightShape(Direction wallSide) {
        return switch (wallSide) {
            case NORTH -> STRAIGHT_NORTH;
            case SOUTH -> STRAIGHT_SOUTH;
            case EAST -> STRAIGHT_EAST;
            case WEST -> STRAIGHT_WEST;
            default -> STRAIGHT_NORTH;
        };
    }

    private static StairsShape computeShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction outward = state.getValue(FACING);
        Direction towardWall = outward.getOpposite();

        BlockState front = level.getBlockState(pos.relative(outward));
        if (isCompatibleCornice(state, front)) {
            StairsShape shape = shapeForNeighbor(outward, outward, front.getValue(FACING));
            if (shape != StairsShape.STRAIGHT) {
                return shape;
            }
        }

        BlockState back = level.getBlockState(pos.relative(towardWall));
        if (isCompatibleCornice(state, back)) {
            StairsShape shape = shapeForNeighbor(outward, towardWall, back.getValue(FACING));
            if (shape != StairsShape.STRAIGHT) {
                return shape;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static StairsShape shapeForNeighbor(Direction outward, Direction side, Direction neighborOutward) {
        if (neighborOutward.getAxis() == outward.getAxis()) {
            return StairsShape.STRAIGHT;
        }

        if (side == outward) {
            return neighborOutward == outward.getCounterClockWise()
                    ? StairsShape.INNER_RIGHT
                    : StairsShape.INNER_LEFT;
        }

        return neighborOutward == outward.getCounterClockWise()
                ? StairsShape.OUTER_LEFT
                : StairsShape.OUTER_RIGHT;
    }

    private static boolean isCompatibleCornice(BlockState state, BlockState other) {
        return other.getBlock() instanceof CorniceBlock && other.getBlock() == state.getBlock();
    }

    private static void refreshShapeNeighbors(Level level, BlockPos pos) {
        refreshShapeAt(level, pos);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockState(neighborPos).getBlock() instanceof CorniceBlock) {
                refreshShapeAt(level, neighborPos);
            }
        }
    }

    private static void refreshShapeAt(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof CorniceBlock)) {
            return;
        }
        StairsShape shape = computeShape(state, level, pos);
        if (state.getValue(SHAPE) != shape) {
            level.setBlock(pos, state.setValue(SHAPE, shape), 3);
        }
    }
}
