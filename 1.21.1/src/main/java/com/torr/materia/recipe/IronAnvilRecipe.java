package com.torr.materia.recipe;

import com.torr.materia.ModRecipes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;

import java.util.List;
import java.util.Optional;

public class IronAnvilRecipe implements Recipe<CraftingInput> {
    private final ItemStack ingredientA;
    private final ItemStack ingredientB; // optional (may be empty)
    private final TagKey<Item> requiredToolTag0;
    private final TagKey<Item> requiredToolTag1;
    private final TagKey<Item> requiredToolTag2;
    private final ItemStack result;

    public IronAnvilRecipe(ItemStack ingredientA, ItemStack ingredientB,
                           TagKey<Item> requiredToolTag0, TagKey<Item> requiredToolTag1, TagKey<Item> requiredToolTag2,
                           ItemStack result) {
        this.ingredientA = ingredientA;
        this.ingredientB = ingredientB;
        this.requiredToolTag0 = requiredToolTag0;
        this.requiredToolTag1 = requiredToolTag1;
        this.requiredToolTag2 = requiredToolTag2;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) { return false; }

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
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) { return result.copy(); }

    // Convenience getter for JEI / UI code
    public ItemStack getResultItem() { return result.copy(); }
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
        public MapCodec<IronAnvilRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IronAnvilRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final Codec<List<TagKey<Item>>> TOOL_TAGS_CODEC = TagKey.codec(Registries.ITEM).listOf();

        private static final MapCodec<IronAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("input_a").forGetter((IronAnvilRecipe r) -> r.ingredientA),
                ModRecipeCodecs.ITEM_STACK_OBJECT_CODEC.optionalFieldOf("input_b").forGetter((IronAnvilRecipe r) -> r.ingredientB.isEmpty() ? Optional.empty() : Optional.of(r.ingredientB)),
                TOOL_TAGS_CODEC.fieldOf("tool_tags").forGetter((IronAnvilRecipe r) -> List.of(r.requiredToolTag0, r.requiredToolTag1, r.requiredToolTag2)),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter((IronAnvilRecipe r) -> r.result)
        ).apply(instance, (a, bOpt, toolTags, result) -> {
            TagKey<Item> tag0 = toolTags.size() > 0 ? toolTags.get(0) : TagKey.create(Registries.ITEM, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "empty"));
            TagKey<Item> tag1 = toolTags.size() > 1 ? toolTags.get(1) : tag0;
            TagKey<Item> tag2 = toolTags.size() > 2 ? toolTags.get(2) : tag1;
            return new IronAnvilRecipe(a, bOpt.orElse(ItemStack.EMPTY), tag0, tag1, tag2, result);
        }));

        private static final StreamCodec<RegistryFriendlyByteBuf, IronAnvilRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
}


