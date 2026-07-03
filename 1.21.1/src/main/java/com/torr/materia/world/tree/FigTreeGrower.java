package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class FigTreeGrower {
    private FigTreeGrower() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> FIG_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "fig_tree")
    );

    public static final TreeGrower GROWER = new TreeGrower(
            materia.MOD_ID + ":fig",
            Optional.empty(),
            Optional.of(FIG_TREE),
            Optional.empty()
    );
}
