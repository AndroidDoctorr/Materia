package com.torr.materia.integration.jei;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.recipe.AdvancedKilnRecipe;
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

public class AdvancedKilnJeiCategory implements IRecipeCategory<AdvancedKilnRecipe> {
    private static final ResourceLocation UID = new ResourceLocation(materia.MOD_ID, "advanced_kiln");

    private final IDrawable background;
    private final IDrawable icon;

    public AdvancedKilnJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 60);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BLAST_FURNACE_KILN.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<AdvancedKilnRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.ADVANCED_KILN;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends AdvancedKilnRecipe> getRecipeClass() {
        return AdvancedKilnRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.materia.advanced_kiln");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AdvancedKilnRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 12)
                .addIngredients(recipe.getIngredient1());

        builder.addSlot(RecipeIngredientRole.INPUT, 20, 32)
                .addIngredients(recipe.getIngredient2());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 22)
                .addItemStack(recipe.getResultItem());
    }
}

