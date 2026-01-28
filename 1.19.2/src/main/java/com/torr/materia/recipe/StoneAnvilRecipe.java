package com.torr.materia.recipe;

import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.network.FriendlyByteBuf;

public class StoneAnvilRecipe implements Recipe<CraftingContainer> {
    private final ResourceLocation id;
    private final ItemStack ingredient; // one metal input
    private final TagKey<Item> requiredToolTag; // e.g., basic_hammers
    private final ItemStack result;

    public StoneAnvilRecipe(ResourceLocation id, ItemStack ingredient, TagKey<Item> requiredToolTag, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.requiredToolTag = requiredToolTag;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        return false; // Not used by vanilla crafting grid
    }

    public boolean matchesStacks(ItemStack inputMetal, ItemStack toolStack) {
        boolean toolOk = toolStack.is(requiredToolTag);
        boolean itemOk = !inputMetal.isEmpty() && inputMetal.getItem() == ingredient.getItem();
        return toolOk && itemOk;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem() { return result.copy(); }
    @Override
    public ResourceLocation getId() { return id; }
    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.STONE_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.STONE_ANVIL_TYPE.get(); }
    
    public ItemStack getIngredient() { return ingredient; }
    public TagKey<Item> getRequiredToolTag() { return requiredToolTag; }

    public static class Serializer implements RecipeSerializer<StoneAnvilRecipe> {
        @Override
        public StoneAnvilRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject inputObj = GsonHelper.getAsJsonObject(json, "input");
            String inputItem = GsonHelper.getAsString(inputObj, "item");
            int inputCount = GsonHelper.getAsInt(inputObj, "count", 1);
            ItemStack ingredient = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(inputItem)), inputCount);

            String toolTagStr = GsonHelper.getAsString(json, "tool_tag");
            TagKey<Item> toolTag = ItemTags.create(new ResourceLocation(toolTagStr));

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String resultItem = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(resultItem)), count);

            return new StoneAnvilRecipe(id, ingredient, toolTag, result);
        }

        @Override
        public StoneAnvilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack ingredient = buf.readItem();
            ResourceLocation tagId = buf.readResourceLocation();
            TagKey<Item> toolTag = ItemTags.create(tagId);
            ItemStack result = buf.readItem();
            return new StoneAnvilRecipe(id, ingredient, toolTag, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, StoneAnvilRecipe recipe) {
            buf.writeItem(recipe.ingredient);
            buf.writeResourceLocation(recipe.requiredToolTag.location());
            buf.writeItem(recipe.result);
        }
    }
}


