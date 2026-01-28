package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class GrapeSeedsItem extends ItemNameBlockItem {
    public GrapeSeedsItem(Item.Properties properties) {
        super(ModBlocks.GRAPE_VINE.get(), properties);
    }
}
