package com.torr.materia.recipe;

import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.RegistryAccess;
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

/**
 * Advanced kiln recipe that requires a chimney and supports dual inputs.
 * Used for smelting alloys like bronze and brass.
 */
public class AdvancedKilnRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient1;
    private final Ingredient ingredient2;
    private final ItemStack result;
    private final int cookingTime;
    private final float experience;
    private final boolean requiresChimney;
    private final boolean requiresBellows;
    private final boolean requiresCokeFuel;

    public AdvancedKilnRecipe(ResourceLocation id, String group, Ingredient ingredient1, Ingredient ingredient2, 
                             ItemStack result, int cookingTime, float experience, boolean requiresChimney, boolean requiresBellows, boolean requiresCokeFuel) {
        this.id = id;
        this.group = group;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.result = result;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.requiresChimney = requiresChimney;
        this.requiresBellows = requiresBellows;
        this.requiresCokeFuel = requiresCokeFuel;
    }

    @Override
    public boolean matches(Container container, Level level) {
        // Advanced kiln uses two input slots: 0 and 3. Do not consider fuel/output slots.
        if (container.getContainerSize() >= 4) {
            ItemStack leftInput = container.getItem(0);
            ItemStack rightInput = container.getItem(3);

            boolean leftMatchesFirst = !leftInput.isEmpty() && ingredient1.test(leftInput);
            boolean rightMatchesSecond = !rightInput.isEmpty() && ingredient2.test(rightInput);

            boolean leftMatchesSecond = !leftInput.isEmpty() && ingredient2.test(leftInput);
            boolean rightMatchesFirst = !rightInput.isEmpty() && ingredient1.test(rightInput);

            return (leftMatchesFirst && rightMatchesSecond) || (leftMatchesSecond && rightMatchesFirst);
        }

        // Fallback for unexpected containers: original matching across all slots
        boolean hasIngredient1 = false;
        boolean hasIngredient2 = false;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (ingredient1.test(stack) && !hasIngredient1) {
                    hasIngredient1 = true;
                } else if (ingredient2.test(stack) && !hasIngredient2) {
                    hasIngredient2 = true;
                }
            }
        }
        return hasIngredient1 && hasIngredient2;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    public Ingredient getIngredient1() {
        return this.ingredient1;
    }

    public Ingredient getIngredient2() {
        return this.ingredient2;
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
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ADVANCED_KILN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ADVANCED_KILN_TYPE.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<AdvancedKilnRecipe> {
        @Override
        public AdvancedKilnRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient1 = Ingredient.fromJson(json.get("ingredient1"));
            Ingredient ingredient2 = Ingredient.fromJson(json.get("ingredient2"));
            
            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String itemName = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            ItemStack result = item != null ? new ItemStack(item, count) : ItemStack.EMPTY;
            
            int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
            float experience = GsonHelper.getAsFloat(json, "experience", 0.0f);
            boolean requiresChimney = GsonHelper.getAsBoolean(json, "requires_chimney", true);
            boolean requiresBellows = GsonHelper.getAsBoolean(json, "requires_bellows", false);
            boolean requiresCokeFuel = GsonHelper.getAsBoolean(json, "requires_coke_fuel", false);
            
            return new AdvancedKilnRecipe(recipeId, group, ingredient1, ingredient2, result, cookingTime, experience, requiresChimney, requiresBellows, requiresCokeFuel);
        }

        @Override
        public @Nullable AdvancedKilnRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int cookingTime = buffer.readInt();
            float experience = buffer.readFloat();
            boolean requiresChimney = buffer.readBoolean();
            boolean requiresBellows = buffer.readBoolean();
            boolean requiresCokeFuel = buffer.readBoolean();
            
            return new AdvancedKilnRecipe(recipeId, group, ingredient1, ingredient2, result, cookingTime, experience, requiresChimney, requiresBellows, requiresCokeFuel);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AdvancedKilnRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient1.toNetwork(buffer);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.cookingTime);
            buffer.writeFloat(recipe.experience);
            buffer.writeBoolean(recipe.requiresChimney);
            buffer.writeBoolean(recipe.requiresBellows);
            buffer.writeBoolean(recipe.requiresCokeFuel);
        }
    }
} 