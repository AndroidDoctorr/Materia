package com.torr.materia.integration.jei.oven;

import com.torr.materia.ModBlocks;
import com.torr.materia.integration.jei.materiaJeiRecipeTypes;
import com.torr.materia.recipe.OvenRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class OvenJeiCategory implements IRecipeCategory<OvenRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public OvenJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.OVEN.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<OvenRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.OVEN;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.oven");
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
    public void setRecipe(IRecipeLayoutBuilder builder, OvenRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 16)
            .addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 16)
            .addItemStack(recipe.getResultItem().copy());
    }
}

