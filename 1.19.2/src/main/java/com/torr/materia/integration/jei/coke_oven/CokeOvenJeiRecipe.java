package com.torr.materia.integration.jei.coke_oven;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * JEI adapter for {@link com.torr.materia.blockentity.CokeOvenBlockEntity} (no datapack recipe type).
 */
public final class CokeOvenJeiRecipe {
    private final Ingredient cokingInput;
    private final Ingredient fuelInput;
    private final ItemStack result;

    public CokeOvenJeiRecipe(Ingredient cokingInput, Ingredient fuelInput, ItemStack result) {
        this.cokingInput = cokingInput;
        this.fuelInput = fuelInput;
        this.result = result.copy();
    }

    public Ingredient getCokingInput() {
        return cokingInput;
    }

    public Ingredient getFuelInput() {
        return fuelInput;
    }

    public ItemStack getResult() {
        return result;
    }
}
