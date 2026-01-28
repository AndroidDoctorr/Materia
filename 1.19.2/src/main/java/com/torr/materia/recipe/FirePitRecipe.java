package com.torr.materia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class FirePitRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final NonNullList<ItemStack> results;
    private final int cookingTime;
    private final float experience;

    public FirePitRecipe(ResourceLocation id, String group, Ingredient ingredient, NonNullList<ItemStack> results, int cookingTime, float experience) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.results = results;
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
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
    public ResourceLocation getId() {
        return this.id;
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
        public FirePitRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            
            JsonArray resultsArray = GsonHelper.getAsJsonArray(json, "results");
            NonNullList<ItemStack> results = NonNullList.create();
            
            for (int i = 0; i < resultsArray.size(); i++) {
                JsonObject resultObj = resultsArray.get(i).getAsJsonObject();
                String itemName = GsonHelper.getAsString(resultObj, "item");
                int count = GsonHelper.getAsInt(resultObj, "count", 1);
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
                if (item != null) {
                    results.add(new ItemStack(item, count));
                }
            }
            
            int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
            float experience = GsonHelper.getAsFloat(json, "experience", 0.0f);
            
            return new FirePitRecipe(recipeId, group, ingredient, results, cookingTime, experience);
        }

        @Override
        public @Nullable FirePitRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            
            int resultCount = buffer.readInt();
            NonNullList<ItemStack> results = NonNullList.create();
            for (int i = 0; i < resultCount; i++) {
                results.add(buffer.readItem());
            }
            
            int cookingTime = buffer.readInt();
            float experience = buffer.readFloat();
            
            return new FirePitRecipe(recipeId, group, ingredient, results, cookingTime, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FirePitRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            
            buffer.writeInt(recipe.results.size());
            for (ItemStack result : recipe.results) {
                buffer.writeItem(result);
            }
            
            buffer.writeInt(recipe.cookingTime);
            buffer.writeFloat(recipe.experience);
        }
    }
} 