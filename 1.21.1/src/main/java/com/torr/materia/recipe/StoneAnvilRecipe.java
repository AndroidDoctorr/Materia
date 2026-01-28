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

public class StoneAnvilRecipe implements Recipe<CraftingInput> {
    private final ItemStack ingredient; // one metal input
    private final TagKey<Item> requiredToolTag; // e.g., basic_hammers
    private final ItemStack result;

    public StoneAnvilRecipe(ItemStack ingredient, TagKey<Item> requiredToolTag, ItemStack result) {
        this.ingredient = ingredient;
        this.requiredToolTag = requiredToolTag;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
        return false; // Not used by vanilla crafting grid
    }

    public boolean matchesStacks(ItemStack inputMetal, ItemStack toolStack) {
        boolean toolOk = toolStack.is(requiredToolTag);
        boolean itemOk = !inputMetal.isEmpty() && inputMetal.getItem() == ingredient.getItem();
        return toolOk && itemOk;
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
    public RecipeSerializer<?> getSerializer() { return ModRecipes.STONE_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.STONE_ANVIL_TYPE.get(); }
    
    public ItemStack getIngredient() { return ingredient; }
    public TagKey<Item> getRequiredToolTag() { return requiredToolTag; }

    public static class Serializer implements RecipeSerializer<StoneAnvilRecipe> {
        @Override
        public MapCodec<StoneAnvilRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StoneAnvilRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final MapCodec<StoneAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("input").forGetter(r -> r.ingredient),
                TagKey.codec(Registries.ITEM).fieldOf("tool_tag").forGetter(r -> r.requiredToolTag),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result)
        ).apply(instance, StoneAnvilRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, StoneAnvilRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
}


