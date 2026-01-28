package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class FlintKnifeRecipe extends CustomRecipe {

    public FlintKnifeRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        int flint = 0;
        int handle = 0;
        int lashing = 0;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
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
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        // Create the knife and transfer wear from the knapped flint to the knife
        ItemStack knife = new ItemStack(ModItems.FLINT_KNIFE.get());

        ItemStack flintStack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
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
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return new ItemStack(ModItems.FLINT_KNIFE.get());
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        // Consume everything (no containers)
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
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