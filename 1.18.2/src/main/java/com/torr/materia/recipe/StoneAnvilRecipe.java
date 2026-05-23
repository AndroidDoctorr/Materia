package com.torr.materia.recipe;

import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.network.FriendlyByteBuf;

public class StoneAnvilRecipe implements Recipe<CraftingContainer> {
    private final ResourceLocation id;
    private final Ingredient metalIngredient;
    private final int metalCount;
    private final TagKey<net.minecraft.world.item.Item> requiredToolTag;
    private final ItemStack result;

    public StoneAnvilRecipe(ResourceLocation id, Ingredient metalIngredient, int metalCount,
                            TagKey<net.minecraft.world.item.Item> requiredToolTag, ItemStack result) {
        this.id = id;
        this.metalIngredient = metalIngredient;
        this.metalCount = metalCount;
        this.requiredToolTag = requiredToolTag;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        return false;
    }

    public boolean matchesStacks(ItemStack inputMetal, ItemStack toolStack) {
        boolean toolOk = toolStack.is(requiredToolTag);
        boolean itemOk = !inputMetal.isEmpty() && metalIngredient.test(inputMetal) && inputMetal.getCount() >= metalCount;
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
    public RecipeType<?> getType() { return ModRecipes.STONE_ANVIL_TYPE; }

    public Ingredient getMetalIngredient() { return metalIngredient; }
    public int getMetalCount() { return metalCount; }
    public TagKey<net.minecraft.world.item.Item> getRequiredToolTag() { return requiredToolTag; }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<StoneAnvilRecipe> {
        public Serializer() {
            setRegistryName(new net.minecraft.resources.ResourceLocation(com.torr.materia.materia.MOD_ID, "stone_anvil"));
        }
        @Override
        public StoneAnvilRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject inputObj = GsonHelper.getAsJsonObject(json, "input");
            CountedMetalInput metalIn = CountedMetalInput.fromJson(inputObj);

            String toolTagStr = GsonHelper.getAsString(json, "tool_tag");
            TagKey<net.minecraft.world.item.Item> toolTag =
                    ItemTags.create(new net.minecraft.resources.ResourceLocation(toolTagStr));

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String resultItem = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            ItemStack result = new ItemStack(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    new net.minecraft.resources.ResourceLocation(resultItem)), count);

            return new StoneAnvilRecipe(id, metalIn.ingredient, metalIn.count, toolTag, result);
        }

        @Override
        public StoneAnvilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedMetalInput metal = CountedMetalInput.fromNetwork(buf);
            ResourceLocation tagId = buf.readResourceLocation();
            TagKey<net.minecraft.world.item.Item> toolTag = ItemTags.create(tagId);
            ItemStack result = buf.readItem();
            return new StoneAnvilRecipe(id, metal.ingredient, metal.count, toolTag, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, StoneAnvilRecipe recipe) {
            new CountedMetalInput(recipe.metalIngredient, recipe.metalCount).toNetwork(buf);
            buf.writeResourceLocation(recipe.requiredToolTag.location());
            buf.writeItem(recipe.result);
        }
    }
}
