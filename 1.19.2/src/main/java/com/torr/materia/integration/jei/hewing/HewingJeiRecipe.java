package com.torr.materia.integration.jei.hewing;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class HewingJeiRecipe {
    private final Ingredient log;
    private final Ingredient axe;
    private final ItemStack output;

    public HewingJeiRecipe(Ingredient log, Ingredient axe, ItemStack output) {
        this.log = log;
        this.axe = axe;
        this.output = output;
    }

    public Ingredient getLog() {
        return log;
    }

    public Ingredient getAxe() {
        return axe;
    }

    public ItemStack getOutput() {
        return output;
    }
}

