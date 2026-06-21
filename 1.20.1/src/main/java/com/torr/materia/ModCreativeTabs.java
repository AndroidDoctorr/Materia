package com.torr.materia;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;
import java.util.List;

public class ModCreativeTabs {
    private static final List<String> PLANT_AND_CROP_ORDER = List.of(
            "esparto",
            "wild_rice", "wild_cotton",
            "rice_crop", "cotton_crop", "tea_bush",
            "rice_seeds", "cotton_seeds", "tea_seeds",
            "shelled_rice", "cooked_rice", "tea_leaves",
            "cotton"
    );

    private static final List<String> COLORED_COTTON_ORDER = List.of(
            "black_cotton", "blue_cotton", "brown_cotton", "charcoal_gray_cotton", "cyan_cotton",
            "gray_cotton", "green_cotton", "indigo_cotton", "lavender_cotton", "light_blue_cotton",
            "light_gray_cotton", "lime_cotton", "magenta_cotton", "ochre_cotton", "olive_cotton",
            "orange_cotton", "pink_cotton", "purple_cotton", "red_cotton", "red_ochre_cotton",
            "taupe_cotton", "tyrian_purple_cotton", "yellow_cotton"
    );

    private static final List<String> NEW_TREE_ORDER = List.of(
            "palm_log", "palm_leaves", "palm_sapling",
            "cypress_log", "cypress_leaves", "cypress_sapling",
            "baobab_log", "baobab_leaves", "baobab_sapling"
    );

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

    private static int sortPriority(ResourceLocation id) {
        if (id == null) return Integer.MAX_VALUE;
        String path = id.getPath();

        int plantIdx = PLANT_AND_CROP_ORDER.indexOf(path);
        if (plantIdx >= 0) return plantIdx;

        int cottonIdx = COLORED_COTTON_ORDER.indexOf(path);
        if (cottonIdx >= 0) return PLANT_AND_CROP_ORDER.size() + cottonIdx;

        int treeIdx = NEW_TREE_ORDER.indexOf(path);
        if (treeIdx >= 0) return PLANT_AND_CROP_ORDER.size() + COLORED_COTTON_ORDER.size() + treeIdx;

        return 100_000;
    }
}
