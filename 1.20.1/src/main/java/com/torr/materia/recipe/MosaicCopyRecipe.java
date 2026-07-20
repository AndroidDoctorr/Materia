package com.torr.materia.recipe;

import com.torr.materia.ModDecorBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.mosaic.MosaicItemData;
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

/** Blank mosaic block + painted mosaic → painted copy on the blank; template mosaic is kept. */
public class MosaicCopyRecipe extends CustomRecipe {

    public MosaicCopyRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        return resolve(inv).isPresent();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        return resolve(inv).map(Match::result).orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        java.util.Optional<Match> match = resolve(inv);
        if (match.isEmpty()) {
            return remaining;
        }
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && i == match.get().templateSlot()) {
                remaining.set(i, stack.copy());
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
        return ModRecipes.MOSAIC_COPY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    private record Match(int templateSlot, ItemStack result) {
    }

    private static java.util.Optional<Match> resolve(CraftingContainer inv) {
        ItemStack blank = ItemStack.EMPTY;
        ItemStack template = ItemStack.EMPTY;
        int templateSlot = -1;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (!stack.is(ModDecorBlocks.MOSAIC_BLOCK.get().asItem())) {
                return java.util.Optional.empty();
            }
            if (MosaicItemData.isBlank(stack)) {
                if (!blank.isEmpty()) {
                    return java.util.Optional.empty();
                }
                blank = stack;
            } else {
                if (!template.isEmpty()) {
                    return java.util.Optional.empty();
                }
                template = stack;
                templateSlot = i;
            }
        }
        if (blank.isEmpty() || template.isEmpty() || !template.hasTag()) {
            return java.util.Optional.empty();
        }
        ItemStack result = blank.copyWithCount(1);
        result.getOrCreateTag().put("BlockEntityTag",
                template.getTag().getCompound("BlockEntityTag").copy());
        return java.util.Optional.of(new Match(templateSlot, result));
    }
}
