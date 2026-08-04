package com.torr.materia;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;
import java.util.List;

public class ModCreativeTabs {
    private static final List<String> PLANT_AND_CROP_ORDER = List.of(
            "esparto", "agave", "yucca", "plantain", "reeds",
            "white_lily", "bluebonnet", "purple_coneflower", "fuchsia", "marigold", "hibiscus", "lotus",
            "wild_rice", "wild_cotton",
            "rice_crop", "cotton_crop", "tea_bush", "taro_crop",
            "rice_seeds", "cotton_seeds", "tea_seeds", "taro",
            "shelled_rice", "cooked_rice", "beans_and_rice", "cooked_taro", "tea_leaves", "fruit_leather",
            "cotton", "burrito", "chili", "baked_squash"
    );

    private static final List<String> COLORED_COTTON_ORDER = List.of(
            "black_cotton", "blue_cotton", "brown_cotton", "charcoal_gray_cotton", "cyan_cotton",
            "gray_cotton", "green_cotton", "indigo_cotton", "lavender_cotton", "light_blue_cotton",
            "light_gray_cotton", "lime_cotton", "magenta_cotton", "ochre_cotton", "olive_cotton",
            "burgundy_cotton", "tan_cotton", "teal_cotton",
            "orange_cotton", "pink_cotton", "purple_cotton", "red_cotton", "red_ochre_cotton",
            "taupe_cotton", "tyrian_purple_cotton", "yellow_cotton"
    );

    private static final List<String> NEW_TREE_ORDER = List.of(
            "palm_log", "palm_leaves", "palm_sapling",
            "cypress_log", "cypress_leaves", "cypress_sapling",
            "baobab_log", "baobab_leaves", "baobab_sapling",
            "maple_log", "maple_leaves", "maple_sapling",
            "fig_log", "fig_leaves", "fig_sapling",
            "rough_fig_plank", "smooth_fig_plank", "fig_planks", "fig_stairs", "fig_slab",
            "fig_fence", "fig_fence_gate", "fig_door", "fig_trapdoor", "fig_joists", "fig_post", "fig_table", "fig_trellis",
            "cedar_log", "cedar_leaves", "cedar_sapling",
            "rough_cedar_plank", "smooth_cedar_plank", "cedar_planks", "cedar_stairs", "cedar_slab",
            "cedar_fence", "cedar_fence_gate", "cedar_door", "cedar_trapdoor", "cedar_joists", "cedar_post", "cedar_table", "cedar_trellis",
            "eucalyptus_log", "rainbow_eucalyptus_log", "eucalyptus_leaves", "rainbow_eucalyptus_leaves",
            "eucalyptus_sapling", "rainbow_eucalyptus_sapling",
            "rough_eucalyptus_plank", "smooth_eucalyptus_plank", "eucalyptus_planks", "eucalyptus_stairs", "eucalyptus_slab",
            "eucalyptus_fence", "eucalyptus_fence_gate", "eucalyptus_door", "eucalyptus_trapdoor", "eucalyptus_joists", "eucalyptus_post", "eucalyptus_table", "eucalyptus_trellis"
    );

    private static final List<String> ROOF_ORDER = List.of(
            "roof_frame", "clay_roof_tile", "terracotta_roof_tile", "roof_tiles", "roof_copper", "shingle", "shingle_roof",
            "thatch_roof", "thatch_slope", "crushed_ceramic"
    );

    private enum TabCategory {
        COMBAT,
        TOOLS,
        FOOD,
        COLORED,
        DECORATIONS,
        REDSTONE,
        FUNCTIONAL,
        NATURAL,
        BUILDING,
        INGREDIENTS
    }

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, materia.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TORRS_MOD_TAB = TABS.register("materia", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.materia"))
                    .icon(() -> new ItemStack(ModItems.HAMMER_STONE.get()))
                    .displayItems((params, output) -> ForgeRegistries.ITEMS.getValues().stream()
                            .filter(item -> item != null && item != net.minecraft.world.item.Items.AIR)
                            .filter(item -> {
                                ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
                                return key != null && materia.MOD_ID.equals(key.getNamespace());
                            })
                            .sorted(Comparator
                                    .comparingInt((Item item) -> sortPriority(ForgeRegistries.ITEMS.getKey(item)))
                                    .thenComparing(item -> {
                                        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
                                        return key == null ? "" : key.getPath();
                                    }))
                            .forEach(output::accept))
                    .build()
    );

    public static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        if (tabKey.location().getNamespace().equals(materia.MOD_ID)) {
            return;
        }

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if (id == null || !materia.MOD_ID.equals(id.getNamespace())) {
                continue;
            }
            if (matchesTab(tabKey, categorize(id.getPath(), item))) {
                event.accept(item);
            }
        }
    }

    private static boolean matchesTab(ResourceKey<CreativeModeTab> tabKey, TabCategory category) {
        return switch (category) {
            case COMBAT -> tabKey == CreativeModeTabs.COMBAT;
            case TOOLS -> tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES;
            case FOOD -> tabKey == CreativeModeTabs.FOOD_AND_DRINKS;
            case COLORED -> tabKey == CreativeModeTabs.COLORED_BLOCKS;
            case DECORATIONS -> tabKey == CreativeModeTabs.BUILDING_BLOCKS;
            case REDSTONE -> tabKey == CreativeModeTabs.REDSTONE_BLOCKS;
            case FUNCTIONAL -> tabKey == CreativeModeTabs.FUNCTIONAL_BLOCKS;
            case NATURAL -> tabKey == CreativeModeTabs.NATURAL_BLOCKS;
            case BUILDING -> tabKey == CreativeModeTabs.BUILDING_BLOCKS;
            case INGREDIENTS -> tabKey == CreativeModeTabs.INGREDIENTS;
        };
    }

    private static TabCategory categorize(String path, Item item) {
        if (isCombat(path)) {
            return TabCategory.COMBAT;
        }
        if (isTool(path)) {
            return TabCategory.TOOLS;
        }
        if (isFood(path, item)) {
            return TabCategory.FOOD;
        }
        if (isColoredBlock(path)) {
            return TabCategory.COLORED;
        }
        if (isDecoration(path)) {
            return TabCategory.DECORATIONS;
        }
        if (isRedstone(path)) {
            return TabCategory.REDSTONE;
        }
        if (isNatural(path)) {
            return TabCategory.NATURAL;
        }
        if (isBuilding(path)) {
            return TabCategory.BUILDING;
        }
        if (isFunctional(path)) {
            return TabCategory.FUNCTIONAL;
        }
        return TabCategory.INGREDIENTS;
    }

    private static boolean isCombat(String path) {
        return path.contains("cannonball")
                || path.contains("_sword")
                || path.contains("_spear")
                || path.contains("_bow")
                || path.contains("_arrow")
                || path.contains("_helmet")
                || path.contains("_chestpiece")
                || path.contains("_leggings")
                || path.contains("_boot")
                || path.contains("shoulder")
                || path.contains("backpiece")
                || path.contains("shield")
                || path.equals("cannon");
    }

    private static boolean isTool(String path) {
        return path.contains("_pickaxe")
                || path.contains("_axe")
                || path.contains("_shovel")
                || path.contains("hammer")
                || path.contains("knife")
                || path.contains("_saw")
                || path.contains("spindle")
                || path.contains("flute")
                || path.contains("harp")
                || path.contains("tongs")
                || path.contains("needle")
                || path.contains("chisel")
                || path.contains("mallet")
                || path.contains("bindle")
                || path.contains("bundle")
                || path.endsWith("_sack")
                || path.contains("maraca")
                || path.equals("drum")
                || path.contains("_drums")
                || path.contains("lashing")
                || path.equals("cart")
                || path.equals("cart_base")
                || path.equals("cart_cover")
                || path.equals("crucible");
    }

    private static boolean isFood(String path, Item item) {
        if (path.contains("maraca")
                || path.equals("drum")
                || path.contains("_drums")
                || path.equals("dried_gourd")) {
            return false;
        }
        return item.isEdible()
                || path.equals("tea_cup")
                || path.equals("tea_bottle")
                || path.contains("wine_cup")
                || path.contains("beer_cup")
                || path.contains("milk_cup");
    }

    private static boolean isColoredBlock(String path) {
        return path.endsWith("_wool")
                || path.endsWith("_glass")
                || path.endsWith("_concrete")
                || path.endsWith("_concrete_powder")
                || path.endsWith("_terracotta")
                || path.endsWith("_candle")
                || path.endsWith("_carpet")
                || path.endsWith("_blanket")
                || path.endsWith("_bed")
                || path.endsWith("_cotton");
    }

    private static boolean isDecoration(String path) {
        return path.equals("esparto")
                || path.equals("reeds")
                || path.equals("agave")
                || path.equals("yucca")
                || path.equals("plantain")
                || path.equals("lotus")
                || path.endsWith("_lily")
                || path.equals("bluebonnet")
                || path.equals("marigold")
                || path.equals("hibiscus")
                || path.equals("fuchsia")
                || path.equals("purple_coneflower")
                || path.endsWith("_curtains")
                || path.startsWith("rug_")
                || path.endsWith("_urn")
                || path.endsWith("_planter")
                || path.endsWith("_balustrade")
                || path.endsWith("_cornice")
                || path.endsWith("_bracket")
                || path.endsWith("_spire")
                || path.endsWith("_finial")
                || path.endsWith("_acorn_finial")
                || path.endsWith("_bust")
                || path.endsWith("_body");
    }

    private static boolean isRedstone(String path) {
        return (path.endsWith("_gate") && !path.contains("fence"))
                || path.equals("mux")
                || path.equals("timer")
                || path.equals("t_flop")
                || path.equals("rs_latch")
                || path.equals("counter")
                || path.equals("half_adder");
    }

    private static boolean isFunctional(String path) {
        return path.contains("kiln")
                || path.contains("oven")
                || path.contains("anvil")
                || path.contains("_pot")
                || path.contains("spinning_wheel")
                || path.contains("drying_rack")
                || path.contains("frame_loom")
                || path.contains("amphora")
                || path.equals("cannon")
                || path.contains("fire_pit")
                || path.contains("chimney")
                || path.contains("crafting_table")
                || path.contains("beehive")
                || path.contains("basket")
                || path.contains("primitive_crafting");
    }

    private static boolean isNatural(String path) {
        return path.contains("_log")
                || path.contains("_leaves")
                || path.contains("_sapling")
                || path.startsWith("wild_")
                || path.endsWith("_crop")
                || path.equals("tea_bush")
                || path.contains("_ore")
                || path.contains("gravel_")
                || path.equals("rock")
                || path.equals("pebble")
                || path.equals("earth")
                || path.equals("bauxite")
                || path.equals("marble")
                || path.equals("limestone")
                || path.contains("flower");
    }

    private static boolean isBuilding(String path) {
        return path.contains("_plank")
                || path.contains("_planks")
                || path.contains("_stairs")
                || path.contains("_slab")
                || path.contains("_fence")
                || path.contains("_joists")
                || path.contains("_post")
                || path.contains("_table")
                || path.contains("_trellis")
                || path.endsWith("_door")
                || path.endsWith("_trapdoor")
                || path.endsWith("_shutters")
                || path.contains("roof")
                || path.contains("thatch")
                || path.contains("wattle")
                || path.contains("daub")
                || path.contains("brick")
                || path.contains("frame")
                || path.contains("tile")
                || path.endsWith("_block")
                || path.endsWith("_column")
                || path.endsWith("_column_capital")
                || path.endsWith("_column_base")
                || path.equals("salt_block");
    }

    private static int sortPriority(ResourceLocation id) {
        if (id == null) {
            return Integer.MAX_VALUE;
        }
        String path = id.getPath();

        int plantIdx = PLANT_AND_CROP_ORDER.indexOf(path);
        if (plantIdx >= 0) {
            return plantIdx;
        }

        int cottonIdx = COLORED_COTTON_ORDER.indexOf(path);
        if (cottonIdx >= 0) {
            return PLANT_AND_CROP_ORDER.size() + cottonIdx;
        }

        int treeIdx = NEW_TREE_ORDER.indexOf(path);
        if (treeIdx >= 0) {
            return PLANT_AND_CROP_ORDER.size() + COLORED_COTTON_ORDER.size() + treeIdx;
        }

        int roofIdx = ROOF_ORDER.indexOf(path);
        if (roofIdx >= 0) {
            return PLANT_AND_CROP_ORDER.size() + COLORED_COTTON_ORDER.size() + NEW_TREE_ORDER.size() + roofIdx;
        }

        return 100_000;
    }
}
