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

public class IronAnvilRecipe implements Recipe<CraftingContainer> {
    private final ResourceLocation id;
    private final ItemStack ingredientA;
    private final ItemStack ingredientB; // optional (may be empty)
    private final TagKey<Item> requiredToolTag0;
    private final TagKey<Item> requiredToolTag1;
    private final TagKey<Item> requiredToolTag2;
    private final ItemStack result;

    public IronAnvilRecipe(ResourceLocation id, ItemStack ingredientA, ItemStack ingredientB,
                           TagKey<Item> requiredToolTag0, TagKey<Item> requiredToolTag1, TagKey<Item> requiredToolTag2,
                           ItemStack result) {
        this.id = id;
        this.ingredientA = ingredientA;
        this.ingredientB = ingredientB;
        this.requiredToolTag0 = requiredToolTag0;
        this.requiredToolTag1 = requiredToolTag1;
        this.requiredToolTag2 = requiredToolTag2;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) { return false; }

    public boolean matchesStacks(ItemStack a, ItemStack b, ItemStack tool0, ItemStack tool1, ItemStack tool2) {
        // Accept input items from either slot!        
        boolean itemAOk = ingredientA.getItem() == a.getItem() || ingredientA.getItem() == b.getItem();
        boolean itemBOk = ingredientB.isEmpty() || ingredientB.getItem() == b.getItem() || ingredientB.getItem() == a.getItem();
        if (!itemAOk || !itemBOk) return false;
        
        // Collect all tools that are present
        java.util.List<ItemStack> availableTools = new java.util.ArrayList<>();
        if (!tool0.isEmpty()) availableTools.add(tool0);
        if (!tool1.isEmpty()) availableTools.add(tool1);
        if (!tool2.isEmpty()) availableTools.add(tool2);
        
        // Collect all required tool tags
        java.util.List<TagKey<Item>> requiredTags = new java.util.ArrayList<>();
        requiredTags.add(requiredToolTag0);
        requiredTags.add(requiredToolTag1);
        requiredTags.add(requiredToolTag2);
        
        // Check if we have all required tools (in any position)
        for (TagKey<Item> requiredTag : requiredTags) {
            boolean found = false;
            for (ItemStack tool : availableTools) {
                if (tool.is(requiredTag)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        
        return true;
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
    public RecipeSerializer<?> getSerializer() { return ModRecipes.IRON_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.IRON_ANVIL_TYPE.get(); }
    
    public ItemStack getIngredientA() { return ingredientA; }
    public ItemStack getIngredientB() { return ingredientB; }
    public TagKey<Item> getRequiredToolTag0() { return requiredToolTag0; }
    public TagKey<Item> getRequiredToolTag1() { return requiredToolTag1; }
    public TagKey<Item> getRequiredToolTag2() { return requiredToolTag2; }

    public static class Serializer implements RecipeSerializer<IronAnvilRecipe> {
        @Override
        public IronAnvilRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject inputA = GsonHelper.getAsJsonObject(json, "input_a");
            String itemA = GsonHelper.getAsString(inputA, "item");
            int countA = GsonHelper.getAsInt(inputA, "count", 1);
            ItemStack a = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemA)), countA);

            ItemStack b = ItemStack.EMPTY;
            if (json.has("input_b")) {
                JsonObject inputB = GsonHelper.getAsJsonObject(json, "input_b");
                String itemB = GsonHelper.getAsString(inputB, "item");
                int countB = GsonHelper.getAsInt(inputB, "count", 1);
                b = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemB)), countB);
            }

            JsonArray tools = GsonHelper.getAsJsonArray(json, "tool_tags");
            TagKey<Item> tag0 = ItemTags.create(new ResourceLocation(tools.get(0).getAsString()));
            TagKey<Item> tag1 = ItemTags.create(new ResourceLocation(tools.get(1).getAsString()));
            TagKey<Item> tag2 = ItemTags.create(new ResourceLocation(tools.get(2).getAsString()));

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String resultItem = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(resultItem)), count);

            return new IronAnvilRecipe(id, a, b, tag0, tag1, tag2, result);
        }

        @Override
        public IronAnvilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack a = buf.readItem();
            ItemStack b = buf.readItem();
            TagKey<Item> tag0 = ItemTags.create(buf.readResourceLocation());
            TagKey<Item> tag1 = ItemTags.create(buf.readResourceLocation());
            TagKey<Item> tag2 = ItemTags.create(buf.readResourceLocation());
            ItemStack result = buf.readItem();
            return new IronAnvilRecipe(id, a, b, tag0, tag1, tag2, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, IronAnvilRecipe r) {
            buf.writeItem(r.ingredientA);
            buf.writeItem(r.ingredientB);
            buf.writeResourceLocation(r.requiredToolTag0.location());
            buf.writeResourceLocation(r.requiredToolTag1.location());
            buf.writeResourceLocation(r.requiredToolTag2.location());
            buf.writeItem(r.result);
        }
    }
}


