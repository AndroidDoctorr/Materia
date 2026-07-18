package com.torr.materia.rug;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public final class RugWeaving {
    private RugWeaving() {
    }

    public static Optional<ItemStack> resolve(ItemStack base, ItemStack dye, ItemStack pattern) {
        if (base.isEmpty() || dye.isEmpty() || pattern.isEmpty()) {
            return Optional.empty();
        }
        if (!base.is(ModItems.RUG_BASE.get())) {
            return Optional.empty();
        }
        if (pattern.is(ModItems.RUG_1_PATTERN.get())) {
            return rugFromDye(1, dye.getItem());
        }
        if (pattern.is(ModItems.RUG_2_PATTERN.get())) {
            return rugFromDye(2, dye.getItem());
        }
        return Optional.empty();
    }

    public static boolean isRugPattern(ItemStack stack) {
        return stack.is(ModItems.RUG_1_PATTERN.get()) || stack.is(ModItems.RUG_2_PATTERN.get());
    }

    private static Optional<ItemStack> rugFromDye(int pattern, Item dyeItem) {
        Item rug = null;
        if (isRedFieldDye(dyeItem)) {
            rug = pattern == 1 ? ModBlocks.RUG_1_RED.get().asItem() : ModBlocks.RUG_2_RED.get().asItem();
        } else if (isBlueFieldDye(dyeItem)) {
            rug = pattern == 1 ? ModBlocks.RUG_1_BLUE.get().asItem() : ModBlocks.RUG_2_BLUE.get().asItem();
        } else if (dyeItem == Items.GREEN_DYE) {
            rug = pattern == 1 ? ModBlocks.RUG_1_GREEN.get().asItem() : ModBlocks.RUG_2_GREEN.get().asItem();
        } else if (isPurpleFieldDye(dyeItem)) {
            rug = pattern == 1 ? ModBlocks.RUG_1_PURPLE.get().asItem() : ModBlocks.RUG_2_PURPLE.get().asItem();
        }
        if (rug == null) {
            return Optional.empty();
        }
        return Optional.of(new ItemStack(rug));
    }

    public static boolean isRedFieldDye(Item item) {
        return item == Items.RED_DYE || item == ModItems.BURGUNDY_DYE.get();
    }

    public static boolean isBlueFieldDye(Item item) {
        return item == Items.BLUE_DYE || item == ModItems.INDIGO_DYE.get();
    }

    public static boolean isPurpleFieldDye(Item item) {
        return item == Items.PURPLE_DYE || item == ModItems.TYRIAN_PURPLE_DYE.get();
    }

    public static boolean isFieldDye(Item item) {
        return isRedFieldDye(item) || isBlueFieldDye(item) || item == Items.GREEN_DYE || isPurpleFieldDye(item);
    }

    /** Dyes accepted in the loom middle slot (vanilla {@link DyeItem} plus Materia field dyes). */
    public static boolean isLoomDye(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        if (item instanceof DyeItem) {
            return isFieldDye(item);
        }
        return isFieldDye(item);
    }
}
