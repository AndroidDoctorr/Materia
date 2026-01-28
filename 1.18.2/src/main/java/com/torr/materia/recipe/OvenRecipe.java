package com.torr.materia.recipe;

import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
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
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class OvenRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookingTime;
    private final float experience;

    public OvenRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, int cookingTime, float experience) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
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
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.OVEN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.OVEN_TYPE;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<OvenRecipe> {
        @Override
        public OvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            
            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String itemName = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            ItemStack result = item != null ? new ItemStack(item, count) : ItemStack.EMPTY;
            
            int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
            float experience = GsonHelper.getAsFloat(json, "experience", 0.0f);
            
            return new OvenRecipe(recipeId, group, ingredient, result, cookingTime, experience);
        }

        @Override
        public @Nullable OvenRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int cookingTime = buffer.readInt();
            float experience = buffer.readFloat();
            
            return new OvenRecipe(recipeId, group, ingredient, result, cookingTime, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, OvenRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.cookingTime);
            buffer.writeFloat(recipe.experience);
        }
    }
}
