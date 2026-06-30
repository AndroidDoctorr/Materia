package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ReedClusterFeature extends Feature<NoneFeatureConfiguration> {

    public ReedClusterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        int count = 4 + random.nextInt(7);
        boolean placed = false;

        for (int i = 0; i < count; i++) {
            int xOffset = random.nextInt(7) - 3;
            int zOffset = random.nextInt(7) - 3;
            BlockPos candidate = origin.offset(xOffset, 0, zOffset);
            BlockPos placePos = findSurface(level, candidate);
            if (placePos != null && canPlaceReed(level, placePos)) {
                level.setBlock(placePos, ModBlocks.REEDS.get().defaultBlockState(), 2);
                placed = true;
            }
        }

        return placed;
    }

    private BlockPos findSurface(WorldGenLevel level, BlockPos pos) {
        for (int y = level.getMaxBuildHeight() - 1; y >= level.getMinBuildHeight(); y--) {
            BlockPos check = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState state = level.getBlockState(check);
            BlockState above = level.getBlockState(check.above());
            if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.SAND)
                    || state.is(Blocks.RED_SAND) || state.is(Blocks.MUD) || state.is(Blocks.PODZOL)) {
                if (above.isAir() || above.getFluidState().is(FluidTags.WATER)) {
                    return check.above();
                }
            }
        }
        return null;
    }

    private boolean canPlaceReed(WorldGenLevel level, BlockPos pos) {
        BlockState state = ModBlocks.REEDS.get().defaultBlockState();
        if (!state.canSurvive(level, pos)) {
            return false;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }
}
