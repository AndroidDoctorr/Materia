package com.torr.materia.util;

import com.torr.materia.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Helpers for matching iron and copper bucket variants across world and block interactions.
 */
public final class MateriaBuckets {

    private MateriaBuckets() {
    }

    public static boolean isEmptyBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.BUCKET || item == ModItems.COPPER_BUCKET.get();
    }

    public static boolean isWaterBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.WATER_BUCKET || item == ModItems.COPPER_BUCKET_WATER.get();
    }

    public static boolean isMilkBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.MILK_BUCKET || item == ModItems.COPPER_BUCKET_MILK.get();
    }

    public static boolean isWineBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.WINE_BUCKET.get() || item == ModItems.COPPER_BUCKET_WINE.get();
    }

    public static boolean isBeerBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.BEER_BUCKET.get() || item == ModItems.COPPER_BUCKET_BEER.get();
    }

    public static boolean isTeaBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.TEA_BUCKET.get() || item == ModItems.COPPER_BUCKET_TEA.get();
    }

    public static boolean isGrapeJuiceBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.GRAPE_JUICE_BUCKET.get() || item == ModItems.COPPER_BUCKET_GRAPE_JUICE.get();
    }

    public static boolean isVinegarBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.VINEGAR_BUCKET.get() || item == ModItems.COPPER_BUCKET_VINEGAR.get();
    }

    public static boolean isOliveOilBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.OLIVE_OIL_BUCKET.get() || item == ModItems.COPPER_BUCKET_OLIVE_OIL.get();
    }

    public static boolean isCopperBucket(ItemStack stack) {
        return isCopperBucket(stack.getItem());
    }

    public static boolean isCopperBucket(Item item) {
        return item == ModItems.COPPER_BUCKET.get()
                || item == ModItems.COPPER_BUCKET_WATER.get()
                || item == ModItems.COPPER_BUCKET_MILK.get()
                || item == ModItems.COPPER_BUCKET_WINE.get()
                || item == ModItems.COPPER_BUCKET_BEER.get()
                || item == ModItems.COPPER_BUCKET_TEA.get()
                || item == ModItems.COPPER_BUCKET_GRAPE_JUICE.get()
                || item == ModItems.COPPER_BUCKET_VINEGAR.get()
                || item == ModItems.COPPER_BUCKET_OLIVE_OIL.get();
    }

    public static ItemStack emptyBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET.get() : Items.BUCKET);
    }

    public static ItemStack waterBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_WATER.get() : Items.WATER_BUCKET);
    }

    public static ItemStack milkBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_MILK.get() : Items.MILK_BUCKET);
    }

    public static ItemStack wineBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_WINE.get() : ModItems.WINE_BUCKET.get());
    }

    public static ItemStack beerBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_BEER.get() : ModItems.BEER_BUCKET.get());
    }

    public static ItemStack teaBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_TEA.get() : ModItems.TEA_BUCKET.get());
    }

    public static ItemStack grapeJuiceBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_GRAPE_JUICE.get() : ModItems.GRAPE_JUICE_BUCKET.get());
    }

    public static ItemStack vinegarBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_VINEGAR.get() : ModItems.VINEGAR_BUCKET.get());
    }

    public static ItemStack oliveOilBucketFrom(ItemStack context) {
        return new ItemStack(isCopperBucket(context) ? ModItems.COPPER_BUCKET_OLIVE_OIL.get() : ModItems.OLIVE_OIL_BUCKET.get());
    }
}
