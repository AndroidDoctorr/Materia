package com.torr.materia.recipe;

import com.torr.materia.ModRecipes;
import net.minecraft.core.NonNullList;
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

public class FirePitRecipe implements Recipe<CraftingInput> {
    private final String group;
    private final Ingredient ingredient;
    private final NonNullList<ItemStack> results;
    private final int cookingTime;
    private final float experience;

    public FirePitRecipe(String group, Ingredient ingredient, NonNullList<ItemStack> results, int cookingTime, float experience) {
        this.group = group;
        this.ingredient = ingredient;
        this.results = results;
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0);
    }

    public NonNullList<ItemStack> getResults() {
        return this.results;
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
        return ModRecipes.FIRE_PIT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FIRE_PIT_TYPE.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<FirePitRecipe> {
        @Override
        public MapCodec<FirePitRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FirePitRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final MapCodec<FirePitRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(FirePitRecipe::getGroup),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.ingredient),
                ModRecipeCodecs.ITEM_STACK_OBJECT_CODEC.listOf().fieldOf("results").xmap(list -> {
                    NonNullList<ItemStack> nn = NonNullList.create();
                    nn.addAll(list);
                    return nn;
                }, list -> list).forGetter(FirePitRecipe::getResults),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(r -> r.cookingTime),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(r -> r.experience)
        ).apply(instance, FirePitRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, FirePitRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
} 