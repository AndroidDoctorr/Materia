package com.torr.materia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.torr.materia.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IronAnvilRecipe implements Recipe<CraftingInput> {
    private final Ingredient ingredientA;
    private final int countA;
    private final Ingredient ingredientB;
    private final int countB;
    private final boolean dualMetal;
    private final TagKey<Item> requiredToolTag0;
    private final TagKey<Item> requiredToolTag1;
    private final TagKey<Item> requiredToolTag2;
    private final ItemStack result;

    public IronAnvilRecipe(Ingredient ingredientA, int countA,
                           Ingredient ingredientB, int countB, boolean dualMetal,
                           TagKey<Item> requiredToolTag0, TagKey<Item> requiredToolTag1, TagKey<Item> requiredToolTag2,
                           ItemStack result) {
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
    public boolean matches(CraftingInput inv, Level level) { return false; }

    public boolean matchesStacks(ItemStack a, ItemStack b, ItemStack tool0, ItemStack tool1, ItemStack tool2) {
        Optional<IronConsumptionPlan> plan = planConsumption(a, b);
        if (plan.isEmpty()) return false;
        List<ItemStack> availableTools = new ArrayList<>();
        if (!tool0.isEmpty()) availableTools.add(tool0);
        if (!tool1.isEmpty()) availableTools.add(tool1);
        if (!tool2.isEmpty()) availableTools.add(tool2);

        List<TagKey<Item>> requiredTags = List.of(requiredToolTag0, requiredToolTag1, requiredToolTag2);

        for (TagKey<Item> requiredTag : requiredTags) {
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
     * Resolves slot consumption for matched inputs. Single-metal recipes consume from whichever input slot carries the ingredient (prefers slot 3).
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
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) { return result.copy(); }

    public ItemStack getResultItem() { return result.copy(); }
    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.IRON_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.IRON_ANVIL_TYPE.get(); }

    public Ingredient getMetalIngredientA() { return ingredientA; }
    public int getMetalCountA() { return countA; }
    public Ingredient getMetalIngredientB() { return ingredientB; }
    public int getMetalCountB() { return countB; }
    public TagKey<Item> getRequiredToolTag0() { return requiredToolTag0; }
    public TagKey<Item> getRequiredToolTag1() { return requiredToolTag1; }
    public TagKey<Item> getRequiredToolTag2() { return requiredToolTag2; }

    public static class Serializer implements RecipeSerializer<IronAnvilRecipe> {
        @Override
        public MapCodec<IronAnvilRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IronAnvilRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final Codec<List<TagKey<Item>>> TOOL_TAGS_CODEC = TagKey.codec(Registries.ITEM).listOf();

        private static final MapCodec<IronAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                CountedMetalInput.CODEC.fieldOf("input_a").forGetter(r -> new CountedMetalInput(r.ingredientA, r.countA)),
                CountedMetalInput.CODEC.optionalFieldOf("input_b").forGetter(r ->
                        r.dualMetal ? Optional.of(new CountedMetalInput(r.ingredientB, r.countB)) : Optional.empty()),
                TOOL_TAGS_CODEC.fieldOf("tool_tags").forGetter(r -> List.of(r.requiredToolTag0, r.requiredToolTag1, r.requiredToolTag2)),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result)
        ).apply(instance, (a, bOpt, toolTags, result) -> {
            boolean dual = bOpt.isPresent();
            CountedMetalInput bIn = bOpt.orElse(CountedMetalInput.emptySlot());
            TagKey<Item> tag0 = !toolTags.isEmpty() ? toolTags.getFirst()
                    : TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "air"));
            TagKey<Item> tag1 = toolTags.size() > 1 ? toolTags.get(1) : tag0;
            TagKey<Item> tag2 = toolTags.size() > 2 ? toolTags.get(2) : tag1;
            return new IronAnvilRecipe(
                    a.ingredient(), a.count(),
                    dual ? bIn.ingredient() : Ingredient.EMPTY, dual ? bIn.count() : 0,
                    dual,
                    tag0, tag1, tag2,
                    result);
        }));

        private static final StreamCodec<RegistryFriendlyByteBuf, IronAnvilRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
}
