package com.torr.materia.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class BronzeKnifeItem extends SwordItem {

    public BronzeKnifeItem(Properties properties) {
        // Bronze-tier knife: better damage than flint, same speed
        super(Tiers.IRON, 2, -1.8F, properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return copy;
    }
}


