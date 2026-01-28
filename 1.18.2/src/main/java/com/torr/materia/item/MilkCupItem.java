package com.torr.materia.item;

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

/**
 * Drinkable milk cup that can be consumed
 * - Plays drink animation
 * - Returns an empty crucible after drinking (like wine cup)
 * - Removes negative status effects (like vanilla milk)
 */
public class MilkCupItem extends Item {

    public MilkCupItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
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
            
            // Remove all status effects (like vanilla milk bucket)
            if (!level.isClientSide) {
                user.removeAllEffects();
            }
            
            // Return empty crucible (like drinking from wine cup returns empty crucible)
            return ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.CRUCIBLE.get()));
        }
        return super.finishUsingItem(stack, level, user);
    }
}
