package com.torr.materia.world.tree;

import com.torr.materia.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class RainbowEucalyptusTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean largeHive) {
        return ModConfiguredFeatures.RAINBOW_EUCALYPTUS_TREE.getHolder().orElse(null);
    }
}
