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

public class OvenRecipe implements Recipe<CraftingInput> {
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookingTime;
    private final float experience;

    public OvenRecipe(String group, Ingredient ingredient, ItemStack result, int cookingTime, float experience) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.cookingTime = cookingTime;
        this.experience = experience;
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

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.OVEN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.OVEN_TYPE.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<OvenRecipe> {
        @Override
        public MapCodec<OvenRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, OvenRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final MapCodec<OvenRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(OvenRecipe::getGroup),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.ingredient),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(r -> r.cookingTime),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(r -> r.experience)
        ).apply(instance, OvenRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, OvenRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
}
