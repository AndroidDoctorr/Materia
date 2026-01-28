package com.torr.materia.recipe;

import com.torr.materia.ModRecipes;
import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Enables dyeing vanilla dyeable leather items using materia custom dyes.
 *
 * Important: this recipe only matches if at least one "custom" (non-vanilla) dye is present,
 * so vanilla-only dyeing continues to be handled by Minecraft's built-in recipe.
 */
public class LeatherArmorDyeRecipe extends CustomRecipe {
    public LeatherArmorDyeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, @NotNull Level level) {
        ItemStack dyeable = ItemStack.EMPTY;
        int dyes = 0;
        boolean hasCustomDye = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (isLeatherArmor(stack)) {
                if (!dyeable.isEmpty()) return false; // only one dyeable item
                dyeable = stack;
                continue;
            }

            CustomSheepColor dyeColor = CustomSheepColor.fromDyeItem(stack.getItem());
            if (dyeColor == null) {
                return false;
            }
            dyes++;
            if (dyeColor.isCustomColor()) {
                hasCustomDye = true;
            }
        }

        return !dyeable.isEmpty() && dyes > 0 && hasCustomDye;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider provider) {
        ItemStack dyeable = ItemStack.EMPTY;

        int[] rgbTotals = new int[3];
        int totalMax = 0;
        int colorContributors = 0;

        boolean hasCustomDye = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (isLeatherArmor(stack)) {
                if (!dyeable.isEmpty()) return ItemStack.EMPTY;
                dyeable = stack.copy();
                dyeable.setCount(1);

                DyedItemColor existing = stack.get(DataComponents.DYED_COLOR);
                if (existing != null) {
                    int rgb = existing.rgb();
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    totalMax += Math.max(r, Math.max(g, b));
                    rgbTotals[0] += r;
                    rgbTotals[1] += g;
                    rgbTotals[2] += b;
                    colorContributors++;
                }
                continue;
            }

            CustomSheepColor dyeColor = CustomSheepColor.fromDyeItem(stack.getItem());
            if (dyeColor == null) {
                return ItemStack.EMPTY;
            }

            if (dyeColor.isCustomColor()) {
                hasCustomDye = true;
            }

            int rgb = dyeColor.getColor();
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            totalMax += Math.max(r, Math.max(g, b));
            rgbTotals[0] += r;
            rgbTotals[1] += g;
            rgbTotals[2] += b;
            colorContributors++;
        }

        if (dyeable.isEmpty() || colorContributors == 0 || !hasCustomDye) {
            return ItemStack.EMPTY;
        }

        int rAvg = rgbTotals[0] / colorContributors;
        int gAvg = rgbTotals[1] / colorContributors;
        int bAvg = rgbTotals[2] / colorContributors;

        float brightnessAvg = (float) totalMax / (float) colorContributors;
        float maxAvg = (float) Math.max(rAvg, Math.max(gAvg, bAvg));
        if (maxAvg > 0.0f) {
            rAvg = (int) ((float) rAvg * brightnessAvg / maxAvg);
            gAvg = (int) ((float) gAvg * brightnessAvg / maxAvg);
            bAvg = (int) ((float) bAvg * brightnessAvg / maxAvg);
        }

        int finalColor = (rAvg << 16) | (gAvg << 8) | bAvg;
        dyeable.set(DataComponents.DYED_COLOR, new DyedItemColor(finalColor, true));
        return dyeable;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        // This recipe's output depends on the input item and dyes
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        return NonNullList.withSize(inv.size(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.LEATHER_ARMOR_DYE_SERIALIZER.get();
    }

    private static boolean isLeatherArmor(ItemStack stack) {
        return stack.is(Items.LEATHER_HELMET)
                || stack.is(Items.LEATHER_CHESTPLATE)
                || stack.is(Items.LEATHER_LEGGINGS)
                || stack.is(Items.LEATHER_BOOTS);
    }
}

