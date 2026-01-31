package com.torr.materia.integration.jei.flint_knife;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class FlintKnifeJeiRecipe {
    private final Ingredient flint;
    private final Ingredient handle;
    private final Ingredient lashing;
    private final ItemStack output;

    public FlintKnifeJeiRecipe(Ingredient flint, Ingredient handle, Ingredient lashing, ItemStack output) {
        this.flint = flint;
        this.handle = handle;
        this.lashing = lashing;
        this.output = output;
    }

    public Ingredient getFlint() {
        return flint;
    }

    public Ingredient getHandle() {
        return handle;
    }

    public Ingredient getLashing() {
        return lashing;
    }

    public ItemStack getOutput() {
        return output;
    }
}

