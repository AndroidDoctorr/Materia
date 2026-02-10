package com.torr.materia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class BronzeAnvilRecipe implements Recipe<CraftingContainer> {
    private final ResourceLocation id;
    private final ItemStack ingredient;
    private final TagKey<Item> requiredToolTag0;
    private final TagKey<Item> requiredToolTag1;
    private final ItemStack result;

    public BronzeAnvilRecipe(ResourceLocation id, ItemStack ingredient,
                             TagKey<Item> requiredToolTag0, TagKey<Item> requiredToolTag1,
                             ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.requiredToolTag0 = requiredToolTag0;
        this.requiredToolTag1 = requiredToolTag1;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) { return false; }

    public boolean matchesStacks(ItemStack inputMetal, ItemStack tool0, ItemStack tool1) {
        // Check if we have the required input
        boolean itemOk = !inputMetal.isEmpty() && inputMetal.getItem() == ingredient.getItem();
        if (!itemOk) return false;
        
        // For recipes that need the same tool in both slots, one tool is enough
        if (requiredToolTag0.equals(requiredToolTag1)) {
            boolean tool0Ok = !tool0.isEmpty() && tool0.is(requiredToolTag0);
            return tool0Ok; // Only need first tool filled
        }
        
        // For recipes that need different tools, check all possible combinations
        boolean hasRequiredTool0 = (!tool0.isEmpty() && tool0.is(requiredToolTag0)) || 
                                   (!tool1.isEmpty() && tool1.is(requiredToolTag0));
        boolean hasRequiredTool1 = (!tool0.isEmpty() && tool0.is(requiredToolTag1)) || 
                                   (!tool1.isEmpty() && tool1.is(requiredToolTag1));
        
        return hasRequiredTool0 && hasRequiredTool1;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) { return result.copy(); }

    // Convenience getter for JEI / UI code
    public ItemStack getResultItem() { return result.copy(); }
    @Override
    public ResourceLocation getId() { return id; }
    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.BRONZE_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.BRONZE_ANVIL_TYPE.get(); }
    
    public ItemStack getIngredient() { return ingredient; }
    public TagKey<Item> getRequiredToolTag0() { return requiredToolTag0; }
    public TagKey<Item> getRequiredToolTag1() { return requiredToolTag1; }

    public static class Serializer implements RecipeSerializer<BronzeAnvilRecipe> {
        @Override
        public BronzeAnvilRecipe fromJson(ResourceLocation id, JsonObject json) {
            // Back-compat: older datapack json used "input_a"
            JsonObject inputObj = json.has("input")
                    ? GsonHelper.getAsJsonObject(json, "input")
                    : GsonHelper.getAsJsonObject(json, "input_a");
            String inputItem = GsonHelper.getAsString(inputObj, "item");
            int inputCount = GsonHelper.getAsInt(inputObj, "count", 1);
            ItemStack ingredient = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(inputItem)), inputCount);

            JsonArray tools = GsonHelper.getAsJsonArray(json, "tool_tags");
            ResourceLocation tag0 = new ResourceLocation(tools.get(0).getAsString());
            ResourceLocation tag1 = new ResourceLocation(tools.get(1).getAsString());
            TagKey<Item> requiredToolTag0 = ItemTags.create(tag0);
            TagKey<Item> requiredToolTag1 = ItemTags.create(tag1);

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String resultItem = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(resultItem)), count);

            return new BronzeAnvilRecipe(id, ingredient, requiredToolTag0, requiredToolTag1, result);
        }

        @Override
        public BronzeAnvilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack ingredient = buf.readItem();
            TagKey<Item> tag0 = ItemTags.create(buf.readResourceLocation());
            TagKey<Item> tag1 = ItemTags.create(buf.readResourceLocation());
            ItemStack result = buf.readItem();
            return new BronzeAnvilRecipe(id, ingredient, tag0, tag1, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BronzeAnvilRecipe recipe) {
            buf.writeItem(recipe.ingredient);
            buf.writeResourceLocation(recipe.requiredToolTag0.location());
            buf.writeResourceLocation(recipe.requiredToolTag1.location());
            buf.writeItem(recipe.result);
        }
    }
}


