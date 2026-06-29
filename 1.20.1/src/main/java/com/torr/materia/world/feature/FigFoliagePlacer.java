package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.torr.materia.ModBlocks;
import com.torr.materia.block.FigTreeLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class FigFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<FigFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) ->
            blobParts(instance).apply(instance, FigFoliagePlacer::new));

    public FigFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.FIG_FOLIAGE_PLACER.get();
    }

    @Override
    protected void placeLeavesRow(LevelSimulatedReader level, FoliageSetter setter, RandomSource random,
                                  TreeConfiguration config, BlockPos pos, int radius, int y, boolean giantTrunk) {
        FoliageSetter wrappedSetter = new FoliageSetter() {
            @Override
            public void set(BlockPos blockPos, BlockState blockState) {
                if (blockState.is(ModBlocks.FIG_LEAVES.get()) && random.nextFloat() < 0.5f) {
                    setter.set(blockPos, blockState.setValue(FigTreeLeavesBlock.HAS_FIGS, true));
                } else {
                    setter.set(blockPos, blockState);
                }
            }

            @Override
            public boolean isSet(BlockPos blockPos) {
                return setter.isSet(blockPos);
            }
        };
        super.placeLeavesRow(level, wrappedSetter, random, config, pos, radius, y, giantTrunk);
    }
}
