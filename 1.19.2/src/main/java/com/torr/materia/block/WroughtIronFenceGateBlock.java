package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WroughtIronFenceGateBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private static final VoxelShape VISUAL_CLOSED_NORTH_SOUTH = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape VISUAL_CLOSED_EAST_WEST = Block.box(7, 0, 0, 9, 16, 16);
    private static final VoxelShape COLLISION_CLOSED_NORTH_SOUTH = Block.box(0, 0, 7, 16, 24, 9);
    private static final VoxelShape COLLISION_CLOSED_EAST_WEST = Block.box(7, 0, 0, 9, 24, 16);

    private static final VoxelShape VISUAL_OPEN_NORTH = Shapes.or(
            Block.box(0, 0, 0, 2, 16, 8),
            Block.box(14, 0, 0, 16, 16, 8));
    private static final VoxelShape VISUAL_OPEN_SOUTH = Shapes.or(
            Block.box(0, 0, 8, 2, 16, 16),
            Block.box(14, 0, 8, 16, 16, 16));
    private static final VoxelShape VISUAL_OPEN_WEST = Shapes.or(
            Block.box(0, 0, 0, 8, 16, 2),
            Block.box(0, 0, 14, 8, 16, 16));
    private static final VoxelShape VISUAL_OPEN_EAST = Shapes.or(
            Block.box(8, 0, 0, 16, 16, 2),
            Block.box(8, 0, 14, 16, 16, 16));

    private static final VoxelShape COLLISION_OPEN_NORTH = Shapes.or(
            Block.box(0, 0, 0, 2, 24, 8),
            Block.box(14, 0, 0, 16, 24, 8));
    private static final VoxelShape COLLISION_OPEN_SOUTH = Shapes.or(
            Block.box(0, 0, 8, 2, 24, 16),
            Block.box(14, 0, 8, 16, 24, 16));
    private static final VoxelShape COLLISION_OPEN_WEST = Shapes.or(
            Block.box(0, 0, 0, 8, 24, 2),
            Block.box(0, 0, 14, 8, 24, 16));
    private static final VoxelShape COLLISION_OPEN_EAST = Shapes.or(
            Block.box(8, 0, 0, 16, 24, 2),
            Block.box(8, 0, 14, 16, 24, 16));

    public WroughtIronFenceGateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
        if (context.getLevel().hasNeighborSignal(context.getClickedPos())) {
            state = state.setValue(OPEN, true);
        }
        return state;
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
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
        Direction facing = state.getValue(FACING);
        if (state.getValue(OPEN)) {
            return switch (facing) {
                case SOUTH -> collision ? COLLISION_OPEN_SOUTH : VISUAL_OPEN_SOUTH;
                case EAST -> collision ? COLLISION_OPEN_EAST : VISUAL_OPEN_EAST;
                case WEST -> collision ? COLLISION_OPEN_WEST : VISUAL_OPEN_WEST;
                default -> collision ? COLLISION_OPEN_NORTH : VISUAL_OPEN_NORTH;
            };
        }
        if (facing.getAxis() == Direction.Axis.X) {
            return collision ? COLLISION_CLOSED_EAST_WEST : VISUAL_CLOSED_EAST_WEST;
        }
        return collision ? COLLISION_CLOSED_NORTH_SOUTH : VISUAL_CLOSED_NORTH_SOUTH;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        state = state.cycle(OPEN);
        level.setBlock(pos, state, 3);
        level.playSound(null, pos,
                state.getValue(OPEN) ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }
}
