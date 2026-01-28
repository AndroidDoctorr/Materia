package com.torr.materia.integration.jei.drying_rack;

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
import net.minecraft.world.level.block.Blocks;

public class DryingRackJeiCategory implements IRecipeCategory<DryingRackJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public DryingRackJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DRYING_RACK.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<DryingRackJeiRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.DRYING_RACK;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.drying_rack");
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
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRackJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 16)
                .addItemStacks(recipe.getInputs());

        builder.addSlot(RecipeIngredientRole.CATALYST, 60, 16)
                .addItemStack(new ItemStack(Blocks.CAMPFIRE));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 16)
                .addItemStack(recipe.getOutput());
    }
}

