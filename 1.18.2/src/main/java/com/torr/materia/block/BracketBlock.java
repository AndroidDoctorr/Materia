package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Wall-mounted bracket. {@link #FACING} matches the clicked horizontal face (outward from the wall).
 * The model back (z=0) sits against the opposite face of the support block.
 */
public class BracketBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape NORTH = Block.box(5, 0, 0, 11, 16, 8);
    private static final VoxelShape SOUTH = Block.box(5, 0, 8, 11, 16, 16);
    private static final VoxelShape WEST = Block.box(0, 0, 5, 8, 16, 11);
    private static final VoxelShape EAST = Block.box(8, 0, 5, 16, 16, 11);

    public BracketBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        if (!clicked.getAxis().isHorizontal()) {
            return null;
        }
        BlockPos pos = WallAttachment.placementPos(context);
        BlockState state = defaultBlockState().setValue(FACING, clicked);
        return state.canSurvive(context.getLevel(), pos) ? state : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeOnWallSide(state.getValue(FACING).getOpposite());
    }

    /** Voxel volume on the wall-adjacent half of the block, matching the rendered model. */
    static VoxelShape shapeOnWallSide(Direction wallSide) {
        return switch (wallSide) {
            case SOUTH -> SOUTH;
            case NORTH -> NORTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> NORTH;
        };
    }
}
