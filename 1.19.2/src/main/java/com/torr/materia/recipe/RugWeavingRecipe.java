package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.rug.RugWeaving;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Rug base + one field dye + rug pattern. Pattern is not consumed (banner-pattern behavior).
 */
public class RugWeavingRecipe extends CustomRecipe {
    public RugWeavingRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        return resolve(inv).isPresent();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv) {
        return resolve(inv).orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        ItemStack base = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;
        ItemStack pattern = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.RUG_BASE.get())) {
                base = stack;
            } else if (RugWeaving.isFieldDye(stack.getItem())) {
                dye = stack;
            } else if (RugWeaving.isRugPattern(stack)) {
                pattern = stack;
            }
        }
        if (RugWeaving.resolve(base, dye, pattern).isEmpty()) {
            return remaining;
        }
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (RugWeaving.isRugPattern(stack)) {
                remaining.set(i, stack.copy());
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.RUG_WEAVING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    private static java.util.Optional<ItemStack> resolve(CraftingContainer inv) {
        ItemStack base = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;
        ItemStack pattern = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.RUG_BASE.get())) {
                if (!base.isEmpty()) {
                    return java.util.Optional.empty();
                }
                base = stack;
                continue;
            }
            if (RugWeaving.isFieldDye(stack.getItem())) {
                if (!dye.isEmpty()) {
                    return java.util.Optional.empty();
                }
                dye = stack;
                continue;
            }
            if (RugWeaving.isRugPattern(stack)) {
                if (!pattern.isEmpty()) {
                    return java.util.Optional.empty();
                }
                pattern = stack;
                continue;
            }
            return java.util.Optional.empty();
        }
        return RugWeaving.resolve(base, dye, pattern);
    }
}
