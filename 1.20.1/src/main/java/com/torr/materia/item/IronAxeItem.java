package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

/**
 * Wrought iron axe â€“ bridges bronze and steel (vanilla iron) tiers.
 */
public class IronAxeItem extends AxeItem {
    public IronAxeItem(Properties properties) {
        // Slightly stronger/faster than bronze axe; below steel (vanilla iron).
        super(ModToolTiers.WROUGHT_IRON, 7.0F, -3.0F, properties);
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

