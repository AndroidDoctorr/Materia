package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Mortar and pestle used for grinding and crushing materials.
 * Survives crafting recipes as a container item.
 */
public class MortarAndPestleItem extends Item {

    public MortarAndPestleItem(Properties properties) {
        super(properties);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        // Return the same item unchanged (mortar doesn't take damage)
        return stack.copy();
    }
}
