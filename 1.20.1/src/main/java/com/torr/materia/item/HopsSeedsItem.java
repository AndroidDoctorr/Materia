package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class HopsSeedsItem extends ItemNameBlockItem {
    public HopsSeedsItem(Item.Properties properties) {
        super(ModBlocks.HOPS_VINE.get(), properties);
    }
}

