package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class PrimitiveCraftingTableRecipe extends CustomRecipe {

    public PrimitiveCraftingTableRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        int rock = 0;
        int stoneHammer = 0;
        int cuttingTool = 0;
        int crucible = 0;
        
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModBlocks.ROCK.get().asItem())) {
                rock++;
            } else if (stack.is(ModItems.STONE_HAMMER.get())) {
                // Check that stone hammer has at least 50% durability
                if (stack.getDamageValue() > stack.getMaxDamage() * 0.5) {
                    return false; // hammer too damaged
                }
                stoneHammer++;
            } else if (stack.is(ModItems.FLINT_KNIFE.get())) {
                // Check that flint knife has at least 50% durability
                if (stack.getDamageValue() > stack.getMaxDamage() * 0.5) {
                    return false; // knife too damaged
                }
                cuttingTool++;
            } else if (stack.is(ModItems.CRUCIBLE.get())) {
                crucible++;
            } else {
                return false; // unexpected ingredient
            }
        }
        return rock == 1 && stoneHammer == 1 && cuttingTool == 1 && crucible == 1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        return new ItemStack(ModBlocks.PRIMITIVE_CRAFTING_TABLE.get().asItem());
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return new ItemStack(ModBlocks.PRIMITIVE_CRAFTING_TABLE.get().asItem());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        ingredients.add(Ingredient.of(ModBlocks.ROCK.get().asItem()));
        ingredients.add(Ingredient.of(ModItems.STONE_HAMMER.get()));
        ingredients.add(Ingredient.of(ModItems.FLINT_KNIFE.get()));
        ingredients.add(Ingredient.of(ModItems.CRUCIBLE.get()));

        return ingredients;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        // Consume everything - no containers returned
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 4;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.PRIMITIVE_CRAFTING_TABLE_SERIALIZER.get();
    }
}
