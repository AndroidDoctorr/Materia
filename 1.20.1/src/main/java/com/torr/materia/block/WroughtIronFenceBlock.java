package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
 * Thin wrought iron fence panels. Each segment keeps a player-facing orientation when isolated;
 * adjacent wrought iron fences update N/S/E/W connection flags for multipart arms.
 */
public class WroughtIronFenceBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final VoxelShape PANEL_NORTH_SOUTH = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape PANEL_EAST_WEST = Block.box(7, 0, 0, 9, 16, 16);
    private static final VoxelShape POST = Block.box(7, 0, 7, 9, 16, 9);
    private static final VoxelShape ARM_NORTH = Block.box(7, 0, 0, 9, 16, 8);
    private static final VoxelShape ARM_SOUTH = Block.box(7, 0, 8, 9, 16, 16);
    private static final VoxelShape ARM_WEST = Block.box(0, 0, 7, 8, 16, 9);
    private static final VoxelShape ARM_EAST = Block.box(8, 0, 7, 16, 16, 9);

    public WroughtIronFenceBlock(Properties properties) {
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

    private static BlockState updateConnections(BlockState state, BlockGetter level, BlockPos pos) {
        return state
                .setValue(NORTH, connectsTo(level, pos, Direction.NORTH))
                .setValue(SOUTH, connectsTo(level, pos, Direction.SOUTH))
                .setValue(EAST, connectsTo(level, pos, Direction.EAST))
                .setValue(WEST, connectsTo(level, pos, Direction.WEST));
    }

    private static boolean connectsTo(BlockGetter level, BlockPos pos, Direction direction) {
        return level.getBlockState(pos.relative(direction)).getBlock() instanceof WroughtIronFenceBlock;
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
            return facing.getAxis() == Direction.Axis.X ? PANEL_EAST_WEST : PANEL_NORTH_SOUTH;
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
