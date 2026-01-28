package com.torr.materia.integration.jei;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.recipe.StoneAnvilRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;

public class StoneAnvilJeiCategory implements IRecipeCategory<StoneAnvilRecipe> {
    private static final ResourceLocation UID = new ResourceLocation(materia.MOD_ID, "stone_anvil");

    private final IDrawable background;
    private final IDrawable icon;

    public StoneAnvilJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.STONE_ANVIL.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<StoneAnvilRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.STONE_ANVIL;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends StoneAnvilRecipe> getRecipeClass() {
        return StoneAnvilRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.materia.stone_anvil");
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
    public void setRecipe(IRecipeLayoutBuilder builder, StoneAnvilRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 16)
                .addItemStack(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.INPUT, 60, 16)
                .addIngredients(Ingredient.of(recipe.getRequiredToolTag()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 16)
                .addItemStack(recipe.getResultItem());
    }
}

