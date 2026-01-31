package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class FlintKnifeRecipe extends CustomRecipe {

    public FlintKnifeRecipe(CraftingBookCategory category) {
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
                // Any knapped flint is allowed; wear will be transferred to the knife
                flint++;
            } else if (stack.is(ModItems.BONE_HANDLE.get())) {
                handle++;
            } else if (stack.is(ModItems.LASHING.get()) || stack.is(ModItems.GLUE.get())) {
                lashing++;
            } else {
                return false; // unexpected ingredient
            }
        }
        return flint == 1 && handle == 1 && lashing == 1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider provider) {
        // Create the knife and transfer wear from the knapped flint to the knife
        ItemStack knife = new ItemStack(ModItems.FLINT_KNIFE.get());

        ItemStack flintStack = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(ModItems.KNAPPED_FLINT.get())) {
                flintStack = stack;
                break;
            }
        }

        if (!flintStack.isEmpty()) {
            int flintDamage = flintStack.getDamageValue();
            int flintMax = flintStack.getMaxDamage();
            int knifeMax = knife.getMaxDamage();

            if (flintMax > 0 && knifeMax > 0) {
                float wearRatio = (float) flintDamage / (float) flintMax;
                int knifeDamage = (int) (wearRatio * knifeMax);
                knife.setDamageValue(knifeDamage);
            }
        }

        return knife;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return new ItemStack(ModItems.FLINT_KNIFE.get());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        // JEI + recipe book display:
        // CustomRecipe defaults to an empty ingredient list unless overridden.
        // This recipe accepts any arrangement of:
        // - 1x knapped flint
        // - 1x bone handle
        // - 1x lashing OR glue
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModItems.KNAPPED_FLINT.get()));
        ingredients.add(Ingredient.of(ModItems.BONE_HANDLE.get()));
        ingredients.add(Ingredient.of(ModItems.LASHING.get(), ModItems.GLUE.get()));
        return ingredients;
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
        return ModRecipes.FLINT_KNIFE_SERIALIZER.get();
    }

    // CustomRecipe already defaults to CRAFTING type, no need to override

    // Serializer handled via SimpleRecipeSerializer in ModRecipes
} 