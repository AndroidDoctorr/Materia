package com.torr.materia;

import com.torr.materia.item.SquashSeedsItem;
import com.torr.materia.item.HammerStoneItem;
import com.torr.materia.item.HandAxeItem;
import com.torr.materia.item.KnappedFlintItem;
import com.torr.materia.item.FlintKnifeItem;
import com.torr.materia.item.FlintSpearItem;
import com.torr.materia.item.FlaxSeedsItem;
import com.torr.materia.item.GrapeSeedsItem;
import com.torr.materia.item.BeansItem;
import com.torr.materia.item.CornItem;
import com.torr.materia.item.PepperSeedsItem;
import com.torr.materia.item.PotItem;
import com.torr.materia.item.WaterPotItem;
import com.torr.materia.item.LavaPotItem;
import com.torr.materia.item.InstrumentItem;
import com.torr.materia.item.TongsItem;
import com.torr.materia.item.PaperFrameItem;
import com.torr.materia.item.MagnetItem;
import com.torr.materia.item.DynamiteItem;
import com.torr.materia.item.PoulticeItem;
import com.torr.materia.item.CannonballItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.torr.materia.item.InstrumentItem.SampleLength;

public class ModItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        materia.MOD_ID);

        public static final RegistryObject<Item> HANDLE = ITEMS.register("handle",
                        () -> new Item(new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_MISC)));

        // Metal ores
        public static final RegistryObject<Item> RAW_TIN = ITEMS.register("raw_tin",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> RAW_ZINC = ITEMS.register("raw_zinc",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Natural items
        public static final RegistryObject<Item> PEBBLE = ITEMS.register("pebble",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PLANT_FIBER = ITEMS.register("plant_fiber",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_OAK_PLANK = ITEMS.register("rough_oak_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_SPRUCE_PLANK = ITEMS.register("rough_spruce_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_JUNGLE_PLANK = ITEMS.register("rough_jungle_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_DARK_OAK_PLANK = ITEMS.register("rough_dark_oak_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_BIRCH_PLANK = ITEMS.register("rough_birch_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_ACACIA_PLANK = ITEMS.register("rough_acacia_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_MANGROVE_PLANK = ITEMS.register("rough_mangrove_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_CRIMSON_PLANK = ITEMS.register("rough_crimson_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_WARPED_PLANK = ITEMS.register("rough_warped_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_RUBBER_WOOD_PLANK = ITEMS.register("rough_rubber_wood_plank",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OCHRE = ITEMS.register("ochre",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> RED_OCHRE = ITEMS.register("red_ochre",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LODESTONE = ITEMS.register("lodestone",
                        () -> new MagnetItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ANIMAL_FAT = ITEMS.register("animal_fat",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Seeds
        public static final RegistryObject<Item> FLAX_SEEDS = ITEMS.register("flax_seeds",
                        () -> new FlaxSeedsItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SQUASH_SEEDS = ITEMS.register("squash_seeds",
                        () -> new SquashSeedsItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GRAPE_SEEDS = ITEMS.register("grape_seeds",
                        () -> new GrapeSeedsItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> WISTERIA_SEEDS = ITEMS.register("wisteria_seeds",
                        () -> new com.torr.materia.item.WisteriaSeedsItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> HEMP_SEEDS = ITEMS.register("hemp_seeds",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PEPPER_SEEDS = ITEMS.register("pepper_seeds",
                        () -> new PepperSeedsItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Substances/Materials
        public static final RegistryObject<Item> SAP = ITEMS.register("sap",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ASH = ITEMS.register("ash",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PITCH = ITEMS.register("pitch",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TAR = ITEMS.register("tar",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GLUE = ITEMS.register("glue",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STRONG_GLUE = ITEMS.register("strong_glue",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GROUT = ITEMS.register("grout",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PILE_OF_SAND = ITEMS.register("pile_of_sand",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PILE_OF_SOUL_SAND = ITEMS.register("pile_of_soul_sand",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OLIVE_DYE = ITEMS.register("olive_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INDIGO_DYE = ITEMS.register("indigo_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CHARCOAL_GRAY_DYE = ITEMS.register("charcoal_gray_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TAUPE_DYE = ITEMS.register("taupe_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TYRIAN_PURPLE_DYE = ITEMS.register("tyrian_purple_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> POZZOLANA = ITEMS.register("pozzolana",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MORTAR = ITEMS.register("mortar",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> QUICKLIME = ITEMS.register("quicklime",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
         public static final RegistryObject<Item> WATER_CUP = ITEMS.register("water_cup",
                         () -> new com.torr.materia.item.WaterCupItem(new Item.Properties()
                                         .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                         .stacksTo(16)));
        public static final RegistryObject<Item> GLASS_MIXTURE = ITEMS.register("glass_mixture",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SODA_ASH = ITEMS.register("soda_ash",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> POTASH = ITEMS.register("potash",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> KELP_ASH = ITEMS.register("kelp_ash",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LYE = ITEMS.register("lye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PAPER_MIXTURE = ITEMS.register("paper_mixture",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PAPER_PULP = ITEMS.register("paper_pulp",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> FINE_PAPER_PULP = ITEMS.register("fine_paper_pulp",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LAVENDER_DYE = ITEMS.register("lavender_dye",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> REDSTONE_FUEL = ITEMS.register("redstone_fuel",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SALTPETER = ITEMS.register("saltpeter",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> COAL_COKE = ITEMS.register("coal_coke",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BONE_CHAR = ITEMS.register("bone_char",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INK_BOTTLE = ITEMS.register("ink_bottle",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INK_CUP = ITEMS.register("ink_cup",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
                                        
        // Liquids
        public static final RegistryObject<Item> WINE_POT = ITEMS.register("wine_pot",
                        () -> new net.minecraft.world.item.BlockItem(ModBlocks.WINE_POT.get(), new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> WINE_BUCKET = ITEMS.register("wine_bucket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> WINE_CUP = ITEMS.register("wine_cup",
                        () -> new com.torr.materia.item.WineCupItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> WINE_BOTTLE = ITEMS.register("wine_bottle",
                        () -> new com.torr.materia.item.AlcoholBottleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> BEER_POT = ITEMS.register("beer_pot",
                        () -> new net.minecraft.world.item.BlockItem(ModBlocks.BEER_POT.get(), new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BEER_BUCKET = ITEMS.register("beer_bucket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BEER_CUP = ITEMS.register("beer_cup",
                        () -> new com.torr.materia.item.WineCupItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> BEER_BOTTLE = ITEMS.register("beer_bottle",
                        () -> new com.torr.materia.item.AlcoholBottleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> MILK_CUP = ITEMS.register("milk_cup",
                        () -> new com.torr.materia.item.MilkCupItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle",
                        () -> new com.torr.materia.item.DrinkableBottleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> GRAPE_JUICE_POT = ITEMS.register("grape_juice_pot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> GRAPE_JUICE_BUCKET = ITEMS.register("grape_juice_bucket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> GRAPE_JUICE_BOTTLE = ITEMS.register("grape_juice_bottle",
                        () -> new com.torr.materia.item.DrinkableBottleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> VINEGAR_POT = ITEMS.register("vinegar_pot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> VINEGAR_BUCKET = ITEMS.register("vinegar_bucket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> VINEGAR_BOTTLE = ITEMS.register("vinegar_bottle",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> OLIVE_OIL_POT = ITEMS.register("olive_oil_pot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> OLIVE_OIL_BUCKET = ITEMS.register("olive_oil_bucket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> OLIVE_OIL_BOTTLE = ITEMS.register("olive_oil_bottle",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));

        // Additional Materials
        public static final RegistryObject<Item> LATEX = ITEMS.register("latex",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> RESIN = ITEMS.register("resin",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> FIRE_BRICK = ITEMS.register("fire_brick",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> FIRE_BRICK_CLAY = ITEMS.register("fire_brick_clay",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLAY_BOWL = ITEMS.register("clay_bowl",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TERRACOTTA_ROOF_TILE = ITEMS.register("terracotta_roof_tile",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GLASS_PUCK = ITEMS.register("glass_puck",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LENS = ITEMS.register("lens",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ROUGH_LENS = ITEMS.register("rough_lens",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SANDPAPER = ITEMS.register("sandpaper",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_SHEARS = ITEMS.register("bronze_shears",
                        () -> new com.torr.materia.item.BronzeShearsItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .durability(238)));
        public static final RegistryObject<Item> IRON_SHEARS = ITEMS.register("iron_shears",
                        () -> new com.torr.materia.item.IronShearsItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .durability(400)));
        public static final RegistryObject<Item> BRONZE_FILE = ITEMS.register("bronze_file",
                        () -> new com.torr.materia.item.BronzeFileItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .durability(250)));
        public static final RegistryObject<Item> IRON_FILE = ITEMS.register("iron_file",
                        () -> new com.torr.materia.item.IronFileItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .durability(400)));
        public static final RegistryObject<Item> BRASS_GEARS = ITEMS.register("brass_gears",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Murex Shell Items
        public static final RegistryObject<Item> MUREX_GLAND_BRANDARIS = ITEMS.register("murex_gland_brandaris",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MUREX_GLAND_TRUNCULUS = ITEMS.register("murex_gland_trunculus",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MUREX_GLAND_HAEMASTOMA = ITEMS.register("murex_gland_haemastoma",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        
        public static final RegistryObject<Item> BOILED_MUREX_GLAND_BRANDARIS = ITEMS.register("boiled_murex_gland_brandaris",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BOILED_MUREX_GLAND_TRUNCULUS = ITEMS.register("boiled_murex_gland_trunculus",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BOILED_MUREX_GLAND_HAEMASTOMA = ITEMS.register("boiled_murex_gland_haemastoma",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        
        public static final RegistryObject<Item> PURPLE_IOTA = ITEMS.register("purple_iota",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MAGENTA_IOTA = ITEMS.register("magenta_iota",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TYRIAN_PURPLE_IOTA = ITEMS.register("tyrian_purple_iota",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PURPLE_BLOB = ITEMS.register("purple_blob",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MAGENTA_BLOB = ITEMS.register("magenta_blob",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TYRIAN_PURPLE_BLOB = ITEMS.register("tyrian_purple_blob",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Stone Age Items
        public static final RegistryObject<Item> LASHING = ITEMS.register("lashing",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BONE_HANDLE = ITEMS.register("bone_handle",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible",
                        () -> new com.torr.materia.item.CrucibleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(64)));
        public static final RegistryObject<Item> LEATHER_CHESTPIECE = ITEMS.register("leather_chestpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEATHER_BACKPIECE = ITEMS.register("leather_backpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEATHER_SHOULDER = ITEMS.register("leather_shoulder",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEATHER_BOOT = ITEMS.register("leather_boot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Bronze Armor Components
        public static final RegistryObject<Item> BRONZE_CHESTPIECE = ITEMS.register("bronze_chestpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_BACKPIECE = ITEMS.register("bronze_backpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_SHOULDER = ITEMS.register("bronze_shoulder",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_BOOT = ITEMS.register("bronze_boot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Gold Armor Components
        public static final RegistryObject<Item> GOLD_CHESTPIECE = ITEMS.register("gold_chestpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_BACKPIECE = ITEMS.register("gold_backpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_SHOULDER = ITEMS.register("gold_shoulder",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_BOOT = ITEMS.register("gold_boot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Bronze Armor
        public static final RegistryObject<Item> BRONZE_HELMET = ITEMS.register("bronze_helmet",
                        () -> new com.torr.materia.item.BronzeArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.BRONZE,
                                        net.minecraft.world.entity.EquipmentSlot.HEAD,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));
        public static final RegistryObject<Item> BRONZE_CHESTPLATE = ITEMS.register("bronze_chestplate",
                        () -> new com.torr.materia.item.BronzeArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.BRONZE,
                                        net.minecraft.world.entity.EquipmentSlot.CHEST,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));
        public static final RegistryObject<Item> BRONZE_LEGGINGS = ITEMS.register("bronze_leggings",
                        () -> new com.torr.materia.item.BronzeArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.BRONZE,
                                        net.minecraft.world.entity.EquipmentSlot.LEGS,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));
        public static final RegistryObject<Item> BRONZE_BOOTS = ITEMS.register("bronze_boots",
                        () -> new com.torr.materia.item.BronzeArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.BRONZE,
                                        net.minecraft.world.entity.EquipmentSlot.FEET,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));

        // Iron Armor Components
        public static final RegistryObject<Item> IRON_CHESTPIECE = ITEMS.register("iron_chestpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_BACKPIECE = ITEMS.register("iron_backpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_SHOULDER = ITEMS.register("iron_shoulder",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_BOOT = ITEMS.register("iron_boot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Wrought Iron Armor
        public static final RegistryObject<Item> IRON_HELMET = ITEMS.register("iron_helmet",
                        () -> new com.torr.materia.item.IronArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.WROUGHT_IRON,
                                        net.minecraft.world.entity.EquipmentSlot.HEAD,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));
        public static final RegistryObject<Item> IRON_CHESTPLATE = ITEMS.register("iron_chestplate",
                        () -> new com.torr.materia.item.IronArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.WROUGHT_IRON,
                                        net.minecraft.world.entity.EquipmentSlot.CHEST,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));
        public static final RegistryObject<Item> IRON_LEGGINGS = ITEMS.register("iron_leggings",
                        () -> new com.torr.materia.item.IronArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.WROUGHT_IRON,
                                        net.minecraft.world.entity.EquipmentSlot.LEGS,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));
        public static final RegistryObject<Item> IRON_BOOTS = ITEMS.register("iron_boots",
                        () -> new com.torr.materia.item.IronArmorItem(
                                        com.torr.materia.item.ModArmorMaterials.WROUGHT_IRON,
                                        net.minecraft.world.entity.EquipmentSlot.FEET,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)));

        // Chainmail Armor Components
        public static final RegistryObject<Item> CHAINMAIL_CHESTPIECE = ITEMS.register("chainmail_chestpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CHAINMAIL_BACKPIECE = ITEMS.register("chainmail_backpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CHAINMAIL_SHOULDER = ITEMS.register("chainmail_shoulder",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CHAINMAIL_BOOT = ITEMS.register("chainmail_boot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Steel Armor Components
        public static final RegistryObject<Item> STEEL_CHESTPIECE = ITEMS.register("steel_chestpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_BACKPIECE = ITEMS.register("steel_backpiece",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_SHOULDER = ITEMS.register("steel_shoulder",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_BOOT = ITEMS.register("steel_boot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Stone Age Tools                                
        public static final RegistryObject<Item> HAND_AXE = ITEMS.register("hand_axe",
                        () -> new HandAxeItem(ModToolTiers.HAND_AXE, 6.0F, -3.2F,
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)));
        public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("flint_knife",
                        () -> new FlintKnifeItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(131)));

        public static final RegistryObject<Item> BRONZE_KNIFE = ITEMS.register("bronze_knife",
                        () -> new com.torr.materia.item.BronzeKnifeItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(250)));
        public static final RegistryObject<Item> BRONZE_SWORD = ITEMS.register("bronze_sword",
                        () -> new com.torr.materia.item.BronzeSwordItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                                        .durability(250)));
        public static final RegistryObject<Item> IRON_SWORD = ITEMS.register("iron_sword",
                        () -> new com.torr.materia.item.IronSwordItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                                        .durability(400)));
        public static final RegistryObject<Item> FLINT_SPEAR = ITEMS.register("flint_spear",
                        () -> new FlintSpearItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                                        .durability(131)));
        public static final RegistryObject<Item> KNAPPED_FLINT = ITEMS.register("knapped_flint",
                        () -> new KnappedFlintItem(
                                        new Item.Properties()
                                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                                        .durability(16)));
        public static final RegistryObject<Item> HAMMER_STONE = ITEMS.register("hammer_stone",
                        () -> new HammerStoneItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(40))); // Basic stone tool
        public static final RegistryObject<Item> STONE_HAMMER = ITEMS.register("stone_hammer",
                        () -> new com.torr.materia.item.StoneHammerItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                                        .durability(150))); // Stone tier
        public static final RegistryObject<Item> BOW_DRILL = ITEMS.register("bow_drill",
                        () -> new com.torr.materia.item.BowDrillItem(
                                        new Item.Properties()
                                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(16)));
        public static final RegistryObject<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle",
                        () -> new com.torr.materia.item.MortarAndPestleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)));
        public static final RegistryObject<Item> BRONZE_SAWBLDE = ITEMS.register("bronze_sawblade",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_SAWBLDE = ITEMS.register("iron_sawblade",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_SAWBLDE = ITEMS.register("steel_sawblade",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_SAW_BAND = ITEMS.register("bronze_saw_band",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
// TODO: Add later...
/*
        public static final RegistryObject<Item> IRON_SAW_BAND = ITEMS.register("iron_saw_band",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_SAW_BAND = ITEMS.register("steel_saw_band",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
*/

        // Weapons
        public static final RegistryObject<Item> STEEL_SPEAR_HEAD = ITEMS.register("steel_spear_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_SPEAR_HEAD = ITEMS.register("iron_spear_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_SPEAR_HEAD = ITEMS.register("bronze_spear_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_SPEAR = ITEMS.register("steel_spear",
                        () -> new com.torr.materia.item.SteelSpearItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .durability(1561)));
        public static final RegistryObject<Item> DIAMOND_SPEAR = ITEMS.register("diamond_spear",
                        () -> new com.torr.materia.item.DiamondSpearItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .durability(1800)));
        public static final RegistryObject<Item> NETHERITE_SPEAR = ITEMS.register("netherite_spear",
                        () -> new com.torr.materia.item.NetheriteSpearItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .durability(2200)));
        public static final RegistryObject<Item> IRON_SPEAR = ITEMS.register("iron_spear",
                        () -> new com.torr.materia.item.IronSpearItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .durability(400)));
        public static final RegistryObject<Item> BRONZE_SPEAR = ITEMS.register("bronze_spear",
                        () -> new com.torr.materia.item.BronzeSpearItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .durability(250)));
        public static final RegistryObject<Item> COMPOSITE_BOW = ITEMS.register("composite_bow",
                        () -> new com.torr.materia.item.CompositeBowItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .durability(500)));

        // Bronze Age Items
        public static final RegistryObject<Item> CLAY_POT = ITEMS.register("clay_pot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TANNED_LEATHER = ITEMS.register("tanned_leather",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> HARDENED_LEATHER = ITEMS.register("hardened_leather",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEATHER_STRETCHED = ITEMS.register("leather_stretched", () -> new Item(
                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TANNED_LEATHER_STRETCHED = ITEMS.register("tanned_leather_stretched",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> POT = ITEMS.register("pot",
                        () -> new PotItem(ModBlocks.POT.get(), new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS)
                                        .stacksTo(4)));
        public static final RegistryObject<Item> WATER_POT = ITEMS.register("water_pot",
                        () -> new WaterPotItem(ModBlocks.WATER_POT.get(), new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> LAVA_POT = ITEMS.register("lava_pot",
                        () -> new LavaPotItem(ModBlocks.LAVA_POT.get(), new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BRASS_ROD = ITEMS.register("brass_rod",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_PLATE = ITEMS.register("brass_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_LATCH = ITEMS.register("brass_latch",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_HINGE = ITEMS.register("brass_hinge",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_RIVETS = ITEMS.register("brass_rivets",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_BUCKLE = ITEMS.register("brass_buckle",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_HINGE = ITEMS.register("iron_hinge",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_LATCH = ITEMS.register("iron_latch",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_RIVETS = ITEMS.register("iron_rivets",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_AXE_HEAD = ITEMS.register("bronze_axe_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BUNDLE = ITEMS.register("bundle",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> FISHING_NET = ITEMS.register("fishing_net",
                        () -> new com.torr.materia.item.FishingNetItem(
                                        new Item.Properties()
                                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(64)));
        public static final RegistryObject<Item> ROPE = ITEMS.register("rope",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEATHER_STRAP = ITEMS.register("leather_strap",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OAR = ITEMS.register("oar",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_NAILS = ITEMS.register("bronze_nails",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_NAILS = ITEMS.register("iron_nails",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PAPER_FRAME = ITEMS.register("paper_frame",
                        () -> new PaperFrameItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> UNFIRED_AMPHORA = ITEMS.register("unfired_amphora",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BRINING_VAT_UNFIRED = ITEMS.register("brining_vat_unfired",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> UNFIRED_LID = ITEMS.register("unfired_lid",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> LID = ITEMS.register("lid",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> SEALED_LID = ITEMS.register("sealed_lid",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_NEEDLE = ITEMS.register("brass_needle",
                        () -> new com.torr.materia.item.NeedleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_NEEDLE = ITEMS.register("steel_needle",
                        () -> new com.torr.materia.item.NeedleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_NEEDLE = ITEMS.register("bronze_needle",
                        () -> new com.torr.materia.item.NeedleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> COPPER_NEEDLE = ITEMS.register("copper_needle",
                        () -> new com.torr.materia.item.NeedleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BEDROLL = ITEMS.register("bedroll",
                        () -> new com.torr.materia.item.BedrollItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BED_FRAME = ITEMS.register("bed_frame",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> LINEN_BAG = ITEMS.register("linen_bag",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> BONE_STRIP = ITEMS.register("bone_strip",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MATTRESS = ITEMS.register("mattress",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(16)));

        // Colored sacks (use the same SackItem behavior; color comes from crafting with carpet)
        public static final RegistryObject<Item> WHITE_SACK = ITEMS.register("white_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> ORANGE_SACK = ITEMS.register("orange_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> MAGENTA_SACK = ITEMS.register("magenta_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> LIGHT_BLUE_SACK = ITEMS.register("light_blue_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> YELLOW_SACK = ITEMS.register("yellow_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> LIME_SACK = ITEMS.register("lime_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> PINK_SACK = ITEMS.register("pink_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> GRAY_SACK = ITEMS.register("gray_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> LIGHT_GRAY_SACK = ITEMS.register("light_gray_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> CYAN_SACK = ITEMS.register("cyan_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> PURPLE_SACK = ITEMS.register("purple_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BLUE_SACK = ITEMS.register("blue_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BROWN_SACK = ITEMS.register("brown_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> GREEN_SACK = ITEMS.register("green_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> RED_SACK = ITEMS.register("red_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> BLACK_SACK = ITEMS.register("black_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));

        // Custom dye sacks
        public static final RegistryObject<Item> OCHRE_SACK = ITEMS.register("ochre_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> RED_OCHRE_SACK = ITEMS.register("red_ochre_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> INDIGO_SACK = ITEMS.register("indigo_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> OLIVE_SACK = ITEMS.register("olive_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> TYRIAN_PURPLE_SACK = ITEMS.register("tyrian_purple_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> LAVENDER_SACK = ITEMS.register("lavender_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> CHARCOAL_GRAY_SACK = ITEMS.register("charcoal_gray_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> TAUPE_SACK = ITEMS.register("taupe_sack",
                        () -> new com.torr.materia.item.SackItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .stacksTo(1)));

        // BLANKETS
        // Vanilla color blankets
        public static final RegistryObject<Item> WHITE_BLANKET = ITEMS.register("white_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ORANGE_BLANKET = ITEMS.register("orange_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MAGENTA_BLANKET = ITEMS.register("magenta_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LIGHT_BLUE_BLANKET = ITEMS.register("light_blue_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> YELLOW_BLANKET = ITEMS.register("yellow_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LIME_BLANKET = ITEMS.register("lime_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PINK_BLANKET = ITEMS.register("pink_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GRAY_BLANKET = ITEMS.register("gray_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LIGHT_GRAY_BLANKET = ITEMS.register("light_gray_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CYAN_BLANKET = ITEMS.register("cyan_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PURPLE_BLANKET = ITEMS.register("purple_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BLUE_BLANKET = ITEMS.register("blue_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BROWN_BLANKET = ITEMS.register("brown_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GREEN_BLANKET = ITEMS.register("green_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> RED_BLANKET = ITEMS.register("red_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BLACK_BLANKET = ITEMS.register("black_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Custom colored blankets
        public static final RegistryObject<Item> OCHRE_BLANKET = ITEMS.register("ochre_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> RED_OCHRE_BLANKET = ITEMS.register("red_ochre_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INDIGO_BLANKET = ITEMS.register("indigo_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OLIVE_BLANKET = ITEMS.register("olive_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TYRIAN_PURPLE_BLANKET = ITEMS.register("tyrian_purple_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LAVENDER_BLANKET = ITEMS.register("lavender_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CHARCOAL_GRAY_BLANKET = ITEMS.register("charcoal_gray_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TAUPE_BLANKET = ITEMS.register("taupe_blanket",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // BEDS (custom colors)
        public static final RegistryObject<Item> OCHRE_BED = ITEMS.register("ochre_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.OCHRE_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> RED_OCHRE_BED = ITEMS.register("red_ochre_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.RED_OCHRE_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> INDIGO_BED = ITEMS.register("indigo_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.INDIGO_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> OLIVE_BED = ITEMS.register("olive_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.OLIVE_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> TYRIAN_PURPLE_BED = ITEMS.register("tyrian_purple_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.TYRIAN_PURPLE_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> LAVENDER_BED = ITEMS.register("lavender_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.LAVENDER_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> CHARCOAL_GRAY_BED = ITEMS.register("charcoal_gray_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.CHARCOAL_GRAY_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));
        public static final RegistryObject<Item> TAUPE_BED = ITEMS.register("taupe_bed",
                        () -> new com.torr.materia.item.CustomBedItem(ModBlocks.TAUPE_BED::get, new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                        .stacksTo(1)));

        // Crushed items
        public static final RegistryObject<Item> CRUSHED_BONES = ITEMS.register("crushed_bones",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CRUSHED_SHELLS = ITEMS.register("crushed_shells",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CRUSHED_CERAMIC = ITEMS.register("crushed_ceramic",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CALCITE_POWDER = ITEMS.register("calcite_powder",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Metal Ingots
        public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_INGOT = ITEMS.register("brass_ingot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> WROUGHT_IRON_INGOT = ITEMS.register("wrought_iron_ingot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
                public static final RegistryObject<Item> ZINC_INGOT = ITEMS.register("zinc_ingot",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Metal Nuggets
        public static final RegistryObject<Item> ALUMINUM_NUGGET = ITEMS.register("aluminum_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TIN_NUGGET = ITEMS.register("tin_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ZINC_NUGGET = ITEMS.register("zinc_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEAD_NUGGET = ITEMS.register("lead_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_NUGGET = ITEMS.register("brass_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_NUGGET = ITEMS.register("bronze_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> WROUGHT_IRON_NUGGET = ITEMS.register("wrought_iron_nugget",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Metal Plates
        public static final RegistryObject<Item> BRONZE_PLATE = ITEMS.register("bronze_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> COPPER_PLATE = ITEMS.register("copper_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TIN_PLATE = ITEMS.register("tin_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ALUMINUM_PLATE = ITEMS.register("aluminum_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_PLATE = ITEMS.register("gold_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEAD_PLATE = ITEMS.register("lead_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_PLATE = ITEMS.register("steel_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ZINC_PLATE = ITEMS.register("zinc_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> THICK_IRON_PLATE = ITEMS.register("thick_iron_plate",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Bronze Age Tools
        public static final RegistryObject<Item> BRONZE_HAMMER = ITEMS.register("bronze_hammer",
                        () -> new com.torr.materia.item.BronzeHammerItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                                        .durability(300))); // Bronze tier
        public static final RegistryObject<Item> BRONZE_SAW = ITEMS.register("bronze_saw",
                        () -> new com.torr.materia.item.BronzeSawItem(
                                        new Item.Properties()
                                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(300)));
        public static final RegistryObject<Item> BRONZE_PICKAXE = ITEMS.register("bronze_pickaxe",
                        () -> new com.torr.materia.item.BronzePickaxeItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(300)));
        public static final RegistryObject<Item> IRON_AXE = ITEMS.register("iron_axe",
                        () -> new com.torr.materia.item.IronAxeItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(400)));
        public static final RegistryObject<Item> IRON_PICKAXE = ITEMS.register("iron_pickaxe",
                        () -> new com.torr.materia.item.IronPickaxeItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(400)));
        public static final RegistryObject<Item> IRON_HOE = ITEMS.register("iron_hoe",
                        () -> new com.torr.materia.item.IronHoeItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                        .durability(400)));
                public static final RegistryObject<Item> BRONZE_AXE = ITEMS.register("bronze_axe",
                () -> new com.torr.materia.item.BronzeAxeItem(
                                new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                .durability(300)));
        public static final RegistryObject<Item> IRON_HAMMER = ITEMS.register("iron_hammer",
                () -> new com.torr.materia.item.IronHammerItem(
                                new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                .durability(400)));
        public static final RegistryObject<Item> BRONZE_SHOVEL = ITEMS.register("bronze_shovel",
                () -> new com.torr.materia.item.BronzeShovelItem(
                                new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                .durability(300)));
        public static final RegistryObject<Item> IRON_SHOVEL = ITEMS.register("iron_shovel",
                () -> new com.torr.materia.item.IronShovelItem(
                                new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                                .durability(400)));
        
        // Metal Rods
        public static final RegistryObject<Item> COPPER_ROD = ITEMS.register("copper_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ALUMINUM_ROD = ITEMS.register("aluminum_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_ROD = ITEMS.register("gold_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEAD_ROD = ITEMS.register("lead_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_ROD = ITEMS.register("steel_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_ROD = ITEMS.register("iron_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TIN_ROD = ITEMS.register("tin_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ZINC_ROD = ITEMS.register("zinc_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Bronze Items  
        public static final RegistryObject<Item> BRONZE_PICKAXE_HEAD = ITEMS.register("bronze_pickaxe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_HAMMER_HEAD = ITEMS.register("bronze_hammer_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_HOE_HEAD = ITEMS.register("bronze_hoe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_ROD = ITEMS.register("bronze_rod",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_BLADE = ITEMS.register("bronze_blade",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_CHISEL = ITEMS.register("bronze_chisel",
                () -> new com.torr.materia.item.BronzeChiselItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                .durability(250)));
        public static final RegistryObject<Item> BRONZE_SWORD_BLADE = ITEMS.register("bronze_sword_blade",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_HANDLE = ITEMS.register("bronze_handle",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_HAMMER = ITEMS.register("steel_hammer",
                () -> new com.torr.materia.item.SteelHammerItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(500)));
        public static final RegistryObject<Item> BRONZE_HOE = ITEMS.register("bronze_hoe",
                () -> new com.torr.materia.item.BronzeHoeItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(300)));
        
        // Steel Items
        public static final RegistryObject<Item> STEEL_HAMMER_HEAD = ITEMS.register("steel_hammer_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_HOE_HEAD = ITEMS.register("steel_hoe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_PICKAXE_HEAD = ITEMS.register("steel_pickaxe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_SWORD_BLADE = ITEMS.register("steel_sword_blade",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_BLADE = ITEMS.register("steel_blade",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_AXE_HEAD = ITEMS.register("steel_axe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Other Hammers
        public static final RegistryObject<Item> DIAMOND_HAMMER = ITEMS.register("diamond_hammer",
                () -> new com.torr.materia.item.DiamondHammerItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(1800)));
        public static final RegistryObject<Item> NETHERITE_HAMMER = ITEMS.register("netherite_hammer",
                () -> new com.torr.materia.item.NetheriteHammerItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(2200)));

        // Weapon/Tool Components
        public static final RegistryObject<Item> BOW_LIMB = ITEMS.register("bow_limb",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_HANDLE = ITEMS.register("brass_handle",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_CROSSBAR = ITEMS.register("bronze_crossbar",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_CROSSBAR = ITEMS.register("iron_crossbar",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_CROSSBAR = ITEMS.register("steel_crossbar",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> DIAMOND_CROSSBAR = ITEMS.register("diamond_crossbar",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> DIAMOND_SWORD_BLADE = ITEMS.register("diamond_sword_blade",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> FISHING_HOOK = ITEMS.register("fishing_hook",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_ARROW_HEAD = ITEMS.register("iron_arrow_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_ARROW_HEAD = ITEMS.register("steel_arrow_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_ARROW_HEAD = ITEMS.register("bronze_arrow_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Arrows (progression: flint -> bronze -> iron -> steel)
        // Vanilla Arrow base damage is ~2.0; these are meant to be a slow-but-worth-it upgrade path.
        public static final RegistryObject<Item> BRONZE_ARROW = ITEMS.register("bronze_arrow",
                        () -> new com.torr.materia.item.MetalArrowItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT),
                                        2.5D));
        public static final RegistryObject<Item> IRON_ARROW = ITEMS.register("iron_arrow",
                        () -> new com.torr.materia.item.MetalArrowItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT),
                                        3.0D));
        public static final RegistryObject<Item> STEEL_ARROW = ITEMS.register("steel_arrow",
                        () -> new com.torr.materia.item.MetalArrowItem(
                                        new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT),
                                        3.5D));
        public static final RegistryObject<Item> FLINT_ARROW_HEAD = ITEMS.register("flint_arrow_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> FLETCHING = ITEMS.register("fletching",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        
        public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife",
                        () -> new com.torr.materia.item.IronKnifeItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .durability(500)));
        public static final RegistryObject<Item> STEEL_KNIFE = ITEMS.register("steel_knife",
                        () -> new com.torr.materia.item.SteelKnifeItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                        .durability(750)));
        public static final RegistryObject<Item> BRONZE_POLE = ITEMS.register("bronze_pole",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_POLE = ITEMS.register("iron_pole",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_POLE = ITEMS.register("steel_pole",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_SHOVEL_HEAD = ITEMS.register("bronze_shovel_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_SHOVEL_HEAD = ITEMS.register("iron_shovel_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_SHOVEL_HEAD = ITEMS.register("steel_shovel_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> DIAMOND_SHOVEL_HEAD = ITEMS.register("diamond_shovel_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_SHOVEL_HEAD = ITEMS.register("gold_shovel_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Wire Items
        public static final RegistryObject<Item> COPPER_WIRE = ITEMS.register("copper_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_WIRE = ITEMS.register("bronze_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_WIRE = ITEMS.register("iron_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_WIRE = ITEMS.register("brass_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TIN_WIRE = ITEMS.register("tin_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> STEEL_WIRE = ITEMS.register("steel_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ALUMINUM_WIRE = ITEMS.register("aluminum_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_WIRE = ITEMS.register("gold_wire",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Ring Items
        public static final RegistryObject<Item> BRONZE_RINGS = ITEMS.register("bronze_rings",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_RINGS = ITEMS.register("iron_rings",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRASS_RINGS = ITEMS.register("brass_rings",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_RINGS = ITEMS.register("gold_rings",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Scale Items
        public static final RegistryObject<Item> BRONZE_SCALES = ITEMS.register("bronze_scales",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_SCALES = ITEMS.register("iron_scales",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Bore Items
        public static final RegistryObject<Item> BRONZE_BORE = ITEMS.register("bronze_bore",
                () -> new com.torr.materia.item.BronzeBoreItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(250))); // Bronze tier
        public static final RegistryObject<Item> IRON_BORE = ITEMS.register("iron_bore",
                () -> new com.torr.materia.item.IronBoreItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(400))); // Iron tier

        // Drawplate Items
        public static final RegistryObject<Item> BRONZE_DRAWPLATE = ITEMS.register("bronze_drawplate",
                () -> new com.torr.materia.item.BronzeDrawplateItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(250))); // Bronze tier
        public static final RegistryObject<Item> IRON_DRAWPLATE = ITEMS.register("iron_drawplate",
                () -> new com.torr.materia.item.IronDrawplateItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(400))); // Iron tier

        // Iron Items (Additional)
        public static final RegistryObject<Item> IRON_NEEDLE = ITEMS.register("iron_needle",
                () -> new com.torr.materia.item.NeedleItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MAGNETIZED_NEEDLE = ITEMS.register("magnetized_needle",
                () -> new MagnetItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MAGNETIZED_ROD = ITEMS.register("magnetized_rod",
                () -> new MagnetItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_CHISEL = ITEMS.register("iron_chisel",
                () -> new com.torr.materia.item.IronChiselItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)
                                .durability(400))); // Iron tier
        public static final RegistryObject<Item> IRON_HAMMER_HEAD = ITEMS.register("iron_hammer_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_AXE_HEAD = ITEMS.register("iron_axe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_HOE_HEAD = ITEMS.register("iron_hoe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_SWORD_BLADE = ITEMS.register("iron_sword_blade",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_PICKAXE_HEAD = ITEMS.register("iron_pickaxe_head",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_BLADE = ITEMS.register("iron_blade",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_HANDLE = ITEMS.register("iron_handle",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Tongs (All Materials)
        public static final RegistryObject<Item> WOOD_TONGS = ITEMS.register("wood_tongs",
                () -> new TongsItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(20))); // Wood tongs
        public static final RegistryObject<Item> BRONZE_TONGS = ITEMS.register("bronze_tongs",
                () -> new TongsItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(200))); // Bronze tongs
        public static final RegistryObject<Item> IRON_TONGS = ITEMS.register("iron_tongs",
                () -> new TongsItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(350))); // Iron tongs
        public static final RegistryObject<Item> STEEL_TONGS = ITEMS.register("steel_tongs",
                () -> new TongsItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)
                                .durability(500))); // Steel tongs

        // Metal Bands
        public static final RegistryObject<Item> STEEL_BAND = ITEMS.register("steel_band",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> IRON_BAND = ITEMS.register("iron_band",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_BAND = ITEMS.register("bronze_band",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        // Slayer
        // Lamb of God
        // Pantera
        // Megadeth
        // Hatesphere

        // Metal Pipes
        public static final RegistryObject<Item> BRASS_PIPE = ITEMS.register("brass_pipe",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        /*
        public static final RegistryObject<Item> IRON_PIPE = ITEMS.register("iron_pipe",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LEAD_PIPE = ITEMS.register("lead_pipe",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BRONZE_PIPE = ITEMS.register("bronze_pipe",
                () -> new Item(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        */
        public static final RegistryObject<Item> STEEL_PIPE = ITEMS.register("steel_pipe",
                () -> new com.torr.materia.item.SteelPipeItem(new Item.Properties()
                                .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
       
        // Processed Wood Items
        public static final RegistryObject<Item> SMOOTH_OAK_PLANKS = ITEMS.register("smooth_oak_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_SPRUCE_PLANKS = ITEMS.register("smooth_spruce_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_JUNGLE_PLANKS = ITEMS.register("smooth_jungle_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_DARK_OAK_PLANKS = ITEMS.register("smooth_dark_oak_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_BIRCH_PLANKS = ITEMS.register("smooth_birch_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_ACACIA_PLANKS = ITEMS.register("smooth_acacia_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_MANGROVE_PLANKS = ITEMS.register("smooth_mangrove_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_CRIMSON_PLANKS = ITEMS.register("smooth_crimson_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_WARPED_PLANKS = ITEMS.register("smooth_warped_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SMOOTH_RUBBER_WOOD_PLANKS = ITEMS.register("smooth_rubber_wood_planks",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        
        // Wood Frames
        public static final RegistryObject<Item> WOOD_FRAME = ITEMS.register("wood_frame",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BOX_FRAME = ITEMS.register("box_frame",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
                                        
        // Food Items
        public static final RegistryObject<Item> PLAIN_CAKE = ITEMS.register("plain_cake",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(4)
                                                .saturationMod(0.5f)
                                                .build())));
        public static final RegistryObject<Item> JERKY = ITEMS.register("jerky",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(6)
                                                .saturationMod(0.8f)
                                                .build())));
        public static final RegistryObject<Item> SQUASH = ITEMS.register("squash",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(2)
                                                .saturationMod(0.3f)
                                                .build())));
        public static final RegistryObject<Item> OLIVES = ITEMS.register("olives",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> BEANS = ITEMS.register("beans",
                        () -> new BeansItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(3)
                                                .saturationMod(0.4f)
                                                .build())));
        public static final RegistryObject<Item> CORN = ITEMS.register("corn",
                        () -> new CornItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PEPPERS = ITEMS.register("peppers",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.2f)
                                                .build())));
        public static final RegistryObject<Item> CORN_COB = ITEMS.register("corn_cob",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(4)
                                                .saturationMod(0.5f)
                                                .build())));
        public static final RegistryObject<Item> GRAPES = ITEMS.register("grapes",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(2)
                                                .saturationMod(0.2f)
                                                .build())));
        public static final RegistryObject<Item> CORNMEAL = ITEMS.register("cornmeal",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> MASA_DOUGH = ITEMS.register("masa_dough",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> POPCORN = ITEMS.register("popcorn",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(4)
                                                .saturationMod(0.5f)
                                                .build())));
        public static final RegistryObject<Item> CHILI = ITEMS.register("chili",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.2f)
                                                .build())));
        public static final RegistryObject<Item> SLICED_SQUASH = ITEMS.register("sliced_squash",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.2f)
                                                .build())));
        public static final RegistryObject<Item> BAKED_SQUASH = ITEMS.register("baked_squash",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(5)
                                                .saturationMod(0.6f)
                                                .build())));
        public static final RegistryObject<Item> SALT = ITEMS.register("salt",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .alwaysEat()
                                                .build())));
        public static final RegistryObject<Item> FLOUR = ITEMS.register("flour",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(1)
                                                .saturationMod(0.1f)
                                                .build())));
        public static final RegistryObject<Item> DOUGH = ITEMS.register("dough",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> BATTER = ITEMS.register("batter",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> TORTILLA = ITEMS.register("tortilla",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(4)
                                                .saturationMod(0.4f)
                                                .build())));
        public static final RegistryObject<Item> OLIVE_OIL = ITEMS.register("olive_oil",
                        () -> new com.torr.materia.item.LiquidCrucibleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> GRAPE_JUICE = ITEMS.register("grape_juice",
                        () -> new com.torr.materia.item.LiquidCrucibleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> VINEGAR = ITEMS.register("vinegar",
                        () -> new com.torr.materia.item.LiquidCrucibleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> PORRIDGE = ITEMS.register("porridge",
                        () -> new com.torr.materia.item.LiquidCrucibleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(7)
                                                .saturationMod(0.8f)
                                                .build())));
        public static final RegistryObject<Item> BURRITO = ITEMS.register("burrito",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(8)
                                                .saturationMod(0.9f)
                                                .build())));
        public static final RegistryObject<Item> FRESH_CHEESE = ITEMS.register("fresh_cheese",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)
                                        .food(new net.minecraft.world.food.FoodProperties.Builder()
                                                .nutrition(4)
                                                .saturationMod(0.5f)
                                                .build())));

        // Wool Items
        public static final RegistryObject<Item> CLUMP_OF_WOOL = ITEMS.register("clump_of_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_WHITE_WOOL = ITEMS.register("clump_of_white_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_BLACK_WOOL = ITEMS.register("clump_of_black_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_BLUE_WOOL = ITEMS.register("clump_of_blue_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_BROWN_WOOL = ITEMS.register("clump_of_brown_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_CYAN_WOOL = ITEMS.register("clump_of_cyan_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_GRAY_WOOL = ITEMS.register("clump_of_gray_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_GREEN_WOOL = ITEMS.register("clump_of_green_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_LIGHT_BLUE_WOOL = ITEMS.register("clump_of_light_blue_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_LIGHT_GRAY_WOOL = ITEMS.register("clump_of_light_gray_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_LIME_WOOL = ITEMS.register("clump_of_lime_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_MAGENTA_WOOL = ITEMS.register("clump_of_magenta_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_ORANGE_WOOL = ITEMS.register("clump_of_orange_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_PINK_WOOL = ITEMS.register("clump_of_pink_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_PURPLE_WOOL = ITEMS.register("clump_of_purple_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_RED_WOOL = ITEMS.register("clump_of_red_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_RED_OCHRE_WOOL = ITEMS.register("clump_of_red_ochre_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_TYRIAN_PURPLE_WOOL = ITEMS.register("clump_of_tyrian_purple_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_LAVENDER_WOOL = ITEMS.register("clump_of_lavender_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_CHARCOAL_GRAY_WOOL = ITEMS.register("clump_of_charcoal_gray_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_INDIGO_WOOL = ITEMS.register("clump_of_indigo_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_TAUPE_WOOL = ITEMS.register("clump_of_taupe_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CLUMP_OF_YELLOW_WOOL = ITEMS.register("clump_of_yellow_wool",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SPINDLE = ITEMS.register("spindle",
                        () -> new com.torr.materia.item.SpindleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)));

        // String Items
        public static final RegistryObject<Item> RED_STRING = ITEMS.register("red_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> RED_OCHRE_STRING = ITEMS.register("red_ochre_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TYRIAN_PURPLE_STRING = ITEMS.register("tyrian_purple_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LAVENDER_STRING = ITEMS.register("lavender_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CHARCOAL_GRAY_STRING = ITEMS.register("charcoal_gray_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TAUPE_STRING = ITEMS.register("taupe_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INDIGO_STRING = ITEMS.register("indigo_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BLUE_STRING = ITEMS.register("blue_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LIGHT_BLUE_STRING = ITEMS.register("light_blue_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CYAN_STRING = ITEMS.register("cyan_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GREEN_STRING = ITEMS.register("green_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PINK_STRING = ITEMS.register("pink_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LIME_STRING = ITEMS.register("lime_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> YELLOW_STRING = ITEMS.register("yellow_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> PURPLE_STRING = ITEMS.register("purple_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> WHITE_STRING = ITEMS.register("white_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GRAY_STRING = ITEMS.register("gray_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> LIGHT_GRAY_STRING = ITEMS.register("light_gray_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BLACK_STRING = ITEMS.register("black_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BROWN_STRING = ITEMS.register("brown_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MAGENTA_STRING = ITEMS.register("magenta_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> ORANGE_STRING = ITEMS.register("orange_string",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Chemicals
        public static final RegistryObject<Item> SLAKED_LIME = ITEMS.register("slaked_lime",
                        () -> new com.torr.materia.item.LiquidCrucibleItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));

        // Instruments and Instrument Parts
        public static final RegistryObject<Item> DRIED_GOURD = ITEMS.register("dried_gourd",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD)));
        public static final RegistryObject<Item> FIRE_MARACA = ITEMS.register("fire_maraca",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD), ModSounds.FIRE_MARACA));
        public static final RegistryObject<Item> WATER_MARACA = ITEMS.register("water_maraca",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD), ModSounds.WATER_MARACA));
        public static final RegistryObject<Item> EARTH_MARACA = ITEMS.register("earth_maraca",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD), ModSounds.EARTH_MARACA));
        public static final RegistryObject<Item> AIR_MARACA = ITEMS.register("air_maraca",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD), ModSounds.AIR_MARACA));
        public static final RegistryObject<Item> LIFE_MARACA = ITEMS.register("life_maraca",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_FOOD), ModSounds.LIFE_MARACA));
        public static final RegistryObject<Item> DRUM = ITEMS.register("drum",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS)));
        // Flute Items
        public static final RegistryObject<Item> AIR_FLUTE = ITEMS.register("air_flute",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.AIR_FLUTE, SampleLength.MEDIUM));
        public static final RegistryObject<Item> EARTH_FLUTE = ITEMS.register("earth_flute",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.EARTH_FLUTE, SampleLength.MEDIUM));
        public static final RegistryObject<Item> FIRE_FLUTE = ITEMS.register("fire_flute",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.FIRE_FLUTE, SampleLength.MEDIUM));
        public static final RegistryObject<Item> WATER_FLUTE = ITEMS.register("water_flute",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.WATER_FLUTE, SampleLength.MEDIUM));

        // Drum Items
        public static final RegistryObject<Item> AIR_DRUMS = ITEMS.register("air_drums",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.AIR_DRUMS));
        public static final RegistryObject<Item> EARTH_DRUMS = ITEMS.register("earth_drums",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.EARTH_DRUMS));
        public static final RegistryObject<Item> FIRE_DRUMS = ITEMS.register("fire_drums",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.FIRE_DRUMS));
        public static final RegistryObject<Item> WATER_DRUMS = ITEMS.register("water_drums",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.WATER_DRUMS));

        // Bass Items
        public static final RegistryObject<Item> AIR_BASS = ITEMS.register("air_bass",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.AIR_BASS, SampleLength.MEDIUM));
        public static final RegistryObject<Item> EARTH_BASS = ITEMS.register("earth_bass",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.EARTH_BASS, SampleLength.MEDIUM));
        public static final RegistryObject<Item> FIRE_BASS = ITEMS.register("fire_bass",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.FIRE_BASS, SampleLength.MEDIUM));
        public static final RegistryObject<Item> WATER_BASS = ITEMS.register("water_bass",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.WATER_BASS, SampleLength.MEDIUM));

        // Harp Items
        public static final RegistryObject<Item> AIR_HARP = ITEMS.register("air_harp",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.AIR_HARP, SampleLength.MEDIUM));
        public static final RegistryObject<Item> EARTH_HARP = ITEMS.register("earth_harp",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.EARTH_HARP, SampleLength.MEDIUM));
        public static final RegistryObject<Item> FIRE_HARP = ITEMS.register("fire_harp",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.FIRE_HARP, SampleLength.MEDIUM));
        public static final RegistryObject<Item> WATER_HARP = ITEMS.register("water_harp",
                        () -> new InstrumentItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_TOOLS), ModSounds.WATER_HARP, SampleLength.MEDIUM));

        // Advanced Items
        public static final RegistryObject<Item> RUBBER_SHEET = ITEMS.register("rubber_sheet",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INSULATED_COPPER_WIRE = ITEMS.register("insulated_copper_wire",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INSULATED_GOLD_WIRE = ITEMS.register("insulated_gold_wire",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> INSULATED_ALUMINUM_WIRE = ITEMS.register("insulated_aluminum_wire",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> SOLENOID = ITEMS.register("solenoid",
                        () -> new MagnetItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BATTERY = ITEMS.register("battery",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> VOLTAIC_LAYER = ITEMS.register("voltaic_layer",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> VOLTAIC_PILE = ITEMS.register("voltaic_pile",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> DYNAMO = ITEMS.register("dynamo",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BLOWER = ITEMS.register("blower",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TARGET = ITEMS.register("target",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> DYNAMITE = ITEMS.register("dynamite",
                        () -> new DynamiteItem(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> STONE_CANNONBALL = ITEMS.register("stone_cannonball",
                        () -> new CannonballItem(() -> ModBlocks.STONE_CANNONBALL_PILE.get(), new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> IRON_CANNONBALL = ITEMS.register("iron_cannonball",
                        () -> new CannonballItem(() -> ModBlocks.CANNONBALL_PILE.get(), new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> CANISTER_SHOT = ITEMS.register("canister_shot",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_COMBAT)
                                        .stacksTo(16)));
        public static final RegistryObject<Item> STEEL_RAIL = ITEMS.register("steel_rail",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> GOLD_RAIL = ITEMS.register("gold_rail",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TOTEM_HEAD = ITEMS.register("totem_head",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> TOTEM_BODY = ITEMS.register("totem_body",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> CANNON_BARREL = ITEMS.register("cannon_barrel",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> WOODEN_WHEEL = ITEMS.register("wooden_wheel",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MINECART_WHEEL = ITEMS.register("minecart_wheel",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> MINECART_AXLE = ITEMS.register("minecart_axle",
                        () -> new Item(new Item.Properties()
                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));

        // Healing Poultices
        public static final RegistryObject<Item> OAK_BARK = ITEMS.register("oak_bark",
                        () -> new Item(new Item.Properties()
                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> BOILED_BARK = ITEMS.register("boiled_bark",
                        () -> new Item(new Item.Properties()
                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> WEAK_POULTICE = ITEMS.register("weak_poultice",
                () -> new PoulticeItem(2.0F, new Item.Properties()
                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BREWING)
                        .stacksTo(16)));
        public static final RegistryObject<Item> MEDIUM_POULTICE = ITEMS.register("medium_poultice",
                () -> new PoulticeItem(4.0F, new Item.Properties()
                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BREWING)
                        .stacksTo(16)));
        public static final RegistryObject<Item> STRONG_POULTICE = ITEMS.register("strong_poultice",
                () -> new PoulticeItem(8.0F, new Item.Properties()
                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BREWING)
                        .stacksTo(16)));
}