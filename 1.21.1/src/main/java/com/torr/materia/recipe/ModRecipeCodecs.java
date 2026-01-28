package com.torr.materia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Shared codecs for custom recipe JSON formats.
 *
 * Our recipes historically used the object form:
 * { "item": "namespace:id", "count": 1 }
 * instead of vanilla's ItemStack codec.
 */
public final class ModRecipeCodecs {
    private ModRecipeCodecs() {}

    public static final MapCodec<ItemStack> ITEM_STACK_OBJECT = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("item").forGetter(s -> BuiltInRegistries.ITEM.getKey(s.getItem())),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
            ).apply(instance, (id, count) -> {
                Item item = BuiltInRegistries.ITEM.get(id);
                int safeCount = Math.max(1, count);
                return new ItemStack(item, safeCount);
            })
    );

    public static final Codec<ItemStack> ITEM_STACK_OBJECT_CODEC = ITEM_STACK_OBJECT.codec();
}
