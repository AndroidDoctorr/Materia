package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Bronze drawplate used for wire making. 
 * Survives crafting recipes while losing durability.
 */
public class BronzeDrawplateItem extends Item {

    public BronzeDrawplateItem(Properties properties) {
        super(properties);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        return copy.getDamageValue() >= copy.getMaxDamage() ? ItemStack.EMPTY : copy;
    }
}
