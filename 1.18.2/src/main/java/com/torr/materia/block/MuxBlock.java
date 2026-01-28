package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MuxBlock extends Block {
    public static final IntegerProperty CHANNEL = IntegerProperty.create("channel", 0, 1);
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public MuxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(CHANNEL, 0)
            .setValue(OUTPUT, false)
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHANNEL, OUTPUT, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        Direction facing = context.getHorizontalDirection();
        Direction leftSide = facing.getCounterClockWise();
        Direction rightSide = facing.getClockWise();
        Direction backSide = facing.getOpposite();

        boolean selRight = hasRedstoneSignal(level, pos, backSide);
        int channel = selRight ? 1 : 0;
        boolean leftIn = hasRedstoneSignal(level, pos, leftSide);
        boolean rightIn = hasRedstoneSignal(level, pos, rightSide);
        boolean out = channel == 1 ? rightIn : leftIn;

        return this.defaultBlockState()
            .setValue(CHANNEL, channel)
            .setValue(OUTPUT, out)
            .setValue(FACING, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            Direction leftSide = facing.getCounterClockWise();
            Direction rightSide = facing.getClockWise();
            Direction backSide = facing.getOpposite();

            boolean selRight = hasRedstoneSignal(level, pos, backSide);
            int channel = selRight ? 1 : 0;
            boolean leftIn = hasRedstoneSignal(level, pos, leftSide);
            boolean rightIn = hasRedstoneSignal(level, pos, rightSide);
            boolean out = channel == 1 ? rightIn : leftIn;

            int curChannel = state.getValue(CHANNEL);
            boolean curOut = state.getValue(OUTPUT);
            if (channel != curChannel || out != curOut) {
                BlockState newState = state.setValue(CHANNEL, channel).setValue(OUTPUT, out);
                level.setBlock(pos, newState, 3);
                updateNeighbors(level, pos, facing);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    protected boolean hasRedstoneSignal(Level level, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);
        
        // Method 1: Ask the neighbor block directly for strong power
        if (neighborState.getDirectSignal(level, neighborPos, direction) > 0) {
            return true;
        }
        
        // Method 2: Ask the neighbor block directly for weak power
        if (neighborState.getSignal(level, neighborPos, direction) > 0) {
            return true;
        }
        
        // Method 3: Check if the neighbor position has any power available
        if (level.getDirectSignal(neighborPos, direction) > 0) {
            return true;
        }
        
        if (level.getSignal(neighborPos, direction) > 0) {
            return true;
        }
        
        // Method 4: For repeaters specifically, check if they're pointing at us
        if (neighborState.getBlock() instanceof net.minecraft.world.level.block.DiodeBlock) {
            Direction repeaterFacing = neighborState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING);
            if (repeaterFacing == direction.getOpposite() && neighborState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED)) {
                return true;
            }
        }
        
        return false;
    }

    protected void updateNeighbors(Level level, BlockPos pos, Direction facing) {
        level.updateNeighborsAt(pos.relative(facing), this);
        level.updateNeighborsAt(pos, this);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        Direction facing = state.getValue(FACING);
        // Receiving block's perspective
        if (direction == facing.getOpposite()) {
            return state.getValue(OUTPUT) ? 15 : 0;
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
