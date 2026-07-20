package com.torr.materia.recipe;

import com.torr.materia.ModDecorBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/** Terracotta + slaked lime + mosaic stylus → blank mosaic block; stylus survives with durability loss. */
public class MosaicBlockCraftRecipe extends CustomRecipe {

    public MosaicBlockCraftRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        return resolve(inv).isPresent();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        return resolve(inv).orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return new ItemStack(ModDecorBlocks.MOSAIC_BLOCK.get());
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        java.util.Optional<ItemStack> stylus = resolveStylus(inv);
        if (stylus.isEmpty()) {
            return remaining;
        }
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(ModItems.MOSAIC_STYLUS.get())) {
                remaining.set(i, ModItems.MOSAIC_STYLUS.get().getCraftingRemainingItem(stack));
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
        return ModRecipes.MOSAIC_BLOCK_CRAFT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    private static java.util.Optional<ItemStack> resolve(CraftingContainer inv) {
        if (resolveStylus(inv).isEmpty()) {
            return java.util.Optional.empty();
        }
        boolean terracotta = false;
        boolean slakedLime = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.MOSAIC_STYLUS.get())) {
                continue;
            }
            if (stack.is(Items.TERRACOTTA)) {
                if (terracotta) {
                    return java.util.Optional.empty();
                }
                terracotta = true;
                continue;
            }
            if (stack.is(ModItems.SLAKED_LIME.get())) {
                if (slakedLime) {
                    return java.util.Optional.empty();
                }
                slakedLime = true;
                continue;
            }
            return java.util.Optional.empty();
        }
        return terracotta && slakedLime
                ? java.util.Optional.of(new ItemStack(ModDecorBlocks.MOSAIC_BLOCK.get()))
                : java.util.Optional.empty();
    }

    private static java.util.Optional<ItemStack> resolveStylus(CraftingContainer inv) {
        ItemStack stylus = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.MOSAIC_STYLUS.get())) {
                if (!stylus.isEmpty()) {
                    return java.util.Optional.empty();
                }
                stylus = stack;
            }
        }
        return stylus.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(stylus);
    }
}
