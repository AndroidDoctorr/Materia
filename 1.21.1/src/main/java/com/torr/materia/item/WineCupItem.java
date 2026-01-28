package com.torr.materia.item;

import com.torr.materia.ModEffects;
import com.torr.materia.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;

/**
 * Drinkable wine cup that gives drunk effects when consumed
 * - Plays drink animation
 * - Returns an empty crucible after drinking (like bottles return empty bottles)
 * - Applies drunk status effect with visual changes
 */
public class WineCupItem extends Item {

    public WineCupItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);
            
            // Apply drunk effect for 60 seconds (1200 ticks)
            if (!level.isClientSide) {
                player.addEffect(new MobEffectInstance(ModEffects.DRUNK.getHolder().orElseThrow(), 1200, 0));
            }
            
            // Return empty crucible (like drinking from bottle returns empty bottle)
            return ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.CRUCIBLE.get()));
        }
        return super.finishUsingItem(stack, level, user);
    }
}
