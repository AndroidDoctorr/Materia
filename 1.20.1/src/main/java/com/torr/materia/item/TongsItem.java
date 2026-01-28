package com.torr.materia.item;

import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TongsItem extends Item {
    private final String hotModelSuffix;
    private final String coldModelSuffix;
    
    public TongsItem(Properties properties, String hotModelSuffix, String coldModelSuffix) {
        super(properties);
        this.hotModelSuffix = hotModelSuffix;
        this.coldModelSuffix = coldModelSuffix;
    }
    
    public TongsItem(Properties properties) {
        this(properties, "_ingot_hot", "_ingot_cold");
    }
    
    /**
     * Get the model suffix based on the contents of the tongs
     */
    public String getModelSuffix(ItemStack stack) {
        if (HotMetalStackingUtils.hasHotItems(stack)) {
            return hotModelSuffix;
        } else if (hasAnyItems(stack)) {
            return coldModelSuffix;
        }
        return ""; // Default model (empty tongs)
    }
    
    private boolean hasAnyItems(ItemStack tongsStack) {
        var tongsCapOptional = tongsStack.getCapability(com.torr.materia.capability.TongsCapability.TONGS_CAPABILITY);
        if (tongsCapOptional.isPresent()) {
            return !tongsCapOptional.resolve().get().isEmpty();
        }
        return false;
    }
    
    /* Container behaviour for crafting - tongs lose durability when used */
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
