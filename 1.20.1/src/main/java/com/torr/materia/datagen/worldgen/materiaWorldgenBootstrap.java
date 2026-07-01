package com.torr.materia.datagen.worldgen;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.world.feature.CypressFoliagePlacer;
import com.torr.materia.world.feature.FigFoliagePlacer;
import com.torr.materia.world.feature.ModFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

/**
 * 1.20.1+ worldgen is datapack-driven. Our existing biome modifiers reference a bunch of
 * materia:*_placed entries, so we must ship matching placed_feature JSONs (and their configured
 * features) or world creation will crash during registry freeze.
 *
 * This class defines the built-in (datapack) configured + placed features for 1.20.1+.
 */
public final class materiaWorldgenBootstrap {
    private materiaWorldgenBootstrap() {}

    // Configured feature keys (match JSON ids)
    private static final ResourceKey<ConfiguredFeature<?, ?>> GRAVEL_TIN_ORE = configured("gravel_tin_ore");
    private static final ResourceKey<ConfiguredFeature<?, ?>> EARTH_UNDERSOIL = configured("earth_undersoil");
    private static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_EARTH = configured("surface_earth");
    private static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_ROCK = configured("surface_rock");
    private static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_ROCK = configured("cave_rock");
    private static final ResourceKey<ConfiguredFeature<?, ?>> BAUXITE_PATCH = configured("bauxite_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> BAUXITE_ORE_BLOB = configured("bauxite_ore_blob");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_FLAX_PATCH = configured("wild_flax_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_SQUASH_PATCH = configured("wild_squash_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_BEANS_PATCH = configured("wild_beans_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_PEPPERS_PATCH = configured("wild_peppers_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_CORN_PATCH = configured("wild_corn_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_GRAPE_VINE = configured("wild_grape_vine");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_WISTERIA_VINE = configured("wild_wisteria_vine");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_HOPS_VINE = configured("wild_hops_vine");
    private static final ResourceKey<ConfiguredFeature<?, ?>> INDIGO_PATCH = configured("indigo_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> MUREX_SHELL_FEATURE = configured("murex_shell_feature");
    private static final ResourceKey<ConfiguredFeature<?, ?>> CLAM_FEATURE = configured("clam_feature");
    private static final ResourceKey<ConfiguredFeature<?, ?>> MALACHITE_ORE = configured("malachite_ore");
    private static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_IRON_ORE = configured("surface_iron_ore");
    private static final ResourceKey<ConfiguredFeature<?, ?>> OCHRE_CLAY = configured("ochre_clay");
    private static final ResourceKey<ConfiguredFeature<?, ?>> RED_OCHRE_CLAY = configured("red_ochre_clay");
    private static final ResourceKey<ConfiguredFeature<?, ?>> MAGNETITE_ORE = configured("magnetite_ore");
    private static final ResourceKey<ConfiguredFeature<?, ?>> SPHALERITE_ORE = configured("sphalerite_ore");
    private static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_ORE = configured("sulfur_ore");
    private static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_TREE = configured("rubber_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> OLIVE_TREE = configured("olive_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> MARBLE_VEIN = configured("marble_vein");
    private static final ResourceKey<ConfiguredFeature<?, ?>> LIMESTONE_VEIN = configured("limestone_vein");
    private static final ResourceKey<ConfiguredFeature<?, ?>> MARBLE_TEST = configured("marble_test");
    private static final ResourceKey<ConfiguredFeature<?, ?>> LIMESTONE_TEST = configured("limestone_test");
    private static final ResourceKey<ConfiguredFeature<?, ?>> LIMESTONE_SURFACE_BIOME = configured("limestone_surface_biome");
    private static final ResourceKey<ConfiguredFeature<?, ?>> SALTPETER_SAND = configured("saltpeter_sand");
    private static final ResourceKey<ConfiguredFeature<?, ?>> SALTPETER_SANDSTONE = configured("saltpeter_sandstone");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_RICE_PATCH = configured("wild_rice_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_COTTON_PATCH = configured("wild_cotton_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> ESPARTO_PATCH = configured("esparto_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> TEA_BUSH_PATCH = configured("tea_bush_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE = configured("palm_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> CYPRESS_TREE = configured("cypress_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> BAOBAB_TREE = configured("baobab_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_TREE = configured("maple_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> FIG_TREE = configured("fig_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_TREE = configured("cedar_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_MEGA_TREE = configured("cedar_mega_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> EUCALYPTUS_TREE = configured("eucalyptus_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_EUCALYPTUS_TREE = configured("rainbow_eucalyptus_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> EUCALYPTUS_GROVE = configured("eucalyptus_grove");
    private static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_EUCALYPTUS_GROVE = configured("rainbow_eucalyptus_grove");
    private static final ResourceKey<ConfiguredFeature<?, ?>> AGAVE_PATCH = configured("agave_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> YUCCA_CLUSTER = configured("yucca_cluster");
    private static final ResourceKey<ConfiguredFeature<?, ?>> PLANTAIN_CLUSTER = configured("plantain_cluster");
    private static final ResourceKey<ConfiguredFeature<?, ?>> WHITE_LILY_CLUSTER = configured("white_lily_cluster");
    private static final ResourceKey<ConfiguredFeature<?, ?>> BLUEBONNET_CLUSTER = configured("bluebonnet_cluster");
    private static final ResourceKey<ConfiguredFeature<?, ?>> PURPLE_CONEFLOWER_PATCH = configured("purple_coneflower_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> FUCHSIA_PATCH = configured("fuchsia_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> MARIGOLD_PATCH = configured("marigold_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> HIBISCUS_PATCH = configured("hibiscus_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> LOTUS_PATCH = configured("lotus_patch");
    private static final ResourceKey<ConfiguredFeature<?, ?>> REEDS_CLUSTER = configured("reeds_cluster");
    private static final ResourceKey<ConfiguredFeature<?, ?>> TARO_PATCH = configured("taro_patch");

    // Placed feature keys (match JSON ids)
    private static final ResourceKey<PlacedFeature> GRAVEL_TIN_ORE_PLACED = placed("gravel_tin_ore_placed");
    private static final ResourceKey<PlacedFeature> GRAVEL_TIN_ORE_RIVER_PLACED = placed("gravel_tin_ore_river_placed");
    private static final ResourceKey<PlacedFeature> EARTH_SUBSOIL_PLACED = placed("earth_subsoil_placed");
    private static final ResourceKey<PlacedFeature> SURFACE_EARTH_RIVER_PLACED = placed("surface_earth_river_placed");
    private static final ResourceKey<PlacedFeature> SURFACE_ROCK_PLACED = placed("surface_rock_placed");
    private static final ResourceKey<PlacedFeature> SURFACE_ROCK_ROCKY_PLACED = placed("surface_rock_rocky_placed");
    private static final ResourceKey<PlacedFeature> BAUXITE_PATCH_PLACED = placed("bauxite_patch_placed");
    private static final ResourceKey<PlacedFeature> BAUXITE_PATCH_ON_ORE_PLACED = placed("bauxite_patch_on_ore_placed");
    private static final ResourceKey<PlacedFeature> BAUXITE_ORE_BLOB_PLACED = placed("bauxite_ore_blob_placed");
    private static final ResourceKey<PlacedFeature> CAVE_ROCK_PLACED = placed("cave_rock_placed");
    private static final ResourceKey<PlacedFeature> CAVE_ROCK_ROCKY_PLACED = placed("cave_rock_rocky_placed");
    private static final ResourceKey<PlacedFeature> WILD_FLAX_PLACED = placed("wild_flax_placed");
    private static final ResourceKey<PlacedFeature> WILD_SQUASH_PLACED = placed("wild_squash_placed");
    private static final ResourceKey<PlacedFeature> WILD_BEANS_PLACED = placed("wild_beans_placed");
    private static final ResourceKey<PlacedFeature> WILD_PEPPERS_PLACED = placed("wild_peppers_placed");
    private static final ResourceKey<PlacedFeature> WILD_CORN_PLACED = placed("wild_corn_placed");
    private static final ResourceKey<PlacedFeature> INDIGO_PLACED = placed("indigo_placed");
    private static final ResourceKey<PlacedFeature> WILD_GRAPE_VINE_PLACED = placed("wild_grape_vine_placed");
    private static final ResourceKey<PlacedFeature> WILD_WISTERIA_VINE_PLACED = placed("wild_wisteria_vine_placed");
    private static final ResourceKey<PlacedFeature> WILD_HOPS_VINE_PLACED = placed("wild_hops_vine_placed");
    private static final ResourceKey<PlacedFeature> MUREX_SHELL_PLACED = placed("murex_shell_placed");
    private static final ResourceKey<PlacedFeature> CLAM_PLACED = placed("clam_placed");
    private static final ResourceKey<PlacedFeature> MALACHITE_ORE_PLACED = placed("malachite_ore_placed");
    private static final ResourceKey<PlacedFeature> SURFACE_IRON_ORE_PLACED = placed("surface_iron_ore_placed");
    private static final ResourceKey<PlacedFeature> OCHRE_CLAY_PLACED = placed("ochre_clay_placed");
    private static final ResourceKey<PlacedFeature> RED_OCHRE_CLAY_PLACED = placed("red_ochre_clay_placed");
    private static final ResourceKey<PlacedFeature> MAGNETITE_ORE_PLACED = placed("magnetite_ore_placed");
    private static final ResourceKey<PlacedFeature> MAGNETITE_ORE_ROCKY_PLACED = placed("magnetite_ore_rocky_placed");
    private static final ResourceKey<PlacedFeature> SPHALERITE_ORE_PLACED = placed("sphalerite_ore_placed");
    private static final ResourceKey<PlacedFeature> SPHALERITE_CAVE_PLACED = placed("sphalerite_cave_placed");
    private static final ResourceKey<PlacedFeature> SULFUR_ORE_PLACED = placed("sulfur_ore_placed");
    private static final ResourceKey<PlacedFeature> SULFUR_ORE_LAVA_PLACED = placed("sulfur_ore_lava_placed");
    private static final ResourceKey<PlacedFeature> RUBBER_TREE_PLACED = placed("rubber_tree_placed");
    private static final ResourceKey<PlacedFeature> OLIVE_TREE_PLACED = placed("olive_tree_placed");
    private static final ResourceKey<PlacedFeature> MARBLE_VEIN_RARE_PLACED = placed("marble_vein_rare_placed");
    private static final ResourceKey<PlacedFeature> MARBLE_VEIN_SURFACE_PLACED = placed("marble_vein_surface_placed");
    private static final ResourceKey<PlacedFeature> LIMESTONE_VEIN_RARE_PLACED = placed("limestone_vein_rare_placed");
    private static final ResourceKey<PlacedFeature> MARBLE_TEST_PLACED = placed("marble_test_placed");
    private static final ResourceKey<PlacedFeature> LIMESTONE_TEST_PLACED = placed("limestone_test_placed");
    private static final ResourceKey<PlacedFeature> LIMESTONE_SURFACE_BIOME_PLACED = placed("limestone_surface_biome_placed");
    private static final ResourceKey<PlacedFeature> SALTPETER_SAND_PLACED = placed("saltpeter_sand_placed");
    private static final ResourceKey<PlacedFeature> SALTPETER_SANDSTONE_PLACED = placed("saltpeter_sandstone_placed");
    private static final ResourceKey<PlacedFeature> WILD_RICE_PLACED = placed("wild_rice_placed");
    private static final ResourceKey<PlacedFeature> WILD_COTTON_PLACED = placed("wild_cotton_placed");
    private static final ResourceKey<PlacedFeature> ESPARTO_PLACED = placed("esparto_placed");
    private static final ResourceKey<PlacedFeature> TEA_BUSH_PLACED = placed("tea_bush_placed");
    private static final ResourceKey<PlacedFeature> PALM_TREE_PLACED = placed("palm_tree_placed");
    private static final ResourceKey<PlacedFeature> CYPRESS_TREE_PLACED = placed("cypress_tree_placed");
    private static final ResourceKey<PlacedFeature> BAOBAB_TREE_PLACED = placed("baobab_tree_placed");
    private static final ResourceKey<PlacedFeature> MAPLE_TREE_PLACED = placed("maple_tree_placed");
    private static final ResourceKey<PlacedFeature> FIG_TREE_PLACED = placed("fig_tree_placed");
    private static final ResourceKey<PlacedFeature> CEDAR_TREE_PLACED = placed("cedar_tree_placed");
    private static final ResourceKey<PlacedFeature> EUCALYPTUS_GROVE_PLACED = placed("eucalyptus_grove_placed");
    private static final ResourceKey<PlacedFeature> RAINBOW_EUCALYPTUS_GROVE_PLACED = placed("rainbow_eucalyptus_grove_placed");
    private static final ResourceKey<PlacedFeature> AGAVE_PLACED = placed("agave_placed");
    private static final ResourceKey<PlacedFeature> YUCCA_PLACED = placed("yucca_placed");
    private static final ResourceKey<PlacedFeature> PLANTAIN_PLACED = placed("plantain_placed");
    private static final ResourceKey<PlacedFeature> WHITE_LILY_PLACED = placed("white_lily_placed");
    private static final ResourceKey<PlacedFeature> BLUEBONNET_PLACED = placed("bluebonnet_placed");
    private static final ResourceKey<PlacedFeature> PURPLE_CONEFLOWER_PLACED = placed("purple_coneflower_placed");
    private static final ResourceKey<PlacedFeature> FUCHSIA_PLACED = placed("fuchsia_placed");
    private static final ResourceKey<PlacedFeature> MARIGOLD_PLACED = placed("marigold_placed");
    private static final ResourceKey<PlacedFeature> HIBISCUS_PLACED = placed("hibiscus_placed");
    private static final ResourceKey<PlacedFeature> LOTUS_PLACED = placed("lotus_placed");
    private static final ResourceKey<PlacedFeature> REEDS_PLACED = placed("reeds_placed");
    private static final ResourceKey<PlacedFeature> TARO_CROP_PLACED = placed("taro_crop_placed");

    public static void bootstrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // gravel_tin_ore
        context.register(GRAVEL_TIN_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.GRAVEL_TIN.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.GRAVEL_TIN.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DIRT), ModBlocks.GRAVEL_TIN.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.GRAVEL), ModBlocks.GRAVEL_TIN.get().defaultBlockState())
                ),
                18
        )));

        // earth_undersoil
        context.register(EARTH_UNDERSOIL, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.DIRT), ModBlocks.EARTH.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.COARSE_DIRT), ModBlocks.EARTH.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.ROOTED_DIRT), ModBlocks.EARTH.get().defaultBlockState())
                ),
                33
        )));

        context.register(SURFACE_EARTH, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.EARTH.get()))));

        context.register(SURFACE_ROCK, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ROCK.get()))));

        context.register(CAVE_ROCK, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ROCK.get()))));

        // bauxite_patch (random patch over simple block)
        {
            ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BAUXITE.get())));
            Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
            PlacedFeature placed = new PlacedFeature(simpleHolder, List.of());
            Holder<PlacedFeature> placedHolder = Holder.direct(placed);
            context.register(BAUXITE_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                    new RandomPatchConfiguration(6, 4, 0, placedHolder)));
        }

        context.register(BAUXITE_ORE_BLOB, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DIRT), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.GRASS_BLOCK), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.COARSE_DIRT), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.PODZOL), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.ROOTED_DIRT), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.MYCELIUM), ModBlocks.BAUXITE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(ModBlocks.EARTH.get()), ModBlocks.BAUXITE_ORE.get().defaultBlockState())
                ),
                28
        )));

        // Wild crop patches
        context.register(WILD_FLAX_PATCH, randomPatch(ModBlocks.WILD_FLAX.get().defaultBlockState(), 32, 6, 2));
        context.register(WILD_SQUASH_PATCH, randomPatch(ModBlocks.WILD_SQUASH.get().defaultBlockState(), 32, 6, 2));
        context.register(WILD_BEANS_PATCH, randomPatch(ModBlocks.WILD_BEANS.get().defaultBlockState(), 32, 6, 2));
        context.register(WILD_PEPPERS_PATCH, randomPatch(ModBlocks.WILD_PEPPERS.get().defaultBlockState(), 32, 6, 2));
        context.register(WILD_CORN_PATCH, randomPatch(ModBlocks.WILD_CORN.get().defaultBlockState(), 32, 6, 2));
        context.register(WILD_RICE_PATCH, randomPatch(ModBlocks.WILD_RICE.get().defaultBlockState(), 32, 6, 2));
        context.register(WILD_COTTON_PATCH, randomPatch(ModBlocks.WILD_COTTON.get().defaultBlockState(), 32, 6, 2));
        context.register(ESPARTO_PATCH, randomPatch(ModBlocks.ESPARTO.get().defaultBlockState(), 24, 5, 2));
        context.register(AGAVE_PATCH, randomPatch(ModBlocks.AGAVE.get().defaultBlockState(), 24, 5, 2));
        context.register(YUCCA_CLUSTER, new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.YUCCA.get()))));
        context.register(PLANTAIN_CLUSTER, new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PLANTAIN.get()))));
        context.register(WHITE_LILY_CLUSTER, new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WHITE_LILY.get()))));
        context.register(BLUEBONNET_CLUSTER, new ConfiguredFeature<>((Feature<SimpleBlockConfiguration>) ModFeatures.TALL_PLANT_CLUSTER_FEATURE.get(),
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BLUEBONNET.get()))));
        context.register(PURPLE_CONEFLOWER_PATCH, randomPatch(ModBlocks.PURPLE_CONEFLOWER.get().defaultBlockState(), 24, 5, 2));
        context.register(FUCHSIA_PATCH, randomPatch(ModBlocks.FUCHSIA.get().defaultBlockState(), 24, 5, 2));
        context.register(MARIGOLD_PATCH, randomPatch(ModBlocks.MARIGOLD.get().defaultBlockState(), 24, 5, 2));
        context.register(HIBISCUS_PATCH, randomPatch(ModBlocks.HIBISCUS.get().defaultBlockState(), 24, 5, 2));
        context.register(LOTUS_PATCH, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.LOTUS_WATER_PATCH_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(REEDS_CLUSTER, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.REED_CLUSTER_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(TARO_PATCH, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.TARO_PATCH_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(TEA_BUSH_PATCH, randomPatch(ModBlocks.TEA_BUSH.get().defaultBlockState(), 16, 4, 2));


        context.register(WILD_GRAPE_VINE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.WILD_GRAPE_VINE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(WILD_WISTERIA_VINE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.WILD_WISTERIA_VINE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(WILD_HOPS_VINE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.WILD_HOPS_VINE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

        context.register(INDIGO_PATCH, randomPatch(ModBlocks.INDIGO.get().defaultBlockState(), 32, 6, 2));

        context.register(MUREX_SHELL_FEATURE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.MUREX_SHELL_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(CLAM_FEATURE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.CLAM_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

        context.register(MALACHITE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.MALACHITE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.MALACHITE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.COPPER_ORE), ModBlocks.MALACHITE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DEEPSLATE_COPPER_ORE), ModBlocks.MALACHITE.get().defaultBlockState())
                ),
                10
        )));

        context.register(SURFACE_IRON_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.IRON_ORE), ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.DEEPSLATE_IRON_ORE), ModBlocks.SURFACE_IRON_ORE.get().defaultBlockState())
                ),
                9
        )));

        context.register(OCHRE_CLAY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(OreConfiguration.target(new BlockMatchTest(Blocks.CLAY), ModBlocks.OCHRE.get().defaultBlockState())),
                48
        )));

        context.register(RED_OCHRE_CLAY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(OreConfiguration.target(new BlockMatchTest(Blocks.CLAY), ModBlocks.RED_OCHRE.get().defaultBlockState())),
                33
        )));

        context.register(MAGNETITE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.MAGNETITE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.MAGNETITE.get().defaultBlockState())
                ),
                5
        )));

        context.register(SPHALERITE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.SPHALERITE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.SPHALERITE.get().defaultBlockState())
                ),
                8
        )));

        context.register(SULFUR_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.SULFUR_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.DEEPSLATE_SULFUR_ORE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.TUFF), ModBlocks.TUFF_SULFUR_ORE.get().defaultBlockState())
                ),
                6
        )));

        context.register(RUBBER_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.RUBBER_WOOD_LOG.get()),
                new StraightTrunkPlacer(6, 3, 2),
                BlockStateProvider.simple(ModBlocks.RUBBER_TREE_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));


        context.register(PALM_TREE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.PALM_TREE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

        context.register(CYPRESS_TREE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.CYPRESS_TREE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

        context.register(BAOBAB_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BAOBAB_LOG.get()),
                new net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer(10, 4, 2),
                BlockStateProvider.simple(ModBlocks.BAOBAB_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 4),
                new TwoLayersFeatureSize(2, 1, 0)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(MAPLE_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.MAPLE_LOG.get()),
                new StraightTrunkPlacer(4, 2, 0),
                BlockStateProvider.simple(ModBlocks.MAPLE_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(FIG_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.FIG_LOG.get()),
                new StraightTrunkPlacer(3, 2, 1),
                BlockStateProvider.simple(ModBlocks.FIG_LEAVES.get()),
                new FigFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(CEDAR_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.CEDAR_LOG.get()),
                new StraightTrunkPlacer(10, 5, 3),
                BlockStateProvider.simple(ModBlocks.CEDAR_LEAVES.get()),
                new SpruceFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(1)),
                new TwoLayersFeatureSize(2, 1, 4)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(CEDAR_MEGA_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.CEDAR_LOG.get()),
                new GiantTrunkPlacer(25, 10, 5),
                BlockStateProvider.simple(ModBlocks.CEDAR_LEAVES.get()),
                new SpruceFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(10)),
                new TwoLayersFeatureSize(3, 2, 4)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(EUCALYPTUS_TREE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_TREE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(RAINBOW_EUCALYPTUS_TREE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.RAINBOW_EUCALYPTUS_TREE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(EUCALYPTUS_GROVE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_GROVE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(RAINBOW_EUCALYPTUS_GROVE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.RAINBOW_EUCALYPTUS_GROVE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

        context.register(OLIVE_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.OLIVE_TREE_LOG.get()),
                new StraightTrunkPlacer(4, 2, 1),
                BlockStateProvider.simple(ModBlocks.OLIVE_TREE_LEAVES.get()),
                // Keep using vanilla blob foliage for JSON stability; custom foliage placer remains registered for later.
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(MARBLE_VEIN, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.MARBLE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.MARBLE.get().defaultBlockState())
                ),
                64
        )));

        context.register(LIMESTONE_VEIN, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.LIMESTONE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.LIMESTONE.get().defaultBlockState())
                ),
                64
        )));

        context.register(MARBLE_TEST, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.MARBLE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.MARBLE.get().defaultBlockState())
                ),
                64
        )));

        context.register(LIMESTONE_TEST, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.LIMESTONE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.LIMESTONE.get().defaultBlockState())
                ),
                64
        )));

        context.register(LIMESTONE_SURFACE_BIOME, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.LIMESTONE.get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.LIMESTONE.get().defaultBlockState())
                ),
                64
        )));

        context.register(SALTPETER_SAND, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.SAND), ModBlocks.SALTPETER_SAND.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.RED_SAND), ModBlocks.SALTPETER_SAND.get().defaultBlockState())
                ),
                12
        )));

        context.register(SALTPETER_SANDSTONE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.SANDSTONE), ModBlocks.SALTPETER_SANDSTONE.get().defaultBlockState()),
                        OreConfiguration.target(new BlockMatchTest(Blocks.RED_SANDSTONE), ModBlocks.SALTPETER_SANDSTONE.get().defaultBlockState())
                ),
                8
        )));
    }

    public static void bootstrapPlacedFeatures(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(GRAVEL_TIN_ORE_PLACED, new PlacedFeature(configured.getOrThrow(GRAVEL_TIN_ORE),
                commonOrePlacement(1, HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(80)))));

        context.register(GRAVEL_TIN_ORE_RIVER_PLACED, new PlacedFeature(configured.getOrThrow(GRAVEL_TIN_ORE),
                commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(90)))));

        context.register(EARTH_SUBSOIL_PLACED, new PlacedFeature(configured.getOrThrow(EARTH_UNDERSOIL),
                List.of(
                        CountPlacement.of(64),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                        RandomOffsetPlacement.vertical(net.minecraft.util.valueproviders.UniformInt.of(-6, -1)),
                        BiomeFilter.biome()
                )));

        context.register(SURFACE_EARTH_RIVER_PLACED, new PlacedFeature(configured.getOrThrow(SURFACE_EARTH),
                List.of(
                        RarityFilter.onAverageOnceEvery(4),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                        BiomeFilter.biome()
                )));

        context.register(SURFACE_ROCK_PLACED, new PlacedFeature(configured.getOrThrow(SURFACE_ROCK),
                List.of(
                        RarityFilter.onAverageOnceEvery(8),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                )));

        context.register(SURFACE_ROCK_ROCKY_PLACED, new PlacedFeature(configured.getOrThrow(SURFACE_ROCK),
                List.of(
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                )));

        context.register(BAUXITE_PATCH_PLACED, new PlacedFeature(configured.getOrThrow(BAUXITE_PATCH),
                List.of(
                        RarityFilter.onAverageOnceEvery(16),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                BlockPredicate.wouldSurvive(ModBlocks.BAUXITE.get().defaultBlockState(), BlockPos.ZERO),
                                BlockPredicate.not(BlockPredicate.matchesBlocks(BlockPos.ZERO, List.of(
                                        Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                BlockPredicate.not(BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), List.of(
                                        Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                BlockPredicate.not(BlockPredicate.matchesTag(new BlockPos(0, -1, 0), BlockTags.LEAVES))
                        )),
                        BiomeFilter.biome()
                )));

        context.register(BAUXITE_PATCH_ON_ORE_PLACED, new PlacedFeature(configured.getOrThrow(BAUXITE_PATCH),
                List.of(
                        RarityFilter.onAverageOnceEvery(8),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                        BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                BlockPredicate.not(BlockPredicate.matchesBlocks(BlockPos.ZERO, List.of(
                                        Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                BlockPredicate.not(BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), List.of(
                                        Blocks.WATER, Blocks.KELP, Blocks.KELP_PLANT, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS))),
                                BlockPredicate.wouldSurvive(ModBlocks.BAUXITE.get().defaultBlockState(), BlockPos.ZERO),
                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), List.of(ModBlocks.BAUXITE_ORE.get()))
                        )),
                        BiomeFilter.biome()
                )));

        context.register(BAUXITE_ORE_BLOB_PLACED, new PlacedFeature(configured.getOrThrow(BAUXITE_ORE_BLOB),
                List.of(
                        RarityFilter.onAverageOnceEvery(18),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(58), VerticalAnchor.absolute(120)),
                        BiomeFilter.biome()
                )));

        context.register(CAVE_ROCK_PLACED, new PlacedFeature(configured.getOrThrow(CAVE_ROCK),
                List.of(
                        CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(50)),
                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                        RandomOffsetPlacement.vertical(net.minecraft.util.valueproviders.ConstantInt.of(1)),
                        BiomeFilter.biome()
                )));

        context.register(CAVE_ROCK_ROCKY_PLACED, new PlacedFeature(configured.getOrThrow(CAVE_ROCK),
                List.of(
                        CountPlacement.of(15),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(50)),
                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                        RandomOffsetPlacement.vertical(net.minecraft.util.valueproviders.ConstantInt.of(1)),
                        BiomeFilter.biome()
                )));

        // Wild crops
        context.register(WILD_FLAX_PLACED, new PlacedFeature(configured.getOrThrow(WILD_FLAX_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(WILD_SQUASH_PLACED, new PlacedFeature(configured.getOrThrow(WILD_SQUASH_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(WILD_BEANS_PLACED, new PlacedFeature(configured.getOrThrow(WILD_BEANS_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(WILD_PEPPERS_PLACED, new PlacedFeature(configured.getOrThrow(WILD_PEPPERS_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));

        context.register(WILD_RICE_PLACED, new PlacedFeature(configured.getOrThrow(WILD_RICE_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(WILD_COTTON_PLACED, new PlacedFeature(configured.getOrThrow(WILD_COTTON_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(48), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(ESPARTO_PLACED, new PlacedFeature(configured.getOrThrow(ESPARTO_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(TEA_BUSH_PLACED, new PlacedFeature(configured.getOrThrow(TEA_BUSH_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(36), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));

        context.register(WILD_CORN_PLACED, new PlacedFeature(configured.getOrThrow(WILD_CORN_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));

        context.register(INDIGO_PLACED, new PlacedFeature(configured.getOrThrow(INDIGO_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));

        context.register(WILD_GRAPE_VINE_PLACED, new PlacedFeature(configured.getOrThrow(WILD_GRAPE_VINE),
                List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(WILD_WISTERIA_VINE_PLACED, new PlacedFeature(configured.getOrThrow(WILD_WISTERIA_VINE),
                List.of(RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(WILD_HOPS_VINE_PLACED, new PlacedFeature(configured.getOrThrow(WILD_HOPS_VINE),
                List.of(RarityFilter.onAverageOnceEvery(6), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));

        context.register(MUREX_SHELL_PLACED, new PlacedFeature(configured.getOrThrow(MUREX_SHELL_FEATURE),
                List.of(RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(CLAM_PLACED, new PlacedFeature(configured.getOrThrow(CLAM_FEATURE),
                List.of(RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));

        context.register(MALACHITE_ORE_PLACED, new PlacedFeature(configured.getOrThrow(MALACHITE_ORE),
                List.of(
                        CountPlacement.of(10),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(120)),
                        EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.ONLY_IN_AIR_PREDICATE, 1),
                        BiomeFilter.biome()
                )));

        context.register(SURFACE_IRON_ORE_PLACED, new PlacedFeature(configured.getOrThrow(SURFACE_IRON_ORE),
                List.of(
                        CountPlacement.of(12),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(42), VerticalAnchor.absolute(132)),
                        EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.ONLY_IN_AIR_PREDICATE, 1),
                        BiomeFilter.biome()
                )));

        context.register(MAGNETITE_ORE_PLACED, new PlacedFeature(configured.getOrThrow(MAGNETITE_ORE),
                commonOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))));
        context.register(MAGNETITE_ORE_ROCKY_PLACED, new PlacedFeature(configured.getOrThrow(MAGNETITE_ORE),
                commonOrePlacement(12, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))));

        context.register(SPHALERITE_ORE_PLACED, new PlacedFeature(configured.getOrThrow(SPHALERITE_ORE),
                commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(80)))));
        context.register(SPHALERITE_CAVE_PLACED, new PlacedFeature(configured.getOrThrow(SPHALERITE_ORE),
                commonOrePlacement(8, HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(40)))));

        context.register(SULFUR_ORE_PLACED, new PlacedFeature(configured.getOrThrow(SULFUR_ORE),
                commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)))));
        context.register(SULFUR_ORE_LAVA_PLACED, new PlacedFeature(configured.getOrThrow(SULFUR_ORE),
                commonOrePlacement(15, HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(20)))));

        context.register(OCHRE_CLAY_PLACED, new PlacedFeature(configured.getOrThrow(OCHRE_CLAY),
                commonOrePlacement(40, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));
        context.register(RED_OCHRE_CLAY_PLACED, new PlacedFeature(configured.getOrThrow(RED_OCHRE_CLAY),
                commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));

        context.register(RUBBER_TREE_PLACED, new PlacedFeature(configured.getOrThrow(RUBBER_TREE),
                List.of(
                        RarityFilter.onAverageOnceEvery(20),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.RUBBER_TREE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                )));


        context.register(PALM_TREE_PLACED, new PlacedFeature(configured.getOrThrow(PALM_TREE),
                List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.PALM_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));
        context.register(CYPRESS_TREE_PLACED, new PlacedFeature(configured.getOrThrow(CYPRESS_TREE),
                List.of(RarityFilter.onAverageOnceEvery(28), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.CYPRESS_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));
        context.register(BAOBAB_TREE_PLACED, new PlacedFeature(configured.getOrThrow(BAOBAB_TREE),
                List.of(RarityFilter.onAverageOnceEvery(48), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.BAOBAB_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(MAPLE_TREE_PLACED, new PlacedFeature(configured.getOrThrow(MAPLE_TREE),
                List.of(RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.MAPLE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(FIG_TREE_PLACED, new PlacedFeature(configured.getOrThrow(FIG_TREE),
                List.of(RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.FIG_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(CEDAR_TREE_PLACED, new PlacedFeature(configured.getOrThrow(CEDAR_TREE),
                List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.CEDAR_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(EUCALYPTUS_GROVE_PLACED, new PlacedFeature(configured.getOrThrow(EUCALYPTUS_GROVE),
                List.of(RarityFilter.onAverageOnceEvery(28), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.EUCALYPTUS_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(RAINBOW_EUCALYPTUS_GROVE_PLACED, new PlacedFeature(configured.getOrThrow(RAINBOW_EUCALYPTUS_GROVE),
                List.of(RarityFilter.onAverageOnceEvery(96), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.RAINBOW_EUCALYPTUS_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(AGAVE_PLACED, new PlacedFeature(configured.getOrThrow(AGAVE_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));

        context.register(YUCCA_PLACED, new PlacedFeature(configured.getOrThrow(YUCCA_CLUSTER),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(PLANTAIN_PLACED, new PlacedFeature(configured.getOrThrow(PLANTAIN_CLUSTER),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(WHITE_LILY_PLACED, new PlacedFeature(configured.getOrThrow(WHITE_LILY_CLUSTER),
                List.of(RarityFilter.onAverageOnceEvery(36), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(BLUEBONNET_PLACED, new PlacedFeature(configured.getOrThrow(BLUEBONNET_CLUSTER),
                List.of(RarityFilter.onAverageOnceEvery(36), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(PURPLE_CONEFLOWER_PLACED, new PlacedFeature(configured.getOrThrow(PURPLE_CONEFLOWER_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(FUCHSIA_PLACED, new PlacedFeature(configured.getOrThrow(FUCHSIA_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(MARIGOLD_PLACED, new PlacedFeature(configured.getOrThrow(MARIGOLD_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(HIBISCUS_PLACED, new PlacedFeature(configured.getOrThrow(HIBISCUS_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(LOTUS_PLACED, new PlacedFeature(configured.getOrThrow(LOTUS_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(28), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(REEDS_PLACED, new PlacedFeature(configured.getOrThrow(REEDS_CLUSTER),
                List.of(RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR), BiomeFilter.biome())));
        context.register(TARO_CROP_PLACED, new PlacedFeature(configured.getOrThrow(TARO_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));

        context.register(OLIVE_TREE_PLACED, new PlacedFeature(configured.getOrThrow(OLIVE_TREE),
                List.of(
                        RarityFilter.onAverageOnceEvery(24),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.OLIVE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                )));

        context.register(MARBLE_VEIN_RARE_PLACED, new PlacedFeature(configured.getOrThrow(MARBLE_VEIN),
                List.of(RarityFilter.onAverageOnceEvery(200), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(40)), BiomeFilter.biome())));
        context.register(MARBLE_VEIN_SURFACE_PLACED, new PlacedFeature(configured.getOrThrow(MARBLE_VEIN),
                List.of(RarityFilter.onAverageOnceEvery(400), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(180)), BiomeFilter.biome())));

        context.register(LIMESTONE_VEIN_RARE_PLACED, new PlacedFeature(configured.getOrThrow(LIMESTONE_VEIN),
                List.of(RarityFilter.onAverageOnceEvery(150), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(20), VerticalAnchor.absolute(200)), BiomeFilter.biome())));

        context.register(MARBLE_TEST_PLACED, new PlacedFeature(configured.getOrThrow(MARBLE_TEST),
                commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(60)))));
        context.register(LIMESTONE_TEST_PLACED, new PlacedFeature(configured.getOrThrow(LIMESTONE_TEST),
                commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(140)))));

        context.register(LIMESTONE_SURFACE_BIOME_PLACED, new PlacedFeature(configured.getOrThrow(LIMESTONE_SURFACE_BIOME),
                List.of(RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(200)), BiomeFilter.biome())));

        context.register(SALTPETER_SAND_PLACED, new PlacedFeature(configured.getOrThrow(SALTPETER_SAND),
                commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.absolute(50), VerticalAnchor.absolute(120)))));
        context.register(SALTPETER_SANDSTONE_PLACED, new PlacedFeature(configured.getOrThrow(SALTPETER_SANDSTONE),
                commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(50), VerticalAnchor.absolute(120)))));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(materia.MOD_ID, path));
    }

    private static ResourceKey<PlacedFeature> placed(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(materia.MOD_ID, path));
    }

    private static ConfiguredFeature<?, ?> randomPatch(net.minecraft.world.level.block.state.BlockState state, int tries, int xzSpread, int ySpread) {
        ConfiguredFeature<SimpleBlockConfiguration, ?> simple = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(state)));
        Holder<ConfiguredFeature<?, ?>> simpleHolder = Holder.direct(simple);
        PlacedFeature placed = new PlacedFeature(simpleHolder, List.of());
        Holder<PlacedFeature> placedHolder = Holder.direct(placed);
        return new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(tries, xzSpread, ySpread, placedHolder));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countOrRarity, PlacementModifier height) {
        return List.of(countOrRarity, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier height) {
        return orePlacement(CountPlacement.of(count), height);
    }
}

