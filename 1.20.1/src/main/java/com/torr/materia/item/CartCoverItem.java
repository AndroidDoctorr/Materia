package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CartCoverItem extends Item {

    private final CartCoverColor color;

    public CartCoverItem(CartCoverColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    public CartCoverColor getColor() {
        return this.color;
    }

    public static CartCoverColor getColor(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof CartCoverItem cover)) {
            return null;
        }
        return cover.getColor();
    }
}
