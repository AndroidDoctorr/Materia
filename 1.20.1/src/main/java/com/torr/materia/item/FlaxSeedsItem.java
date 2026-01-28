package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class FlaxSeedsItem extends ItemNameBlockItem {
    public FlaxSeedsItem(Item.Properties properties) {
        super(ModBlocks.FLAX_CROP.get(), properties);
    }
} 