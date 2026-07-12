package com.torr.materia.block;

import com.torr.materia.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CurtainsBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty OUTSET = BooleanProperty.create("outset");

    private static final VoxelShape NORTH = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SOUTH = Block.box(0, 0, 14, 16, 16, 16);
    private static final VoxelShape WEST = Block.box(0, 0, 0, 2, 16, 16);
    private static final VoxelShape EAST = Block.box(14, 0, 0, 16, 16, 16);

    public CurtainsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(OUTSET, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, OUTSET);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = WallAttachment.uprightFacing(context);
        BlockState state = this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(OUTSET, WallAttachment.isOutsetPlacement(context, facing))
                .setValue(OPEN, false);
        BlockPos placePos = WallAttachment.placementPos(context);
        return state.canSurvive(context.getLevel(), placePos) ? state : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        BlockState supportState = level.getBlockState(supportPos);
        if (supportState.getBlock() instanceof ShutterBlock) {
            return true;
        }
        return supportState.isFaceSturdy(level, supportPos, facing);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)
                ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction panel = WallAttachment.panelSide(state, FACING, OUTSET);
        return switch (panel) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        state = state.cycle(OPEN);
        level.setBlock(pos, state, 3);
        level.playSound(null, pos, ModSounds.CURTAINS.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }
}
