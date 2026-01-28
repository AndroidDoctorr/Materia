package com.torr.materia.item;

import com.torr.materia.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Represents a liquid contained in a crucible.
 * When used in crafting or consumed, returns an empty crucible.
 */
public class LiquidCrucibleItem extends Item {

    public LiquidCrucibleItem(Properties properties) {
        super(properties);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        // Return an empty crucible when this liquid is used
        return new ItemStack(ModItems.CRUCIBLE.get());
    }
}

