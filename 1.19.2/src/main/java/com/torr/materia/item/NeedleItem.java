package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Needle used for sewing and crafting textiles.
 * Survives crafting recipes as a container item.
 */
public class NeedleItem extends Item {

    public NeedleItem(Properties properties) {
        super(properties);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        // Return the same item unchanged (needle doesn't take damage)
        return stack.copy();
    }
}

