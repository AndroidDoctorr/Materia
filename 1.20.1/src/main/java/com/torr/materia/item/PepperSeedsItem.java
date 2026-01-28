package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class PepperSeedsItem extends ItemNameBlockItem {
    public PepperSeedsItem(Item.Properties properties) {
        super(ModBlocks.PEPPERS_CROP.get(), properties);
    }
} 