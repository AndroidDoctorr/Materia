package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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

/** 1 bone + advanced cutting tool -> 3 bone strips; tool is returned unchanged. */
public class BoneStripRecipe extends CustomRecipe {

    private static final TagKey<Item> ADVANCED_CUTTING_TOOLS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "advanced_cutting_tools"));

    public BoneStripRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level level) {
        int bones = 0;
        int tools = 0;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(Items.BONE)) {
                bones++;
            } else if (stack.is(ADVANCED_CUTTING_TOOLS)) {
                tools++;
            } else {
                return false;
            }
            if (bones > 1 || tools > 1) {
                return false;
            }
        }
        return bones == 1 && tools == 1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider provider) {
        return new ItemStack(ModItems.BONE_STRIP.get(), 3);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return new ItemStack(ModItems.BONE_STRIP.get(), 3);
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(ADVANCED_CUTTING_TOOLS)) {
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
        return ModRecipes.BONE_STRIP_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
