package com.torr.materia.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods to build 1.21+ {@link RecipeInput} instances from our machine inventories.
 */
public final class RecipeInputs {
    private RecipeInputs() {}

    /**
     * Creates a 1xN {@link CraftingInput} from the first {@code size} slots of an {@link ItemStackHandler}.
     * This is enough for simple "one input slot" machine recipes and for our kiln (which looks at slots 0 and 3).
     */
    public static CraftingInput fromItemHandler(ItemStackHandler handler, int size) {
        List<ItemStack> items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add(handler.getStackInSlot(i));
        }
        return CraftingInput.of(size, 1, items);
    }

    /**
     * Convenience for single-input vanilla recipes (smelting, etc.).
     */
    public static SingleRecipeInput single(ItemStack stack) {
        return new SingleRecipeInput(stack);
    }
}

