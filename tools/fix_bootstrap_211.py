from pathlib import Path

p = Path(r"C:/MCMods/Materia/1.21.1/src/main/java/com/torr/materia/datagen/worldgen/materiaWorldgenBootstrap.java")
text = p.read_text(encoding="utf-8")
if "FIG_TREE =" not in text:
    text = text.replace(
        'import com.torr.materia.world.feature.ModFeatures;\n',
        'import com.torr.materia.world.feature.ModFeatures;\nimport com.torr.materia.world.feature.FigFoliagePlacer;\n',
    )
    text = text.replace(
        'import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;\n',
        'import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;\nimport net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;\n',
    )
    text = text.replace(
        'import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;\n',
        'import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;\nimport net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;\n',
    )
    text = text.replace(
        '    private static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_TREE = configured("maple_tree");\n\n    // Placed feature keys',
        '''    private static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_TREE = configured("maple_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> FIG_TREE = configured("fig_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_TREE = configured("cedar_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> CEDAR_MEGA_TREE = configured("cedar_mega_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> EUCALYPTUS_TREE = configured("eucalyptus_tree");
    private static final ResourceKey<ConfiguredFeature<?, ?>> EUCALYPTUS_GROVE = configured("eucalyptus_grove");
    private static final ResourceKey<ConfiguredFeature<?, ?>> AGAVE_PATCH = configured("agave_patch");

    // Placed feature keys''',
    )
    text = text.replace(
        '    private static final ResourceKey<PlacedFeature> MAPLE_TREE_PLACED = placed("maple_tree_placed");\n\n    public static void bootstrapConfiguredFeatures',
        '''    private static final ResourceKey<PlacedFeature> MAPLE_TREE_PLACED = placed("maple_tree_placed");
    private static final ResourceKey<PlacedFeature> FIG_TREE_PLACED = placed("fig_tree_placed");
    private static final ResourceKey<PlacedFeature> CEDAR_TREE_PLACED = placed("cedar_tree_placed");
    private static final ResourceKey<PlacedFeature> EUCALYPTUS_GROVE_PLACED = placed("eucalyptus_grove_placed");
    private static final ResourceKey<PlacedFeature> RAINBOW_EUCALYPTUS_GROVE_PLACED = placed("rainbow_eucalyptus_grove_placed");
    private static final ResourceKey<PlacedFeature> AGAVE_PLACED = placed("agave_placed");

    public static void bootstrapConfiguredFeatures''',
    )
    text = text.replace(
        '        context.register(ESPARTO_PATCH, randomPatch(ModBlocks.ESPARTO.get().defaultBlockState(), 24, 5, 2));\n        context.register(TEA_BUSH_PATCH, randomPatch(ModBlocks.TEA_BUSH.get().defaultBlockState(), 16, 4, 2));',
        '        context.register(ESPARTO_PATCH, randomPatch(ModBlocks.ESPARTO.get().defaultBlockState(), 24, 5, 2));\n        context.register(AGAVE_PATCH, randomPatch(ModBlocks.AGAVE.get().defaultBlockState(), 24, 5, 2));\n        context.register(TEA_BUSH_PATCH, randomPatch(ModBlocks.TEA_BUSH.get().defaultBlockState(), 16, 4, 2));',
    )
    text = text.replace(
        '''        context.register(MAPLE_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.MAPLE_LOG.get()),
                new StraightTrunkPlacer(4, 2, 0),
                BlockStateProvider.simple(ModBlocks.MAPLE_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(OLIVE_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(''',
        '''        context.register(MAPLE_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
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
                new SpruceFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(2)),
                new TwoLayersFeatureSize(2, 0, 2)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(CEDAR_MEGA_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.CEDAR_LOG.get()),
                new GiantTrunkPlacer(25, 10, 5),
                BlockStateProvider.simple(ModBlocks.CEDAR_LEAVES.get()),
                new SpruceFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(2)),
                new TwoLayersFeatureSize(2, 1, 0)
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

        context.register(EUCALYPTUS_TREE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_TREE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(EUCALYPTUS_GROVE, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_GROVE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

        context.register(OLIVE_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(''',
    )
    text = text.replace(
        '''        context.register(MAPLE_TREE_PLACED, new PlacedFeature(configured.getOrThrow(MAPLE_TREE),
                List.of(RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.MAPLE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(OLIVE_TREE_PLACED, new PlacedFeature(configured.getOrThrow(OLIVE_TREE),''',
        '''        context.register(MAPLE_TREE_PLACED, new PlacedFeature(configured.getOrThrow(MAPLE_TREE),
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

        context.register(RAINBOW_EUCALYPTUS_GROVE_PLACED, new PlacedFeature(configured.getOrThrow(EUCALYPTUS_GROVE),
                List.of(RarityFilter.onAverageOnceEvery(96), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.RAINBOW_EUCALYPTUS_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())));

        context.register(AGAVE_PLACED, new PlacedFeature(configured.getOrThrow(AGAVE_PATCH),
                List.of(RarityFilter.onAverageOnceEvery(12), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING), BiomeFilter.biome())));

        context.register(OLIVE_TREE_PLACED, new PlacedFeature(configured.getOrThrow(OLIVE_TREE),''',
    )
    p.write_text(text, encoding="utf-8")
    print("bootstrap patched")
else:
    print("bootstrap already patched")
