package com.torr.materia.world.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.torr.materia.ModBlocks;
import com.torr.materia.block.OliveTreeLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class OliveFoliagePlacer extends BlobFoliagePlacer {
    public static final MapCodec<OliveFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            blobParts(instance).apply(instance, OliveFoliagePlacer::new)
    );

    public OliveFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.OLIVE_FOLIAGE_PLACER.get();
    }

    @Override
    protected void placeLeavesRow(LevelSimulatedReader level, FoliagePlacer.FoliageSetter setter, RandomSource random, TreeConfiguration config, BlockPos pos, int radius, int y, boolean giantTrunk) {
        FoliagePlacer.FoliageSetter wrappedSetter = new FoliagePlacer.FoliageSetter() {
            @Override
            public void set(BlockPos blockPos, BlockState blockState) {
                // 30% chance for leaves to have olives when the tree is generated
                if (blockState.is(ModBlocks.OLIVE_TREE_LEAVES.get()) && random.nextFloat() < 0.3f) {
                    BlockState olivesState = blockState.setValue(OliveTreeLeavesBlock.HAS_OLIVES, true);
                    setter.set(blockPos, olivesState);
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
