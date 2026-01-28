package com.torr.materia.recipe;

import com.torr.materia.ModRecipes;
import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
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
    private static final String DISPLAY_TAG = "display";
    private static final String COLOR_TAG = "color";

    public LeatherArmorDyeRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        ItemStack dyeable = ItemStack.EMPTY;
        int dyes = 0;
        boolean hasCustomDye = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof DyeableLeatherItem) {
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
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv) {
        ItemStack dyeable = ItemStack.EMPTY;

        int[] rgbTotals = new int[3];
        int totalMax = 0;
        int colorContributors = 0;

        boolean hasCustomDye = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof DyeableLeatherItem) {
                if (!dyeable.isEmpty()) return ItemStack.EMPTY;
                dyeable = stack.copy();
                dyeable.setCount(1);

                if (hasArmorColor(stack)) {
                    int existing = getArmorColor(stack);
                    int r = (existing >> 16) & 0xFF;
                    int g = (existing >> 8) & 0xFF;
                    int b = existing & 0xFF;

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
        setArmorColor(dyeable, finalColor);
        return dyeable;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.LEATHER_ARMOR_DYE_SERIALIZER.get();
    }

    private static boolean hasArmorColor(ItemStack stack) {
        CompoundTag display = stack.getTagElement(DISPLAY_TAG);
        return display != null && display.contains(COLOR_TAG, 99);
    }

    private static int getArmorColor(ItemStack stack) {
        CompoundTag display = stack.getTagElement(DISPLAY_TAG);
        if (display != null && display.contains(COLOR_TAG, 99)) {
            return display.getInt(COLOR_TAG);
        }
        return 0xA06540; // vanilla leather default
    }

    private static void setArmorColor(ItemStack stack, int rgb) {
        CompoundTag display = stack.getOrCreateTagElement(DISPLAY_TAG);
        display.putInt(COLOR_TAG, rgb);
    }
}

