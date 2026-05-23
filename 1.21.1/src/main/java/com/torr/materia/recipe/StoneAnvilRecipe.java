package com.torr.materia.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.torr.materia.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class StoneAnvilRecipe implements Recipe<CraftingInput> {
    private final Ingredient metalIngredient;
    private final int metalCount;
    private final TagKey<Item> requiredToolTag;
    private final ItemStack result;

    public StoneAnvilRecipe(Ingredient metalIngredient, int metalCount, TagKey<Item> requiredToolTag, ItemStack result) {
        this.metalIngredient = metalIngredient;
        this.metalCount = metalCount;
        this.requiredToolTag = requiredToolTag;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
        return false;
    }

    public boolean matchesStacks(ItemStack inputMetal, ItemStack toolStack) {
        boolean toolOk = toolStack.is(requiredToolTag);
        boolean itemOk = !inputMetal.isEmpty() && metalIngredient.test(inputMetal) && inputMetal.getCount() >= metalCount;
        return toolOk && itemOk;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) { return result.copy(); }

    public ItemStack getResultItem() { return result.copy(); }
    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.STONE_ANVIL_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.STONE_ANVIL_TYPE.get(); }

    public Ingredient getMetalIngredient() { return metalIngredient; }
    public int getMetalCount() { return metalCount; }
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
                CountedMetalInput.CODEC.fieldOf("input").forGetter(
                        r -> new CountedMetalInput(r.metalIngredient, r.metalCount)),
                TagKey.codec(Registries.ITEM).fieldOf("tool_tag").forGetter(r -> r.requiredToolTag),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result)
        ).apply(instance, (in, tag, res) ->
                new StoneAnvilRecipe(in.ingredient(), in.count(), tag, res)));

        private static final StreamCodec<RegistryFriendlyByteBuf, StoneAnvilRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
}
