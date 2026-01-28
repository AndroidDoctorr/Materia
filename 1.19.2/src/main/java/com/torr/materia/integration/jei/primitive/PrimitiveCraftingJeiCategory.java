package com.torr.materia.integration.jei.primitive;

import com.torr.materia.ModBlocks;
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

public class PrimitiveCraftingJeiCategory implements IRecipeCategory<PrimitiveCraftingJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public PrimitiveCraftingJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.PRIMITIVE_CRAFTING_TABLE.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<PrimitiveCraftingJeiRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.PRIMITIVE_CRAFTING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.primitive_crafting");
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
    public void setRecipe(IRecipeLayoutBuilder builder, PrimitiveCraftingJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 16)
                .addIngredients(recipe.getRock());

        builder.addSlot(RecipeIngredientRole.INPUT, 40, 16)
                .addIngredients(recipe.getHammer());

        builder.addSlot(RecipeIngredientRole.INPUT, 70, 16)
                .addIngredients(recipe.getCuttingTool());

        builder.addSlot(RecipeIngredientRole.INPUT, 100, 16)
                .addIngredients(recipe.getCrucible());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 130, 16)
                .addItemStack(recipe.getOutput());
    }
}

