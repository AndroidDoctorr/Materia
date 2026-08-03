package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

/**
 * Places a loose falling block in the highest air cell with sturdy, dry ground directly below.
 * Works for surface rocks, cave rocks, and bauxite patches without fragile placement predicates.
 */
public class LooseGroundBlockFeature extends Feature<SimpleBlockConfiguration> {
    private static final int SCAN_ABOVE = 2;
    private static final int SCAN_BELOW = 32;

    public LooseGroundBlockFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockState state = context.config().toPlace().getState(random, origin);

        int topY = level.getMaxBuildHeight() - 1;
        int bottomY = Math.max(level.getMinBuildHeight() + 1, origin.getY() - SCAN_BELOW);
        int scanTop = Math.min(topY, origin.getY() + SCAN_ABOVE);

        for (int y = scanTop; y >= bottomY; y--) {
            BlockPos pos = new BlockPos(origin.getX(), y, origin.getZ());
            if (!level.isEmptyBlock(pos)) {
                continue;
            }

            BlockPos below = pos.below();
            BlockState ground = level.getBlockState(below);
            if (!canSupport(level, below, ground) || isAquatic(ground)) {
                continue;
            }
            if (isAquatic(level.getBlockState(pos))) {
                continue;
            }

            level.setBlock(pos, state, 2);
            return true;
        }

        return false;
    }

    private static boolean canSupport(WorldGenLevel level, BlockPos below, BlockState ground) {
        return !ground.isAir()
                && !ground.is(BlockTags.LEAVES)
                && ground.isFaceSturdy(level, below, Direction.UP);
    }

    private static boolean isAquatic(BlockState state) {
        return state.is(Blocks.WATER)
                || state.is(Blocks.KELP)
                || state.is(Blocks.KELP_PLANT)
                || state.is(Blocks.SEAGRASS)
                || state.is(Blocks.TALL_SEAGRASS);
    }
}
