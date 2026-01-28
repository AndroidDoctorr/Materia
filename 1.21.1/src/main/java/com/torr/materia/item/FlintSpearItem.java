package com.torr.materia.item;

import com.torr.materia.ModEntities;
import com.torr.materia.entity.FlintSpearEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;

public class FlintSpearItem extends SwordItem {

    public FlintSpearItem(Properties properties) {
        // Stone tier, medium damage modifier (2), medium swing (-1.0F)
        super(Tiers.STONE, properties.attributes(SwordItem.createAttributes(Tiers.STONE, 2, -1.8F)));
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

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        return 72000; // allow holding right-click to "charge" (same as trident)
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity livingEntity,
            int timeLeft) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        // Minimal charge time before spear can be thrown (10 ticks)
        int charge = this.getUseDuration(stack, livingEntity) - timeLeft;
        if (charge < 10) {
            return;
        }

        if (!level.isClientSide) {
            FlintSpearEntity spear = new FlintSpearEntity(level, player, stack.copy());
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
            level.addFreshEntity(spear);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
    }
} 