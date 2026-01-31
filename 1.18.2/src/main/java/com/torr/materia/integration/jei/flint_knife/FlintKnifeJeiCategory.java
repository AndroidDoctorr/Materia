package com.torr.materia.integration.jei.flint_knife;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import com.torr.materia.integration.jei.materiaJeiRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FlintKnifeJeiCategory implements IRecipeCategory<FlintKnifeJeiRecipe> {
    private static final ResourceLocation UID = new ResourceLocation(materia.MOD_ID, "flint_knife");

    private final IDrawable background;
    private final IDrawable icon;

    public FlintKnifeJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(120, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.FLINT_KNIFE.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<FlintKnifeJeiRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.FLINT_KNIFE;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends FlintKnifeJeiRecipe> getRecipeClass() {
        return FlintKnifeJeiRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.materia.flint_knife");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FlintKnifeJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 16).addIngredients(recipe.getFlint());
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 16).addIngredients(recipe.getHandle());
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 16).addIngredients(recipe.getLashing());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 16).addItemStack(recipe.getOutput());
    }
}

