package com.torr.materia.integration.jei.coke_oven;

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

public class CokeOvenJeiCategory implements IRecipeCategory<CokeOvenJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public CokeOvenJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 62);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COKE_OVEN.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<CokeOvenJeiRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.COKE_OVEN;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.coke_oven");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CokeOvenJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 8)
                .addIngredients(recipe.getCokingInput());

        builder.addSlot(RecipeIngredientRole.INPUT, 18, 32)
                .addIngredients(recipe.getFuelInput())
                .addTooltipCallback((recipeSlotView, tooltip) ->
                        tooltip.add(Component.translatable("jei.materia.coke_oven.fuel_slot")));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 20)
                .addItemStack(recipe.getResult().copy());
    }
}
