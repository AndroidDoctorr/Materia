package com.torr.materia.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.materia.cart_cover.phantom_shelter")
                .withStyle(ChatFormatting.GRAY));
    }
}
