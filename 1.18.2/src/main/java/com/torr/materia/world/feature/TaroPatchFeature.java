package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import com.torr.materia.TaroCropBlock;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TaroPatchFeature extends Feature<NoneFeatureConfiguration> {

    public TaroPatchFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        Random random = context.random();

        int count = 4 + random.nextInt(5);
        boolean placed = false;

        for (int i = 0; i < count; i++) {
            int xOffset = random.nextInt(7) - 3;
            int zOffset = random.nextInt(7) - 3;
            BlockPos candidate = origin.offset(xOffset, 0, zOffset);
            BlockPos placePos = findSurface(level, candidate);
            if (placePos != null) {
                BlockState existing = level.getBlockState(placePos);
                if (!existing.isAir() && !existing.canBeReplaced()) {
                    continue;
                }
                int age = 2 + random.nextInt(2);
                BlockState state = ModBlocks.TARO_CROP.get().defaultBlockState()
                        .setValue(TaroCropBlock.AGE, age);
                if (state.canSurvive(level, placePos)) {
                    if (!existing.isAir()) {
                        level.setBlock(placePos, Blocks.AIR.defaultBlockState(), 2);
                    }
                    level.setBlock(placePos, state, 2);
                    placed = true;
                }
            }
        }

        return placed;
    }

    private BlockPos findSurface(WorldGenLevel level, BlockPos pos) {
        for (int y = level.getMaxBuildHeight() - 1; y >= level.getMinBuildHeight(); y--) {
            BlockPos check = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState state = level.getBlockState(check);
            BlockState above = level.getBlockState(check.above());
            if ((state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.PODZOL))
                    && (above.isAir() || above.canBeReplaced())) {
                return check.above();
            }
        }
        return null;
    }
}
