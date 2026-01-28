package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.torr.materia.ModBlocks;
import com.torr.materia.block.OliveTreeLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class OliveFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<OliveFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return blobParts(instance).apply(instance, OliveFoliagePlacer::new);
    });

    public OliveFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.OLIVE_FOLIAGE_PLACER.get();
    }

    @Override
    protected void placeLeavesRow(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, Random random, TreeConfiguration config, BlockPos pos, int radius, int y, boolean giantTrunk) {
        super.placeLeavesRow(level, (blockPos, blockState) -> {
            // 30% chance for leaves to have olives when the tree is generated
            if (blockState.is(ModBlocks.OLIVE_TREE_LEAVES.get()) && random.nextFloat() < 0.3f) {
                BlockState olivesState = blockState.setValue(OliveTreeLeavesBlock.HAS_OLIVES, true);
                blockSetter.accept(blockPos, olivesState);
            } else {
                blockSetter.accept(blockPos, blockState);
            }
        }, random, config, pos, radius, y, giantTrunk);
    }
}
