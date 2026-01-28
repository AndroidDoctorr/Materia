package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class RsLatchBlock extends Block {
    public static final BooleanProperty R = BooleanProperty.create("r");
    public static final BooleanProperty S = BooleanProperty.create("s");
    public static final BooleanProperty Q = BooleanProperty.create("q");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RsLatchBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(R, false)
            .setValue(S, false)
            .setValue(Q, false)
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(R, S, Q, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();

        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        boolean s = hasRedstoneSignal(level, pos, left);
        boolean r = hasRedstoneSignal(level, pos, right);
        boolean q = computeNextQ(false, r, s); // start from false on placement

        return this.defaultBlockState()
            .setValue(FACING, facing)
            .setValue(R, r)
            .setValue(S, s)
            .setValue(Q, q);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();

            boolean s = hasRedstoneSignal(level, pos, left);
            boolean r = hasRedstoneSignal(level, pos, right);
            boolean curQ = state.getValue(Q);
            boolean nextQ = computeNextQ(curQ, r, s);

            if (s != state.getValue(S) || r != state.getValue(R) || nextQ != curQ) {
                BlockState newState = state.setValue(S, s).setValue(R, r).setValue(Q, nextQ);
                level.setBlock(pos, newState, 3);
                updateNeighbors(level, pos, facing);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    private boolean computeNextQ(boolean currentQ, boolean r, boolean s) {
        if (r && s) return false;       // invalid: force reset
        if (s && !r) return true;       // set
        if (r && !s) return false;      // reset
        return currentQ;                // latch
    }

    private boolean hasRedstoneSignal(Level level, BlockPos pos, Direction fromDirection) {
        BlockPos neighborPos = pos.relative(fromDirection);
        BlockState neighborState = level.getBlockState(neighborPos);
        
        // Method 1: Ask the neighbor block directly for strong power
        if (neighborState.getDirectSignal(level, neighborPos, fromDirection) > 0) {
            return true;
        }
        
        // Method 2: Ask the neighbor block directly for weak power
        if (neighborState.getSignal(level, neighborPos, fromDirection) > 0) {
            return true;
        }
        
        // Method 3: Check if the neighbor position has any power available
        if (level.getDirectSignal(neighborPos, fromDirection) > 0) {
            return true;
        }
        
        if (level.getSignal(neighborPos, fromDirection) > 0) {
            return true;
        }
        
        // Method 4: For repeaters specifically, check if they're pointing at us
        if (neighborState.getBlock() instanceof net.minecraft.world.level.block.DiodeBlock) {
            Direction repeaterFacing = neighborState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (repeaterFacing == fromDirection.getOpposite() && neighborState.getValue(BlockStateProperties.POWERED)) {
                return true;
            }
        }
        
        return false;
    }

    private void updateNeighbors(Level level, BlockPos pos, Direction facing) {
        // Notify both outputs: front (Q) and back (!Q), plus self
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(facing), this);
        level.updateNeighborsAt(pos.relative(facing.getOpposite()), this);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        Direction facing = state.getValue(FACING);
        boolean r = state.getValue(R);
        boolean s = state.getValue(S);
        if (r && s) {
            // invalid condition forces both outputs low
            return 0;
        }
        if (direction == facing.getOpposite()) {
            // receiving block is in front of us → provide Q
            return state.getValue(Q) ? 15 : 0;
        }
        if (direction == facing) {
            // receiving block is behind us → provide !Q
            return state.getValue(Q) ? 0 : 15;
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Provide strong power to allow direct connection to other logic blocks
        return getSignal(state, level, pos, direction);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
}


