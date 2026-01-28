package com.torr.materia.integration.jei.primitive;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class PrimitiveCraftingJeiRecipe {
    private final Ingredient rock;
    private final Ingredient hammer;
    private final Ingredient cuttingTool;
    private final Ingredient crucible;
    private final ItemStack output;

    public PrimitiveCraftingJeiRecipe(Ingredient rock, Ingredient hammer, Ingredient cuttingTool, Ingredient crucible, ItemStack output) {
        this.rock = rock;
        this.hammer = hammer;
        this.cuttingTool = cuttingTool;
        this.crucible = crucible;
        this.output = output;
    }

    public Ingredient getRock() {
        return rock;
    }

    public Ingredient getHammer() {
        return hammer;
    }

    public Ingredient getCuttingTool() {
        return cuttingTool;
    }

    public Ingredient getCrucible() {
        return crucible;
    }

    public ItemStack getOutput() {
        return output;
    }
}

