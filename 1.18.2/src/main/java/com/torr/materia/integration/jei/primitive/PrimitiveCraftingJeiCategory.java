package com.torr.materia.integration.jei.primitive;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.integration.jei.materiaJeiRecipeTypes;
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

public class PrimitiveCraftingJeiCategory implements IRecipeCategory<PrimitiveCraftingJeiRecipe> {
    private static final ResourceLocation UID = new ResourceLocation(materia.MOD_ID, "primitive_crafting");

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
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends PrimitiveCraftingJeiRecipe> getRecipeClass() {
        return PrimitiveCraftingJeiRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.materia.primitive_crafting");
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
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 10)
                .addIngredients(recipe.getRock());
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 10)
                .addIngredients(recipe.getHammer());
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 30)
                .addIngredients(recipe.getCuttingTool());
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 30)
                .addIngredients(recipe.getCrucible());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 20)
                .addItemStack(recipe.getOutput());
    }
}

