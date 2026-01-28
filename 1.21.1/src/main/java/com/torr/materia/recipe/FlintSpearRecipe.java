package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class FlintSpearRecipe extends CustomRecipe {

    public FlintSpearRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, @NotNull Level level) {
        int flint = 0;
        int handle = 0;
        int lashing = 0;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.KNAPPED_FLINT.get())) {
                if (stack.getDamageValue() == 0) {
                    flint++;
                } else {
                    return false; // damaged flint not allowed
                }
            } else if (stack.is(ModItems.HANDLE.get())) {
                handle++;
            } else if (stack.is(ModItems.LASHING.get()) || stack.is(ModItems.GLUE.get())) {
                lashing++;
            } else {
                return false; // unexpected ingredient
            }
        }
        return flint == 1 && handle == 2 && lashing == 1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider provider) {
        return new ItemStack(ModItems.FLINT_SPEAR.get());
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return new ItemStack(ModItems.FLINT_SPEAR.get());
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        // Consume everything (no containers)
        return NonNullList.withSize(inv.size(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.FLINT_SPEAR_SERIALIZER.get();
    }

    // CustomRecipe already defaults to CRAFTING type, no need to override

    // Serializer handled via SimpleRecipeSerializer in ModRecipes
} 