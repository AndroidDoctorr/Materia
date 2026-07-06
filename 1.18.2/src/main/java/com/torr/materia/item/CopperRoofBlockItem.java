package com.torr.materia.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CopperRoofBlockItem extends RoofTilesBlockItem {

    public CopperRoofBlockItem(Block block, Properties properties) {
        super(block, properties, 0);
    }

    @Override
    public Component getName(ItemStack stack) {
        return new TranslatableComponent("item.materia.roof_copper");
    }
}
