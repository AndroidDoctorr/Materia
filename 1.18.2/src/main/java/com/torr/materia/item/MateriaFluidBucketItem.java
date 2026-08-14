package com.torr.materia.item;

import com.torr.materia.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

/**
 * Bucket variant that returns a custom empty bucket when its fluid is placed,
 * and custom filled buckets when picking up water or milking cows.
 */
public class MateriaFluidBucketItem extends BucketItem {
    private final Supplier<? extends Item> emptyBucket;
    private final boolean usesCustomEmpty;

    public MateriaFluidBucketItem(Supplier<? extends Fluid> fluid, Supplier<? extends Item> emptyBucket, Properties properties) {
        super(fluid, properties);
        this.emptyBucket = emptyBucket;
        this.usesCustomEmpty = fluid.get() != Fluids.EMPTY;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, hand);
        if (result.getResult() != InteractionResult.SUCCESS) {
            return result;
        }

        ItemStack returned = result.getObject();
        if (!usesCustomEmpty && returned.is(Items.WATER_BUCKET)) {
            return InteractionResultHolder.sidedSuccess(new ItemStack(ModItems.COPPER_BUCKET_WATER.get()), level.isClientSide);
        }
        if (usesCustomEmpty && returned.is(Items.BUCKET)) {
            return InteractionResultHolder.sidedSuccess(new ItemStack(emptyBucket.get()), level.isClientSide);
        }
        return result;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (usesCustomEmpty || !(entity instanceof Cow cow) || cow.isBaby() || !cow.isAlive() || stack.getCount() != 1) {
            return InteractionResult.PASS;
        }

        cow.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
        ItemStack milk = new ItemStack(ModItems.COPPER_BUCKET_MILK.get());
        player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, milk));
        return InteractionResult.sidedSuccess(player.level.isClientSide);
    }

    public static MateriaFluidBucketItem copperEmpty(Properties properties) {
        return new MateriaFluidBucketItem(
                () -> Fluids.EMPTY,
                () -> ModItems.COPPER_BUCKET.get(),
                properties);
    }

    public static MateriaFluidBucketItem copperWater(Properties properties) {
        return new MateriaFluidBucketItem(
                () -> Fluids.WATER,
                () -> ModItems.COPPER_BUCKET.get(),
                properties);
    }
}
