package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MagnetItem extends Item {
    
    public MagnetItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        // Return the same magnet item - it survives crafting
        return stack.copy();
    }
}
