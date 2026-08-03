package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EucalyptusGroveFeature extends Feature<NoneFeatureConfiguration> {
    private final boolean rainbowGrove;

    public EucalyptusGroveFeature(Codec<NoneFeatureConfiguration> codec, boolean rainbowGrove) {
        super(codec);
        this.rainbowGrove = rainbowGrove;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        Block logBlock = rainbowGrove ? ModBlocks.RAINBOW_EUCALYPTUS_LOG.get() : ModBlocks.EUCALYPTUS_LOG.get();
        Block leavesBlock = rainbowGrove ? ModBlocks.RAINBOW_EUCALYPTUS_LEAVES.get() : ModBlocks.EUCALYPTUS_LEAVES.get();

        int treeCount = 3 + random.nextInt(5);
        boolean placedAny = false;

        for (int i = 0; i < treeCount; i++) {
            int offsetX = random.nextInt(17) - 8;
            int offsetZ = random.nextInt(17) - 8;
            BlockPos treePos = origin.offset(offsetX, 0, offsetZ);
            if (EucalyptusTreeFeature.placeTree(level, treePos, random, logBlock, leavesBlock)) {
                placedAny = true;
            }
        }
        return placedAny;
    }
}
