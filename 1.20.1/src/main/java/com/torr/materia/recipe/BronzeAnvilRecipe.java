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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class BronzeAnvilRecipe implements Recipe<CraftingContainer> {
    private final ResourceLocation id;
    private final Ingredient metalIngredient;
    private final int metalCount;
    private final TagKey<Item> requiredToolTag0;
    private final TagKey<Item> requiredToolTag1;
    private final ItemStack result;

    public BronzeAnvilRecipe(ResourceLocation id, Ingredient metalIngredient, int metalCount,
                             TagKey<Item> requiredToolTag0, TagKey<Item> requiredToolTag1,
                             ItemStack result) {
        this.id = id;
        this.metalIngredient = metalIngredient;
        this.metalCount = metalCount;
        this.requiredToolTag0 = requiredToolTag0;
        this.requiredToolTag1 = requiredToolTag1;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) { return false; }

    public boolean matchesStacks(ItemStack inputMetal, ItemStack tool0, ItemStack tool1) {
        boolean itemOk = !inputMetal.isEmpty() && metalIngredient.test(inputMetal) && inputMetal.getCount() >= metalCount;
        if (!itemOk) return false;

        if (requiredToolTag0.equals(requiredToolTag1)) {
            return !tool0.isEmpty() && tool0.is(requiredToolTag0);
        }

        boolean hasRequiredTool0 = (!tool0.isEmpty() && tool0.is(requiredToolTag0))
                || (!tool1.isEmpty() && tool1.is(requiredToolTag0));
        boolean hasRequiredTool1 = (!tool0.isEmpty() && tool0.is(requiredToolTag1))
                || (!tool1.isEmpty() && tool1.is(requiredToolTag1));

        return hasRequiredTool0 && hasRequiredTool1;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) { return result.copy(); }

    public ItemStack getResultItem() { return result.copy(); }
    @Override
    public ResourceLocation getId() { return id; }
    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.BRONZE_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.BRONZE_ANVIL_TYPE.get(); }

    public Ingredient getMetalIngredient() { return metalIngredient; }
    public int getMetalCount() { return metalCount; }
    public TagKey<Item> getRequiredToolTag0() { return requiredToolTag0; }
    public TagKey<Item> getRequiredToolTag1() { return requiredToolTag1; }

    public static class Serializer implements RecipeSerializer<BronzeAnvilRecipe> {
        @Override
        public BronzeAnvilRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject inputObj = GsonHelper.getAsJsonObject(json, "input");
            CountedMetalInput metalIn = CountedMetalInput.fromJson(inputObj);

            JsonArray tools = GsonHelper.getAsJsonArray(json, "tool_tags");
            ResourceLocation tag0 = new ResourceLocation(tools.get(0).getAsString());
            ResourceLocation tag1 = new ResourceLocation(tools.get(1).getAsString());
            TagKey<Item> requiredToolTag0 = ItemTags.create(tag0);
            TagKey<Item> requiredToolTag1 = ItemTags.create(tag1);

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String resultItem = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            ItemStack result = new ItemStack(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(resultItem)), count);

            return new BronzeAnvilRecipe(id, metalIn.ingredient, metalIn.count, requiredToolTag0, requiredToolTag1, result);
        }

        @Override
        public BronzeAnvilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedMetalInput metal = CountedMetalInput.fromNetwork(buf);
            TagKey<Item> tag0 = ItemTags.create(buf.readResourceLocation());
            TagKey<Item> tag1 = ItemTags.create(buf.readResourceLocation());
            ItemStack result = buf.readItem();
            return new BronzeAnvilRecipe(id, metal.ingredient, metal.count, tag0, tag1, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BronzeAnvilRecipe recipe) {
            new CountedMetalInput(recipe.metalIngredient, recipe.metalCount).toNetwork(buf);
            buf.writeResourceLocation(recipe.requiredToolTag0.location());
            buf.writeResourceLocation(recipe.requiredToolTag1.location());
            buf.writeItem(recipe.result);
        }
    }
}
