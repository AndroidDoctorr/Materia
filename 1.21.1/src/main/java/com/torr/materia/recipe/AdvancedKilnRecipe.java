package com.torr.materia.recipe;

import com.torr.materia.ModRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Advanced kiln recipe that requires a chimney and supports dual inputs.
 * Used for smelting alloys like bronze and brass.
 */
public class AdvancedKilnRecipe implements Recipe<CraftingInput> {
    private final String group;
    private final Ingredient ingredient1;
    private final Ingredient ingredient2;
    private final ItemStack result;
    private final int cookingTime;
    private final float experience;
    private final boolean requiresChimney;
    private final boolean requiresBellows;
    private final boolean requiresCokeFuel;

    public AdvancedKilnRecipe(String group, Ingredient ingredient1, Ingredient ingredient2,
                             ItemStack result, int cookingTime, float experience, boolean requiresChimney, boolean requiresBellows, boolean requiresCokeFuel) {
        this.group = group;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.result = result;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.requiresChimney = requiresChimney;
        this.requiresBellows = requiresBellows;
        this.requiresCokeFuel = requiresCokeFuel;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        // Advanced kiln uses two input slots: 0 and 3. Do not consider fuel/output slots.
        if (input.size() >= 4) {
            ItemStack leftInput = input.getItem(0);
            ItemStack rightInput = input.getItem(3);

            boolean leftMatchesFirst = !leftInput.isEmpty() && ingredient1.test(leftInput);
            boolean rightMatchesSecond = !rightInput.isEmpty() && ingredient2.test(rightInput);

            boolean leftMatchesSecond = !leftInput.isEmpty() && ingredient2.test(leftInput);
            boolean rightMatchesFirst = !rightInput.isEmpty() && ingredient1.test(rightInput);

            return (leftMatchesFirst && rightMatchesSecond) || (leftMatchesSecond && rightMatchesFirst);
        }

        // Fallback for unexpected containers: original matching across all slots
        boolean hasIngredient1 = false;
        boolean hasIngredient2 = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (ingredient1.test(stack) && !hasIngredient1) {
                    hasIngredient1 = true;
                } else if (ingredient2.test(stack) && !hasIngredient2) {
                    hasIngredient2 = true;
                }
            }
        }
        return hasIngredient1 && hasIngredient2;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    public Ingredient getIngredient1() {
        return this.ingredient1;
    }

    public Ingredient getIngredient2() {
        return this.ingredient2;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public float getExperience() {
        return this.experience;
    }

    public boolean requiresChimney() {
        return this.requiresChimney;
    }

    public boolean requiresBellows() {
        return this.requiresBellows;
    }

    /**
     * If true, this recipe requires coal coke fuel to run.
     * Bellows-tier recipes are considered "hot" and require coal coke by default.
     */
    public boolean requiresCokeFuel() {
        return this.requiresCokeFuel || this.requiresBellows;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ADVANCED_KILN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ADVANCED_KILN_TYPE.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<AdvancedKilnRecipe> {
        @Override
        public MapCodec<AdvancedKilnRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AdvancedKilnRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final MapCodec<AdvancedKilnRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(AdvancedKilnRecipe::getGroup),
                Ingredient.CODEC.fieldOf("ingredient1").forGetter(r -> r.ingredient1),
                Ingredient.CODEC.fieldOf("ingredient2").forGetter(r -> r.ingredient2),
                ModRecipeCodecs.ITEM_STACK_OBJECT.fieldOf("result").forGetter(r -> r.result),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(r -> r.cookingTime),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(r -> r.experience),
                Codec.BOOL.optionalFieldOf("requires_chimney", true).forGetter(r -> r.requiresChimney),
                Codec.BOOL.optionalFieldOf("requires_bellows", false).forGetter(r -> r.requiresBellows),
                Codec.BOOL.optionalFieldOf("requires_coke_fuel", false).forGetter(r -> r.requiresCokeFuel)
        ).apply(instance, AdvancedKilnRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, AdvancedKilnRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
    }
} 