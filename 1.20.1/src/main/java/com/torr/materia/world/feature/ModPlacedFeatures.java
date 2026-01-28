package com.torr.materia.world.feature;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.createOptional(Registries.PLACED_FEATURE, materia.MOD_ID);

    // Common placement for tin gravel - rare but big alluvial deposits
    public static final RegistryObject<PlacedFeature> GRAVEL_TIN_ORE_PLACED = PLACED_FEATURES.register("gravel_tin_ore_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.GRAVEL_TIN_ORE.get()),
                    commonOrePlacement(2, // 2 veins per chunk (rare but findable)
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(80)))));

    // River-specific placement (more frequent alluvial deposits)
    public static final RegistryObject<PlacedFeature> GRAVEL_TIN_ORE_RIVER_PLACED = PLACED_FEATURES.register("gravel_tin_ore_river_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.GRAVEL_TIN_ORE.get()),
                    commonOrePlacement(4, // 4 veins per chunk in rivers (more common in water areas)
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(90)))));

    // Earth subsoil placement - replaces dirt in the shallow subsurface layer (1-6 blocks below surface)
    public static final RegistryObject<PlacedFeature> EARTH_SUBSOIL_PLACED = PLACED_FEATURES.register("earth_subsoil_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.EARTH_UNDERSOIL.get()),
                    List.of(
                            // NOTE: vanilla codec clamps CountPlacement to [0..64] for serialization into worldgen settings.
                            // Values > 64 can make worlds fail to load ("Value 80 outside of range [0:64]").
                            CountPlacement.of(64),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            RandomOffsetPlacement.vertical(net.minecraft.util.valueproviders.UniformInt.of(-6, -1)), // 1-6 blocks below surface
                            BiomeFilter.biome())));

    // Surface earth placement for riverbeds and similar
    public static final RegistryObject<PlacedFeature> SURFACE_EARTH_RIVER_PLACED = PLACED_FEATURES.register("surface_earth_river_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SURFACE_EARTH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Surface rock placement - spawns on the ocean floor (solid ground under water)
    public static final RegistryObject<PlacedFeature> SURFACE_ROCK_PLACED = PLACED_FEATURES.register("surface_rock_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SURFACE_ROCK.get()),
                    List.of(RarityFilter.onAverageOnceEvery(8), // 1 in 8 chance per attempt (increased from 16)
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

    // Bauxite surface clusters - warm/wet surface clumps on stone or dirt
    public static final RegistryObject<PlacedFeature> BAUXITE_PATCH_PLACED = PLACED_FEATURES.register("bauxite_patch_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.BAUXITE_PATCH.get()),
                    List.of(
                            RarityFilter.onAverageOnceEvery(16),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                    BlockPredicate.wouldSurvive(ModBlocks.BAUXITE.get().defaultBlockState(), BlockPos.ZERO),
                                    BlockPredicate.not(BlockPredicate.matchesBlocks(BlockPos.ZERO, java.util.List.of(
                                            Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                    BlockPredicate.not(BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), java.util.List.of(
                                            Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                    BlockPredicate.not(BlockPredicate.matchesTag(new BlockPos(0, -1, 0), BlockTags.LEAVES)))),
                            BiomeFilter.biome())));

    // Loose chunks that must sit atop exposed bauxite ore
    public static final RegistryObject<PlacedFeature> BAUXITE_PATCH_ON_ORE_PLACED = PLACED_FEATURES.register("bauxite_patch_on_ore_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.BAUXITE_PATCH.get()),
                    List.of(
                            RarityFilter.onAverageOnceEvery(8),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                    BlockPredicate.not(BlockPredicate.matchesBlocks(BlockPos.ZERO, java.util.List.of(
                                            Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                    BlockPredicate.not(BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), java.util.List.of(
                                            Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                    BlockPredicate.wouldSurvive(ModBlocks.BAUXITE.get().defaultBlockState(), BlockPos.ZERO),
                                    BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), java.util.List.of(ModBlocks.BAUXITE_ORE.get().defaultBlockState().getBlock())))),
                            BiomeFilter.biome())));

    // Bauxite ore blobs under patches (surface height, stone/dirt base)
    public static final RegistryObject<PlacedFeature> BAUXITE_ORE_BLOB_PLACED = PLACED_FEATURES.register("bauxite_ore_blob_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.BAUXITE_ORE_BLOB.get()),
                    List.of(
                            // similar frequency to surface patch so blobs sit beneath
                            RarityFilter.onAverageOnceEvery(16),
                            InSquarePlacement.spread(),
                            // near-surface range to keep exposed tops
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(58), VerticalAnchor.absolute(120)),
                            BiomeFilter.biome())));

    // Cave rock placement - spawns in caves/underground
    public static final RegistryObject<PlacedFeature> CAVE_ROCK_PLACED = PLACED_FEATURES.register("cave_rock_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.CAVE_ROCK.get()),
                    List.of(CountPlacement.of(8), // 8 attempts per chunk (increased from 5 for more cave rocks)
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(50)),
                            EnvironmentScanPlacement.scanningFor(net.minecraft.core.Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                            RandomOffsetPlacement.vertical(net.minecraft.util.valueproviders.ConstantInt.of(1)),
                            BiomeFilter.biome())));

    // Rocky biome surface rock placement - higher spawn rate for mountains/hills
    public static final RegistryObject<PlacedFeature> SURFACE_ROCK_ROCKY_PLACED = PLACED_FEATURES.register("surface_rock_rocky_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SURFACE_ROCK.get()),
                    List.of(RarityFilter.onAverageOnceEvery(2), // 1 in 2 chance (increased from 4 - very common in mountains!)
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

    // Enhanced cave rock placement for rocky biomes
    public static final RegistryObject<PlacedFeature> CAVE_ROCK_ROCKY_PLACED = PLACED_FEATURES.register("cave_rock_rocky_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.CAVE_ROCK.get()),
                    List.of(CountPlacement.of(15), // 15 attempts per chunk (increased from 10, very common in mountain caves!)
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(50)),
                            EnvironmentScanPlacement.scanningFor(net.minecraft.core.Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                            RandomOffsetPlacement.vertical(net.minecraft.util.valueproviders.ConstantInt.of(1)),
                            BiomeFilter.biome())));

    // Wild flax placement â€“ flower-style patches
    public static final RegistryObject<PlacedFeature> WILD_FLAX_PLACED = PLACED_FEATURES.register("wild_flax_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.WILD_FLAX_PATCH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(32), // 1 patch every 32 chunks on average
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Wild squash placement â€“ flower-style patches
    public static final RegistryObject<PlacedFeature> WILD_SQUASH_PLACED = PLACED_FEATURES.register("wild_squash_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.WILD_SQUASH_PATCH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(64), // 1 patch every 64 chunks on average
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Wild beans placement â€“ flower-style patches
    public static final RegistryObject<PlacedFeature> WILD_BEANS_PLACED = PLACED_FEATURES.register("wild_beans_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.WILD_BEANS_PATCH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(64), // 1 patch every 64 chunks on average
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Wild peppers placement â€“ flower-style patches
    public static final RegistryObject<PlacedFeature> WILD_PEPPERS_PLACED = PLACED_FEATURES.register("wild_peppers_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.WILD_PEPPERS_PATCH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(64), // 1 patch every 64 chunks on average
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Wild corn placement â€“ flower-style patches
    public static final RegistryObject<PlacedFeature> WILD_CORN_PLACED = PLACED_FEATURES.register("wild_corn_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.WILD_CORN_PATCH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(64), // 1 patch every 64 chunks on average
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Ochre clay placement - rare
    public static final RegistryObject<PlacedFeature> OCHRE_CLAY_PLACED = PLACED_FEATURES.register("ochre_clay_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.OCHRE_CLAY.get()),
                    commonOrePlacement(40, // 40 veins per chunk (much more common)
                            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));

    // Red ochre clay placement - very rare
    public static final RegistryObject<PlacedFeature> RED_OCHRE_CLAY_PLACED = PLACED_FEATURES.register("red_ochre_clay_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.RED_OCHRE_CLAY.get()),
                    commonOrePlacement(10, // 10 veins per chunk (still rarer)
                            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));

    // Indigo flower placement
    public static final RegistryObject<PlacedFeature> INDIGO_PLACED = PLACED_FEATURES.register("indigo_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.INDIGO_PATCH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(64),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Wild grape vine placement - spawns on trees in temperate forests
    public static final RegistryObject<PlacedFeature> WILD_GRAPE_VINE_PLACED = PLACED_FEATURES.register("wild_grape_vine_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.WILD_GRAPE_VINE.get()),
                    List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Wild wisteria vine placement - spawns on trees in temperate forests
    public static final RegistryObject<PlacedFeature> WILD_WISTERIA_VINE_PLACED = PLACED_FEATURES.register("wild_wisteria_vine_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.WILD_WISTERIA_VINE.get()),
                    List.of(RarityFilter.onAverageOnceEvery(8),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Murex shell placement - spawns on beaches
    public static final RegistryObject<PlacedFeature> MUREX_SHELL_PLACED = PLACED_FEATURES.register("murex_shell_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.MUREX_SHELL_FEATURE.get()),
                    List.of(RarityFilter.onAverageOnceEvery(16), // 1 in 8 chance per attempt
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Clam placement - spawns on beaches
    public static final RegistryObject<PlacedFeature> CLAM_PLACED = PLACED_FEATURES.register("clam_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.CLAM_FEATURE.get()),
                    List.of(RarityFilter.onAverageOnceEvery(8), // Slightly rarer than murex shells
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                            BiomeFilter.biome())));

    // Malachite ore placement - targets surface exposed copper ore (air-exposed only)
    public static final RegistryObject<PlacedFeature> MALACHITE_ORE_PLACED = PLACED_FEATURES.register("malachite_ore_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.MALACHITE_ORE.get()),
                    List.of(
                            CountPlacement.of(10),
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(120)),
                            EnvironmentScanPlacement.scanningFor(net.minecraft.core.Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.ONLY_IN_AIR_PREDICATE, 1),
                            BiomeFilter.biome())));

    // Surface iron ore placement - targets surface exposed iron ore (air-exposed only)
    public static final RegistryObject<PlacedFeature> SURFACE_IRON_ORE_PLACED = PLACED_FEATURES.register("surface_iron_ore_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SURFACE_IRON_ORE.get()),
                    List.of(
                            CountPlacement.of(7),
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(120)),
                            EnvironmentScanPlacement.scanningFor(net.minecraft.core.Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.ONLY_IN_AIR_PREDICATE, 1),
                            BiomeFilter.biome())));

    // Magnetite ore placement - emerald-like rarity, mountain biased
    public static final RegistryObject<PlacedFeature> MAGNETITE_ORE_PLACED = PLACED_FEATURES.register("magnetite_ore_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.MAGNETITE_ORE.get()),
                    commonOrePlacement(2, // 2 attempts per chunk (base)
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))));

    // Rocky biome magnetite placement - higher spawn rate for mountains (like emerald)
    public static final RegistryObject<PlacedFeature> MAGNETITE_ORE_ROCKY_PLACED = PLACED_FEATURES.register("magnetite_ore_rocky_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.MAGNETITE_ORE.get()),
                    commonOrePlacement(12, // 12 attempts per chunk in mountains (higher)
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))));

    // Sphalerite (zinc ore) placement - common near surface, realistic depth range
    public static final RegistryObject<PlacedFeature> SPHALERITE_ORE_PLACED = PLACED_FEATURES.register("sphalerite_ore_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SPHALERITE_ORE.get()),
                    commonOrePlacement(12, // 12 veins per chunk (moderately common)
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(80)))));

    // Cave-focused sphalerite placement - higher concentration in caves
    public static final RegistryObject<PlacedFeature> SPHALERITE_CAVE_PLACED = PLACED_FEATURES.register("sphalerite_cave_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SPHALERITE_ORE.get()),
                    commonOrePlacement(8, // 8 veins per chunk in caves
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(40)))));

    // Sulfur ore placement - spawns near lava pools and in tuff veins
    public static final RegistryObject<PlacedFeature> SULFUR_ORE_PLACED = PLACED_FEATURES.register("sulfur_ore_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SULFUR_ORE.get()),
                    commonOrePlacement(10, // 10 veins per chunk (common)
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)))));

    // Sulfur ore near lava placement - higher concentration near lava level
    public static final RegistryObject<PlacedFeature> SULFUR_ORE_LAVA_PLACED = PLACED_FEATURES.register("sulfur_ore_lava_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SULFUR_ORE.get()),
                    commonOrePlacement(15, // 15 veins per chunk near lava level (very common)
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(20)))));

    // Rubber tree placement - spawns in tropical biomes (jungles, savannas)
    public static final RegistryObject<PlacedFeature> RUBBER_TREE_PLACED = PLACED_FEATURES.register("rubber_tree_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.RUBBER_TREE.get()),
                    List.of(RarityFilter.onAverageOnceEvery(20), // 1 in 20 chance per attempt (~5% chance per chunk)
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.RUBBER_TREE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

    // Olive tree placement - spawns in Mediterranean-style biomes (plains, forests)
    public static final RegistryObject<PlacedFeature> OLIVE_TREE_PLACED = PLACED_FEATURES.register("olive_tree_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.OLIVE_TREE.get()),
                    List.of(RarityFilter.onAverageOnceEvery(24), // 1 in 24 chance per attempt (~4% chance per chunk)
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.OLIVE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

    // Marble vein placement - small individual veins but very common in clusters
    public static final RegistryObject<PlacedFeature> MARBLE_VEIN_RARE_PLACED = PLACED_FEATURES.register("marble_vein_rare_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.MARBLE_VEIN.get()),
                    List.of(RarityFilter.onAverageOnceEvery(200), // Much more common - creates clusters
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(40)), // Deeper - mostly underground
                            BiomeFilter.biome())));

    // Marble vein placement - surface exposure, also more common
    public static final RegistryObject<PlacedFeature> MARBLE_VEIN_SURFACE_PLACED = PLACED_FEATURES.register("marble_vein_surface_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.MARBLE_VEIN.get()),
                    List.of(RarityFilter.onAverageOnceEvery(400), // More common surface veins
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(180)), // Surface range
                            BiomeFilter.biome())));

    // Limestone vein placement - small individual veins but very common in clusters
    public static final RegistryObject<PlacedFeature> LIMESTONE_VEIN_RARE_PLACED = PLACED_FEATURES.register("limestone_vein_rare_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.LIMESTONE_VEIN.get()),
                    List.of(RarityFilter.onAverageOnceEvery(150), // Very common - creates large clusters
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(20), VerticalAnchor.absolute(200)), // Surface oriented - more visible
                            BiomeFilter.biome())));

    // Test marble placement - deeper, moderate rarity for testing
    public static final RegistryObject<PlacedFeature> MARBLE_TEST_PLACED = PLACED_FEATURES.register("marble_test_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.MARBLE_TEST.get()),
                    commonOrePlacement(2, // 2 attempts per chunk - moderate for testing
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(60))))); // Deeper range

    // Test limestone placement - surface oriented, moderate rarity for testing
    public static final RegistryObject<PlacedFeature> LIMESTONE_TEST_PLACED = PLACED_FEATURES.register("limestone_test_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.LIMESTONE_TEST.get()),
                    commonOrePlacement(2, // 2 attempts per chunk - moderate for testing
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(140))))); // Surface range

    // Limestone surface biome placement - small individual veins but very common in rocky biomes
    public static final RegistryObject<PlacedFeature> LIMESTONE_SURFACE_BIOME_PLACED = PLACED_FEATURES.register("limestone_surface_biome_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.LIMESTONE_SURFACE_BIOME.get()),
                    List.of(RarityFilter.onAverageOnceEvery(80), // Very common in rocky biomes - creates limestone cliff regions
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(200)), // Surface focused - visible limestone cliffs
                            BiomeFilter.biome())));

    // Saltpeter sand placement - spawns in desert biomes, somewhat rare
    public static final RegistryObject<PlacedFeature> SALTPETER_SAND_PLACED = PLACED_FEATURES.register("saltpeter_sand_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SALTPETER_SAND.get()),
                    commonOrePlacement(3, // 3 attempts per chunk (somewhat rare)
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(50), VerticalAnchor.absolute(120))))); // Surface to mid-level

    // Saltpeter sandstone placement - spawns in desert biomes, rarer than sand
    public static final RegistryObject<PlacedFeature> SALTPETER_SANDSTONE_PLACED = PLACED_FEATURES.register("saltpeter_sandstone_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SALTPETER_SANDSTONE.get()),
                    commonOrePlacement(2, // 2 attempts per chunk (rarer than sand)
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(50), VerticalAnchor.absolute(120))))); // Surface to mid-level

    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
} 