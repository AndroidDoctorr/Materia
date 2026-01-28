package com.torr.materia.recipe;

import com.torr.materia.ModRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class KilnRecipe implements Recipe<CraftingInput> {
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookingTime;
    private final float experience;
    private final boolean requiresChimney;
    private final boolean requiresBellows;
    private final boolean requiresCokeFuel;

    public KilnRecipe(String group, Ingredient ingredient, ItemStack result, int cookingTime, float experience, boolean requiresChimney, boolean requiresBellows, boolean requiresCokeFuel) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.requiresChimney = requiresChimney;
        this.requiresBellows = requiresBellows;
        this.requiresCokeFuel = requiresCokeFuel;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public float getExperience() {
        return this.experience;
    }

    public boolean requiresChimney() {
        return this.requiresChimney;
    }

    public boolean requiresBellows() {
        return this.requiresBellows;
    }

    /**
     * If true, this recipe requires coal coke fuel to run.
     * Bellows-tier recipes are considered "hot" and require coal coke by default.
     */
    public boolean requiresCokeFuel() {
        return this.requiresCokeFuel || this.requiresBellows;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.KILN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.KILN_TYPE.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<KilnRecipe> {
        @Override
        public MapCodec<KilnRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, KilnRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final MapCodec<KilnRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(KilnRecipe::getGroup),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.ingredient),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(r -> r.cookingTime),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(r -> r.experience),
                Codec.BOOL.optionalFieldOf("requires_chimney", false).forGetter(r -> r.requiresChimney),
                Codec.BOOL.optionalFieldOf("requires_bellows", false).forGetter(r -> r.requiresBellows),
                Codec.BOOL.optionalFieldOf("requires_coke_fuel", false).forGetter(r -> r.requiresCokeFuel)
        ).apply(instance, KilnRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, KilnRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
} 