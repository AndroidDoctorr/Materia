package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Iron bore used for making holes in metal plates. 
 * Survives crafting recipes while losing durability.
 */
public class IronBoreItem extends Item {

    public IronBoreItem(Properties properties) {
        super(properties);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        return copy.getDamageValue() >= copy.getMaxDamage() ? ItemStack.EMPTY : copy;
    }
}
