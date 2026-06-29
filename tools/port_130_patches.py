from pathlib import Path
ROOT = Path(r"C:/MCMods/Materia")

TAB = ".tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)"
TAB_FOOD = ".tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)"

def patch_moditems(path: Path, legacy_tabs: bool):
    text = path.read_text(encoding="utf-8")
    if "VERDIGRIS" in text:
        return
    mat = TAB if legacy_tabs else ""
    food_tab = TAB_FOOD if legacy_tabs else ""
    text = text.replace(
        """        public static final RegistryObject<Item> LAVENDER_DYE = ITEMS.register("lavender_dye",
                        () -> new Item(new Item.Properties()"""
        + (TAB if legacy_tabs else "")
        + """));
        public static final RegistryObject<Item> REDSTONE_FUEL = ITEMS.register("redstone_fuel",""",
        """        public static final RegistryObject<Item> LAVENDER_DYE = ITEMS.register("lavender_dye",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> VERDIGRIS = ITEMS.register("verdigris",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> BURGUNDY_DYE = ITEMS.register("burgundy_dye",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> TAN_DYE = ITEMS.register("tan_dye",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> REDSTONE_FUEL = ITEMS.register("redstone_fuel",""",
    )
    plank = """
        public static final RegistryObject<Item> ROUGH_FIG_PLANK = ITEMS.register("rough_fig_plank",
                        () -> new Item(new Item.Properties()"""
    plank_end = mat + """));
        public static final RegistryObject<Item> SMOOTH_FIG_PLANK = ITEMS.register("smooth_fig_plank",
                        () -> new Item(new Item.Properties()""" + mat + """));
        public static final RegistryObject<Item> ROUGH_CEDAR_PLANK = ITEMS.register("rough_cedar_plank",
                        () -> new Item(new Item.Properties()""" + mat + """));
        public static final RegistryObject<Item> SMOOTH_CEDAR_PLANK = ITEMS.register("smooth_cedar_plank",
                        () -> new Item(new Item.Properties()""" + mat + """));
        public static final RegistryObject<Item> ROUGH_EUCALYPTUS_PLANK = ITEMS.register("rough_eucalyptus_plank",
                        () -> new Item(new Item.Properties()""" + mat + """));
        public static final RegistryObject<Item> SMOOTH_EUCALYPTUS_PLANK = ITEMS.register("smooth_eucalyptus_plank",
                        () -> new Item(new Item.Properties()""" + mat + """));
        
"""
    text = text.replace(
        """        public static final RegistryObject<Item> SMOOTH_RUBBER_WOOD_PLANKS = ITEMS.register("smooth_rubber_wood_planks",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        
        // Wood Frames""",
        """        public static final RegistryObject<Item> SMOOTH_RUBBER_WOOD_PLANKS = ITEMS.register("smooth_rubber_wood_planks",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));"""
        + plank
        + plank_end
        + """        // Wood Frames""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVES = ITEMS.register("olives",
                        () -> new Item(new Item.Properties()"""
        + food_tab
        + """
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> BEANS = ITEMS.register("beans",""",
        """        public static final RegistryObject<Item> OLIVES = ITEMS.register("olives",
                        () -> new Item(new Item.Properties()"""
        + food_tab
        + """
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> FIG = ITEMS.register("fig",
                        () -> new Item(new Item.Properties()"""
        + food_tab
        + """
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(2)
                                                .saturationMod(0.3f)
                                                .build())));
        public static final RegistryObject<Item> BEANS = ITEMS.register("beans",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVE_COTTON = ITEMS.register("olive_cotton",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> ORANGE_COTTON = ITEMS.register("orange_cotton",""",
        """        public static final RegistryObject<Item> OLIVE_COTTON = ITEMS.register("olive_cotton",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> BURGUNDY_COTTON = ITEMS.register("burgundy_cotton",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> TAN_COTTON = ITEMS.register("tan_cotton",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> ORANGE_COTTON = ITEMS.register("orange_cotton",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> CLUMP_OF_BURGUNDY_WOOL = ITEMS.register("clump_of_burgundy_wool",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> CLUMP_OF_TAN_WOOL = ITEMS.register("clump_of_tan_wool",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> BURGUNDY_STRING = ITEMS.register("burgundy_string",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> TAN_STRING = ITEMS.register("tan_string",
                        () -> new Item(new Item.Properties()"""
        + mat
        + """));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
    )
    path.write_text(text, encoding="utf-8")

CFG_TREES = """
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
                    new SpruceFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(2)),
                    new TwoLayersFeatureSize(2, 0, 2)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> CEDAR_MEGA_TREE = CONFIGURED_FEATURES.register("cedar_mega_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.CEDAR_LOG.get()),
                    new GiantTrunkPlacer(25, 10, 5),
                    BlockStateProvider.simple(ModBlocks.CEDAR_LEAVES.get()),
                    new SpruceFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(2)),
                    new TwoLayersFeatureSize(2, 1, 0)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> EUCALYPTUS_TREE = CONFIGURED_FEATURES.register("eucalyptus_tree",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_TREE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<ConfiguredFeature<?, ?>> EUCALYPTUS_GROVE = CONFIGURED_FEATURES.register("eucalyptus_grove",
            () -> new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>) ModFeatures.EUCALYPTUS_GROVE_FEATURE.get(),
                    NoneFeatureConfiguration.INSTANCE));

"""

def patch_mod_configured(path: Path):
    text = path.read_text(encoding="utf-8")
    if "FIG_TREE" in text:
        return
    if "FigFoliagePlacer" not in text:
        text = text.replace(
            "import com.torr.materia.world.feature.OliveFoliagePlacer;\n",
            "import com.torr.materia.world.feature.OliveFoliagePlacer;\nimport com.torr.materia.world.feature.FigFoliagePlacer;\n",
        )
    if "SpruceFoliagePlacer" not in text:
        text = text.replace(
            "import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;\n",
            "import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;\nimport net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;\n",
        )
    if "AGAVE_PATCH" not in text:
        text = text.replace(
            """    public static final RegistryObject<ConfiguredFeature<?, ?>> ESPARTO_PATCH = CONFIGURED_FEATURES.register("esparto_patch",
            () -> randomPatch(ModBlocks.ESPARTO.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> TEA_BUSH_PATCH = CONFIGURED_FEATURES.register("tea_bush_patch",""",
            """    public static final RegistryObject<ConfiguredFeature<?, ?>> ESPARTO_PATCH = CONFIGURED_FEATURES.register("esparto_patch",
            () -> randomPatch(ModBlocks.ESPARTO.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> AGAVE_PATCH = CONFIGURED_FEATURES.register("agave_patch",
            () -> randomPatch(ModBlocks.AGAVE.get(), 24, 5, 2));

    public static final RegistryObject<ConfiguredFeature<?, ?>> TEA_BUSH_PATCH = CONFIGURED_FEATURES.register("tea_bush_patch",""",
        )
    text = text.replace(
        """    public static final RegistryObject<ConfiguredFeature<?, ?>> MAPLE_TREE = CONFIGURED_FEATURES.register("maple_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.MAPLE_LOG.get()),
                    new StraightTrunkPlacer(4, 2, 0),
                    BlockStateProvider.simple(ModBlocks.MAPLE_LEAVES.get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));

    // Very small marble veins""",
        """    public static final RegistryObject<ConfiguredFeature<?, ?>> MAPLE_TREE = CONFIGURED_FEATURES.register("maple_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.MAPLE_LOG.get()),
                    new StraightTrunkPlacer(4, 2, 0),
                    BlockStateProvider.simple(ModBlocks.MAPLE_LEAVES.get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()));
"""
        + CFG_TREES
        + """
    // Very small marble veins""",
    )
    path.write_text(text, encoding="utf-8")

def placed_holder(expr: str, legacy: bool) -> str:
    if legacy:
        return f"ModConfiguredFeatures.{expr}.getHolder().get()"
    return f"Holder.direct(ModConfiguredFeatures.{expr}.get())"

def patch_mod_placed(path: Path, legacy: bool):
    text = path.read_text(encoding="utf-8")
    if "FIG_TREE_PLACED" in text:
        return
    h = lambda k: placed_holder(k, legacy)
    block = f"""
    public static final RegistryObject<PlacedFeature> AGAVE_PLACED = PLACED_FEATURES.register("agave_placed",
            () -> new PlacedFeature({h('AGAVE_PATCH')},
                    List.of(RarityFilter.onAverageOnceEvery(12),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                            BiomeFilter.biome())));

"""
    if "AGAVE_PLACED" not in text:
        text = text.replace(
            """    public static final RegistryObject<PlacedFeature> ESPARTO_PLACED = PLACED_FEATURES.register("esparto_placed",""",
            block + """    public static final RegistryObject<PlacedFeature> ESPARTO_PLACED = PLACED_FEATURES.register("esparto_placed",""",
        )
    trees = f"""
    public static final RegistryObject<PlacedFeature> FIG_TREE_PLACED = PLACED_FEATURES.register("fig_tree_placed",
            () -> new PlacedFeature({h('FIG_TREE')},
                    List.of(RarityFilter.onAverageOnceEvery(24),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.FIG_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> CEDAR_TREE_PLACED = PLACED_FEATURES.register("cedar_tree_placed",
            () -> new PlacedFeature({h('CEDAR_TREE')},
                    List.of(RarityFilter.onAverageOnceEvery(20),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.CEDAR_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> EUCALYPTUS_GROVE_PLACED = PLACED_FEATURES.register("eucalyptus_grove_placed",
            () -> new PlacedFeature({h('EUCALYPTUS_GROVE')},
                    List.of(RarityFilter.onAverageOnceEvery(28),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.EUCALYPTUS_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> RAINBOW_EUCALYPTUS_GROVE_PLACED = PLACED_FEATURES.register("rainbow_eucalyptus_grove_placed",
            () -> new PlacedFeature({h('EUCALYPTUS_GROVE')},
                    List.of(RarityFilter.onAverageOnceEvery(96),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.RAINBOW_EUCALYPTUS_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome())));

"""
    text = text.replace(
        """
    // Marble vein placement""",
        trees + """
    // Marble vein placement""",
        1,
    )
    path.write_text(text, encoding="utf-8")

def patch_mod_features(path: Path):
    text = path.read_text(encoding="utf-8")
    if "EUCALYPTUS_TREE_FEATURE" in text:
        return
    text = text.replace(
        """    public static final RegistryObject<Feature<?>> CYPRESS_TREE_FEATURE = FEATURES.register("cypress_tree_feature",
            () -> new CypressTreeFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

""",
        """    public static final RegistryObject<Feature<?>> CYPRESS_TREE_FEATURE = FEATURES.register("cypress_tree_feature",
            () -> new CypressTreeFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> EUCALYPTUS_TREE_FEATURE = FEATURES.register("eucalyptus_tree_feature",
            () -> new EucalyptusTreeFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> EUCALYPTUS_GROVE_FEATURE = FEATURES.register("eucalyptus_grove_feature",
            () -> new EucalyptusGroveFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

""",
    )
    path.write_text(text, encoding="utf-8")

def patch_foliage_types(path: Path):
    text = path.read_text(encoding="utf-8")
    if "FIG_FOLIAGE_PLACER" in text:
        return
    text = text.replace(
        """    public static void register(IEventBus eventBus) {
        FOLIAGE_PLACER_TYPES.register(eventBus);
    }
}""",
        """    public static final RegistryObject<FoliagePlacerType<FigFoliagePlacer>> FIG_FOLIAGE_PLACER =
            FOLIAGE_PLACER_TYPES.register("fig_foliage_placer",
                    () -> new FoliagePlacerType<>(FigFoliagePlacer.CODEC));

    public static void register(IEventBus eventBus) {
        FOLIAGE_PLACER_TYPES.register(eventBus);
    }
}""",
    )
    path.write_text(text, encoding="utf-8")

def patch_mod_recipes(path: Path, legacy: bool):
    text = path.read_text(encoding="utf-8")
    if "VERDIGRIS_SERIALIZER" in text:
        return
    if "import com.torr.materia.recipe.VerdigrisRecipe;" not in text:
        text = text.replace(
            "import com.torr.materia.recipe.HewingRecipe;\n",
            "import com.torr.materia.recipe.HewingRecipe;\nimport com.torr.materia.recipe.VerdigrisRecipe;\n",
        )
    if legacy:
        insert = """
    public static final RegistryObject<RecipeSerializer<VerdigrisRecipe>> VERDIGRIS_SERIALIZER =
            RECIPE_SERIALIZERS.register("verdigris", () -> new SimpleCraftingRecipeSerializer<>(VerdigrisRecipe::new));
"""
    else:
        insert = """
    public static final RegistryObject<RecipeSerializer<VerdigrisRecipe>> VERDIGRIS_SERIALIZER =
            RECIPE_SERIALIZERS.register("verdigris", () -> new SimpleCraftingRecipeSerializer<>(VerdigrisRecipe::new));
"""
    text = text.replace(
        """    public static final RegistryObject<RecipeSerializer<com.torr.materia.recipe.HewingRecipe>> HEWING_SERIALIZER =
            RECIPE_SERIALIZERS.register("hewing", () -> new SimpleCraftingRecipeSerializer<>(com.torr.materia.recipe.HewingRecipe::new));

""",
        """    public static final RegistryObject<RecipeSerializer<com.torr.materia.recipe.HewingRecipe>> HEWING_SERIALIZER =
            RECIPE_SERIALIZERS.register("hewing", () -> new SimpleCraftingRecipeSerializer<>(com.torr.materia.recipe.HewingRecipe::new));
"""
        + insert
        + "\n",
    )
    path.write_text(text, encoding="utf-8")

HEWING_LINES = """
        if (log.is(ModBlocks.MAPLE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_BIRCH_PLANK.get(), 4);
        if (log.is(ModBlocks.FIG_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_FIG_PLANK.get(), 4);
        if (log.is(ModBlocks.CEDAR_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_CEDAR_PLANK.get(), 4);
        if (log.is(ModBlocks.EUCALYPTUS_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_EUCALYPTUS_PLANK.get(), 4);
        if (log.is(ModBlocks.RAINBOW_EUCALYPTUS_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_EUCALYPTUS_PLANK.get(), 4);
"""

def patch_hewing(path: Path):
    text = path.read_text(encoding="utf-8")
    if "FIG_LOG" in text:
        return
    text = text.replace(
        """        if (log.is(ModBlocks.MAPLE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_BIRCH_PLANK.get(), 4);
        if (log.is(ModBlocks.SAPPED_SPRUCE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_SPRUCE_PLANK.get(), 4);""",
        HEWING_LINES + """        if (log.is(ModBlocks.SAPPED_SPRUCE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_SPRUCE_PLANK.get(), 4);""",
    )
    path.write_text(text, encoding="utf-8")

def patch_textile(path: Path):
    text = path.read_text(encoding="utf-8")
    if "BURGUNDY_WOOL" in text:
        return
    text = text.replace(
        "        if (clump == ModItems.CLUMP_OF_OLIVE_WOOL.get()) return ModItems.OLIVE_STRING.get();\n        if (clump == ModItems.CLUMP_OF_INDIGO_WOOL.get()) return ModItems.INDIGO_STRING.get();",
        "        if (clump == ModItems.CLUMP_OF_OLIVE_WOOL.get()) return ModItems.OLIVE_STRING.get();\n        if (clump == ModItems.CLUMP_OF_BURGUNDY_WOOL.get()) return ModItems.BURGUNDY_STRING.get();\n        if (clump == ModItems.CLUMP_OF_TAN_WOOL.get()) return ModItems.TAN_STRING.get();\n        if (clump == ModItems.CLUMP_OF_INDIGO_WOOL.get()) return ModItems.INDIGO_STRING.get();",
    )
    text = text.replace(
        "        if (cotton == ModItems.OLIVE_COTTON.get()) return ModItems.OLIVE_STRING.get();\n        if (cotton == ModItems.ORANGE_COTTON.get()) return ModItems.ORANGE_STRING.get();",
        "        if (cotton == ModItems.OLIVE_COTTON.get()) return ModItems.OLIVE_STRING.get();\n        if (cotton == ModItems.BURGUNDY_COTTON.get()) return ModItems.BURGUNDY_STRING.get();\n        if (cotton == ModItems.TAN_COTTON.get()) return ModItems.TAN_STRING.get();\n        if (cotton == ModItems.ORANGE_COTTON.get()) return ModItems.ORANGE_STRING.get();",
    )
    path.write_text(text, encoding="utf-8")

def patch_sheep(path: Path):
    text = path.read_text(encoding="utf-8")
    if "BURGUNDY" in text:
        return
    text = text.replace(
        "    OLIVE(null, ModItems.CLUMP_OF_OLIVE_WOOL, 0x556B2F),\n    INDIGO(null, ModItems.CLUMP_OF_INDIGO_WOOL, 0x4B0082),",
        "    OLIVE(null, ModItems.CLUMP_OF_OLIVE_WOOL, 0x556B2F),\n    BURGUNDY(null, ModItems.CLUMP_OF_BURGUNDY_WOOL, 0x800020),\n    TAN(null, ModItems.CLUMP_OF_TAN_WOOL, 0xC2B280),\n    INDIGO(null, ModItems.CLUMP_OF_INDIGO_WOOL, 0x4B0082),",
    )
    text = text.replace(
        "        if (dyeItem == ModItems.INDIGO_DYE.get()) return INDIGO;",
        "        if (dyeItem == ModItems.BURGUNDY_DYE.get()) return BURGUNDY;\n        if (dyeItem == ModItems.TAN_DYE.get()) return TAN;\n        if (dyeItem == ModItems.INDIGO_DYE.get()) return INDIGO;",
    )
    path.write_text(text, encoding="utf-8")

JEI_EXTRA = """
                new HewingJeiRecipe(Ingredient.of(ModBlocks.MAPLE_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_BIRCH_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.FIG_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_FIG_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.CEDAR_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_CEDAR_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.EUCALYPTUS_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_EUCALYPTUS_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.RAINBOW_EUCALYPTUS_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_EUCALYPTUS_PLANK.get(), 4))
"""

def patch_jei(path: Path):
    text = path.read_text(encoding="utf-8")
    if "FIG_LOG" in text:
        return
    text = text.replace(
        """                new HewingJeiRecipe(Ingredient.of(ModBlocks.BAOBAB_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4))
        );""",
        """                new HewingJeiRecipe(Ingredient.of(ModBlocks.BAOBAB_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4)),"""
        + JEI_EXTRA
        + """
        );""",
    )
    path.write_text(text, encoding="utf-8")

CLIENT_CUTOUTS = """
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AGAVE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FIG_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FIG_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CEDAR_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CEDAR_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EUCALYPTUS_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EUCALYPTUS_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAINBOW_EUCALYPTUS_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BURGUNDY_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TAN_GLASS.get(), RenderType.translucent());
"""

def patch_client(path: Path):
    text = path.read_text(encoding="utf-8")
    if "ModBlocks.AGAVE" in text:
        return
    # insert after maple cutouts if present
    for anchor in (
        "ItemBlockRenderTypes.setRenderLayer(ModBlocks.MAPLE_LEAVES.get(), RenderType.cutoutMipped());",
        "ItemBlockRenderTypes.setRenderLayer(ModBlocks.MAPLE_SAPLING.get(), RenderType.cutout());",
    ):
        if anchor in text:
            text = text.replace(anchor, anchor + CLIENT_CUTOUTS, 1)
            break
    else:
        text = text.replace(
            "ItemBlockRenderTypes.setRenderLayer(ModBlocks.ESPARTO.get(), RenderType.cutout());",
            "ItemBlockRenderTypes.setRenderLayer(ModBlocks.ESPARTO.get(), RenderType.cutout());" + CLIENT_CUTOUTS,
        )
    path.write_text(text, encoding="utf-8")

for ver, legacy, legacy_placed in [("1.18.2", True, True), ("1.19.2", True, True), ("1.21.1", False, False)]:
    base = ROOT / ver / "src/main/java/com/torr/materia"
    patch_moditems(base / "ModItems.java", legacy)
    patch_mod_configured(base / "world/feature/ModConfiguredFeatures.java")
    patch_mod_placed(base / "world/feature/ModPlacedFeatures.java", legacy_placed)
    patch_mod_features(base / "world/feature/ModFeatures.java")
    patch_foliage_types(base / "world/feature/ModFoliagePlacerTypes.java")
    patch_mod_recipes(base / "ModRecipes.java", legacy)
    patch_hewing(base / "recipe/HewingRecipe.java")
    patch_textile(base / "utils/TextileUtils.java")
    patch_sheep(base / "entity/CustomSheepColor.java")
    patch_jei(base / "integration/jei/materiaJeiPlugin.java")
    patch_client(base / "client/ClientSetup.java")
    print("patched", ver)
