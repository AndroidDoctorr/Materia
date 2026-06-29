package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class VerdigrisRecipe extends CustomRecipe {
    private static final TagKey<Item> VINEGAR_TAG = ItemTags.create(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "vinegar"));
    private static final TagKey<Item> RAW_COPPER_TAG = ItemTags.create(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("forge", "raw_materials/copper"));

    public VerdigrisRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, @NotNull Level level) {
        boolean hasVinegar = false;
        boolean hasCopper = false;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(VINEGAR_TAG)) {
                if (hasVinegar) return false;
                hasVinegar = true;
            } else if (stack.is(RAW_COPPER_TAG) || stack.is(Items.RAW_COPPER)) {
                if (hasCopper) return false;
                hasCopper = true;
            } else {
                return false;
            }
        }
        return hasVinegar && hasCopper;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registryAccess) {
        return new ItemStack(ModItems.VERDIGRIS.get());
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(VINEGAR_TAG)) {
                remaining.set(i, getEmptyVessel(stack));
            }
        }
        return remaining;
    }

    private static ItemStack getEmptyVessel(ItemStack vinegarStack) {
        Item item = vinegarStack.getItem();
        if (item == ModItems.VINEGAR.get()) {
            return new ItemStack(ModItems.CLAY_BOWL.get());
        }
        if (item == ModItems.VINEGAR_BOTTLE.get()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        if (item == ModItems.VINEGAR_POT.get()) {
            return new ItemStack(ModItems.POT.get());
        }
        if (item == ModItems.VINEGAR_BUCKET.get()) {
            return new ItemStack(Items.BUCKET);
        }
        if (item == ModItems.CRUCIBLE.get().asItem()) {
            return new ItemStack(ModItems.CRUCIBLE.get());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.VERDIGRIS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}

