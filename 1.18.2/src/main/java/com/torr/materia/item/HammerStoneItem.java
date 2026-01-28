package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HammerStoneItem extends Item {
    public HammerStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy(); // create a separate instance
        copy.setDamageValue(copy.getDamageValue() + 1); // damage by 1 each craft
        // If the tool has reached or exceeded its max durability, it breaks
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return copy;
    }
} 