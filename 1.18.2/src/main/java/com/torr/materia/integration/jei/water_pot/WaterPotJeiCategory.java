package com.torr.materia.integration.jei.water_pot;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.integration.jei.materiaJeiRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class WaterPotJeiCategory implements IRecipeCategory<WaterPotJeiRecipe> {
    private static final ResourceLocation UID = new ResourceLocation(materia.MOD_ID, "water_pot");

    private final IDrawable background;
    private final IDrawable icon;

    public WaterPotJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WATER_POT.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<WaterPotJeiRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.WATER_POT;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends WaterPotJeiRecipe> getRecipeClass() {
        return WaterPotJeiRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.materia.water_pot");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WaterPotJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 16)
                .addItemStacks(recipe.getInputs());

        if (recipe.requiresBoiling()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 60, 16)
                    .addItemStack(new ItemStack(Blocks.CAMPFIRE));
        }

        List<ItemStack> outputs = recipe.getOutputs();
        if (!outputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 8)
                    .addItemStack(outputs.get(0));
        }
        if (outputs.size() > 1) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 28)
                    .addItemStack(outputs.get(1));
        }
    }
}

