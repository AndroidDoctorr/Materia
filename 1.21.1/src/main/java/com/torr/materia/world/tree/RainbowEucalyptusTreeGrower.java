package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class RainbowEucalyptusTreeGrower {
    private RainbowEucalyptusTreeGrower() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_EUCALYPTUS_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rainbow_eucalyptus_tree")
    );

    public static final TreeGrower GROWER = new TreeGrower(
            materia.MOD_ID + ":rainbow_eucalyptus",
            Optional.empty(),
            Optional.of(RAINBOW_EUCALYPTUS_TREE),
            Optional.empty()
    );
}
