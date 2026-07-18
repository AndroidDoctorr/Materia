package com.torr.materia.item;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;

/**
 * Rug design template for the loom pattern slot (extends {@link BannerPatternItem} for slot compatibility).
 */
public class RugPatternItem extends BannerPatternItem {
    public static final TagKey<BannerPattern> RUG_1_TAG =
            TagKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rug_1"));
    public static final TagKey<BannerPattern> RUG_2_TAG =
            TagKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rug_2"));

    public RugPatternItem(TagKey<BannerPattern> tag, Item.Properties properties) {
        super(tag, properties);
    }
}
