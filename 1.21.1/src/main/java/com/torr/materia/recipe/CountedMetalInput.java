package com.torr.materia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Anvil-style metal input: Forge {@link Ingredient} JSON plus count, or legacy
 * {@code {"item": ...}} / {@code {"tag": ...}} with optional count.
 */
public record CountedMetalInput(Ingredient ingredient, int count) {
    /** Secondary slot absent for iron anvil single-metal recipes. */
    public static CountedMetalInput emptySlot() {
        return new CountedMetalInput(Ingredient.EMPTY, 0);
    }

    public CountedMetalInput {
        if (ingredient == null) {
            ingredient = Ingredient.EMPTY;
        }
        boolean emptyIng = Ingredient.EMPTY.equals(ingredient);
        if (emptyIng) {
            count = 0;
        } else if (count < 1) {
            count = 1;
        }
    }

    private static final MapCodec<CountedMetalInput> NESTED =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(CountedMetalInput::ingredient),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(CountedMetalInput::count)
            ).apply(instance, CountedMetalInput::new));

    private static ResourceLocation encodeItemIdFallback(CountedMetalInput c) {
        ItemStack[] stacks = c.ingredient().getItems();
        return stacks.length == 0 ? BuiltInRegistries.ITEM.getKey(Items.AIR) : BuiltInRegistries.ITEM.getKey(stacks[0].getItem());
    }

    private static TagKey<Item> encodeTagFallback(CountedMetalInput c) {
        // Unused in practice: decoding uses this shape; encoding always prefers NESTED.
        return TagKey.create(Registries.ITEM, BuiltInRegistries.ITEM.getKey(Items.AIR.asItem()));
    }

    private static final MapCodec<CountedMetalInput> LEGACY_ITEM =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("item").forGetter(CountedMetalInput::encodeItemIdFallback),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(CountedMetalInput::count)
            ).apply(instance, (id, c) ->
                    new CountedMetalInput(Ingredient.of(BuiltInRegistries.ITEM.get(id)), c)));

    private static final MapCodec<CountedMetalInput> LEGACY_TAG =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(CountedMetalInput::encodeTagFallback),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(CountedMetalInput::count)
            ).apply(instance, (tag, c) -> new CountedMetalInput(Ingredient.of(tag), c)));

    /** Decode accepts nested or legacy; encode prefers nested ingredient form. */
    public static final Codec<CountedMetalInput> CODEC = Codec.withAlternative(
            Codec.withAlternative(NESTED.codec(), LEGACY_ITEM.codec()),
            LEGACY_TAG.codec());
}
