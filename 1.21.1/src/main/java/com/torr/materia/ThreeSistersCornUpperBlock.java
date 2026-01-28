package com.torr.materia;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThreeSistersCornUpperBlock extends Block {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 4, 7); // Only tall stages
    
    // Define shapes for the upper part - progressively taller
    private static final VoxelShape[] UPPER_SHAPE_BY_AGE = new VoxelShape[]{
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), // Age 4 (3/4 block)
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), // Age 5 (7/8 block)
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D), // Age 6 (full block)
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)  // Age 7 (full block)
    };

    public ThreeSistersCornUpperBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 4));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int age = state.getValue(AGE);
        return UPPER_SHAPE_BY_AGE[age - 4]; // Offset since our ages start at 4
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Upper corn should have NO collision - players can walk through it like regular crops
        return net.minecraft.world.phys.shapes.Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Must be above a Three Sisters block with corn
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        
        if (belowState.getBlock() instanceof ThreeSistersBlock) {
            // Check if the Three Sisters block has corn at stage 4+
            return belowState.getValue(ThreeSistersBlock.CORN_AGE) >= 4;
        }
        
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        // Check if we should still exist
        if (dir == Direction.DOWN) {
            if (!this.canSurvive(state, level, pos)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player) {
        // Don't drop anything - let the Three Sisters block handle all loot
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true; // Allow light through like crops
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F; // Full brightness like crops
    }
}