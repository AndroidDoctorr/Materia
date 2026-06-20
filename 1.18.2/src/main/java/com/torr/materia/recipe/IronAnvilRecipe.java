package com.torr.materia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.torr.materia.ModRecipes;
import net.minecraft.network.FriendlyByteBuf;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IronAnvilRecipe implements Recipe<CraftingContainer> {
    private final ResourceLocation id;
    private final Ingredient ingredientA;
    private final int countA;
    private final Ingredient ingredientB;
    private final int countB;
    private final boolean dualMetal;
    private final TagKey<net.minecraft.world.item.Item> requiredToolTag0;
    private final TagKey<net.minecraft.world.item.Item> requiredToolTag1;
    private final TagKey<net.minecraft.world.item.Item> requiredToolTag2;
    private final ItemStack result;

    public IronAnvilRecipe(ResourceLocation id,
                           Ingredient ingredientA, int countA,
                           Ingredient ingredientB, int countB, boolean dualMetal,
                           TagKey<net.minecraft.world.item.Item> requiredToolTag0,
                           TagKey<net.minecraft.world.item.Item> requiredToolTag1,
                           TagKey<net.minecraft.world.item.Item> requiredToolTag2,
                           ItemStack result) {
        this.id = id;
        this.ingredientA = ingredientA;
        this.countA = countA;
        this.ingredientB = ingredientB;
        this.countB = countB;
        this.dualMetal = dualMetal;
        this.requiredToolTag0 = requiredToolTag0;
        this.requiredToolTag1 = requiredToolTag1;
        this.requiredToolTag2 = requiredToolTag2;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) { return false; }

    public boolean matchesStacks(ItemStack a, ItemStack b, ItemStack tool0, ItemStack tool1, ItemStack tool2) {
        Optional<IronConsumptionPlan> plan = planConsumption(a, b);
        if (plan.isEmpty()) return false;
        List<ItemStack> availableTools = new ArrayList<>();
        if (!tool0.isEmpty()) availableTools.add(tool0);
        if (!tool1.isEmpty()) availableTools.add(tool1);
        if (!tool2.isEmpty()) availableTools.add(tool2);

        List<TagKey<net.minecraft.world.item.Item>> requiredTags = new ArrayList<>();
        requiredTags.add(requiredToolTag0);
        requiredTags.add(requiredToolTag1);
        requiredTags.add(requiredToolTag2);

        for (TagKey<net.minecraft.world.item.Item> requiredTag : requiredTags) {
            boolean found = false;
            for (ItemStack tool : availableTools) {
                if (!tool.isEmpty() && tool.is(requiredTag)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    /**
     * Resolves slot consumption for matched inputs. Single-metal recipes consume from whichever input slot carries the ingredient.
     */
    public Optional<IronConsumptionPlan> planConsumption(ItemStack slot3, ItemStack slot4) {
        ItemStack s3 = slot3;
        ItemStack s4 = slot4;
        if (dualMetal) {
            boolean order1 = ingredientA.test(s3) && s3.getCount() >= countA
                    && ingredientB.test(s4) && s4.getCount() >= countB;
            boolean order2 = ingredientA.test(s4) && s4.getCount() >= countA
                    && ingredientB.test(s3) && s3.getCount() >= countB;
            if (order1) return Optional.of(new IronConsumptionPlan(countA, countB));
            if (order2) return Optional.of(new IronConsumptionPlan(countB, countA));
            return Optional.empty();
        }
        // Single metal: prefer slot 3 first (matches common UI placement).
        if (ingredientA.test(s3) && s3.getCount() >= countA) {
            return Optional.of(new IronConsumptionPlan(countA, 0));
        }
        if (ingredientA.test(s4) && s4.getCount() >= countA) {
            return Optional.of(new IronConsumptionPlan(0, countA));
        }
        return Optional.empty();
    }

    public boolean requiresSecondMetal() {
        return dualMetal;
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
    public RecipeSerializer<?> getSerializer() { return ModRecipes.IRON_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.IRON_ANVIL_TYPE; }

    public Ingredient getMetalIngredientA() { return ingredientA; }
    public int getMetalCountA() { return countA; }
    public Ingredient getMetalIngredientB() { return ingredientB; }
    public int getMetalCountB() { return countB; }
    public TagKey<net.minecraft.world.item.Item> getRequiredToolTag0() { return requiredToolTag0; }
    public TagKey<net.minecraft.world.item.Item> getRequiredToolTag1() { return requiredToolTag1; }
    public TagKey<net.minecraft.world.item.Item> getRequiredToolTag2() { return requiredToolTag2; }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<IronAnvilRecipe> {
        @Override
        public IronAnvilRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject inputAObj = GsonHelper.getAsJsonObject(json, "input_a");
            CountedMetalInput aIn = CountedMetalInput.fromJson(inputAObj);

            CountedMetalInput bIn;
            boolean dual;
            if (json.has("input_b")) {
                JsonObject inputBObj = GsonHelper.getAsJsonObject(json, "input_b");
                bIn = CountedMetalInput.fromJson(inputBObj);
                dual = true;
            } else {
                bIn = new CountedMetalInput(Ingredient.EMPTY, 0);
                dual = false;
            }

            JsonArray tools = GsonHelper.getAsJsonArray(json, "tool_tags");
            TagKey<net.minecraft.world.item.Item> tag0 = ItemTags.create(new ResourceLocation(tools.get(0).getAsString()));
            TagKey<net.minecraft.world.item.Item> tag1 = ItemTags.create(new ResourceLocation(tools.get(1).getAsString()));
            TagKey<net.minecraft.world.item.Item> tag2 = ItemTags.create(new ResourceLocation(tools.get(2).getAsString()));

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String resultItem = GsonHelper.getAsString(resultObj, "item");
            int count = GsonHelper.getAsInt(resultObj, "count", 1);
            ItemStack result = new ItemStack(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(resultItem)), count);

            return new IronAnvilRecipe(id, aIn.ingredient, aIn.count, bIn.ingredient, bIn.count, dual, tag0, tag1, tag2, result);
        }

        @Override
        public IronAnvilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedMetalInput aIn = CountedMetalInput.fromNetwork(buf);
            CountedMetalInput bIn = CountedMetalInput.fromNetwork(buf);
            boolean dual = buf.readBoolean();
            TagKey<net.minecraft.world.item.Item> tag0 = ItemTags.create(buf.readResourceLocation());
            TagKey<net.minecraft.world.item.Item> tag1 = ItemTags.create(buf.readResourceLocation());
            TagKey<net.minecraft.world.item.Item> tag2 = ItemTags.create(buf.readResourceLocation());
            ItemStack result = buf.readItem();
            return new IronAnvilRecipe(id, aIn.ingredient, aIn.count, bIn.ingredient, bIn.count, dual,
                    tag0, tag1, tag2, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, IronAnvilRecipe recipe) {
            new CountedMetalInput(recipe.ingredientA, recipe.countA).toNetwork(buf);
            new CountedMetalInput(recipe.ingredientB, recipe.countB).toNetwork(buf);
            buf.writeBoolean(recipe.dualMetal);
            buf.writeResourceLocation(recipe.requiredToolTag0.location());
            buf.writeResourceLocation(recipe.requiredToolTag1.location());
            buf.writeResourceLocation(recipe.requiredToolTag2.location());
            buf.writeItem(recipe.result);
        }
    }
}
