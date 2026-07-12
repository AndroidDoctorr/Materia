package com.torr.materia;

import com.torr.materia.block.BalustradeBlock;
import com.torr.materia.block.CurtainsBlock;
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

    public static final RegistryObject<Block> STONE_BALUSTRADE = ModBlocks.registerDecorBlock("stone_balustrade",
            () -> new BalustradeBlock(BALUSTRADE_PROPS));

    private ModDecorBlocks() {
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

    private static RegistryObject<Block> shutter(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new ShutterBlock(SHUTTER_PROPS));
    }

    private static RegistryObject<Block> curtains(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new CurtainsBlock(CURTAIN_PROPS));
    }

    private static RegistryObject<Block> stoneCube(String name) {
        return ModBlocks.registerDecorBlock(name, () -> new Block(STONE_DECOR_PROPS));
    }

    public static void registerRenderLayers(java.util.function.BiConsumer<Block, net.minecraft.client.renderer.RenderType> registrar) {
        net.minecraft.client.renderer.RenderType cutout = net.minecraft.client.renderer.RenderType.cutout();
        for (RegistryObject<Block> block : ALL_SHUTTERS) {
            registrar.accept(block.get(), cutout);
        }
        for (RegistryObject<Block> block : ALL_CURTAINS) {
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
        registrar.accept(STONE_BALUSTRADE.get(), cutout);
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
            STONE_COLUMN, MARBLE_COLUMN, LIMESTONE_COLUMN, SANDSTONE_COLUMN, BLACKSTONE_COLUMN
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
