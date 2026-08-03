package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RainbowEucalyptusTreeFeature extends Feature<NoneFeatureConfiguration> {
    public RainbowEucalyptusTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return EucalyptusTreeFeature.placeTree(context.level(), context.origin(), context.random(),
                ModBlocks.RAINBOW_EUCALYPTUS_LOG.get(), ModBlocks.RAINBOW_EUCALYPTUS_LEAVES.get());
    }
}
