package com.torr.materia.integration.jei.water_pot;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WaterPotJeiRecipe {
    private final List<ItemStack> inputs;
    private final List<ItemStack> outputs;
    private final boolean requiresBoiling;

    public WaterPotJeiRecipe(List<ItemStack> inputs, List<ItemStack> outputs, boolean requiresBoiling) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.requiresBoiling = requiresBoiling;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public boolean requiresBoiling() {
        return requiresBoiling;
    }
}

