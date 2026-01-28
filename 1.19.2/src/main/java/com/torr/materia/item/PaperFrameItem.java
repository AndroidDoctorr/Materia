package com.torr.materia.item;

import com.torr.materia.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PaperFrameItem extends Item {
    
    public PaperFrameItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(ModItems.PAPER_FRAME.get());
    }
}
