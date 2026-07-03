package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class CedarTreeGrower {
    private CedarTreeGrower() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "cedar_tree")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_MEGA_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "cedar_mega_tree")
    );

    public static final TreeGrower GROWER = new TreeGrower(
            materia.MOD_ID + ":cedar",
            0.0F,
            Optional.of(CEDAR_MEGA_TREE),
            Optional.empty(),
            Optional.of(CEDAR_TREE),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}

