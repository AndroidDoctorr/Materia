package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class LotusWaterPatchFeature extends Feature<NoneFeatureConfiguration> {

    public LotusWaterPatchFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        var random = context.random();
        BlockState lotusState = ModBlocks.LOTUS.get().defaultBlockState();

        int count = 3 + random.nextInt(4);
        boolean placed = false;

        for (int i = 0; i < count; i++) {
            int xOffset = random.nextInt(7) - 3;
            int zOffset = random.nextInt(7) - 3;
            BlockPos lotusPos = findLotusPosition(level, origin.offset(xOffset, 0, zOffset), lotusState);
            if (lotusPos != null) {
                level.setBlock(lotusPos, lotusState, 2);
                placed = true;
            }
        }

        return placed;
    }

    private BlockPos findLotusPosition(WorldGenLevel level, BlockPos horizontal, BlockState lotusState) {
        int minY = level.getMinBuildHeight() + 1;
        int maxY = level.getMaxBuildHeight() - 1;

        for (int y = minY; y <= maxY; y++) {
            BlockPos waterPos = new BlockPos(horizontal.getX(), y, horizontal.getZ());
            if (!level.getFluidState(waterPos).is(Fluids.WATER)) {
                continue;
            }
            BlockPos lotusPos = waterPos.above();
            if (!level.isEmptyBlock(lotusPos)) {
                continue;
            }
            if (lotusState.canSurvive(level, lotusPos)) {
                return lotusPos;
            }
        }

        return null;
    }
}
