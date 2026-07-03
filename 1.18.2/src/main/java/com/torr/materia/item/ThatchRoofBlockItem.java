package com.torr.materia.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ThatchRoofBlockItem extends RoofTilesBlockItem {

    public ThatchRoofBlockItem(Block block, Properties properties) {
        super(block, properties, 8);
    }

    @Override
    public Component getName(ItemStack stack) {
        return new TranslatableComponent("item.materia.thatch_roof");
    }
}
