package com.torr.materia.integration.jei.frame_loom;

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

public class FrameLoomJeiCategory implements IRecipeCategory<FrameLoomJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public FrameLoomJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FRAME_LOOM.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<FrameLoomJeiRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.FRAME_LOOM;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.frame_loom");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FrameLoomJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 16)
                .addItemStacks(recipe.getInputs());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 16)
                .addItemStack(recipe.getOutput());
    }
}

