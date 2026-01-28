package com.torr.materia;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class BeansCropBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);
    
    // Define the shapes for each growth stage
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),  // Age 0
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),  // Age 1
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),  // Age 2
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),  // Age 3
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), // Age 4
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), // Age 5
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), // Age 6
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)  // Age 7 (full grown)
    };

    public BeansCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.BEANS.get();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    protected int getBonemealAgeIncrease(net.minecraft.world.level.Level level) {
        return super.getBonemealAgeIncrease(level);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (world.getRawBrightness(pos, 0) >= 9) {
            int age = state.getValue(AGE);
            if (age < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, world, pos);
                if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
                    world.setBlock(pos, state.setValue(AGE, age + 1), 2);
                }
            }
        }
    }
} 