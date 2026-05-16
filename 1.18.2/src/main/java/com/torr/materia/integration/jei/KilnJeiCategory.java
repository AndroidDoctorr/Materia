package com.torr.materia.integration.jei;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.materia;
import com.torr.materia.recipe.KilnRecipe;
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
import net.minecraft.world.item.Items;

import java.util.Arrays;

public class KilnJeiCategory implements IRecipeCategory<KilnRecipe> {
    private static final ResourceLocation UID = new ResourceLocation(materia.MOD_ID, "kiln");

    private final IDrawable background;
    private final IDrawable icon;

    public KilnJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 64);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FURNACE_KILN.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<KilnRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.KILN;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends KilnRecipe> getRecipeClass() {
        return KilnRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.materia.kiln");
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
    public void setRecipe(IRecipeLayoutBuilder builder, KilnRecipe recipe, IFocusGroup focuses) {
        boolean cokeOnlyFuel = recipe.requiresCokeFuel();
        boolean bellowsHeavySolidFuel = recipe.requiresBellows() && !cokeOnlyFuel;
        int ingredientY = (cokeOnlyFuel || bellowsHeavySolidFuel) ? 6 : 16;

        builder.addSlot(RecipeIngredientRole.INPUT, 20, ingredientY)
                .addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, ingredientY)
                .addItemStack(recipe.getResultItem().copy());

        if (cokeOnlyFuel) {
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 36)
                    .addItemStack(new ItemStack(ModItems.COAL_COKE.get()))
                    .addTooltipCallback((recipeSlotView, tooltip) ->
                            tooltip.add(new TranslatableComponent("jei.materia.coal_coke_fuels_only")));
        } else if (bellowsHeavySolidFuel) {
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 36)
                    .addItemStacks(Arrays.asList(
                            new ItemStack(Items.CHARCOAL),
                            new ItemStack(Items.COAL),
                            new ItemStack(ModItems.COAL_COKE.get())))
                    .addTooltipCallback((recipeSlotView, tooltip) ->
                            tooltip.add(new TranslatableComponent("jei.materia.bellows_fuels_heavy_solids_only")));
        }
    }
}
