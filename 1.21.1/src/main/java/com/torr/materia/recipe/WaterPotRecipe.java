package com.torr.materia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.torr.materia.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class WaterPotRecipe implements Recipe<CraftingInput> {
    private final Ingredient ingredient;
    private final int ingredientCount;
    private final NonNullList<ItemStack> results;
    private final int cookingTime;
    private final boolean requiresBoiling;
    private final boolean consumesWater;
    @Nullable
    private final ResourceLocation resultBlock;
    private final boolean requiresWater;

    public WaterPotRecipe(Ingredient ingredient, int ingredientCount,
                          NonNullList<ItemStack> results, int cookingTime,
                          boolean requiresBoiling, boolean consumesWater,
                          @Nullable ResourceLocation resultBlock, boolean requiresWater) {
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
        this.results = results;
        this.cookingTime = cookingTime;
        this.requiresBoiling = requiresBoiling;
        this.consumesWater = consumesWater;
        this.resultBlock = resultBlock;
        this.requiresWater = requiresWater;
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

    public NonNullList<ItemStack> getResults() { return results; }
    public Ingredient getIngredient() { return ingredient; }
    public int getIngredientCount() { return ingredientCount; }
    public int getCookingTime() { return cookingTime; }
    public boolean requiresBoiling() { return requiresBoiling; }
    public boolean consumesWater() { return consumesWater; }

    public boolean requiresWater() {
        return requiresWater || consumesWater || resultBlock != null;
    }

    @Nullable
    public ResourceLocation getResultBlock() {
        return resultBlock;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WATER_POT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WATER_POT_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<WaterPotRecipe> {
        private static final MapCodec<WaterPotRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.ingredient),
                Codec.INT.optionalFieldOf("ingredient_count", 1).forGetter(r -> r.ingredientCount),
                ModRecipeCodecs.ITEM_STACK_OBJECT_CODEC.listOf().optionalFieldOf("results", List.of()).forGetter(r -> r.results),
                Codec.INT.optionalFieldOf("cookingtime", 160).forGetter(r -> r.cookingTime),
                Codec.BOOL.optionalFieldOf("requires_boiling", true).forGetter(r -> r.requiresBoiling),
                Codec.BOOL.optionalFieldOf("consumes_water", false).forGetter(r -> r.consumesWater),
                ResourceLocation.CODEC.optionalFieldOf("result_block").forGetter(r -> Optional.ofNullable(r.resultBlock)),
                Codec.BOOL.optionalFieldOf("requires_water", false).forGetter(r -> r.requiresWater)
        ).apply(instance, (ingredient, ingredientCount, results, cookingTime, requiresBoiling, consumesWater, resultBlock, requiresWater) -> {
            NonNullList<ItemStack> nn = NonNullList.create();
            nn.addAll(results);
            return new WaterPotRecipe(ingredient, ingredientCount, nn, cookingTime, requiresBoiling, consumesWater,
                    resultBlock.orElse(null), requiresWater);
        }));

        private static final StreamCodec<RegistryFriendlyByteBuf, WaterPotRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

        @Override
        public MapCodec<WaterPotRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WaterPotRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
