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
        int patternIndex = patternIndex(pattern);
        if (patternIndex == 0) {
            return Optional.empty();
        }
        return rugFromDye(patternIndex, dye.getItem());
    }

    public static boolean isRugPattern(ItemStack stack) {
        return patternIndex(stack) != 0;
    }

    private static int patternIndex(ItemStack pattern) {
        if (pattern.is(ModItems.RUG_1_PATTERN.get())) {
            return 1;
        }
        if (pattern.is(ModItems.RUG_2_PATTERN.get())) {
            return 2;
        }
        if (pattern.is(ModItems.RUG_3_PATTERN.get())) {
            return 3;
        }
        if (pattern.is(ModItems.RUG_4_PATTERN.get())) {
            return 4;
        }
        return 0;
    }

    private static Optional<ItemStack> rugFromDye(int pattern, Item dyeItem) {
        Item rug = null;
        if (isRedFieldDye(dyeItem)) {
            rug = redRug(pattern);
        } else if (isBlueFieldDye(dyeItem)) {
            rug = blueRug(pattern);
        } else if (dyeItem == Items.GREEN_DYE) {
            rug = greenRug(pattern);
        } else if (isPurpleFieldDye(dyeItem)) {
            rug = purpleRug(pattern);
        }
        if (rug == null) {
            return Optional.empty();
        }
        return Optional.of(new ItemStack(rug));
    }

    private static Item redRug(int pattern) {
        return switch (pattern) {
            case 1 -> ModBlocks.RUG_1_RED.get().asItem();
            case 2 -> ModBlocks.RUG_2_RED.get().asItem();
            case 3 -> ModBlocks.RUG_3_RED.get().asItem();
            case 4 -> ModBlocks.RUG_4_RED.get().asItem();
            default -> null;
        };
    }

    private static Item blueRug(int pattern) {
        return switch (pattern) {
            case 1 -> ModBlocks.RUG_1_BLUE.get().asItem();
            case 2 -> ModBlocks.RUG_2_BLUE.get().asItem();
            case 3 -> ModBlocks.RUG_3_BLUE.get().asItem();
            case 4 -> ModBlocks.RUG_4_BLUE.get().asItem();
            default -> null;
        };
    }

    private static Item greenRug(int pattern) {
        return switch (pattern) {
            case 1 -> ModBlocks.RUG_1_GREEN.get().asItem();
            case 2 -> ModBlocks.RUG_2_GREEN.get().asItem();
            case 3 -> ModBlocks.RUG_3_GREEN.get().asItem();
            case 4 -> ModBlocks.RUG_4_GREEN.get().asItem();
            default -> null;
        };
    }

    private static Item purpleRug(int pattern) {
        return switch (pattern) {
            case 1 -> ModBlocks.RUG_1_PURPLE.get().asItem();
            case 2 -> ModBlocks.RUG_2_PURPLE.get().asItem();
            case 3 -> ModBlocks.RUG_3_PURPLE.get().asItem();
            case 4 -> ModBlocks.RUG_4_PURPLE.get().asItem();
            default -> null;
        };
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
