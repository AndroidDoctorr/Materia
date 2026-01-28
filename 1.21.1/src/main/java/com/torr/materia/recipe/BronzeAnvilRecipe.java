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

public class BronzeAnvilRecipe implements Recipe<CraftingInput> {
    private final ItemStack ingredient;
    private final TagKey<Item> requiredToolTag0;
    private final TagKey<Item> requiredToolTag1;
    private final ItemStack result;

    public BronzeAnvilRecipe(ItemStack ingredient,
                             TagKey<Item> requiredToolTag0, TagKey<Item> requiredToolTag1,
                             ItemStack result) {
        this.ingredient = ingredient;
        this.requiredToolTag0 = requiredToolTag0;
        this.requiredToolTag1 = requiredToolTag1;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) { return false; }

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
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) { return result.copy(); }

    // Convenience getter for JEI / UI code
    public ItemStack getResultItem() { return result.copy(); }
    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.BRONZE_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.BRONZE_ANVIL_TYPE.get(); }
    
    public ItemStack getIngredient() { return ingredient; }
    public TagKey<Item> getRequiredToolTag0() { return requiredToolTag0; }
    public TagKey<Item> getRequiredToolTag1() { return requiredToolTag1; }

    public static class Serializer implements RecipeSerializer<BronzeAnvilRecipe> {
        @Override
        public MapCodec<BronzeAnvilRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BronzeAnvilRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final Codec<List<TagKey<Item>>> TOOL_TAGS_CODEC = TagKey.codec(Registries.ITEM).listOf();

        private static final MapCodec<BronzeAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("input").forGetter(r -> r.ingredient),
                TOOL_TAGS_CODEC.fieldOf("tool_tags").forGetter(r -> List.of(r.requiredToolTag0, r.requiredToolTag1)),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result)
        ).apply(instance, (ingredient, toolTags, result) -> {
            TagKey<Item> tag0 = toolTags.size() > 0 ? toolTags.get(0) : TagKey.create(Registries.ITEM, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "empty"));
            TagKey<Item> tag1 = toolTags.size() > 1 ? toolTags.get(1) : tag0;
            return new BronzeAnvilRecipe(ingredient, tag0, tag1, result);
        }));

        private static final StreamCodec<RegistryFriendlyByteBuf, BronzeAnvilRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
}


