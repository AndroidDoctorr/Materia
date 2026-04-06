package com.torr.materia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class WaterPotRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final int ingredientCount;
    private final NonNullList<ItemStack> results;
    private final int cookingTime;
    private final boolean requiresBoiling;
    private final boolean consumesWater;

    public WaterPotRecipe(ResourceLocation id, Ingredient ingredient, int ingredientCount,
                          NonNullList<ItemStack> results, int cookingTime,
                          boolean requiresBoiling, boolean consumesWater) {
        this.id = id;
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
        this.results = results;
        this.cookingTime = cookingTime;
        this.requiresBoiling = requiresBoiling;
        this.consumesWater = consumesWater;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0);
    }

    public NonNullList<ItemStack> getResults() { return results; }
    public Ingredient getIngredient() { return ingredient; }
    public int getIngredientCount() { return ingredientCount; }
    public int getCookingTime() { return cookingTime; }
    public boolean requiresBoiling() { return requiresBoiling; }
    public boolean consumesWater() { return consumesWater; }

    @Override
    public ResourceLocation getId() { return id; }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WATER_POT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WATER_POT_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<WaterPotRecipe> {
        @Override
        public WaterPotRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            int ingredientCount = GsonHelper.getAsInt(json, "ingredient_count", 1);

            JsonArray resultsArray = GsonHelper.getAsJsonArray(json, "results");
            NonNullList<ItemStack> results = NonNullList.create();
            for (int i = 0; i < resultsArray.size(); i++) {
                JsonObject resultObj = resultsArray.get(i).getAsJsonObject();
                String itemName = GsonHelper.getAsString(resultObj, "item");
                int count = GsonHelper.getAsInt(resultObj, "count", 1);
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
                if (item != null) results.add(new ItemStack(item, count));
            }

            int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 160);
            boolean requiresBoiling = GsonHelper.getAsBoolean(json, "requires_boiling", true);
            boolean consumesWater = GsonHelper.getAsBoolean(json, "consumes_water", false);

            return new WaterPotRecipe(recipeId, ingredient, ingredientCount, results,
                    cookingTime, requiresBoiling, consumesWater);
        }

        @Override
        public @Nullable WaterPotRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int ingredientCount = buffer.readInt();

            int resultCount = buffer.readInt();
            NonNullList<ItemStack> results = NonNullList.create();
            for (int i = 0; i < resultCount; i++) results.add(buffer.readItem());

            int cookingTime = buffer.readInt();
            boolean requiresBoiling = buffer.readBoolean();
            boolean consumesWater = buffer.readBoolean();

            return new WaterPotRecipe(recipeId, ingredient, ingredientCount, results,
                    cookingTime, requiresBoiling, consumesWater);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WaterPotRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeInt(recipe.ingredientCount);

            buffer.writeInt(recipe.results.size());
            for (ItemStack result : recipe.results) buffer.writeItem(result);

            buffer.writeInt(recipe.cookingTime);
            buffer.writeBoolean(recipe.requiresBoiling);
            buffer.writeBoolean(recipe.consumesWater);
        }
    }
}
