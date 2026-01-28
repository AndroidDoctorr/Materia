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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CounterBlock extends Block {
    public static final BooleanProperty INPUT = BooleanProperty.create("input");
    public static final BooleanProperty RESET = BooleanProperty.create("reset");
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");
    public static final BooleanProperty CARRY = BooleanProperty.create("carry");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public CounterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(INPUT, false)
            .setValue(RESET, false)
            .setValue(OUTPUT, false)
            .setValue(CARRY, false)
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INPUT, RESET, OUTPUT, CARRY, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        return this.defaultBlockState()
            .setValue(FACING, facing)
            .setValue(INPUT, false)
            .setValue(RESET, false)
            .setValue(OUTPUT, false)
            .setValue(CARRY, false);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();

            boolean in = hasDirectSignal(level, pos, left);
            boolean rst = hasDirectSignal(level, pos, back);

            boolean prevIn = state.getValue(INPUT);
            boolean prevRst = state.getValue(RESET);
            boolean q = state.getValue(OUTPUT);

            boolean changed = false;

            // Rising edge on reset: reset to 0, clear carry
            if (rst && !prevRst) {
                state = state.setValue(OUTPUT, false).setValue(CARRY, false);
                changed = true;
            }

            // Rising edge on input: toggle, carry pulse on 1->0 only (unless reset active)
            if (in && !prevIn && !rst) {
                if (q) {
                    // 1 -> 0, emit carry (while input is high)
                    state = state.setValue(OUTPUT, false).setValue(CARRY, true);
                } else {
                    // 0 -> 1, no carry
                    state = state.setValue(OUTPUT, true).setValue(CARRY, false);
                }
                changed = true;
            }

            // Falling edge on input: ensure carry is cleared so no carry when input is false
            if (!in && prevIn) {
                if (state.getValue(CARRY)) {
                    state = state.setValue(CARRY, false);
                    changed = true;
                }
            }

            // Store latest input/reset booleans for model and edge detection
            if (prevIn != in || prevRst != rst) {
                state = state.setValue(INPUT, in).setValue(RESET, rst);
                changed = true;
            }

            if (changed) {
                level.setBlock(pos, state, 3);
                notifyOutputs(level, pos, facing);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    private boolean hasDirectSignal(Level level, BlockPos pos, Direction fromDir) {
        BlockPos neighborPos = pos.relative(fromDir);
        BlockState neighborState = level.getBlockState(neighborPos);
        
        // Method 1: Ask the neighbor block directly for strong power
        if (neighborState.getDirectSignal(level, neighborPos, fromDir) > 0) {
            return true;
        }
        
        // Method 2: Ask the neighbor block directly for weak power
        if (neighborState.getSignal(level, neighborPos, fromDir) > 0) {
            return true;
        }
        
        // Method 3: Check if the neighbor position has any power available
        if (level.getDirectSignal(neighborPos, fromDir) > 0) {
            return true;
        }
        
        if (level.getSignal(neighborPos, fromDir) > 0) {
            return true;
        }
        
        // Method 4: For repeaters specifically, check if they're pointing at us
        if (neighborState.getBlock() instanceof net.minecraft.world.level.block.DiodeBlock) {
            Direction repeaterFacing = neighborState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (repeaterFacing == fromDir.getOpposite() && neighborState.getValue(BlockStateProperties.POWERED)) {
                return true;
            }
        }
        
        return false;
    }

    private void notifyOutputs(Level level, BlockPos pos, Direction facing) {
        level.updateNeighborsAt(pos, this);
        // front output (Q)
        level.updateNeighborsAt(pos.relative(facing), this);
        // right side output (carry)
        level.updateNeighborsAt(pos.relative(facing.getClockWise()), this);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        Direction facing = state.getValue(FACING);
        // Receiving blocks: front neighbor queries with facing.getOpposite()
        if (direction == facing.getOpposite()) {
            return state.getValue(OUTPUT) ? 15 : 0;
        }
        // Right-side neighbor queries with left direction
        if (direction == facing.getCounterClockWise()) {
            return state.getValue(CARRY) ? 15 : 0;
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}


