package com.torr.materia.integration.jei;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.recipe.IronAnvilRecipe;
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

public class IronAnvilJeiCategory implements IRecipeCategory<IronAnvilRecipe> {
    private static final ResourceLocation UID = new ResourceLocation(materia.MOD_ID, "iron_anvil");

    private final IDrawable background;
    private final IDrawable icon;

    public IronAnvilJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 70);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.IRON_ANVIL.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<IronAnvilRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.IRON_ANVIL;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends IronAnvilRecipe> getRecipeClass() {
        return IronAnvilRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.materia.iron_anvil");
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
    public void setRecipe(IRecipeLayoutBuilder builder, IronAnvilRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16)
                .addItemStack(recipe.getIngredientA());

        if (!recipe.getIngredientB().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 16, 40)
                    .addItemStack(recipe.getIngredientB());
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 60, 6)
                .addIngredients(Ingredient.of(recipe.getRequiredToolTag0()));
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 28)
                .addIngredients(Ingredient.of(recipe.getRequiredToolTag1()));
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 50)
                .addIngredients(Ingredient.of(recipe.getRequiredToolTag2()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 28)
                .addItemStack(recipe.getResultItem());
    }
}

