package com.torr.materia;

import com.torr.materia.recipe.FlintKnifeRecipe;
import com.torr.materia.recipe.FlintSpearRecipe;
import com.torr.materia.recipe.LeatherArmorDyeRecipe;
import com.torr.materia.recipe.PrimitiveCraftingTableRecipe;
import com.torr.materia.recipe.FirePitRecipe;
import com.torr.materia.recipe.KilnRecipe;
import com.torr.materia.recipe.OvenRecipe;
import com.torr.materia.recipe.AdvancedKilnRecipe;
import com.torr.materia.recipe.StoneAnvilRecipe;
import com.torr.materia.recipe.BronzeAnvilRecipe;
import com.torr.materia.recipe.IronAnvilRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, materia.MOD_ID);

    /**
     * Recipe types MUST be registered during mod construction (not during CommonSetup),
     * otherwise datapack reload can attempt to build the recipe map while these are still null,
     * causing "null key in entry" crashes.
     */
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, materia.MOD_ID);

    public static final RegistryObject<RecipeSerializer<FlintKnifeRecipe>> FLINT_KNIFE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_flint_knife", () -> new SimpleRecipeSerializer<>(FlintKnifeRecipe::new));

    public static final RegistryObject<RecipeSerializer<FlintSpearRecipe>> FLINT_SPEAR_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_flint_spear", () -> new SimpleRecipeSerializer<>(FlintSpearRecipe::new));

    public static final RegistryObject<RecipeSerializer<PrimitiveCraftingTableRecipe>> PRIMITIVE_CRAFTING_TABLE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_primitive_crafting_table", () -> new SimpleRecipeSerializer<>(PrimitiveCraftingTableRecipe::new));

    public static final RegistryObject<RecipeSerializer<LeatherArmorDyeRecipe>> LEATHER_ARMOR_DYE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_leather_armor_dyeing", () -> new SimpleRecipeSerializer<>(LeatherArmorDyeRecipe::new));

    public static final RegistryObject<RecipeSerializer<FirePitRecipe>> FIRE_PIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("fire_pit", () -> new FirePitRecipe.Serializer());

    public static final RegistryObject<RecipeSerializer<KilnRecipe>> KILN_SERIALIZER =
            RECIPE_SERIALIZERS.register("kiln", () -> new KilnRecipe.Serializer());

    public static final RegistryObject<RecipeSerializer<OvenRecipe>> OVEN_SERIALIZER =
            RECIPE_SERIALIZERS.register("oven", () -> new OvenRecipe.Serializer());

    public static final RegistryObject<RecipeSerializer<AdvancedKilnRecipe>> ADVANCED_KILN_SERIALIZER =
            RECIPE_SERIALIZERS.register("advanced_kiln", () -> new AdvancedKilnRecipe.Serializer());

    public static final RegistryObject<RecipeSerializer<com.torr.materia.recipe.HewingRecipe>> HEWING_SERIALIZER =
            RECIPE_SERIALIZERS.register("hewing", () -> new SimpleRecipeSerializer<>(com.torr.materia.recipe.HewingRecipe::new));
    
    public static final RegistryObject<RecipeSerializer<StoneAnvilRecipe>> STONE_ANVIL_SERIALIZER =
            RECIPE_SERIALIZERS.register("stone_anvil", () -> new StoneAnvilRecipe.Serializer());
    public static final RegistryObject<RecipeSerializer<BronzeAnvilRecipe>> BRONZE_ANVIL_SERIALIZER =
            RECIPE_SERIALIZERS.register("bronze_anvil", () -> new BronzeAnvilRecipe.Serializer());
    public static final RegistryObject<RecipeSerializer<IronAnvilRecipe>> IRON_ANVIL_SERIALIZER =
            RECIPE_SERIALIZERS.register("iron_anvil", () -> new IronAnvilRecipe.Serializer());
    
    // Recipe types (registered early)
    public static final RegistryObject<RecipeType<FirePitRecipe>> FIRE_PIT_TYPE =
            RECIPE_TYPES.register("fire_pit", () -> new RecipeType<>() { });
    public static final RegistryObject<RecipeType<KilnRecipe>> KILN_TYPE =
            RECIPE_TYPES.register("kiln", () -> new RecipeType<>() { });
    public static final RegistryObject<RecipeType<OvenRecipe>> OVEN_TYPE =
            RECIPE_TYPES.register("oven", () -> new RecipeType<>() { });
    public static final RegistryObject<RecipeType<AdvancedKilnRecipe>> ADVANCED_KILN_TYPE =
            RECIPE_TYPES.register("advanced_kiln", () -> new RecipeType<>() { });
    public static final RegistryObject<RecipeType<StoneAnvilRecipe>> STONE_ANVIL_TYPE =
            RECIPE_TYPES.register("stone_anvil", () -> new RecipeType<>() { });
    public static final RegistryObject<RecipeType<BronzeAnvilRecipe>> BRONZE_ANVIL_TYPE =
            RECIPE_TYPES.register("bronze_anvil", () -> new RecipeType<>() { });
    public static final RegistryObject<RecipeType<IronAnvilRecipe>> IRON_ANVIL_TYPE =
            RECIPE_TYPES.register("iron_anvil", () -> new RecipeType<>() { });

} 