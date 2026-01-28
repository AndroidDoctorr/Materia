package com.torr.materia.integration.jei.hewing;

import com.torr.materia.ModItems;
import com.torr.materia.integration.jei.materiaJeiRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class HewingJeiCategory implements IRecipeCategory<HewingJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public HewingJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.ROUGH_OAK_PLANK.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<HewingJeiRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.HEWING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.hewing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, HewingJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 16)
                .addIngredients(recipe.getLog());

        builder.addSlot(RecipeIngredientRole.INPUT, 60, 16)
                .addIngredients(recipe.getAxe());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 16)
                .addItemStack(recipe.getOutput());
    }
}

