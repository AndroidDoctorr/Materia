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
 * Half Adder Block - performs binary addition of two bits
 * Inputs: A (back), B (left side)
 * Outputs: Sum (front/forward), Carry (right side)
 * 
 * Logic:
 * - Sum = A XOR B (true when exactly one input is true)
 * - Carry = A AND B (true when both inputs are true)
 * 
 * This configuration allows easy chaining:
 * - Sum output feeds into next adder's A input (back)
 * - Carry outputs align on the right for OR gate placement
 * - All B inputs on left, all carries on right, all sums on front
 */
public class HalfAdderBlock extends Block {
    
    public static final BooleanProperty A = BooleanProperty.create("a");
    public static final BooleanProperty B = BooleanProperty.create("b");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    // Shape for the block (slightly smaller than full block)
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    
    public HalfAdderBlock(Properties properties) {
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
        Direction backSide = facing.getOpposite();
        Direction leftSide = facing.getCounterClockWise();
        
        boolean inputA = hasRedstoneSignal(level, pos, backSide);
        boolean inputB = hasRedstoneSignal(level, pos, leftSide);
        
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
            Direction backSide = facing.getOpposite();
            Direction leftSide = facing.getCounterClockWise();
            
            boolean inputA = hasRedstoneSignal(level, pos, backSide);
            boolean inputB = hasRedstoneSignal(level, pos, leftSide);
            
            boolean currentA = state.getValue(A);
            boolean currentB = state.getValue(B);
            
            // Update state if inputs changed
            if (inputA != currentA || inputB != currentB) {
                BlockState newState = state.setValue(A, inputA).setValue(B, inputB);
                level.setBlock(pos, newState, 3);
                
                // Update neighbors to propagate output signals
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
     * Update neighboring blocks to propagate the output signals
     */
    protected void updateNeighbors(Level level, BlockPos pos, Direction facing) {
        // Notify the front output (sum) and right output (carry)
        level.updateNeighborsAt(pos.relative(facing), this);
        level.updateNeighborsAt(pos.relative(facing.getClockWise()), this);
        level.updateNeighborsAt(pos, this);
    }
    
    /**
     * Compute the sum output: A XOR B
     */
    protected boolean computeSum(boolean a, boolean b) {
        return a ^ b; // XOR operation
    }
    
    /**
     * Compute the carry output: A AND B
     */
    protected boolean computeCarry(boolean a, boolean b) {
        return a && b; // AND operation
    }
    
    /**
     * Get the weak redstone signal strength this block provides
     */
    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        Direction facing = state.getValue(FACING);
        boolean a = state.getValue(A);
        boolean b = state.getValue(B);
        
        // Front output (sum): A XOR B
        if (direction == facing.getOpposite()) {
            return computeSum(a, b) ? 15 : 0;
        }
        
        // Right output (carry): A AND B
        if (direction == facing.getCounterClockWise()) {
            return computeCarry(a, b) ? 15 : 0;
        }
        
        return 0;
    }

    /**
     * Get the strong redstone signal strength this block provides
     */
    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Half adder provides strong power to allow direct connection to other logic blocks
        return getSignal(state, level, pos, direction);
    }
    
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
}

