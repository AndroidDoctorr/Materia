from pathlib import Path
ROOT = Path(r"C:/MCMods/Materia")

def patch_legacy(path: Path):
    text = path.read_text(encoding="utf-8")
    if "VERDIGRIS" in text:
        return
    text = text.replace(
        """        public static final RegistryObject<Item> LAVENDER_DYE = ITEMS.register("lavender_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> REDSTONE_FUEL = ITEMS.register("redstone_fuel",""",
        """        public static final RegistryObject<Item> LAVENDER_DYE = ITEMS.register("lavender_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> VERDIGRIS = ITEMS.register("verdigris",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BURGUNDY_DYE = ITEMS.register("burgundy_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TAN_DYE = ITEMS.register("tan_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> REDSTONE_FUEL = ITEMS.register("redstone_fuel",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> SMOOTH_RUBBER_WOOD_PLANKS = ITEMS.register("smooth_rubber_wood_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        
        // Wood Frames""",
        """        public static final RegistryObject<Item> SMOOTH_RUBBER_WOOD_PLANKS = ITEMS.register("smooth_rubber_wood_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_FIG_PLANK = ITEMS.register("rough_fig_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_FIG_PLANK = ITEMS.register("smooth_fig_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_CEDAR_PLANK = ITEMS.register("rough_cedar_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_CEDAR_PLANK = ITEMS.register("smooth_cedar_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_EUCALYPTUS_PLANK = ITEMS.register("rough_eucalyptus_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_EUCALYPTUS_PLANK = ITEMS.register("smooth_eucalyptus_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        
        // Wood Frames""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVES = ITEMS.register("olives",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> BEANS = ITEMS.register("beans",""",
        """        public static final RegistryObject<Item> OLIVES = ITEMS.register("olives",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> FIG = ITEMS.register("fig",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(2)
                                                .saturationMod(0.3f)
                                                .build())));
        public static final RegistryObject<Item> BEANS = ITEMS.register("beans",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVE_COTTON = ITEMS.register("olive_cotton",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ORANGE_COTTON = ITEMS.register("orange_cotton",""",
        """        public static final RegistryObject<Item> OLIVE_COTTON = ITEMS.register("olive_cotton",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BURGUNDY_COTTON = ITEMS.register("burgundy_cotton",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TAN_COTTON = ITEMS.register("tan_cotton",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ORANGE_COTTON = ITEMS.register("orange_cotton",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_BURGUNDY_WOOL = ITEMS.register("clump_of_burgundy_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_TAN_WOOL = ITEMS.register("clump_of_tan_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BURGUNDY_STRING = ITEMS.register("burgundy_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TAN_STRING = ITEMS.register("tan_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
    )
    path.write_text(text, encoding="utf-8")

def patch_211(path: Path):
    text = path.read_text(encoding="utf-8")
    if "VERDIGRIS" in text:
        return
    text = text.replace(
        """        public static final RegistryObject<Item> LAVENDER_DYE = ITEMS.register("lavender_dye",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> REDSTONE_FUEL = ITEMS.register("redstone_fuel",""",
        """        public static final RegistryObject<Item> LAVENDER_DYE = ITEMS.register("lavender_dye",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> VERDIGRIS = ITEMS.register("verdigris",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> BURGUNDY_DYE = ITEMS.register("burgundy_dye",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> TAN_DYE = ITEMS.register("tan_dye",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> REDSTONE_FUEL = ITEMS.register("redstone_fuel",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> SMOOTH_RUBBER_WOOD_PLANKS = ITEMS.register("smooth_rubber_wood_planks",
                        () -> new Item(new Item.Properties()
                                        ));
        
        // Wood Frames""",
        """        public static final RegistryObject<Item> SMOOTH_RUBBER_WOOD_PLANKS = ITEMS.register("smooth_rubber_wood_planks",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> ROUGH_FIG_PLANK = ITEMS.register("rough_fig_plank",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> SMOOTH_FIG_PLANK = ITEMS.register("smooth_fig_plank",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> ROUGH_CEDAR_PLANK = ITEMS.register("rough_cedar_plank",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> SMOOTH_CEDAR_PLANK = ITEMS.register("smooth_cedar_plank",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> ROUGH_EUCALYPTUS_PLANK = ITEMS.register("rough_eucalyptus_plank",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> SMOOTH_EUCALYPTUS_PLANK = ITEMS.register("smooth_eucalyptus_plank",
                        () -> new Item(new Item.Properties()));
        
        // Wood Frames""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVES = ITEMS.register("olives",
                        () -> new Item(new Item.Properties()
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> BEANS = ITEMS.register("beans",""",
        """        public static final RegistryObject<Item> OLIVES = ITEMS.register("olives",
                        () -> new Item(new Item.Properties()
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> FIG = ITEMS.register("fig",
                        () -> new Item(new Item.Properties()
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(2)
                                                .saturationMod(0.3f)
                                                .build())));
        public static final RegistryObject<Item> BEANS = ITEMS.register("beans",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVE_COTTON = ITEMS.register("olive_cotton",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> ORANGE_COTTON = ITEMS.register("orange_cotton",""",
        """        public static final RegistryObject<Item> OLIVE_COTTON = ITEMS.register("olive_cotton",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> BURGUNDY_COTTON = ITEMS.register("burgundy_cotton",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> TAN_COTTON = ITEMS.register("tan_cotton",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> ORANGE_COTTON = ITEMS.register("orange_cotton",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> CLUMP_OF_BURGUNDY_WOOL = ITEMS.register("clump_of_burgundy_wool",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> CLUMP_OF_TAN_WOOL = ITEMS.register("clump_of_tan_wool",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
    )
    text = text.replace(
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> BURGUNDY_STRING = ITEMS.register("burgundy_string",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> TAN_STRING = ITEMS.register("tan_string",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
    )
    path.write_text(text, encoding="utf-8")

for ver in ("1.18.2", "1.19.2"):
    patch_legacy(ROOT / ver / "src/main/java/com/torr/materia/ModItems.java")
patch_211(ROOT / "1.21.1/src/main/java/com/torr/materia/ModItems.java")
print("moditems fixed")
