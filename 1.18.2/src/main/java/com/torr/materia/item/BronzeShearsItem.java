package com.torr.materia.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;

public class BronzeShearsItem extends ShearsItem {
    public BronzeShearsItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return copy;
    }
}


