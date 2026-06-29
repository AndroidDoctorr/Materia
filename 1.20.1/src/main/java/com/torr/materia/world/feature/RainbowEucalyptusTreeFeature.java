package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RainbowEucalyptusTreeFeature extends Feature<NoneFeatureConfiguration> {
    public RainbowEucalyptusTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return EucalyptusTreeFeature.placeTree(context.level(), context.origin(), context.random(), true);
    }
}
