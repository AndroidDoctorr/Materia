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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class BronzeAnvilRecipe implements Recipe<CraftingInput> {
    private final Ingredient metalIngredient;
    private final int metalCount;
    private final TagKey<Item> requiredToolTag0;
    private final TagKey<Item> requiredToolTag1;
    private final ItemStack result;

    public BronzeAnvilRecipe(Ingredient metalIngredient, int metalCount,
                             TagKey<Item> requiredToolTag0, TagKey<Item> requiredToolTag1,
                             ItemStack result) {
        this.metalIngredient = metalIngredient;
        this.metalCount = metalCount;
        this.requiredToolTag0 = requiredToolTag0;
        this.requiredToolTag1 = requiredToolTag1;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) { return false; }

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
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider) { return result.copy(); }
    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) { return result.copy(); }

    public ItemStack getResultItem() { return result.copy(); }
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
        public MapCodec<BronzeAnvilRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BronzeAnvilRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final Codec<List<TagKey<Item>>> TOOL_TAGS_CODEC = TagKey.codec(Registries.ITEM).listOf();

        private static final MapCodec<BronzeAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                CountedMetalInput.CODEC.fieldOf("input").forGetter(
                        r -> new CountedMetalInput(r.metalIngredient, r.metalCount)),
                TOOL_TAGS_CODEC.fieldOf("tool_tags").forGetter(r -> List.of(r.requiredToolTag0, r.requiredToolTag1)),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result)
        ).apply(instance, (in, toolTags, result) -> {
            TagKey<Item> tag0 = !toolTags.isEmpty() ? toolTags.getFirst()
                    : TagKey.create(Registries.ITEM, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "air"));
            TagKey<Item> tag1 = toolTags.size() > 1 ? toolTags.get(1) : tag0;
            return new BronzeAnvilRecipe(in.ingredient(), in.count(), tag0, tag1, result);
        }));

        private static final StreamCodec<RegistryFriendlyByteBuf, BronzeAnvilRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
}
