package com.torr.materia.world.feature;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.world.feature.ModFeatures;
import com.torr.materia.world.feature.OliveFoliagePlacer;
import com.torr.materia.world.feature.FigFoliagePlacer;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;

import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(new ResourceLocation("minecraft", "worldgen/configured_feature"), materia.MOD_ID);

    // Configure the tin gravel ore generation - alluvial deposits in stone/dirt layers
    public static final RegistryObject<ConfiguredFeature<?, ?>> GRAVEL_TIN_ORE = CONFIGURED_FEATURES.register("gravel_tin_ore",
            () -> {
                // Target stone layers for natural tin gravel deposits (alluvial/placer deposits)
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.GRAVEL_TIN.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.GRAVEL_TIN.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DIRT), ModBlocks.GRAVEL_TIN.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.GRAVEL), ModBlocks.GRAVEL_TIN.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 18));
            });

    // Configure earth subsoil (column scan below preserved topsoil)
    public static final RegistryObject<ConfiguredFeature<?, ?>> EARTH_UNDERSOIL = CONFIGURED_FEATURES.register("earth_undersoil",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EARTH_SUBSOIL_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    // Configure surface earth generation (for riverbeds, etc.)
    public static final RegistryObject<ConfiguredFeature<?, ?>> SURFACE_EARTH = CONFIGURED_FEATURES.register("surface_earth",
            () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.EARTH.get()))));

    // Configure surface rock generation
    public static final RegistryObject<ConfiguredFeature<?, ?>> SURFACE_ROCK = CONFIGURED_FEATURES.register("surface_rock",
            () -> new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.LOOSE_GROUND_BLOCK_FEATURE.get(),
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ROCK.get()))));

    // Configure cave rock generation  
    public static final RegistryObject<ConfiguredFeature<?, ?>> CAVE_ROCK = CONFIGURED_FEATURES.register("cave_rock",
            () -> new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.LOOSE_GROUND_BLOCK_FEATURE.get(),
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ROCK.get()))));

    // Configure bauxite surface clumps - small clustered patches
    public static final RegistryObject<ConfiguredFeature<?, ?>> BAUXITE_PATCH = CONFIGURED_FEATURES.register("bauxite_patch",
            () -> {
                ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.LOOSE_GROUND_BLOCK_FEATURE.get(),
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BAUXITE.get())));
                Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
                PlacedFeature placed = new PlacedFeature(simpleHolder, List.of());
                Holder<PlacedFeature> placedHolder = Holder.direct(placed);
                return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(6, 4, 0, placedHolder)); // fewer loose chunks per patch
            });

    // Solid bauxite ore blobs (like granite/diorite/andesite size)
    public static final RegistryObject<ConfiguredFeature<?, ?>> BAUXITE_ORE_BLOB = CONFIGURED_FEATURES.register("bauxite_ore_blob",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DIRT), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.GRASS_BLOCK), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.COARSE_DIRT), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.PODZOL), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.ROOTED_DIRT), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.MYCELIUM), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(ModBlocks.EARTH.get()), ModBlocks.BAUXITE_ORE.get().defaultBlockState())
                );
                // vein size around 33 like granite/diorite; adjust down slightly to 28
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 28));
            });

    // Configure wild flax patch - using RandomPatchConfiguration for flower-style patches
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_FLAX_PATCH = CONFIGURED_FEATURES.register("wild_flax_patch",
            () -> {
                ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_FLAX.get())));
                Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
                PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());
                Holder<PlacedFeature> placedHolder = Holder.direct(placed);
                return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(32, 6, 2, placedHolder));
            });

    // Configure wild squash patch
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_SQUASH_PATCH = CONFIGURED_FEATURES.register("wild_squash_patch",
            () -> {
                ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_SQUASH.get())));
                Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
                PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());
                Holder<PlacedFeature> placedHolder = Holder.direct(placed);
                return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(32, 6, 2, placedHolder));
            });

    // Configure wild beans patch
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_BEANS_PATCH = CONFIGURED_FEATURES.register("wild_beans_patch",
            () -> {
                ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_BEANS.get())));
                Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
                PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());
                Holder<PlacedFeature> placedHolder = Holder.direct(placed);
                return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(32, 6, 2, placedHolder));
            });

    // Configure wild peppers patch
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_PEPPERS_PATCH = CONFIGURED_FEATURES.register("wild_peppers_patch",
            () -> {
                ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_PEPPERS.get())));
                Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
                PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());
                Holder<PlacedFeature> placedHolder = Holder.direct(placed);
                return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(32, 6, 2, placedHolder));
            });

    // Configure wild corn patch
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_CORN_PATCH = CONFIGURED_FEATURES.register("wild_corn_patch",
            () -> {
                ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_CORN.get())));
                Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
                PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());
                Holder<PlacedFeature> placedHolder = Holder.direct(placed);
                return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(32, 6, 2, placedHolder));
            });



    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_RICE_PATCH = CONFIGURED_FEATURES.register("wild_rice_patch",
            () -> randomPatch(ModBlocks.WILD_RICE.get(), 32, 6, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_COTTON_PATCH = CONFIGURED_FEATURES.register("wild_cotton_patch",
            () -> randomPatch(ModBlocks.WILD_COTTON.get(), 32, 6, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ESPARTO_PATCH = CONFIGURED_FEATURES.register("esparto_patch",
            () -> randomPatch(ModBlocks.ESPARTO.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> AGAVE_PATCH = CONFIGURED_FEATURES.register("agave_patch",
            () -> randomPatch(ModBlocks.AGAVE.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> YUCCA_CLUSTER = CONFIGURED_FEATURES.register("yucca_cluster",
            () -> new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.YUCCA.get()))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> PLANTAIN_CLUSTER = CONFIGURED_FEATURES.register("plantain_cluster",
            () -> new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PLANTAIN.get()))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> WHITE_LILY_CLUSTER = CONFIGURED_FEATURES.register("white_lily_cluster",
            () -> new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WHITE_LILY.get()))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> BLUEBONNET_CLUSTER = CONFIGURED_FEATURES.register("bluebonnet_cluster",
            () -> new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BLUEBONNET.get()))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> PURPLE_CONEFLOWER_PATCH = CONFIGURED_FEATURES.register("purple_coneflower_patch",
            () -> randomPatch(ModBlocks.PURPLE_CONEFLOWER.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> FUCHSIA_PATCH = CONFIGURED_FEATURES.register("fuchsia_patch",
            () -> randomPatch(ModBlocks.FUCHSIA.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> MARIGOLD_PATCH = CONFIGURED_FEATURES.register("marigold_patch",
            () -> randomPatch(ModBlocks.MARIGOLD.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> HIBISCUS_PATCH = CONFIGURED_FEATURES.register("hibiscus_patch",
            () -> randomPatch(ModBlocks.HIBISCUS.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> LOTUS_PATCH = CONFIGURED_FEATURES.register("lotus_patch",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.LOTUS_WATER_PATCH_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> REEDS_CLUSTER = CONFIGURED_FEATURES.register("reeds_cluster",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.REED_CLUSTER_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> TARO_PATCH = CONFIGURED_FEATURES.register("taro_patch",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.TARO_PATCH_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> TEA_BUSH_PATCH = CONFIGURED_FEATURES.register("tea_bush_patch",
            () -> randomPatch(ModBlocks.TEA_BUSH.get(), 16, 4, 2));

    // Configure wild grape vine feature
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_GRAPE_VINE = CONFIGURED_FEATURES.register("wild_grape_vine",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>)ModFeatures.WILD_GRAPE_VINE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

    // Configure wild hops vine feature
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_HOPS_VINE = CONFIGURED_FEATURES.register("wild_hops_vine",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>)ModFeatures.WILD_HOPS_VINE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

    // Configure wild wisteria vine feature
    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_WISTERIA_VINE = CONFIGURED_FEATURES.register("wild_wisteria_vine",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>)ModFeatures.WILD_WISTERIA_VINE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

    // Indigo flower patch
    public static final RegistryObject<ConfiguredFeature<?, ?>> INDIGO_PATCH = CONFIGURED_FEATURES.register("indigo_patch",
            () -> {
                ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.INDIGO.get())));
                Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
                PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());
                Holder<PlacedFeature> placedHolder = Holder.direct(placed);
                return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(32, 6, 2, placedHolder));
            });

    // Murex shell feature - spawns on beaches with random directions
    public static final RegistryObject<ConfiguredFeature<?, ?>> MUREX_SHELL_FEATURE = CONFIGURED_FEATURES.register("murex_shell_feature",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.MUREX_SHELL_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    // Clam feature - spawns on beaches with random directions
    public static final RegistryObject<ConfiguredFeature<?, ?>> CLAM_FEATURE = CONFIGURED_FEATURES.register("clam_feature",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.CLAM_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    // Configure malachite ore generation - replaces surface copper ore
    public static final RegistryObject<ConfiguredFeature<?, ?>> MALACHITE_ORE = CONFIGURED_FEATURES.register("malachite_ore",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.MALACHITE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.MALACHITE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.COPPER_ORE), ModBlocks.MALACHITE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DEEPSLATE_COPPER_ORE), ModBlocks.MALACHITE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 10)); // larger surface veins
            });

    // Configure surface iron ore generation - replaces surface iron ore
    public static final RegistryObject<ConfiguredFeature<?, ?>> SURFACE_IRON_ORE = CONFIGURED_FEATURES.register("surface_iron_ore",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.IRON_ORE), ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DEEPSLATE_IRON_ORE), ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 9));
            });

    // Configure ochre clay deposits - replaces clay with ochre (rare)
    public static final RegistryObject<ConfiguredFeature<?, ?>> OCHRE_CLAY = CONFIGURED_FEATURES.register("ochre_clay",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.CLAY), ModBlocks.OCHRE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 48)); // very large clay patch replacement
            });

    // Configure red_ochre clay deposits - replaces clay with red ochre (rarer)
    public static final RegistryObject<ConfiguredFeature<?, ?>> RED_OCHRE_CLAY = CONFIGURED_FEATURES.register("red_ochre_clay",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.CLAY), ModBlocks.RED_OCHRE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 33));
            });

    // Configure magnetite ore generation - emerald-like rarity, mountain biased
    public static final RegistryObject<ConfiguredFeature<?, ?>> MAGNETITE_ORE = CONFIGURED_FEATURES.register("magnetite_ore",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.MAGNETITE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.MAGNETITE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 5)); // Slightly larger, still small
            });

    // Configure sphalerite (zinc ore) generation - common near surface and in caves
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPHALERITE_ORE = CONFIGURED_FEATURES.register("sphalerite_ore",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.SPHALERITE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.SPHALERITE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 8)); // Medium-sized veins
            });

    // Configure sulfur ore generation - spawns near lava and in tuff veins
    public static final RegistryObject<ConfiguredFeature<?, ?>> SULFUR_ORE = CONFIGURED_FEATURES.register("sulfur_ore",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.SULFUR_ORE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_SULFUR_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.TUFF), ModBlocks.TUFF_SULFUR_ORE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 6)); // Medium-small veins
            });

    // Configure rubber tree generation - tall birch/small jungle style
    public static final RegistryObject<ConfiguredFeature<?, ?>> RUBBER_TREE = CONFIGURED_FEATURES.register("rubber_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.RUBBER_WOOD_LOG.get()),  // trunk
                    new StraightTrunkPlacer(6, 3, 2),                           // base height 6, random 0-3, random 0-2 for top
                    BlockStateProvider.simple(ModBlocks.RUBBER_TREE_LEAVES.get()), // leaves
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), // radius 2, offset 0, height 3
                    new TwoLayersFeatureSize(1, 0, 1)                           // trunk layers
            ).ignoreVines()
             .dirt(BlockStateProvider.simple(Blocks.DIRT)) // Specify dirt requirement
             .build()));

    // Configure olive tree generation - shorter than rubber trees, more like apple trees
    public static final RegistryObject<ConfiguredFeature<?, ?>> OLIVE_TREE = CONFIGURED_FEATURES.register("olive_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.OLIVE_TREE_LOG.get()),  // trunk
                    new StraightTrunkPlacer(4, 2, 1),                          // base height 4, random 0-2, random 0-1 for top
                    BlockStateProvider.simple(ModBlocks.OLIVE_TREE_LEAVES.get()), // leaves
                    new OliveFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2), // radius 2, offset 0, height 2
                    new TwoLayersFeatureSize(1, 0, 1)                           // trunk layers
            ).ignoreVines()
             .dirt(BlockStateProvider.simple(Blocks.DIRT)) // Specify dirt requirement
             .build()));


    public static final RegistryObject<ConfiguredFeature<?, ?>> PALM_TREE = CONFIGURED_FEATURES.register("palm_tree",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.PALM_TREE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> CYPRESS_TREE = CONFIGURED_FEATURES.register("cypress_tree",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.CYPRESS_TREE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> BAOBAB_TREE = CONFIGURED_FEATURES.register("baobab_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.BAOBAB_LOG.get()),
                    new GiantTrunkPlacer(10, 4, 2),
                    BlockStateProvider.simple(ModBlocks.BAOBAB_LEAVES.get()),
                    new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 4),
                    new TwoLayersFeatureSize(2, 1, 0)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> MAPLE_TREE = CONFIGURED_FEATURES.register("maple_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.MAPLE_LOG.get()),
                    new StraightTrunkPlacer(4, 2, 0),
                    BlockStateProvider.simple(ModBlocks.MAPLE_LEAVES.get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> FIG_TREE = CONFIGURED_FEATURES.register("fig_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.FIG_LOG.get()),
                    new StraightTrunkPlacer(3, 2, 1),
                    BlockStateProvider.simple(ModBlocks.FIG_LEAVES.get()),
                    new FigFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> CEDAR_TREE = CONFIGURED_FEATURES.register("cedar_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.CEDAR_LOG.get()),
                    new StraightTrunkPlacer(10, 5, 3),
                    BlockStateProvider.simple(ModBlocks.CEDAR_LEAVES.get()),
                    new SpruceFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(1)),
                    new TwoLayersFeatureSize(2, 1, 4)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> CEDAR_MEGA_TREE = CONFIGURED_FEATURES.register("cedar_mega_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.CEDAR_LOG.get()),
                    new GiantTrunkPlacer(25, 10, 5),
                    BlockStateProvider.simple(ModBlocks.CEDAR_LEAVES.get()),
                    new SpruceFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(10)),
                    new TwoLayersFeatureSize(3, 2, 4)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> EUCALYPTUS_TREE = CONFIGURED_FEATURES.register("eucalyptus_tree",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_TREE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> RAINBOW_EUCALYPTUS_TREE = CONFIGURED_FEATURES.register("rainbow_eucalyptus_tree",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.RAINBOW_EUCALYPTUS_TREE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> EUCALYPTUS_GROVE = CONFIGURED_FEATURES.register("eucalyptus_grove",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_GROVE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> RAINBOW_EUCALYPTUS_GROVE = CONFIGURED_FEATURES.register("rainbow_eucalyptus_grove",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.RAINBOW_EUCALYPTUS_GROVE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));


    // Very small marble veins - ultra-safe size, no chunk boundary issues possible
    public static final RegistryObject<ConfiguredFeature<?, ?>> MARBLE_VEIN = CONFIGURED_FEATURES.register("marble_vein",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.MARBLE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.MARBLE.get().defaultBlockState())
                );
                // Very small size - similar to vanilla ores, absolutely safe
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 64));
            });

    // Very small limestone veins - ultra-safe size, no chunk boundary issues possible
    public static final RegistryObject<ConfiguredFeature<?, ?>> LIMESTONE_VEIN = CONFIGURED_FEATURES.register("limestone_vein",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.LIMESTONE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.LIMESTONE.get().defaultBlockState())
                );
                // Very small size - similar to vanilla ores, absolutely safe
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 64));
            });

    // Large test marble veins for debugging - still substantial
    public static final RegistryObject<ConfiguredFeature<?, ?>> MARBLE_TEST = CONFIGURED_FEATURES.register("marble_test",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.MARBLE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.MARBLE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 80)); // Large test size
            });

    // Large test limestone veins for debugging - still substantial
    public static final RegistryObject<ConfiguredFeature<?, ?>> LIMESTONE_TEST = CONFIGURED_FEATURES.register("limestone_test",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.LIMESTONE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.LIMESTONE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 80)); // Large test size
            });

    // Limestone surface replacement - creates limestone cliff/mining biomes (very safe size)
    public static final RegistryObject<ConfiguredFeature<?, ?>> LIMESTONE_SURFACE_BIOME = CONFIGURED_FEATURES.register("limestone_surface_biome",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.LIMESTONE.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.LIMESTONE.get().defaultBlockState())
                );
                // Very safe surface replacement - similar to vanilla ore sizes
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 128));
            });

    // Configure saltpeter sand generation - replaces sand in desert biomes
    public static final RegistryObject<ConfiguredFeature<?, ?>> SALTPETER_SAND = CONFIGURED_FEATURES.register("saltpeter_sand",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.SAND), ModBlocks.SALTPETER_SAND.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.RED_SAND), ModBlocks.SALTPETER_SAND.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 12)); // Medium-sized patches
            });

    // Configure saltpeter sandstone generation - replaces sandstone in desert biomes
    public static final RegistryObject<ConfiguredFeature<?, ?>> SALTPETER_SANDSTONE = CONFIGURED_FEATURES.register("saltpeter_sandstone",
            () -> {
                List<OreConfiguration.TargetBlockState> targets = List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.SANDSTONE), ModBlocks.SALTPETER_SANDSTONE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.RED_SANDSTONE), ModBlocks.SALTPETER_SANDSTONE.get().defaultBlockState())
                );
                return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 8)); // Smaller patches for sandstone
            });

    
    private static java.util.List<PlacementModifier> patchBlockPlacement() {
        return java.util.List.of(BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE));
    }

    private static ConfiguredFeature<RandomPatchConfiguration, ?> randomPatch(net.minecraft.world.level.block.Block block,
                                                                                int tries, int xz, int y) {
        ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(block)));
        Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
        PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());
        Holder<PlacedFeature> placedHolder = Holder.direct(placed);
        return new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(tries, xz, y, placedHolder));
    }

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
} 