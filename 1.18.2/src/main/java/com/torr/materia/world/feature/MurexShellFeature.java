package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import com.torr.materia.MurexShellBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class MurexShellFeature extends Feature<NoneFeatureConfiguration> {
    
    public MurexShellFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        Random random = context.random();
        
        boolean placed = false;
        
        // Random variety - each patch should be consistent
        int variety = random.nextInt(3); // 0=brandaris, 1=trunculus, 2=haemastoma
        
        // Try to place a few shells in the area
        for (int i = 0; i < 3; i++) {
            // Random offset within the feature area
            int xOffset = random.nextInt(16) - 8;
            int zOffset = random.nextInt(16) - 8;
            BlockPos pos = origin.offset(xOffset, 0, zOffset);
            
            // Find the top sand block
            BlockPos sandPos = findTopSand(level, pos);
            if (sandPos != null) {
                BlockPos placePos = sandPos.above();
                FluidState aboveFluid = level.getFluidState(placePos);
                boolean waterlogged = aboveFluid.getType() == Fluids.WATER;
                if (canPlaceShell(level, sandPos, waterlogged)) {
                    Direction[] cardinal = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
                    Direction facing = cardinal[random.nextInt(4)];
                    BlockState shellState = ModBlocks.MUREX_SHELL.get().defaultBlockState()
                            .setValue(MurexShellBlock.FACING, facing)
                            .setValue(MurexShellBlock.VARIETY, variety)
                            .setValue(MurexShellBlock.WATERLOGGED, waterlogged)
                            .setValue(MurexShellBlock.DEAD, false);
                    level.setBlock(placePos, shellState, 2);
                    if (waterlogged) {
                        level.scheduleTick(placePos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                    }
                    placed = true;
                }
            }
        }
        
        return placed;
    }
    
    private BlockPos findTopSand(WorldGenLevel level, BlockPos pos) {
        for (int y = level.getMaxBuildHeight() - 1; y >= level.getMinBuildHeight(); y--) {
            BlockPos check = new BlockPos(pos.getX(), y, pos.getZ());
            if (!level.getBlockState(check).is(Blocks.SAND)) continue;
            BlockPos above = check.above();
            BlockState aboveState = level.getBlockState(above);
            FluidState fluid = level.getFluidState(above);
            // Accept either air or water above
            if (aboveState.isAir() || fluid.getType() == Fluids.WATER) {
                return check;
            }
        }
        return null;
    }
    
    private boolean canPlaceShell(WorldGenLevel level, BlockPos sandPos, boolean waterlogged) {
        BlockPos above = sandPos.above();
        if (waterlogged) {
            return level.getFluidState(above).getType() == Fluids.WATER && level.getBlockState(above).getBlock() == Blocks.WATER;
        } else {
            return level.getBlockState(above).isAir();
        }
    }
}
