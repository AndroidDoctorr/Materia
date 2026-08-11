package com.torr.materia.item;

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

import java.util.function.Supplier;

/**
 * Milk bucket variant that returns a custom empty bucket after drinking.
 */
public class MateriaMilkBucketItem extends Item {
    private final Supplier<? extends Item> emptyBucket;

    public MateriaMilkBucketItem(Supplier<? extends Item> emptyBucket, Properties properties) {
        super(properties);
        this.emptyBucket = emptyBucket;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player && !level.isClientSide) {
            player.awardStat(Stats.ITEM_USED.get(this));
            player.getFoodData().eat(1, 0.1F);
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            return new ItemStack(emptyBucket.get());
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public net.minecraft.sounds.SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public net.minecraft.sounds.SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }
}
