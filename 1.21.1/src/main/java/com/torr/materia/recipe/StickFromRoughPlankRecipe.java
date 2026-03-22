package com.torr.materia.recipe;

import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Sticks from rough planks recipe: 1 rough plank + 1 axe (any, not saw) -> 4 sticks.
 * Axe survives with 1 durability damage (breaks when it reaches max damage).
 * Single JSON entry covers all rough plank wood types.
 */
public class StickFromRoughPlankRecipe extends CustomRecipe {

    private static final TagKey<Item> ROUGH_PLANKS_TAG = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rough_planks"));
    private static final TagKey<Item> ALL_SAWS_TAG = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "all_saws"));

    public StickFromRoughPlankRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level level) {
        int planks = 0;
        int axes = 0;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ROUGH_PLANKS_TAG)) {
                planks++;
            } else if (stack.getItem() instanceof net.minecraft.world.item.AxeItem) {
                if (stack.is(ALL_SAWS_TAG)) {
                    return false;
                }
                axes++;
            } else {
                return false;
            }
            if (planks > 1 || axes > 1) return false;
        }
        return planks == 1 && axes == 1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider provider) {
        return new ItemStack(Items.STICK, 4);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return new ItemStack(Items.STICK, 4);
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof net.minecraft.world.item.AxeItem) {
                ItemStack copy = stack.copy();
                copy.setDamageValue(copy.getDamageValue() + 1);
                if (copy.getDamageValue() < copy.getMaxDamage()) {
                    remaining.set(i, copy);
                }
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.STICKS_FROM_ROUGH_PLANK_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
