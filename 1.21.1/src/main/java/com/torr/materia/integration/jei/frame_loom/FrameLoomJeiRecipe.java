package com.torr.materia.integration.jei.frame_loom;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FrameLoomJeiRecipe {
    private final List<ItemStack> inputs;
    private final ItemStack output;

    public FrameLoomJeiRecipe(List<ItemStack> inputs, ItemStack output) {
        this.inputs = inputs;
        this.output = output;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }
}

