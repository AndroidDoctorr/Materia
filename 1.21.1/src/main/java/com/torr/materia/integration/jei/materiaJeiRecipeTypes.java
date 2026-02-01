package com.torr.materia.integration.jei;

import com.torr.materia.materia;
import com.torr.materia.integration.jei.drying_rack.DryingRackJeiRecipe;
import com.torr.materia.integration.jei.flint_knife.FlintKnifeJeiRecipe;
import com.torr.materia.integration.jei.frame_loom.FrameLoomJeiRecipe;
import com.torr.materia.recipe.AdvancedKilnRecipe;
import com.torr.materia.recipe.BronzeAnvilRecipe;
import com.torr.materia.recipe.IronAnvilRecipe;
import com.torr.materia.recipe.KilnRecipe;
import com.torr.materia.recipe.OvenRecipe;
import com.torr.materia.integration.jei.hewing.HewingJeiRecipe;
import com.torr.materia.integration.jei.primitive.PrimitiveCraftingJeiRecipe;
import com.torr.materia.recipe.StoneAnvilRecipe;
import com.torr.materia.integration.jei.water_pot.WaterPotJeiRecipe;
import mezz.jei.api.recipe.RecipeType;

public final class materiaJeiRecipeTypes {
    private materiaJeiRecipeTypes() {}

    public static final RecipeType<KilnRecipe> KILN = RecipeType.create(materia.MOD_ID, "kiln", KilnRecipe.class);
    public static final RecipeType<AdvancedKilnRecipe> ADVANCED_KILN = RecipeType.create(materia.MOD_ID, "advanced_kiln", AdvancedKilnRecipe.class);
    public static final RecipeType<OvenRecipe> OVEN = RecipeType.create(materia.MOD_ID, "oven", OvenRecipe.class);

    public static final RecipeType<StoneAnvilRecipe> STONE_ANVIL = RecipeType.create(materia.MOD_ID, "stone_anvil", StoneAnvilRecipe.class);
    public static final RecipeType<BronzeAnvilRecipe> BRONZE_ANVIL = RecipeType.create(materia.MOD_ID, "bronze_anvil", BronzeAnvilRecipe.class);
    public static final RecipeType<IronAnvilRecipe> IRON_ANVIL = RecipeType.create(materia.MOD_ID, "iron_anvil", IronAnvilRecipe.class);

    public static final RecipeType<HewingJeiRecipe> HEWING = RecipeType.create(materia.MOD_ID, "hewing", HewingJeiRecipe.class);
    public static final RecipeType<PrimitiveCraftingJeiRecipe> PRIMITIVE_CRAFTING = RecipeType.create(materia.MOD_ID, "primitive_crafting", PrimitiveCraftingJeiRecipe.class);
    public static final RecipeType<FlintKnifeJeiRecipe> FLINT_KNIFE = RecipeType.create(materia.MOD_ID, "flint_knife", FlintKnifeJeiRecipe.class);

    public static final RecipeType<FrameLoomJeiRecipe> FRAME_LOOM = RecipeType.create(materia.MOD_ID, "frame_loom", FrameLoomJeiRecipe.class);
    public static final RecipeType<WaterPotJeiRecipe> WATER_POT = RecipeType.create(materia.MOD_ID, "water_pot", WaterPotJeiRecipe.class);
    public static final RecipeType<DryingRackJeiRecipe> DRYING_RACK = RecipeType.create(materia.MOD_ID, "drying_rack", DryingRackJeiRecipe.class);
}

