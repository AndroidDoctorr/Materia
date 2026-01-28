package com.torr.materia.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class HandAxeItem extends AxeItem {
    public HandAxeItem(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, properties.attributes(DiggerItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1); // damage by 1 each craft
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY; // breaks when max durability reached
        }
        return copy;
    }
} 