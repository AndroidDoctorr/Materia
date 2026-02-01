package com.torr.materia.item;

import com.torr.materia.entity.BombEntity;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BombItem extends Item {

    public BombItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack offhandStack = player.getOffhandItem();

        boolean hasIgniter = offhandStack.getItem() instanceof BowDrillItem ||
            offhandStack.getItem() == Items.FLINT_AND_STEEL;

        if (!hasIgniter) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        ItemStack offhandStack = player.getOffhandItem();

        boolean hasIgniter = offhandStack.getItem() instanceof BowDrillItem ||
            offhandStack.getItem() == Items.FLINT_AND_STEEL;

        if (!hasIgniter) return;

        int useDuration = this.getUseDuration(stack, entity) - timeLeft;
        float power = DynamiteItem.getPowerForTime(useDuration);

        if ((double) power < 0.1D) return;

        if (!level.isClientSide) {
            BombEntity bomb = new BombEntity(level, player);
            bomb.setItem(stack);
            bomb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 1.5F, 1.0F);
            level.addFreshEntity(bomb);

            if (offhandStack.getItem() instanceof BowDrillItem) {
                offhandStack.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            } else if (offhandStack.getItem() == Items.FLINT_AND_STEEL) {
                offhandStack.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            }
        }

        level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0F,
            1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public net.minecraft.world.item.UseAnim getUseAnimation(ItemStack stack) {
        return net.minecraft.world.item.UseAnim.BOW;
    }

    public static void registerItemProperties() {
        ItemProperties.register(com.torr.materia.ModItems.BOMB.get(),
            ResourceLocation.withDefaultNamespace("pull"), (stack, level, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                }
                return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
            });

        ItemProperties.register(com.torr.materia.ModItems.BOMB.get(),
            ResourceLocation.withDefaultNamespace("pulling"), (stack, level, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            });
    }
}

