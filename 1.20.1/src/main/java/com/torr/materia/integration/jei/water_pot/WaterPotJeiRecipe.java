package com.torr.materia.integration.jei.water_pot;

import com.torr.materia.recipe.WaterPotRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
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

    public static WaterPotJeiRecipe fromRecipe(WaterPotRecipe recipe) {
        List<ItemStack> inputs = new ArrayList<>();
        int count = Math.max(1, recipe.getIngredientCount());
        for (ItemStack stack : recipe.getIngredient().getItems()) {
            ItemStack input = stack.copy();
            input.setCount(count);
            inputs.add(input);
        }

        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack result : recipe.getResults()) {
            outputs.add(result.copy());
        }
        if (recipe.getResultBlock() != null) {
            var block = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(recipe.getResultBlock());
            if (block != null) {
                outputs.add(new ItemStack(block.asItem()));
            }
        }

        return new WaterPotJeiRecipe(inputs, outputs, recipe.requiresBoiling());
    }
}

