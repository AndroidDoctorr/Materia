package com.torr.materia;

import com.torr.materia.block.BronzeAnvilBlock;
import com.torr.materia.block.IronAnvilBlock;
import com.torr.materia.block.OliveTreeLeavesBlock;
import com.torr.materia.block.OliveTreeSaplingBlock;
import com.torr.materia.block.RubberTreeLeavesBlock;
import com.torr.materia.block.RubberTreeSaplingBlock;
import com.torr.materia.block.StoneAnvilBlock;
import com.torr.materia.block.TableBlock;
import com.torr.materia.block.CarpetBlock;
import com.torr.materia.block.TappedRubberTreeLogBlock;
import com.torr.materia.block.TrellisBlock;
import com.torr.materia.block.PostBlock;
import com.torr.materia.block.GrapeVineBlock;
import com.torr.materia.block.HopsVineBlock;
import com.torr.materia.WildHopsVineBlock;
import com.torr.materia.block.CannonballPileBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import com.torr.materia.DryingRackBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import com.torr.materia.block.BedrollBlock;
import com.torr.materia.block.AndGateBlock;
import com.torr.materia.block.OrGateBlock;
import com.torr.materia.block.NorGateBlock;
import com.torr.materia.block.XorGateBlock;
import com.torr.materia.block.XnorGateBlock;
import com.torr.materia.block.NandGateBlock;
import com.torr.materia.block.NotGateBlock;
import com.torr.materia.block.TFlopBlock;
import com.torr.materia.block.MuxBlock;
import com.torr.materia.block.TimerBlock;
import com.torr.materia.block.RsLatchBlock;
import com.torr.materia.block.CounterBlock;
import com.torr.materia.block.HalfAdderBlock;
import com.torr.materia.BauxiteBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.item.DyeColor;

public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        materia.MOD_ID);
        // NATURAL BLOCKS
        public static final RegistryObject<Block> EARTH = registerBlock("earth",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
        public static final RegistryObject<Block> GRAVEL_TIN = registerBlock("gravel_tin",
                        () -> new FallingBlock(BlockBehaviour.Properties.of(Material.SAND).strength(0.6f)
                                        .sound(net.minecraft.world.level.block.SoundType.GRAVEL)));
        public static final RegistryObject<Block> ROCK = registerBlock("rock",
                        () -> new RockBlock(BlockBehaviour.Properties.of(Material.DIRT).strength(0.1f)
                                        .sound(SoundType.STONE).noOcclusion()));
        // Surface bauxite chunks (existing)
        public static final RegistryObject<Block> BAUXITE = registerBlock("bauxite",
                        () -> new BauxiteBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.4f)
                                        .sound(SoundType.STONE).noOcclusion()));
        // Solid bauxite ore (new)
        public static final RegistryObject<Block> BAUXITE_ORE = registerBlock("bauxite_ore",
                        () -> new com.torr.materia.block.FullBauxiteBlock());
        public static final RegistryObject<Block> MUREX_SHELL = registerBlock("murex_shell",
                        () -> new MurexShellBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> CLAM = registerBlock("clam",
                        () -> new ClamBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> OCHRE = registerBlock("ochre_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.GRAVEL)));
        public static final RegistryObject<Block> RED_OCHRE = registerBlock("red_ochre_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.GRAVEL)));
        public static final RegistryObject<Block> MAGNETITE = registerBlock("magnetite",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).requiresCorrectToolForDrops().noOcclusion()));
        public static final RegistryObject<Block> MALACHITE = registerBlock("malachite",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).requiresCorrectToolForDrops().noOcclusion()));
        public static final RegistryObject<Block> SURFACE_IRON_ORE = registerBlock("surface_iron_ore",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> SPHALERITE = registerBlock("sphalerite",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).requiresCorrectToolForDrops().noOcclusion()));
        public static final RegistryObject<Block> MARBLE = registerBlock("marble",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).requiresCorrectToolForDrops().noOcclusion()));
        public static final RegistryObject<Block> LIMESTONE = registerBlock("limestone",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).requiresCorrectToolForDrops().noOcclusion()));
        public static final RegistryObject<Block> SULFUR_ORE = registerBlock("sulfur_ore",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> DEEPSLATE_SULFUR_ORE = registerBlock("deepslate_sulfur_ore",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(3.0f)
                                        .sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> TUFF_SULFUR_ORE = registerBlock("tuff_sulfur_ore",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.TUFF).requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> SALTPETER_SAND = registerBlock("saltpeter_sand",
                        () -> new FallingBlock(BlockBehaviour.Properties.of(Material.SAND).strength(0.5f)
                                        .sound(SoundType.SAND)));
        public static final RegistryObject<Block> SALTPETER_SANDSTONE = registerBlock("saltpeter_sandstone",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(0.8f)
                                        .sound(SoundType.STONE)));

        // CRAFTING BLOCKS
        public static final RegistryObject<Block> PRIMITIVE_CRAFTING_TABLE = registerBlock("primitive_crafting_table",
                        () -> new PrimitiveCraftingTableBlock(BlockBehaviour.Properties.of(Material.STONE)
                                        .strength(0.5f).sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> CANNON = registerBlock("cannon",
                        () -> new com.torr.materia.block.CannonBlock(
                                        BlockBehaviour.Properties.of(Material.METAL)
                                                        .strength(3.0f)
                                                        .sound(SoundType.METAL)
                                                        .noOcclusion()));
        // Render helper block for the cannon barrel (no item registered)
        public static final RegistryObject<Block> CANNON_BARREL = BLOCKS.register("cannon_barrel",
                        () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                                        .strength(3.0f)
                                        .sound(SoundType.METAL)
                                        .noOcclusion()));

        // Cannonball piles (placed by crouch-right-clicking with cannonballs)
        public static final RegistryObject<Block> CANNONBALL_PILE = registerBlock("cannonball_pile",
                        () -> new CannonballPileBlock(
                                        () -> ModItems.IRON_CANNONBALL.get(),
                                        BlockBehaviour.Properties.of(Material.STONE)
                                                        .strength(1.5f)
                                                        .sound(SoundType.STONE)
                                                        .noOcclusion()));
        public static final RegistryObject<Block> STONE_CANNONBALL_PILE = registerBlock("stone_cannonball_pile",
                        () -> new CannonballPileBlock(
                                        () -> ModItems.STONE_CANNONBALL.get(),
                                        BlockBehaviour.Properties.of(Material.STONE)
                                                        .strength(1.5f)
                                                        .sound(SoundType.STONE)
                                                        .noOcclusion()));
        public static final RegistryObject<Block> KILN = registerBlock("kiln",
                        () -> new KilnBlock(BlockBehaviour.Properties.of(Material.DIRT).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()
                                        .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));
        public static final RegistryObject<Block> OVEN = registerBlock("oven",
                        () -> new OvenBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()
                                        .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));
        public static final RegistryObject<Block> COKE_OVEN = registerBlock("coke_oven",
                        () -> new com.torr.materia.block.CokeOvenBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f)
                                        .sound(SoundType.STONE).noOcclusion()
                                        .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));
        public static final RegistryObject<Block> FURNACE_KILN = registerBlock("furnace_kiln",
                        () -> new com.torr.materia.block.FurnaceKilnBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).noOcclusion()
                                        .lightLevel(state -> 13)));
        public static final RegistryObject<Block> BLAST_FURNACE_KILN = registerBlock("blast_furnace_kiln",
                        () -> new com.torr.materia.block.BlastFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f)
                                        .sound(SoundType.STONE).noOcclusion()
                                        .lightLevel(state -> 15)));
        public static final RegistryObject<Block> FIRE_PIT = registerBlock("fire_pit",
                        () -> new FirePitBlock(BlockBehaviour.Properties.of(Material.DIRT).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()
                                        .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));
        public static final RegistryObject<Block> POT = BLOCKS.register("pot",
                        () -> new PotBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> WATER_POT = BLOCKS.register("water_pot",
                        () -> new WaterPotBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> MILK_POT = BLOCKS.register("milk_pot",
                        () -> new MilkPotBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> WINE_POT = BLOCKS.register("wine_pot",
                        () -> new WinePotBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> BEER_POT = BLOCKS.register("beer_pot",
                        () -> new BeerPotBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> LAVA_POT = BLOCKS.register("lava_pot",
                        () -> new PotBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> BRINING_VAT = registerBlock("brining_vat",
                        () -> new com.torr.materia.block.BriningVatBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.6f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> AMPHORA = registerBlockWithCustomItem("amphora",
                        () -> new AmphoraBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()),
                        (block) -> new com.torr.materia.item.AmphoraItem(block, 
                                new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS).stacksTo(1)));
        public static final RegistryObject<Block> BASKET = registerBlockWithCustomItem("basket",
                        () -> new BasketBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.3f)
                                        .sound(SoundType.WOOD).noOcclusion()),
                        (block) -> new com.torr.materia.item.BasketItem(block, 
                                new Item.Properties().tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS).stacksTo(1)));
        public static final RegistryObject<Block> OIL_LAMP = registerBlock("oil_lamp",
                        () -> new OilLampBlock(BlockBehaviour.Properties.of(Material.DECORATION)
                                        .strength(0.0f)
                                        .noCollission()
                                        .sound(SoundType.WOOD)
                                        .noOcclusion()
                                        .lightLevel(state -> 15)));
        public static final RegistryObject<Block> DRYING_RACK = registerBlock("drying_rack",
                        () -> new DryingRackBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.6f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> CHIMNEY = registerBlock("chimney",
                        () -> new com.torr.materia.block.ChimneyBlock(BlockBehaviour.Properties.of(Material.STONE)
                                        .strength(1.0f).sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> BELLOWS = registerBlock("bellows",
                        () -> new com.torr.materia.block.BellowsBlock(BlockBehaviour.Properties.of(Material.WOOD)
                                        .strength(1.0f).sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> FISH_TRAP = registerBlock("fish_trap",
                        () -> new com.torr.materia.block.FishTrapBlock(BlockBehaviour.Properties.of(Material.WOOD)
                                        .strength(1.0f).sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> FRAME_LOOM = registerBlock("frame_loom",
                        () -> new FrameLoomBlock(BlockBehaviour.Properties.of(Material.WOOD)
                                        .strength(1.0f).sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> SPINNING_WHEEL = registerBlock("spinning_wheel",
                        () -> new SpinningWheelBlock(BlockBehaviour.Properties.of(Material.WOOD)
                                        .strength(1.0f).sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> FURNACE_CHIMNEY = registerBlock("furnace_chimney",
                        () -> new com.torr.materia.block.FurnaceChimneyBlock(BlockBehaviour.Properties.of(Material.STONE)
                                        .strength(1.0f).sound(SoundType.STONE).noOcclusion()));

        // TEMPORARY SLEEP SURFACE (no block item here; item is custom BedrollItem)
        public static final RegistryObject<Block> BEDROLL = BLOCKS.register("bedroll",
                        () -> new BedrollBlock(BlockBehaviour.Properties.of(Material.WOOL)
                                        .strength(0.2f)
                                        .sound(SoundType.WOOL)
                                        .noOcclusion()));

        // CUSTOM COLORED BEDS
        public static final RegistryObject<Block> OCHRE_BED = BLOCKS.register("ochre_bed",
                        () -> new BedBlock(DyeColor.YELLOW, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));
        public static final RegistryObject<Block> RED_OCHRE_BED = BLOCKS.register("red_ochre_bed",
                        () -> new BedBlock(DyeColor.RED, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));
        public static final RegistryObject<Block> INDIGO_BED = BLOCKS.register("indigo_bed",
                        () -> new BedBlock(DyeColor.BLUE, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));
        public static final RegistryObject<Block> OLIVE_BED = BLOCKS.register("olive_bed",
                        () -> new BedBlock(DyeColor.GREEN, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));
        public static final RegistryObject<Block> TYRIAN_PURPLE_BED = BLOCKS.register("tyrian_purple_bed",
                        () -> new BedBlock(DyeColor.PURPLE, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));
        public static final RegistryObject<Block> LAVENDER_BED = BLOCKS.register("lavender_bed",
                        () -> new BedBlock(DyeColor.PURPLE, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));
        public static final RegistryObject<Block> CHARCOAL_GRAY_BED = BLOCKS.register("charcoal_gray_bed",
                        () -> new BedBlock(DyeColor.GRAY, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));
        public static final RegistryObject<Block> TAUPE_BED = BLOCKS.register("taupe_bed",
                        () -> new BedBlock(DyeColor.BROWN, BlockBehaviour.Properties.copy(Blocks.WHITE_BED)));

        // ANVILS
        public static final RegistryObject<Block> STONE_ANVIL = registerBlock("stone_anvil",
                        () -> new StoneAnvilBlock(BlockBehaviour.Properties.of(Material.STONE)
                                        .strength(1.0f).sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> BRONZE_ANVIL = registerBlock("bronze_anvil",
                        () -> new BronzeAnvilBlock(BlockBehaviour.Properties.of(Material.STONE)
                                        .strength(1.0f).sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> IRON_ANVIL = registerBlock("iron_anvil",
                        () -> new IronAnvilBlock(BlockBehaviour.Properties.of(Material.STONE)
                                        .strength(1.0f).sound(SoundType.STONE).noOcclusion()));

        // CROPS
        public static final RegistryObject<Block> FLAX_CROP = BLOCKS.register("flax_crop",
                        () -> new FlaxCropBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> SQUASH_CROP = BLOCKS.register("squash_crop",
                        () -> new SquashCropBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> BEANS_CROP = BLOCKS.register("beans_crop",
                        () -> new BeansCropBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> PEPPERS_CROP = BLOCKS.register("peppers_crop",
                        () -> new PeppersCropBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> CORN_CROP = BLOCKS.register("corn_crop",
                        () -> new CornCropBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> THREE_SISTERS_CROP = BLOCKS.register("three_sisters_crop",
                        () -> new ThreeSistersBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> THREE_SISTERS_CORN_UPPER = BLOCKS.register("three_sisters_corn_upper",
                        () -> new ThreeSistersCornUpperBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> GRAPE_VINE = BLOCKS.register("grape_vine",
                        () -> new GrapeVineBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> HOPS_VINE = BLOCKS.register("hops_vine",
                        () -> new HopsVineBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> WISTERIA_VINE = BLOCKS.register("wisteria_vine",
                        () -> new com.torr.materia.block.WisteriaVineBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> WISTERIA_HANGING = registerBlock("wisteria_hanging",
                        () -> new com.torr.materia.block.WisteriaHangingBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> GRAPES_HANGING = registerBlock("grapes_hanging",
                        () -> new com.torr.materia.block.GrapesHangingBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));
        public static final RegistryObject<Block> HOPS_HANGING = registerBlock("hops_hanging",
                        () -> new com.torr.materia.block.HopsHangingBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission()
                                        .randomTicks().instabreak().sound(SoundType.CROP)));

        // WILD PLANTS
        public static final RegistryObject<Block> WILD_FLAX = registerBlock("wild_flax",
                        () -> new WildFlaxBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        public static final RegistryObject<Block> INDIGO = registerBlock("indigo",
                        () -> new net.minecraft.world.level.block.FlowerBlock(
                                net.minecraft.world.effect.MobEffects.SATURATION,
                                0,
                                BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.CORNFLOWER)));
        public static final RegistryObject<Block> WILD_SQUASH = registerBlock("wild_squash",
                        () -> new WildSquashBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        public static final RegistryObject<Block> WILD_BEANS = registerBlock("wild_beans",
                        () -> new WildBeansBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        public static final RegistryObject<Block> WILD_PEPPERS = registerBlock("wild_peppers",
                        () -> new WildPeppersBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        public static final RegistryObject<Block> WILD_CORN = registerBlock("wild_corn",
                        () -> new WildCornBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        public static final RegistryObject<Block> WILD_GRAPE_VINE = registerBlock("wild_grape_vine",
                        () -> new WildGrapeVineBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        public static final RegistryObject<Block> WILD_HOPS_VINE = registerBlock("wild_hops_vine",
                        () -> new WildHopsVineBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        public static final RegistryObject<Block> WILD_WISTERIA_VINE = registerBlock("wild_wisteria_vine",
                        () -> new WildWisteriaVineBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.CROP)));
        
        // OTHER BLOCKS
        public static final RegistryObject<Block> SAPPED_SPRUCE_LOG = registerBlock("sapped_spruce_log",
                        () -> new com.torr.materia.block.SappedSpruceLogBlock());
        public static final RegistryObject<Block> TAPPED_RUBBER_TREE_LOG = registerBlock("tapped_rubber_tree_log",
                        () -> new com.torr.materia.block.TappedRubberTreeLogBlock());

        // DECORATIVE BLOCKS
        public static final RegistryObject<Block> MARBLE_BUST = registerBlock("marble_bust",
                        () -> new MarbleBustBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> MARBLE_BODY = registerBlock("marble_body",
                        () -> new MarbleBodyBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> AQUILA_AUREA = registerBlock("aquila_aurea",
                        () -> new AquilaAureaBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).noOcclusion()));

        // OCHRE BLOCKS
        public static final RegistryObject<Block> OCHRE_CONCRETE = registerBlock("ochre_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.ORANGE_CONCRETE)));
        public static final RegistryObject<Block> OCHRE_CONCRETE_POWDER = registerBlock("ochre_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.OCHRE_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.ORANGE_CONCRETE_POWDER)));
        public static final RegistryObject<Block> OCHRE_TERRACOTTA = registerBlock("ochre_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA)));
        public static final RegistryObject<Block> OCHRE_WOOL = registerBlock("ochre_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.ORANGE_WOOL)));
        public static final RegistryObject<Block> OCHRE_GLASS = registerBlock("ochre_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.YELLOW_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> OCHRE_CARPET = registerBlock("ochre_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.YELLOW_CARPET)));

        // RED OCHRE BLOCKS
        public static final RegistryObject<Block> RED_OCHRE_WOOL = registerBlock("red_ochre_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
        public static final RegistryObject<Block> RED_OCHRE_GLASS = registerBlock("red_ochre_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.RED_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> RED_OCHRE_CONCRETE = registerBlock("red_ochre_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_CONCRETE)));
        public static final RegistryObject<Block> RED_OCHRE_CONCRETE_POWDER = registerBlock("red_ochre_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.RED_OCHRE_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.RED_CONCRETE_POWDER)));
        public static final RegistryObject<Block> RED_OCHRE_TERRACOTTA = registerBlock("red_ochre_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA)));
        public static final RegistryObject<Block> RED_OCHRE_CARPET = registerBlock("red_ochre_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.RED_CARPET)));

        // OLIVE BLOCKS
        public static final RegistryObject<Block> OLIVE_WOOL = registerBlock("olive_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.GREEN_WOOL)));
        public static final RegistryObject<Block> OLIVE_GLASS = registerBlock("olive_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.GREEN_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> OLIVE_CONCRETE = registerBlock("olive_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.GREEN_CONCRETE)));
        public static final RegistryObject<Block> OLIVE_CONCRETE_POWDER = registerBlock("olive_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.OLIVE_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.GREEN_CONCRETE_POWDER)));
        public static final RegistryObject<Block> OLIVE_TERRACOTTA = registerBlock("olive_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.GREEN_TERRACOTTA)));
        public static final RegistryObject<Block> OLIVE_CARPET = registerBlock("olive_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.GREEN_CARPET)));

        // INDIGO BLOCKS
        public static final RegistryObject<Block> INDIGO_WOOL = registerBlock("indigo_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_WOOL)));
        public static final RegistryObject<Block> INDIGO_GLASS = registerBlock("indigo_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.BLUE_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> INDIGO_CONCRETE = registerBlock("indigo_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_CONCRETE)));
        public static final RegistryObject<Block> INDIGO_CONCRETE_POWDER = registerBlock("indigo_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.INDIGO_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.BLUE_CONCRETE_POWDER)));
        public static final RegistryObject<Block> INDIGO_TERRACOTTA = registerBlock("indigo_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_TERRACOTTA)));
        public static final RegistryObject<Block> INDIGO_CARPET = registerBlock("indigo_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.BLUE_CARPET)));

        // TYRIAN PURPLE BLOCKS
        public static final RegistryObject<Block> TYRIAN_PURPLE_WOOL = registerBlock("tyrian_purple_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_WOOL)));
        public static final RegistryObject<Block> TYRIAN_PURPLE_GLASS = registerBlock("tyrian_purple_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.BLUE_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> TYRIAN_PURPLE_CONCRETE = registerBlock("tyrian_purple_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_CONCRETE)));
        public static final RegistryObject<Block> TYRIAN_PURPLE_CONCRETE_POWDER = registerBlock("tyrian_purple_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.TYRIAN_PURPLE_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.BLUE_CONCRETE_POWDER)));
        public static final RegistryObject<Block> TYRIAN_PURPLE_TERRACOTTA = registerBlock("tyrian_purple_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_TERRACOTTA)));
        public static final RegistryObject<Block> TYRIAN_PURPLE_CARPET = registerBlock("tyrian_purple_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.BLUE_CARPET)));

        // CHARCOAL GRAY BLOCKS
        public static final RegistryObject<Block> CHARCOAL_GRAY_WOOL = registerBlock("charcoal_gray_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
        public static final RegistryObject<Block> CHARCOAL_GRAY_GLASS = registerBlock("charcoal_gray_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> CHARCOAL_GRAY_CONCRETE = registerBlock("charcoal_gray_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_CONCRETE)));
        public static final RegistryObject<Block> CHARCOAL_GRAY_CONCRETE_POWDER = registerBlock("charcoal_gray_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.CHARCOAL_GRAY_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.BLACK_CONCRETE_POWDER)));
        public static final RegistryObject<Block> CHARCOAL_GRAY_TERRACOTTA = registerBlock("charcoal_gray_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_TERRACOTTA)));
        public static final RegistryObject<Block> CHARCOAL_GRAY_CARPET = registerBlock("charcoal_gray_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_CARPET)));

        // TAUPE BLOCKS
        public static final RegistryObject<Block> TAUPE_WOOL = registerBlock("taupe_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
        public static final RegistryObject<Block> TAUPE_GLASS = registerBlock("taupe_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> TAUPE_CONCRETE = registerBlock("taupe_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_CONCRETE)));
        public static final RegistryObject<Block> TAUPE_CONCRETE_POWDER = registerBlock("taupe_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.TAUPE_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.BLACK_CONCRETE_POWDER)));
        public static final RegistryObject<Block> TAUPE_TERRACOTTA = registerBlock("taupe_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_TERRACOTTA)));
        public static final RegistryObject<Block> TAUPE_CARPET = registerBlock("taupe_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_CARPET)));

        // LAVENDER BLOCKS
        public static final RegistryObject<Block> LAVENDER_WOOL = registerBlock("lavender_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.PURPLE_WOOL)));
        public static final RegistryObject<Block> LAVENDER_GLASS = registerBlock("lavender_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.PURPLE_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> LAVENDER_CONCRETE = registerBlock("lavender_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.PURPLE_CONCRETE)));
        public static final RegistryObject<Block> LAVENDER_CONCRETE_POWDER = registerBlock("lavender_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.LAVENDER_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.PURPLE_CONCRETE_POWDER)));
        public static final RegistryObject<Block> LAVENDER_TERRACOTTA = registerBlock("lavender_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.PURPLE_TERRACOTTA)));
        public static final RegistryObject<Block> LAVENDER_CARPET = registerBlock("lavender_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.PURPLE_CARPET)));

        // BUILDING BLOCKS
        public static final RegistryObject<Block> WATTLE_AND_DAUB = registerBlock("wattle_and_daub",
                        () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> THATCH = registerBlock("thatch",
                        () -> new Block(BlockBehaviour.Properties.of(Material.PLANT).strength(0.5f)
                                        .sound(SoundType.GRASS).noOcclusion()));
        public static final RegistryObject<Block> THATCH_SLAB = registerBlock("thatch_slab",
                        () -> new net.minecraft.world.level.block.SlabBlock(
                                BlockBehaviour.Properties.copy(ModBlocks.THATCH.get())));
        public static final RegistryObject<Block> THATCH_SLOPE = registerBlock("thatch_slope",
                        () -> new net.minecraft.world.level.block.StairBlock(
                                () -> ModBlocks.THATCH.get().defaultBlockState(),
                                BlockBehaviour.Properties.copy(ModBlocks.THATCH.get())));
        public static final RegistryObject<Block> GUNPOWDER_TRAIL = registerBlockWithCustomItem("gunpowder_trail",
                        () -> new com.torr.materia.block.GunpowderTrailBlock(
                                BlockBehaviour.Properties.of(Material.DECORATION)
                                        .noCollission()
                                        .strength(0.0f)
                                        .sound(SoundType.SAND)
                                        .noOcclusion()),
                        (block) -> new BlockItem(block, new Item.Properties()));
        public static final RegistryObject<Block> OBSIDIAN_SLAB = registerBlock("obsidian_slab",
                        () -> new net.minecraft.world.level.block.SlabBlock(
                                BlockBehaviour.Properties.of(Material.STONE)
                                        .strength(50.0f, 1200.0f)
                                        .sound(SoundType.STONE)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> OAK_POST = registerBlock("oak_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> SPRUCE_POST = registerBlock("spruce_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> BIRCH_POST = registerBlock("birch_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> JUNGLE_POST = registerBlock("jungle_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> ACACIA_POST = registerBlock("acacia_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> CRIMSON_POST = registerBlock("crimson_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> WARPED_POST = registerBlock("warped_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> DARK_OAK_POST = registerBlock("dark_oak_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> FIRE_BRICK_BLOCK = registerBlock("fire_brick_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f)
                                        .sound(SoundType.STONE)));
        public static final RegistryObject<Block> MARBLE_COLUMN = registerBlock("marble_column",
                        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> MARBLE_CARVED = registerBlock("marble_carved",
                        () -> new MarbleCarvedBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f)
                                        .sound(SoundType.STONE).noOcclusion()));

        // METAL BLOCKS
        public static final RegistryObject<Block> ALUMINUM_BLOCK = registerBlock("aluminum_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f)
                                        .sound(SoundType.METAL)));
        public static final RegistryObject<Block> BRASS_BLOCK = registerBlock("brass_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f)
                                        .sound(SoundType.METAL)));
        public static final RegistryObject<Block> BRONZE_BLOCK = registerBlock("bronze_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f)
                                        .sound(SoundType.METAL)));
        public static final RegistryObject<Block> TIN_BLOCK = registerBlock("tin_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f)
                                        .sound(SoundType.METAL)));
        public static final RegistryObject<Block> WROUGHT_IRON_BLOCK = registerBlock("wrought_iron_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f)
                                        .sound(SoundType.METAL)));
        public static final RegistryObject<Block> ZINC_BLOCK = registerBlock("zinc_block",
                        () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f)
                                        .sound(SoundType.METAL)));

        // Rubber Tree/Wood blocks
        public static final RegistryObject<Block> RUBBER_TREE_LOG = registerBlock("rubber_tree_log",
                        () -> new net.minecraft.world.level.block.RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD)
                                        .strength(2.0f)
                                        .sound(SoundType.WOOD)));
        public static final RegistryObject<Block> RUBBER_TREE_SAPLING = registerBlock("rubber_tree_sapling",
                        () -> new RubberTreeSaplingBlock(BlockBehaviour.Properties.of(Material.PLANT)
                                        .noCollission()
                                        .instabreak()
                                        .sound(SoundType.GRASS)
                                        .randomTicks()));
        public static final RegistryObject<Block> RUBBER_TREE_LEAVES = registerBlock("rubber_tree_leaves",
                        () -> new RubberTreeLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES)
                                        .strength(0.2f)
                                        .randomTicks()
                                        .sound(SoundType.GRASS)
                                        .noOcclusion()
                                        .isValidSpawn((state, world, pos, type) -> false)
                                        .isSuffocating((state, world, pos) -> false)
                                        .isViewBlocking((state, world, pos) -> false)));
        public static final RegistryObject<Block> RUBBER_WOOD_PLANKS = registerBlock("rubber_wood_planks",
                        () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> RUBBER_WOOD_POST = registerBlock("rubber_wood_post",
                        () -> new PostBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> RUBBER_WOOD_STAIRS = registerBlock("rubber_wood_stairs",
                        () -> new net.minecraft.world.level.block.StairBlock(
                                () -> ModBlocks.RUBBER_WOOD_PLANKS.get().defaultBlockState(),
                                BlockBehaviour.Properties.copy(ModBlocks.RUBBER_WOOD_PLANKS.get())));
        public static final RegistryObject<Block> RUBBER_WOOD_SLAB = registerBlock("rubber_wood_slab",
                        () -> new net.minecraft.world.level.block.SlabBlock(
                                BlockBehaviour.Properties.copy(ModBlocks.RUBBER_WOOD_PLANKS.get())));
        public static final RegistryObject<Block> RUBBER_WOOD_FENCE = registerBlock("rubber_wood_fence",
                        () -> new net.minecraft.world.level.block.FenceBlock(
                                BlockBehaviour.Properties.copy(ModBlocks.RUBBER_WOOD_PLANKS.get())));
        public static final RegistryObject<Block> RUBBER_WOOD_FENCE_GATE = registerBlock("rubber_wood_fence_gate",
                        () -> new net.minecraft.world.level.block.FenceGateBlock(
                                BlockBehaviour.Properties.copy(ModBlocks.RUBBER_WOOD_PLANKS.get())));

        // Olive Tree/Wood blocks
        public static final RegistryObject<Block> OLIVE_TREE_LOG = registerBlock("olive_tree_log",
                        () -> new net.minecraft.world.level.block.RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD)
                                        .strength(2.0f)
                                        .sound(SoundType.WOOD)));
        public static final RegistryObject<Block> OLIVE_SAPLING = registerBlock("olive_sapling",
                        () -> new OliveTreeSaplingBlock(BlockBehaviour.Properties.of(Material.PLANT)
                                        .noCollission()
                                        .instabreak()
                                        .sound(SoundType.GRASS)
                                        .randomTicks()));
        public static final RegistryObject<Block> OLIVE_TREE_LEAVES = registerBlock("olive_tree_leaves",
                        () -> new OliveTreeLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES)
                                        .strength(0.2f)
                                        .randomTicks()
                                        .sound(SoundType.GRASS)
                                        .noOcclusion()
                                        .isValidSpawn((state, world, pos, type) -> false)
                                        .isSuffocating((state, world, pos) -> false)
                                        .isViewBlocking((state, world, pos) -> false)));


        // TABLES
        public static final RegistryObject<Block> ACACIA_TABLE = registerBlock("acacia_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> CRIMSON_TABLE = registerBlock("crimson_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> WARPED_TABLE = registerBlock("warped_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> BIRCH_TABLE = registerBlock("birch_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> DARK_OAK_TABLE = registerBlock("dark_oak_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> JUNGLE_TABLE = registerBlock("jungle_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> OAK_TABLE = registerBlock("oak_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> SPRUCE_TABLE = registerBlock("spruce_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> RUBBER_WOOD_TABLE = registerBlock("rubber_wood_table",
                        () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));

        // TRELLISES
        public static final RegistryObject<Block> OAK_TRELLIS = registerBlock("oak_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> DARK_OAK_TRELLIS = registerBlock("dark_oak_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> SPRUCE_TRELLIS = registerBlock("spruce_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> BIRCH_TRELLIS = registerBlock("birch_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> JUNGLE_TRELLIS = registerBlock("jungle_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> ACACIA_TRELLIS = registerBlock("acacia_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> CRIMSON_TRELLIS = registerBlock("crimson_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> WARPED_TRELLIS = registerBlock("warped_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> RUBBER_WOOD_TRELLIS = registerBlock("rubber_wood_trellis",
                        () -> new TrellisBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));

        // JOISTS
        public static final RegistryObject<Block> OAK_JOISTS = registerBlock("oak_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> DARK_OAK_JOISTS = registerBlock("dark_oak_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> SPRUCE_JOISTS = registerBlock("spruce_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> BIRCH_JOISTS = registerBlock("birch_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> JUNGLE_JOISTS = registerBlock("jungle_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> ACACIA_JOISTS = registerBlock("acacia_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> CRIMSON_JOISTS = registerBlock("crimson_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> WARPED_JOISTS = registerBlock("warped_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));
        public static final RegistryObject<Block> RUBBER_WOOD_JOISTS = registerBlock("rubber_wood_joists",
                        () -> new com.torr.materia.block.JoistsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f)
                                        .sound(SoundType.WOOD).noOcclusion()));

        // LOGIC GATES
        public static final RegistryObject<Block> AND_GATE = registerBlock("and_gate",
                        () -> new AndGateBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> OR_GATE = registerBlock("or_gate",
                        () -> new OrGateBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> NOR_GATE = registerBlock("nor_gate",
                        () -> new NorGateBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> XOR_GATE = registerBlock("xor_gate",
                        () -> new XorGateBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> XNOR_GATE = registerBlock("xnor_gate",
                        () -> new XnorGateBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> NAND_GATE = registerBlock("nand_gate",
                        () -> new NandGateBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> NOT_GATE = registerBlock("not_gate",
                        () -> new NotGateBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> T_FLOP = registerBlock("t_flop",
                        () -> new TFlopBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> MUX = registerBlock("mux",
                        () -> new MuxBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> TIMER = registerBlock("timer",
                        () -> new TimerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> RS_LATCH = registerBlock("rs_latch",
                        () -> new RsLatchBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> COUNTER = registerBlock("counter",
                        () -> new CounterBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> HALF_ADDER = registerBlock("half_adder",
                        () -> new HalfAdderBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0f)
                                        .sound(SoundType.STONE).noOcclusion()));

        // HELPER METHODS

        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
                RegistryObject<T> toReturn = BLOCKS.register(name, block);
                registerBlockItem(name, toReturn);
                return toReturn;
        }

        private static <T extends Block> RegistryObject<T> registerBlockWithCustomItem(String name, Supplier<T> block, java.util.function.Function<T, Item> itemFactory) {
                RegistryObject<T> toReturn = BLOCKS.register(name, block);
                ModItems.ITEMS.register(name, () -> itemFactory.apply(toReturn.get()));
                return toReturn;
        }

        private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
                if ("rock".equals(name)) {
                        ModItems.ITEMS.register(name, () -> new com.torr.materia.item.ThrowingRockBlockItem(
                                        block.get(),
                                        new Item.Properties()
                                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS)));
                } else {
                        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                                        new Item.Properties()
                                                        .tab(net.minecraft.world.item.CreativeModeTab.TAB_BUILDING_BLOCKS)));
                }
        }
}