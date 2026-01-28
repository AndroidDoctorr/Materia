package com.torr.materia.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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

public class PoulticeItem extends Item {
    private final float healAmount;
    private static final int USE_TICKS = 20;

    public PoulticeItem(float healAmount, Properties properties) {
        super(properties);
        this.healAmount = healAmount;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        // Bow draw animation gives visible “using” feedback without eat/drink
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_TICKS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return stack;
        }

        if (!level.isClientSide) {
            // Heal (this is what drives the vanilla heart “pop”/highlight behavior client-side)
            player.heal(healAmount);

            // Potion-like feedback (no drink animation, but still feels responsive)
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.8F, 1.0F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.INSTANT_EFFECT,
                        player.getX(), player.getY() + 1.0D, player.getZ(),
                        16, 0.35D, 0.5D, 0.35D, 0.15D);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return stack;
    }
}

