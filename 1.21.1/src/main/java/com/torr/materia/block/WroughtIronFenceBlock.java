package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Thin wrought iron fence panels. Isolated segments face the player; straight runs use a single
 * panel along the connected axis; corners and T-junctions use a post with side arms.
 */
public class WroughtIronFenceBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

    private static final VoxelShape VISUAL_PANEL_EAST_WEST = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape VISUAL_PANEL_NORTH_SOUTH = Block.box(7, 0, 0, 9, 16, 16);
    private static final VoxelShape VISUAL_POST = Block.box(7, 0, 7, 9, 16, 9);
    private static final VoxelShape VISUAL_ARM_NORTH = Block.box(7, 0, 0, 9, 16, 8);
    private static final VoxelShape VISUAL_ARM_SOUTH = Block.box(7, 0, 8, 9, 16, 16);
    private static final VoxelShape VISUAL_ARM_WEST = Block.box(0, 0, 7, 8, 16, 9);
    private static final VoxelShape VISUAL_ARM_EAST = Block.box(8, 0, 7, 16, 16, 9);

    private static final VoxelShape COLLISION_PANEL_EAST_WEST = Block.box(0, 0, 7, 16, 24, 9);
    private static final VoxelShape COLLISION_PANEL_NORTH_SOUTH = Block.box(7, 0, 0, 9, 24, 16);
    private static final VoxelShape COLLISION_POST = Block.box(7, 0, 7, 9, 24, 9);
    private static final VoxelShape COLLISION_ARM_NORTH = Block.box(7, 0, 0, 9, 24, 8);
    private static final VoxelShape COLLISION_ARM_SOUTH = Block.box(7, 0, 8, 9, 24, 16);
    private static final VoxelShape COLLISION_ARM_WEST = Block.box(0, 0, 7, 8, 24, 9);
    private static final VoxelShape COLLISION_ARM_EAST = Block.box(8, 0, 7, 16, 24, 9);

    public WroughtIronFenceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(SHAPE, Shape.PANEL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, NORTH, SOUTH, EAST, WEST, SHAPE);
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

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    private static BlockState updateConnections(BlockState state, BlockGetter level, BlockPos pos) {
        boolean north = connectsTo(level, pos, Direction.NORTH);
        boolean south = connectsTo(level, pos, Direction.SOUTH);
        boolean east = connectsTo(level, pos, Direction.EAST);
        boolean west = connectsTo(level, pos, Direction.WEST);
        boolean nsAxis = north || south;
        boolean ewAxis = east || west;

        Shape shape;
        if (!nsAxis && !ewAxis) {
            shape = Shape.PANEL;
        } else if (nsAxis && !ewAxis) {
            shape = Shape.STRAIGHT_NS;
        } else if (ewAxis && !nsAxis) {
            shape = Shape.STRAIGHT_EW;
        } else {
            shape = Shape.JUNCTION;
        }

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(EAST, east)
                .setValue(WEST, west)
                .setValue(SHAPE, shape);
    }

    private static boolean connectsTo(BlockGetter level, BlockPos pos, Direction direction) {
        Block block = level.getBlockState(pos.relative(direction)).getBlock();
        return block instanceof WroughtIronFenceBlock || block instanceof WroughtIronFenceGateBlock;
    }

    private static void refreshNeighbors(Level level, BlockPos pos) {
        refreshAt(level, pos);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            Block neighbor = level.getBlockState(neighborPos).getBlock();
            if (neighbor instanceof WroughtIronFenceBlock || neighbor instanceof WroughtIronFenceGateBlock) {
                refreshAt(level, neighborPos);
            }
        }
    }

    private static void refreshAt(Level level, BlockPos pos) {
        BlockState current = level.getBlockState(pos);
        if (!(current.getBlock() instanceof WroughtIronFenceBlock)) {
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
        BlockState belowState = level.getBlockState(below);
        return belowState.isFaceSturdy(level, below, Direction.UP)
                || belowState.getBlock() instanceof WroughtIronFenceBlock
                || belowState.getBlock() instanceof WroughtIronFenceGateBlock;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeFor(state, false);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeFor(state, true);
    }

    private static VoxelShape shapeFor(BlockState state, boolean collision) {
        return switch (state.getValue(SHAPE)) {
            case PANEL -> panelShape(state, collision);
            case STRAIGHT_NS -> collision ? COLLISION_PANEL_NORTH_SOUTH : VISUAL_PANEL_NORTH_SOUTH;
            case STRAIGHT_EW -> collision ? COLLISION_PANEL_EAST_WEST : VISUAL_PANEL_EAST_WEST;
            case JUNCTION -> junctionShape(state, collision);
        };
    }

    private static VoxelShape panelShape(BlockState state, boolean collision) {
        boolean northSouth = state.getValue(FACING).getAxis() == Direction.Axis.X;
        if (collision) {
            return northSouth ? COLLISION_PANEL_NORTH_SOUTH : COLLISION_PANEL_EAST_WEST;
        }
        return northSouth ? VISUAL_PANEL_NORTH_SOUTH : VISUAL_PANEL_EAST_WEST;
    }

    private static VoxelShape junctionShape(BlockState state, boolean collision) {
        VoxelShape shape = collision ? COLLISION_POST : VISUAL_POST;
        if (state.getValue(NORTH)) {
            shape = Shapes.or(shape, collision ? COLLISION_ARM_NORTH : VISUAL_ARM_NORTH);
        }
        if (state.getValue(SOUTH)) {
            shape = Shapes.or(shape, collision ? COLLISION_ARM_SOUTH : VISUAL_ARM_SOUTH);
        }
        if (state.getValue(WEST)) {
            shape = Shapes.or(shape, collision ? COLLISION_ARM_WEST : VISUAL_ARM_WEST);
        }
        if (state.getValue(EAST)) {
            shape = Shapes.or(shape, collision ? COLLISION_ARM_EAST : VISUAL_ARM_EAST);
        }
        return shape;
    }

    public enum Shape implements StringRepresentable {
        PANEL("panel"),
        STRAIGHT_NS("straight_ns"),
        STRAIGHT_EW("straight_ew"),
        JUNCTION("junction");

        private final String name;

        Shape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
