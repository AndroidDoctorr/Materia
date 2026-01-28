package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class WisteriaSeedsItem extends ItemNameBlockItem {
    public WisteriaSeedsItem(Item.Properties properties) {
        super(ModBlocks.WISTERIA_VINE.get(), properties);
    }
}
