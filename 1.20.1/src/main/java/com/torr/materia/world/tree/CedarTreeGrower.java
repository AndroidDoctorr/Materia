package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class CedarTreeGrower extends AbstractMegaTreeGrower {
    private static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            new ResourceLocation(materia.MOD_ID, "cedar_tree")
    );
    private static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_MEGA_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            new ResourceLocation(materia.MOD_ID, "cedar_mega_tree")
    );

    @Override
    @Nullable
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
        return CEDAR_TREE;
    }

    @Override
    @Nullable
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
        return CEDAR_MEGA_TREE;
    }
}
