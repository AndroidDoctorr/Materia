package com.torr.materia.item;

import com.torr.materia.utils.TextileUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpindleItem extends Item {
    private static final int SPIN_TIME_TICKS = 40; // "a little time"

    public SpindleItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.copy();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return SPIN_TIME_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack spindle = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.consume(spindle);

        if (!hasAnyWoolClump(player)) {
            return InteractionResultHolder.pass(spindle);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(spindle);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack spindle, Level level, LivingEntity entity) {
        if (level.isClientSide) return spindle;
        if (!(entity instanceof Player player)) return spindle;

        ItemStack clump = takeOneWoolClump(player);
        if (clump.isEmpty()) return spindle;

        var outItem = TextileUtils.getStringItemForClump(clump.getItem());
        if (outItem == null) return spindle;

        ItemStack out = new ItemStack(outItem, 2);
        player.getInventory().placeItemBackInInventory(out);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 0.7F, 1.0F);
        player.getCooldowns().addCooldown(this, 10);
        return spindle;
    }

    private static boolean hasAnyWoolClump(Player player) {
        if (TextileUtils.getStringItemForClump(player.getOffhandItem().getItem()) != null) return true;
        for (ItemStack s : player.getInventory().items) {
            if (!s.isEmpty() && TextileUtils.getStringItemForClump(s.getItem()) != null) return true;
        }
        return false;
    }

    private static ItemStack takeOneWoolClump(Player player) {
        ItemStack offhand = player.getOffhandItem();
        if (!offhand.isEmpty() && TextileUtils.getStringItemForClump(offhand.getItem()) != null) {
            ItemStack taken = offhand.copy();
            taken.setCount(1);
            offhand.shrink(1);
            return taken;
        }

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack s = player.getInventory().items.get(i);
            if (!s.isEmpty() && TextileUtils.getStringItemForClump(s.getItem()) != null) {
                ItemStack taken = s.copy();
                taken.setCount(1);
                s.shrink(1);
                return taken;
            }
        }

        return ItemStack.EMPTY;
    }
}


