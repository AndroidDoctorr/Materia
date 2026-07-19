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
 * Vertical wrought iron panel. {@link #ATTACH} is the clicked face of the support block (grate sits
 * in the adjacent cell on that side). {@link #FACING} is the placer's horizontal look direction and
 * chooses a north/south (thin on Z) or east/west (thin on X) panel.
 */
public class WroughtIronGrateBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final DirectionProperty ATTACH = DirectionProperty.create("attach", Direction.values());

    private static final VoxelShape VISUAL_NORTH_SOUTH = Block.box(0, 0, 8, 16, 16, 9);
    private static final VoxelShape VISUAL_EAST_WEST = Block.box(8, 0, 0, 9, 16, 16);
    private static final VoxelShape COLLISION_NORTH_SOUTH = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape COLLISION_EAST_WEST = Block.box(7, 0, 0, 9, 16, 16);

    public WroughtIronGrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ATTACH, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        BlockPos pos = context.getClickedPos();
        if (!context.getLevel().getBlockState(pos).canBeReplaced(context)) {
            pos = pos.relative(clicked);
        }

        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(ATTACH, clicked);
        return state.canSurvive(context.getLevel(), pos) ? state : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction attach = state.getValue(ATTACH);
        BlockPos supportPos = pos.relative(attach.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, attach);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return visualShapeFor(state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return collisionShapeFor(state.getValue(FACING));
    }

    private static VoxelShape visualShapeFor(Direction facing) {
        return facing.getAxis() == Direction.Axis.X ? VISUAL_EAST_WEST : VISUAL_NORTH_SOUTH;
    }

    private static VoxelShape collisionShapeFor(Direction facing) {
        return facing.getAxis() == Direction.Axis.X ? COLLISION_EAST_WEST : COLLISION_NORTH_SOUTH;
    }
}
