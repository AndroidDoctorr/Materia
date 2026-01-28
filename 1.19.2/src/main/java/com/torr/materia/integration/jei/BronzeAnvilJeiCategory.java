package com.torr.materia.integration.jei;

import com.torr.materia.ModBlocks;
import com.torr.materia.recipe.BronzeAnvilRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BronzeAnvilJeiCategory implements IRecipeCategory<BronzeAnvilRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public BronzeAnvilJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 60);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BRONZE_ANVIL.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<BronzeAnvilRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.BRONZE_ANVIL;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.bronze_anvil");
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
    public void setRecipe(IRecipeLayoutBuilder builder, BronzeAnvilRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 22)
                .addItemStack(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.INPUT, 60, 12)
                .addIngredients(Ingredient.of(recipe.getRequiredToolTag0()));

        builder.addSlot(RecipeIngredientRole.INPUT, 60, 32)
                .addIngredients(Ingredient.of(recipe.getRequiredToolTag1()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 22)
                .addItemStack(recipe.getResultItem());
    }
}

