package com.torr.materia.integration.jei.fire_pit;

import com.torr.materia.ModBlocks;
import com.torr.materia.integration.jei.materiaJeiRecipeTypes;
import com.torr.materia.recipe.FirePitRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FirePitJeiCategory implements IRecipeCategory<FirePitRecipe> {
    private static final int SLOT_STEP = 22;
    private static final int COLUMN_X = 100;

    private final IDrawable background;
    private final IDrawable icon;

    public FirePitJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(160, 90);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FIRE_PIT.get()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<FirePitRecipe> getRecipeType() {
        return materiaJeiRecipeTypes.FIRE_PIT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.materia.fire_pit");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FirePitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 32)
                .addIngredients(recipe.getIngredient())
                .addTooltipCallback((view, tooltip) -> {
                    int ticks = recipe.getCookingTime();
                    int secs = Math.max(1, (ticks + 19) / 20);
                    tooltip.add(Component.translatable("jei.materia.fire_pit.duration_ticks", secs, ticks));
                });

        int y = 10;
        for (ItemStack stack : recipe.getResults()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, COLUMN_X, y).addItemStack(stack.copy());
            y += SLOT_STEP;
        }
    }
}
