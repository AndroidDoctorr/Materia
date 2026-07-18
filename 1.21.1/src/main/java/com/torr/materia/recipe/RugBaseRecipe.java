package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import com.torr.materia.rug.RugWeaving;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * One neutral blanket plus sixteen white or taupe string yields a {@link com.torr.materia.item.RugBaseItem}.
 */
public class RugBaseRecipe extends CustomRecipe {
    private static final TagKey<Item> NEUTRAL_BLANKETS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rug_neutral_blankets"));
    private static final TagKey<Item> BASE_STRINGS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rug_base_strings"));
    private static final int STRING_COST = 16;

    public RugBaseRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level level) {
        return analyze(inv).matches();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider provider) {
        return new ItemStack(ModItems.RUG_BASE.get());
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return new ItemStack(ModItems.RUG_BASE.get());
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        Analysis analysis = analyze(inv);
        if (!analysis.matches()) {
            return remaining;
        }
        int toConsume = STRING_COST;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty() || !stack.is(BASE_STRINGS)) {
                continue;
            }
            int take = Math.min(stack.getCount(), toConsume);
            ItemStack left = stack.copy();
            left.shrink(take);
            remaining.set(i, left);
            toConsume -= take;
            if (toConsume <= 0) {
                break;
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
        return ModRecipes.RUG_BASE_SERIALIZER.get();
    }

    private static Analysis analyze(CraftingInput inv) {
        boolean hasBlanket = false;
        int strings = 0;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(NEUTRAL_BLANKETS)) {
                if (hasBlanket) {
                    return Analysis.invalid();
                }
                hasBlanket = true;
                continue;
            }
            if (stack.is(BASE_STRINGS)) {
                strings += stack.getCount();
                continue;
            }
            return Analysis.invalid();
        }
        return new Analysis(hasBlanket, strings);
    }

    private record Analysis(boolean hasBlanket, int strings) {
        static Analysis invalid() {
            return new Analysis(false, 0);
        }

        boolean matches() {
            return hasBlanket && strings >= STRING_COST;
        }
    }
}
