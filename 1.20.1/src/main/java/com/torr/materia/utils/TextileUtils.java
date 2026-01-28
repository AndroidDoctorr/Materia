package com.torr.materia.utils;

import com.torr.materia.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;

public final class TextileUtils {
    private TextileUtils() {}

    @Nullable
    public static Item getStringItemForClump(Item clump) {
        if (clump == ModItems.CLUMP_OF_WOOL.get()) return Items.STRING;

        if (clump == ModItems.CLUMP_OF_WHITE_WOOL.get()) return ModItems.WHITE_STRING.get();
        if (clump == ModItems.CLUMP_OF_LIGHT_GRAY_WOOL.get()) return ModItems.LIGHT_GRAY_STRING.get();
        if (clump == ModItems.CLUMP_OF_GRAY_WOOL.get()) return ModItems.GRAY_STRING.get();
        if (clump == ModItems.CLUMP_OF_BLACK_WOOL.get()) return ModItems.BLACK_STRING.get();
        if (clump == ModItems.CLUMP_OF_BROWN_WOOL.get()) return ModItems.BROWN_STRING.get();
        if (clump == ModItems.CLUMP_OF_RED_WOOL.get()) return ModItems.RED_STRING.get();
        if (clump == ModItems.CLUMP_OF_ORANGE_WOOL.get()) return ModItems.ORANGE_STRING.get();
        if (clump == ModItems.CLUMP_OF_YELLOW_WOOL.get()) return ModItems.YELLOW_STRING.get();
        if (clump == ModItems.CLUMP_OF_LIME_WOOL.get()) return ModItems.LIME_STRING.get();
        if (clump == ModItems.CLUMP_OF_GREEN_WOOL.get()) return ModItems.GREEN_STRING.get();
        if (clump == ModItems.CLUMP_OF_CYAN_WOOL.get()) return ModItems.CYAN_STRING.get();
        if (clump == ModItems.CLUMP_OF_LIGHT_BLUE_WOOL.get()) return ModItems.LIGHT_BLUE_STRING.get();
        if (clump == ModItems.CLUMP_OF_BLUE_WOOL.get()) return ModItems.BLUE_STRING.get();
        if (clump == ModItems.CLUMP_OF_PURPLE_WOOL.get()) return ModItems.PURPLE_STRING.get();
        if (clump == ModItems.CLUMP_OF_MAGENTA_WOOL.get()) return ModItems.MAGENTA_STRING.get();
        if (clump == ModItems.CLUMP_OF_PINK_WOOL.get()) return ModItems.PINK_STRING.get();

        // Custom colors
        if (clump == ModItems.CLUMP_OF_OCHRE_WOOL.get()) return ModItems.OCHRE_STRING.get();
        if (clump == ModItems.CLUMP_OF_RED_OCHRE_WOOL.get()) return ModItems.RED_OCHRE_STRING.get();
        if (clump == ModItems.CLUMP_OF_OLIVE_WOOL.get()) return ModItems.OLIVE_STRING.get();
        if (clump == ModItems.CLUMP_OF_INDIGO_WOOL.get()) return ModItems.INDIGO_STRING.get();
        if (clump == ModItems.CLUMP_OF_LAVENDER_WOOL.get()) return ModItems.LAVENDER_STRING.get();
        if (clump == ModItems.CLUMP_OF_TYRIAN_PURPLE_WOOL.get()) return ModItems.TYRIAN_PURPLE_STRING.get();
        if (clump == ModItems.CLUMP_OF_CHARCOAL_GRAY_WOOL.get()) return ModItems.CHARCOAL_GRAY_STRING.get();

        return null;
    }
}

