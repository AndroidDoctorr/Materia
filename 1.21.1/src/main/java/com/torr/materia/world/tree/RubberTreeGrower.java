package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class RubberTreeGrower {
    private RubberTreeGrower() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rubber_tree")
    );

    public static final TreeGrower GROWER = new TreeGrower(
            materia.MOD_ID + ":rubber",
            Optional.of(RUBBER_TREE),
            Optional.empty(),
            Optional.empty()
    );
}
