package com.torr.materia.block;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.blockentity.TimerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;

public class TimerBlock extends BaseEntityBlock {
    public static final IntegerProperty INTERVAL = IntegerProperty.create("interval", 0, 3);
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape SHAPE = BaseEntityBlock.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public TimerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(INTERVAL, 0)
            .setValue(OUTPUT, false)
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(INTERVAL, OUTPUT, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        return this.defaultBlockState()
            .setValue(FACING, facing)
            .setValue(INTERVAL, 0)
            .setValue(OUTPUT, false);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.TIMER_BLOCK_ENTITY.get(), TimerBlockEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            int cur = state.getValue(INTERVAL);
            int next = (cur + 1) % 4;
            BlockState newState = state.setValue(INTERVAL, next);
            level.setBlock(pos, newState, 3);
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TimerBlockEntity timer) {
                timer.resetCountdownForInterval(next);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static int intervalToTicks(int interval) {
        switch (interval) {
            case 0: return 20; // 1s
            case 1: return 10; // 0.5s
            case 2: return 5;  // 0.25s
            case 3: return 3;  // ~0.15s (closest to 1/8th)
            default: return 20;
        }
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        Direction facing = state.getValue(FACING);
        if (direction == facing.getOpposite()) {
            return state.getValue(OUTPUT) ? 15 : 0;
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
}


