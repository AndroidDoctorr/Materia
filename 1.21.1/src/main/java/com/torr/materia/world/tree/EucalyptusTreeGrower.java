package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class EucalyptusTreeGrower {
    private EucalyptusTreeGrower() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> EUCALYPTUS_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "eucalyptus_tree")
    );

    public static final TreeGrower GROWER = new TreeGrower(
            materia.MOD_ID + ":eucalyptus_tree",
            Optional.of(EUCALYPTUS_TREE),
            Optional.empty(),
            Optional.empty()
    );
}
