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
 * NOT Gate Block - inverts the input signal.
 * Input is on the back side (opposite to output), output is on the front.
 */
public class NotGateBlock extends Block {
    
    public static final BooleanProperty INPUT = BooleanProperty.create("input");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    // Shape for the logic gate block (slightly smaller than full block)
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    
    public NotGateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(INPUT, false)
            .setValue(FACING, Direction.NORTH));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INPUT, FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        // Face away from the player
        Direction facing = context.getHorizontalDirection();
        
        // Check initial redstone input from the back side
        Direction backSide = facing.getOpposite();
        boolean input = hasRedstoneSignal(level, pos, backSide);
        
        return this.defaultBlockState()
            .setValue(INPUT, input)
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
            
            boolean input = hasRedstoneSignal(level, pos, backSide);
            boolean currentInput = state.getValue(INPUT);
            
            // Update state if input changed
            if (input != currentInput) {
                BlockState newState = state.setValue(INPUT, input);
                level.setBlock(pos, newState, 3);
                
                // Update neighbors to propagate output signal
                updateNeighbors(level, pos, facing);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            Direction backSide = facing.getOpposite();
            boolean input = hasRedstoneSignal(level, pos, backSide);
            if (input != state.getValue(INPUT)) {
                level.setBlock(pos, state.setValue(INPUT, input), 3);
            }
        }
        super.onPlace(state, level, pos, oldState, isMoving);
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
     * Update neighboring blocks to propagate the output signal
     */
    protected void updateNeighbors(Level level, BlockPos pos, Direction facing) {
        // Notify the output side that the signal changed
        level.updateNeighborsAt(pos.relative(facing), this);
        level.updateNeighborsAt(pos, this);
    }
    
    /**
     * Get the redstone signal strength this block provides
     */
    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Only provide signal in the output direction
        Direction facing = state.getValue(FACING);
        if (direction == facing.getOpposite()) {
            // NOT gate: output is inverted input
            return !state.getValue(INPUT) ? 15 : 0;
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
