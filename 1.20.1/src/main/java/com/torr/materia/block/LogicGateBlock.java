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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Base class for logic gate blocks that handle redstone signals.
 * Each gate has two inputs (A from left side, B from right side) and one output (front).
 * The facing property determines which direction is "forward" (output direction).
 */
public abstract class LogicGateBlock extends Block {
    
    public static final BooleanProperty A = BooleanProperty.create("a");
    public static final BooleanProperty B = BooleanProperty.create("b");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    // Shape for the logic gate block (slightly smaller than full block)
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    
    public LogicGateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(A, false)
            .setValue(B, false)
            .setValue(FACING, Direction.NORTH));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(A, B, FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        // Face away from the player
        Direction facing = context.getHorizontalDirection();
        
        // Check initial redstone inputs based on the facing direction
        Direction leftSide = facing.getCounterClockWise();
        Direction rightSide = facing.getClockWise();
        
        boolean inputA = hasRedstoneSignal(level, pos, leftSide);
        boolean inputB = hasRedstoneSignal(level, pos, rightSide);
        
        return this.defaultBlockState()
            .setValue(A, inputA)
            .setValue(B, inputB)
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
            
            boolean inputA = hasRedstoneSignal(level, pos, leftSide);
            boolean inputB = hasRedstoneSignal(level, pos, rightSide);
            
            boolean currentA = state.getValue(A);
            boolean currentB = state.getValue(B);
            
            // Update state if inputs changed
            if (inputA != currentA || inputB != currentB) {
                BlockState newState = state.setValue(A, inputA).setValue(B, inputB);
                level.setBlock(pos, newState, 3);
                
                // Update neighbors to propagate output signal
                updateNeighbors(level, pos, facing);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }
    
    /**
     * Check if there's a redstone signal coming from the specified direction
     */
    protected boolean hasRedstoneSignal(Level level, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);
        
        // Key insight: When querying power, the direction should be FROM the neighbor TO us
        // So if we're checking our left side, the neighbor is TO THE LEFT (direction)
        // But when asking "what power are you giving me?", we use direction (not opposite!)
        
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
            Direction repeaterFacing = neighborState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (repeaterFacing == direction.getOpposite() && neighborState.getValue(BlockStateProperties.POWERED)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Update neighboring blocks to propagate the output signal
     */
    protected void updateNeighbors(Level level, BlockPos pos, Direction facing) {
        // Notify the output side that the signal changed
        level.updateNeighborsAt(pos.relative(facing), this);
        level.updateNeighborsAt(pos, this);
    }
    
    /**
     * Get the weak redstone signal strength this block provides
     */
    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Only provide signal in the output direction (to the neighbor in front of us)
        // The engine queries from the receiving block's perspective, so compare with facing.getOpposite()
        Direction facing = state.getValue(FACING);
        if (direction == facing.getOpposite()) {
            return computeOutput(state.getValue(A), state.getValue(B)) ? 15 : 0;
        }
        return 0;
    }

    /**
     * Get the strong redstone signal strength this block provides
     */
    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Logic gates provide strong power to allow direct connection to other logic gates
        return getSignal(state, level, pos, direction);
    }
    
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
    
    /**
     * Compute the logic gate output based on inputs A and B
     * Must be implemented by each specific gate type
     */
    protected abstract boolean computeOutput(boolean inputA, boolean inputB);
}
