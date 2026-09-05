package com.torr.materia;

import com.torr.materia.block.AwningBlock;
import com.torr.materia.block.BalustradeBlock;
import com.torr.materia.block.BracketBlock;
import com.torr.materia.block.CorniceBlock;
import com.torr.materia.block.CurtainsBlock;
import com.torr.materia.block.FinialBlock;
import com.torr.materia.block.MetalFinialCrossBlock;
// import com.torr.materia.block.AlphabetBlock; // reserved for future Materia Signage mod
// import com.torr.materia.block.MosaicBlock; // reserved for future Materia Signage mod
import com.torr.materia.block.PlanterBlock;
import com.torr.materia.block.ShutterBlock;
import com.torr.materia.block.UrnBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

/**
 * Decorative shutters, curtains, and carved stone blocks.
 */
public final class ModDecorBlocks {

    private static final BlockBehaviour.Properties SHUTTER_PROPS = BlockBehaviour.Properties.of()
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .noOcclusion();

    private static final BlockBehaviour.Properties CURTAIN_PROPS = BlockBehaviour.Properties.of()
            .strength(0.5F)
            .sound(SoundType.WOOL)
            .noOcclusion();

    private static final BlockBehaviour.Properties AWNING_PROPS = BlockBehaviour.Properties.of()
            .strength(0.5F)
            .sound(SoundType.WOOL)
            .noOcclusion();

    private static final BlockBehaviour.Properties STONE_DECOR_PROPS = BlockBehaviour.Properties.of()
            .strength(1.5F)
            .sound(SoundType.STONE);

    private static final BlockBehaviour.Properties STONE_SCULPTURE_PROPS = BlockBehaviour.Properties.of()
            .strength(1.5F)
            .sound(SoundType.STONE)
            .noOcclusion();

    private static final BlockBehaviour.Properties BALUSTRADE_PROPS = BlockBehaviour.Properties.of()
            .strength(1.5F)
            .sound(SoundType.STONE)
            .noOcclusion();

    private static final BlockBehaviour.Properties METAL_FINIAL_PROPS = BlockBehaviour.Properties.of()
            .strength(2.0F)
            .sound(SoundType.METAL)
            .noOcclusion();

    private static final BlockBehaviour.Properties WROUGHT_IRON_DECOR_PROPS = BlockBehaviour.Properties.of()
            .strength(5.0F)
            .sound(SoundType.METAL)
            .noOcclusion();

    public static final RegistryObject<Block> OAK_SHUTTERS = shutter("oak_shutters");
    public static final RegistryObject<Block> SPRUCE_SHUTTERS = shutter("spruce_shutters");
    public static final RegistryObject<Block> BIRCH_SHUTTERS = shutter("birch_shutters");
    public static final RegistryObject<Block> JUNGLE_SHUTTERS = shutter("jungle_shutters");
    public static final RegistryObject<Block> ACACIA_SHUTTERS = shutter("acacia_shutters");
    public static final RegistryObject<Block> DARK_OAK_SHUTTERS = shutter("dark_oak_shutters");
    public static final RegistryObject<Block> CHERRY_SHUTTERS = shutter("cherry_shutters");
    public static final RegistryObject<Block> MANGROVE_SHUTTERS = shutter("mangrove_shutters");
    public static final RegistryObject<Block> RUBBER_WOOD_SHUTTERS = shutter("rubber_wood_shutters");
    public static final RegistryObject<Block> FIG_SHUTTERS = shutter("fig_shutters");
    public static final RegistryObject<Block> CEDAR_SHUTTERS = shutter("cedar_shutters");
    public static final RegistryObject<Block> EUCALYPTUS_SHUTTERS = shutter("eucalyptus_shutters");

    public static final RegistryObject<Block> WHITE_CURTAINS = curtains("white_curtains");
    public static final RegistryObject<Block> ORANGE_CURTAINS = curtains("orange_curtains");
    public static final RegistryObject<Block> MAGENTA_CURTAINS = curtains("magenta_curtains");
    public static final RegistryObject<Block> LIGHT_BLUE_CURTAINS = curtains("light_blue_curtains");
    public static final RegistryObject<Block> YELLOW_CURTAINS = curtains("yellow_curtains");
    public static final RegistryObject<Block> LIME_CURTAINS = curtains("lime_curtains");
    public static final RegistryObject<Block> PINK_CURTAINS = curtains("pink_curtains");
    public static final RegistryObject<Block> GRAY_CURTAINS = curtains("gray_curtains");
    public static final RegistryObject<Block> LIGHT_GRAY_CURTAINS = curtains("light_gray_curtains");
    public static final RegistryObject<Block> CYAN_CURTAINS = curtains("cyan_curtains");
    public static final RegistryObject<Block> PURPLE_CURTAINS = curtains("purple_curtains");
    public static final RegistryObject<Block> BLUE_CURTAINS = curtains("blue_curtains");
    public static final RegistryObject<Block> BROWN_CURTAINS = curtains("brown_curtains");
    public static final RegistryObject<Block> GREEN_CURTAINS = curtains("green_curtains");
    public static final RegistryObject<Block> RED_CURTAINS = curtains("red_curtains");
    public static final RegistryObject<Block> BLACK_CURTAINS = curtains("black_curtains");
    public static final RegistryObject<Block> OCHRE_CURTAINS = curtains("ochre_curtains");
    public static final RegistryObject<Block> RED_OCHRE_CURTAINS = curtains("red_ochre_curtains");
    public static final RegistryObject<Block> LAVENDER_CURTAINS = curtains("lavender_curtains");
    public static final RegistryObject<Block> INDIGO_CURTAINS = curtains("indigo_curtains");
    public static final RegistryObject<Block> TYRIAN_PURPLE_CURTAINS = curtains("tyrian_purple_curtains");
    public static final RegistryObject<Block> TAUPE_CURTAINS = curtains("taupe_curtains");
    public static final RegistryObject<Block> OLIVE_CURTAINS = curtains("olive_curtains");
    public static final RegistryObject<Block> CHARCOAL_GRAY_CURTAINS = curtains("charcoal_gray_curtains");
    public static final RegistryObject<Block> BURGUNDY_CURTAINS = curtains("burgundy_curtains");
    public static final RegistryObject<Block> TEAL_CURTAINS = curtains("teal_curtains");

    public static final RegistryObject<Block> ORANGE_AWNING = awning("orange_awning");
    public static final RegistryObject<Block> MAGENTA_AWNING = awning("magenta_awning");
    public static final RegistryObject<Block> LIGHT_BLUE_AWNING = awning("light_blue_awning");
    public static final RegistryObject<Block> YELLOW_AWNING = awning("yellow_awning");
    public static final RegistryObject<Block> LIME_AWNING = awning("lime_awning");
    public static final RegistryObject<Block> PINK_AWNING = awning("pink_awning");
    public static final RegistryObject<Block> GRAY_AWNING = awning("gray_awning");
    public static final RegistryObject<Block> LIGHT_GRAY_AWNING = awning("light_gray_awning");
    public static final RegistryObject<Block> CYAN_AWNING = awning("cyan_awning");
    public static final RegistryObject<Block> PURPLE_AWNING = awning("purple_awning");
    public static final RegistryObject<Block> BLUE_AWNING = awning("blue_awning");
    public static final RegistryObject<Block> BROWN_AWNING = awning("brown_awning");
    public static final RegistryObject<Block> GREEN_AWNING = awning("green_awning");
    public static final RegistryObject<Block> RED_AWNING = awning("red_awning");
    public static final RegistryObject<Block> BLACK_AWNING = awning("black_awning");
    public static final RegistryObject<Block> OCHRE_AWNING = awning("ochre_awning");
    public static final RegistryObject<Block> RED_OCHRE_AWNING = awning("red_ochre_awning");
    public static final RegistryObject<Block> LAVENDER_AWNING = awning("lavender_awning");
    public static final RegistryObject<Block> INDIGO_AWNING = awning("indigo_awning");
    public static final RegistryObject<Block> TYRIAN_PURPLE_AWNING = awning("tyrian_purple_awning");
    public static final RegistryObject<Block> TAUPE_AWNING = awning("taupe_awning");
    public static final RegistryObject<Block> OLIVE_AWNING = awning("olive_awning");
    public static final RegistryObject<Block> CHARCOAL_GRAY_AWNING = awning("charcoal_gray_awning");
    public static final RegistryObject<Block> BURGUNDY_AWNING = awning("burgundy_awning");
    public static final RegistryObject<Block> TEAL_AWNING = awning("teal_awning");
    public static final RegistryObject<Block> TAN_AWNING = awning("tan_awning");

    public static final RegistryObject<Block> STONE_TILES = stoneCube("stone_tiles");
    public static final RegistryObject<Block> STONE_BRICKS_SMALL = stoneCube("stone_bricks_small");
    public static final RegistryObject<Block> MARBLE_BRICKS = stoneCube("marble_bricks");
    public static final RegistryObject<Block> MARBLE_BRICKS_SMALL = stoneCube("marble_bricks_small");
    public static final RegistryObject<Block> POLISHED_MARBLE = stoneCube("polished_marble");
    public static final RegistryObject<Block> MARBLE_TILES = stoneCube("marble_tiles");
    public static final RegistryObject<Block> LIMESTONE_BRICKS = stoneCube("limestone_bricks");
    public static final RegistryObject<Block> LIMESTONE_BRICKS_SMALL = stoneCube("limestone_bricks_small");
    public static final RegistryObject<Block> POLISHED_LIMESTONE = stoneCube("polished_limestone");
    public static final RegistryObject<Block> LIMESTONE_TILES = stoneCube("limestone_tiles");
    public static final RegistryObject<Block> LIMESTONE_CHISELED = stoneCube("limestone_chiseled");
    public static final RegistryObject<Block> SANDSTONE_BRICKS = stoneCube("sandstone_bricks");
    public static final RegistryObject<Block> SANDSTONE_TILES = stoneCube("sandstone_tiles");
    public static final RegistryObject<Block> BLACKSTONE_TILES = stoneCube("blackstone_tiles");
    public static final RegistryObject<Block> TERRACOTTA_TILES = stoneCube("terracotta_tiles");

    public static final RegistryObject<Block> STONE_URN = sculptureUrn("stone_urn");
    public static final RegistryObject<Block> MARBLE_URN = sculptureUrn("marble_urn");
    public static final RegistryObject<Block> LIMESTONE_URN = sculptureUrn("limestone_urn");
    public static final RegistryObject<Block> SANDSTONE_URN = sculptureUrn("sandstone_urn");
    public static final RegistryObject<Block> BLACKSTONE_URN = sculptureUrn("blackstone_urn");
    public static final RegistryObject<Block> TERRACOTTA_URN = sculptureUrn("terracotta_urn");

    public static final RegistryObject<Block> STONE_PLANTER = sculpturePlanter("stone_planter");
    public static final RegistryObject<Block> MARBLE_PLANTER = sculpturePlanter("marble_planter");
    public static final RegistryObject<Block> LIMESTONE_PLANTER = sculpturePlanter("limestone_planter");
    public static final RegistryObject<Block> SANDSTONE_PLANTER = sculpturePlanter("sandstone_planter");
    public static final RegistryObject<Block> BLACKSTONE_PLANTER = sculpturePlanter("blackstone_planter");
    public static final RegistryObject<Block> TERRACOTTA_PLANTER = sculpturePlanter("terracotta_planter");

    public static final RegistryObject<Block> STONE_COLUMN = stoneColumn("stone_column");
    public static final RegistryObject<Block> MARBLE_COLUMN = stoneColumn("marble_column");
    public static final RegistryObject<Block> LIMESTONE_COLUMN = stoneColumn("limestone_column");
    public static final RegistryObject<Block> SANDSTONE_COLUMN = stoneColumn("sandstone_column");
    public static final RegistryObject<Block> BLACKSTONE_COLUMN = stoneColumn("blackstone_column");
    public static final RegistryObject<Block> TERRACOTTA_COLUMN = stoneColumn("terracotta_column");

    public static final RegistryObject<Block> STONE_COLUMN_CAPITAL = stoneColumn("stone_column_capital");
    public static final RegistryObject<Block> MARBLE_COLUMN_CAPITAL = stoneColumn("marble_column_capital");
    public static final RegistryObject<Block> LIMESTONE_COLUMN_CAPITAL = stoneColumn("limestone_column_capital");
    public static final RegistryObject<Block> SANDSTONE_COLUMN_CAPITAL = stoneColumn("sandstone_column_capital");
    public static final RegistryObject<Block> BLACKSTONE_COLUMN_CAPITAL = stoneColumn("blackstone_column_capital");
    public static final RegistryObject<Block> TERRACOTTA_COLUMN_CAPITAL = stoneColumn("terracotta_column_capital");

    public static final RegistryObject<Block> STONE_COLUMN_BASE = stoneColumn("stone_column_base");
    public static final RegistryObject<Block> MARBLE_COLUMN_BASE = stoneColumn("marble_column_base");
    public static final RegistryObject<Block> LIMESTONE_COLUMN_BASE = stoneColumn("limestone_column_base");
    public static final RegistryObject<Block> SANDSTONE_COLUMN_BASE = stoneColumn("sandstone_column_base");
    public static final RegistryObject<Block> BLACKSTONE_COLUMN_BASE = stoneColumn("blackstone_column_base");
    public static final RegistryObject<Block> TERRACOTTA_COLUMN_BASE = stoneColumn("terracotta_column_base");

    public static final RegistryObject<Block> MARBLE_CORNICE = cornice("marble_cornice");
    public static final RegistryObject<Block> MARBLE_BRACKET = bracket("marble_bracket");
    public static final RegistryObject<Block> LIMESTONE_CORNICE = cornice("limestone_cornice");
    public static final RegistryObject<Block> LIMESTONE_BRACKET = bracket("limestone_bracket");
    public static final RegistryObject<Block> STONE_CORNICE = cornice("stone_cornice");
    public static final RegistryObject<Block> STONE_BRACKET = bracket("stone_bracket");
    public static final RegistryObject<Block> SANDSTONE_CORNICE = cornice("sandstone_cornice");
    public static final RegistryObject<Block> SANDSTONE_BRACKET = bracket("sandstone_bracket");
    public static final RegistryObject<Block> BLACKSTONE_CORNICE = cornice("blackstone_cornice");
    public static final RegistryObject<Block> BLACKSTONE_BRACKET = bracket("blackstone_bracket");
    public static final RegistryObject<Block> TERRACOTTA_CORNICE = cornice("terracotta_cornice");
    public static final RegistryObject<Block> TERRACOTTA_BRACKET = bracket("terracotta_bracket");
    public static final RegistryObject<Block> WROUGHT_IRON_BRACKET = wroughtIronBracket("wrought_iron_bracket");

    public static final RegistryObject<Block> STONE_BALUSTRADE = balustrade("stone_balustrade");
    public static final RegistryObject<Block> LIMESTONE_BALUSTRADE = balustrade("limestone_balustrade");
    public static final RegistryObject<Block> MARBLE_BALUSTRADE = balustrade("marble_balustrade");
    public static final RegistryObject<Block> TERRACOTTA_BALUSTRADE = balustrade("terracotta_balustrade");
    public static final RegistryObject<Block> BLACKSTONE_BALUSTRADE = balustrade("blackstone_balustrade");
    public static final RegistryObject<Block> SANDSTONE_BALUSTRADE = balustrade("sandstone_balustrade");

    public static final RegistryObject<Block> BRONZE_SPIRE = tallFinial("bronze_spire");
    public static final RegistryObject<Block> GOLD_SPIRE = tallFinial("gold_spire");
    public static final RegistryObject<Block> WROUGHT_IRON_SPIRE = tallFinial("wrought_iron_spire");
    public static final RegistryObject<Block> BRONZE_BALL_FINIAL = tallFinial("bronze_ball_finial");
    public static final RegistryObject<Block> GOLD_BALL_FINIAL = tallFinial("gold_ball_finial");
    public static final RegistryObject<Block> WROUGHT_IRON_BALL_FINIAL = tallFinial("wrought_iron_ball_finial");
    public static final RegistryObject<Block> BRONZE_ACORN_FINIAL = finialCross("bronze_acorn_finial");
    public static final RegistryObject<Block> GOLD_ACORN_FINIAL = finialCross("gold_acorn_finial");
    public static final RegistryObject<Block> WROUGHT_IRON_ACORN_FINIAL = finialCross("wrought_iron_acorn_finial");

    public static final RegistryObject<Block> STONE_ACORN_FINIAL = stoneAcornFinial("stone_acorn_finial");
    public static final RegistryObject<Block> LIMESTONE_ACORN_FINIAL = stoneAcornFinial("limestone_acorn_finial");
    public static final RegistryObject<Block> MARBLE_ACORN_FINIAL = stoneAcornFinial("marble_acorn_finial");
    public static final RegistryObject<Block> SANDSTONE_ACORN_FINIAL = stoneAcornFinial("sandstone_acorn_finial");
    public static final RegistryObject<Block> TERRACOTTA_ACORN_FINIAL = stoneAcornFinial("terracotta_acorn_finial");

    /*
     * DISABLED — reserved for a separate Materia Signage mod.
     * Reference implementations remain under 1.20.1/src/main/java (excluded from compile in build.gradle).
     *
    public static final RegistryObject<Block> MOSAIC_BLOCK = mosaicBlock("mosaic_block");

    public static final RegistryObject<Block> CHARACTER_BLOCK_1 = characterBlock("character_block_1");
    public static final RegistryObject<Block> CHARACTER_BLOCK_2 = characterBlock("character_block_2");
    public static final RegistryObject<Block> CHARACTER_BLOCK_3 = characterBlock("character_block_3");
    public static final RegistryObject<Block> CHARACTER_BLOCK_4 = characterBlock("character_block_4");
    public static final RegistryObject<Block> CHARACTER_BLOCK_5 = characterBlock("character_block_5");
    public static final RegistryObject<Block> CHARACTER_BLOCK_6 = characterBlock("character_block_6");
    public static final RegistryObject<Block> CHARACTER_BLOCK_7 = characterBlock("character_block_7");
    public static final RegistryObject<Block> CHARACTER_BLOCK_8 = characterBlock("character_block_8");
    public static final RegistryObject<Block> CHARACTER_BLOCK_9 = characterBlock("character_block_9");
    public static final RegistryObject<Block> CHARACTER_BLOCK_10 = characterBlock("character_block_10");
     */

    private ModDecorBlocks() {
    }

    private static RegistryObject<Block> balustrade(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new BalustradeBlock(BALUSTRADE_PROPS));
    }

    private static RegistryObject<Block> cornice(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new CorniceBlock(STONE_SCULPTURE_PROPS));
    }

    private static RegistryObject<Block> bracket(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new BracketBlock(STONE_SCULPTURE_PROPS));
    }

    private static RegistryObject<Block> wroughtIronBracket(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new BracketBlock(WROUGHT_IRON_DECOR_PROPS));
    }

    private static RegistryObject<Block> sculptureUrn(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new UrnBlock(STONE_SCULPTURE_PROPS));
    }

    private static RegistryObject<Block> sculpturePlanter(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new PlanterBlock(STONE_SCULPTURE_PROPS));
    }

    private static RegistryObject<Block> stoneColumn(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new Block(STONE_SCULPTURE_PROPS));
    }

    private static RegistryObject<Block> tallFinial(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new FinialBlock(
                METAL_FINIAL_PROPS.offsetType(BlockBehaviour.OffsetType.NONE)));
    }

    private static RegistryObject<Block> finialCross(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new MetalFinialCrossBlock(METAL_FINIAL_PROPS));
    }

    private static RegistryObject<Block> stoneAcornFinial(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new Block(STONE_SCULPTURE_PROPS));
    }

    private static RegistryObject<Block> shutter(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new ShutterBlock(SHUTTER_PROPS));
    }

    private static RegistryObject<Block> curtains(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new CurtainsBlock(CURTAIN_PROPS));
    }

    private static RegistryObject<Block> awning(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new AwningBlock(AWNING_PROPS));
    }

    private static RegistryObject<Block> stoneCube(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new Block(STONE_DECOR_PROPS));
    }

    /*
    private static RegistryObject<Block> mosaicBlock(String name) {
        return ModBlocks.registerDecorBlockWithCustomItem(name,
                () -> new MosaicBlock(STONE_DECOR_PROPS),
                block -> new com.torr.materia.item.MosaicBlockItem(block, new net.minecraft.world.item.Item.Properties()));
    }

    private static RegistryObject<Block> characterBlock(String name) {
        return ModBlocks.registerDecorBlockWithCustomItem(name,
                () -> new AlphabetBlock(STONE_DECOR_PROPS),
                block -> new com.torr.materia.item.AlphabetBlockItem(block, new net.minecraft.world.item.Item.Properties()));
    }
    */

    public static void registerRenderLayers(java.util.function.BiConsumer<Block, net.minecraft.client.renderer.RenderType> registrar) {
        net.minecraft.client.renderer.RenderType cutout = net.minecraft.client.renderer.RenderType.cutout();
        for (RegistryObject<Block> block : ALL_SHUTTERS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_CURTAINS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_AWNINGS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_URNS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_PLANTERS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_COLUMNS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_COLUMN_CAPITALS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_COLUMN_BASES) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_CORNICES) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_BRACKETS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_FINIALS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_BALUSTRADES) {
            registrar.accept(block.get(), cutout);
        }
    }

    private static final RegistryObject<Block>[] ALL_SHUTTERS = new RegistryObject[] {
            OAK_SHUTTERS, SPRUCE_SHUTTERS, BIRCH_SHUTTERS, JUNGLE_SHUTTERS, ACACIA_SHUTTERS,
            DARK_OAK_SHUTTERS, CHERRY_SHUTTERS, MANGROVE_SHUTTERS, RUBBER_WOOD_SHUTTERS,
            FIG_SHUTTERS, CEDAR_SHUTTERS, EUCALYPTUS_SHUTTERS
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_CURTAINS = new RegistryObject[] {
            WHITE_CURTAINS, ORANGE_CURTAINS, MAGENTA_CURTAINS, LIGHT_BLUE_CURTAINS, YELLOW_CURTAINS,
            LIME_CURTAINS, PINK_CURTAINS, GRAY_CURTAINS, LIGHT_GRAY_CURTAINS, CYAN_CURTAINS,
            PURPLE_CURTAINS, BLUE_CURTAINS, BROWN_CURTAINS, GREEN_CURTAINS, RED_CURTAINS, BLACK_CURTAINS,
            OCHRE_CURTAINS, RED_OCHRE_CURTAINS, LAVENDER_CURTAINS, INDIGO_CURTAINS, TYRIAN_PURPLE_CURTAINS,
            TAUPE_CURTAINS, OLIVE_CURTAINS, CHARCOAL_GRAY_CURTAINS, BURGUNDY_CURTAINS, TEAL_CURTAINS
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_AWNINGS = new RegistryObject[] {
            ORANGE_AWNING, MAGENTA_AWNING, LIGHT_BLUE_AWNING, YELLOW_AWNING, LIME_AWNING, PINK_AWNING,
            GRAY_AWNING, LIGHT_GRAY_AWNING, CYAN_AWNING, PURPLE_AWNING, BLUE_AWNING, BROWN_AWNING,
            GREEN_AWNING, RED_AWNING, BLACK_AWNING, OCHRE_AWNING, RED_OCHRE_AWNING, LAVENDER_AWNING,
            INDIGO_AWNING, TYRIAN_PURPLE_AWNING, TAUPE_AWNING, OLIVE_AWNING, CHARCOAL_GRAY_AWNING,
            BURGUNDY_AWNING, TEAL_AWNING, TAN_AWNING
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_URNS = new RegistryObject[] {
            STONE_URN, MARBLE_URN, LIMESTONE_URN, SANDSTONE_URN, BLACKSTONE_URN, TERRACOTTA_URN
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_PLANTERS = new RegistryObject[] {
            STONE_PLANTER, MARBLE_PLANTER, LIMESTONE_PLANTER, SANDSTONE_PLANTER, BLACKSTONE_PLANTER,
            TERRACOTTA_PLANTER
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_COLUMNS = new RegistryObject[] {
            STONE_COLUMN, MARBLE_COLUMN, LIMESTONE_COLUMN, SANDSTONE_COLUMN, BLACKSTONE_COLUMN, TERRACOTTA_COLUMN
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_COLUMN_CAPITALS = new RegistryObject[] {
            STONE_COLUMN_CAPITAL, MARBLE_COLUMN_CAPITAL, LIMESTONE_COLUMN_CAPITAL, SANDSTONE_COLUMN_CAPITAL,
            BLACKSTONE_COLUMN_CAPITAL, TERRACOTTA_COLUMN_CAPITAL
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_COLUMN_BASES = new RegistryObject[] {
            STONE_COLUMN_BASE, MARBLE_COLUMN_BASE, LIMESTONE_COLUMN_BASE, SANDSTONE_COLUMN_BASE,
            BLACKSTONE_COLUMN_BASE, TERRACOTTA_COLUMN_BASE
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_CORNICES = new RegistryObject[] {
            MARBLE_CORNICE, LIMESTONE_CORNICE, STONE_CORNICE, SANDSTONE_CORNICE, BLACKSTONE_CORNICE, TERRACOTTA_CORNICE
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_BRACKETS = new RegistryObject[] {
            MARBLE_BRACKET, LIMESTONE_BRACKET, STONE_BRACKET, SANDSTONE_BRACKET, BLACKSTONE_BRACKET,
            TERRACOTTA_BRACKET, WROUGHT_IRON_BRACKET
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_BALUSTRADES = new RegistryObject[] {
            STONE_BALUSTRADE, LIMESTONE_BALUSTRADE, MARBLE_BALUSTRADE, TERRACOTTA_BALUSTRADE,
            BLACKSTONE_BALUSTRADE, SANDSTONE_BALUSTRADE
    };

    @SuppressWarnings("unchecked")
    private static final RegistryObject<Block>[] ALL_FINIALS = new RegistryObject[] {
            BRONZE_SPIRE, GOLD_SPIRE, WROUGHT_IRON_SPIRE,
            BRONZE_BALL_FINIAL, GOLD_BALL_FINIAL, WROUGHT_IRON_BALL_FINIAL,
            BRONZE_ACORN_FINIAL, GOLD_ACORN_FINIAL, WROUGHT_IRON_ACORN_FINIAL,
            STONE_ACORN_FINIAL, LIMESTONE_ACORN_FINIAL, MARBLE_ACORN_FINIAL, SANDSTONE_ACORN_FINIAL,
            TERRACOTTA_ACORN_FINIAL
    };

    public static Block[] planterBlocks() {
        Block[] blocks = new Block[ALL_PLANTERS.length];
        for (int i = 0; i < ALL_PLANTERS.length; i++) {
            blocks[i] = ALL_PLANTERS[i].get();
        }
        return blocks;
    }

    public static Block[] urnBlocks() {
        Block[] blocks = new Block[ALL_URNS.length];
        for (int i = 0; i < ALL_URNS.length; i++) {
            blocks[i] = ALL_URNS[i].get();
        }
        return blocks;
    }
}
