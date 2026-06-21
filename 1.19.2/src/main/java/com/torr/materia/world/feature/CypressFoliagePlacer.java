package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.function.BiConsumer;

public class CypressFoliagePlacer extends FoliagePlacer {
    public static final Codec<CypressFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    IntProvider.codec(0, 16).fieldOf("radius").forGetter(p -> p.radius),
                    IntProvider.codec(0, 16).fieldOf("offset").forGetter(p -> p.offset),
                    Codec.intRange(1, 16).fieldOf("height").forGetter(p -> p.height)
            ).apply(instance, CypressFoliagePlacer::new)
    );

    private final int height;

    public CypressFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.CYPRESS_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        BlockPos base = attachment.pos();
        int topRadius = this.radius.sample(random);
        for (int layer = 0; layer < height; layer++) {
            int radius = topRadius * (height - 1 - layer) / Math.max(1, height - 1);
            placeLeavesRow(level, blockSetter, random, config, base.above(layer), radius, 0, attachment.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int dz, int currentRadius, int layerRadius, boolean doubleTrunk) {
        return dx * dx + dz * dz > layerRadius * layerRadius + layerRadius;
    }
}
