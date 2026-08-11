package com.torr.materia.item;

import com.torr.materia.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

/**
 * Bucket variant that returns a custom empty bucket when its fluid is placed.
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
        if (!usesCustomEmpty || result.getResult() != InteractionResult.SUCCESS) {
            return result;
        }

        ItemStack returned = result.getObject();
        if (returned.is(Items.BUCKET)) {
            return InteractionResultHolder.sidedSuccess(new ItemStack(emptyBucket.get()), level.isClientSide());
        }
        return result;
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
