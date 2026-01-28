package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class BronzeAxeItem extends AxeItem {
    public BronzeAxeItem(Properties properties) {
        // Bronze tier stats - better than stone but not as good as iron
        // Attack damage: base 6.0 + tier damage (2.0) = 8.0 total
        super(ModToolTiers.BRONZE, 6.0F, -3.1F, properties);
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