package com.torr.materia.utils;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Utility class for fuel-related operations
 */
public class FuelHelper {
    
    // Define fuel tags
    private static final TagKey<Item> ROUGH_PLANKS = ItemTags.create(new ResourceLocation(materia.MOD_ID, "rough_planks"));
    private static final TagKey<Item> SMOOTH_PLANKS = ItemTags.create(new ResourceLocation(materia.MOD_ID, "smooth_planks"));
    private static final TagKey<Item> POSTS = ItemTags.create(new ResourceLocation(materia.MOD_ID, "posts"));
    private static final TagKey<Item> SAPLINGS = ItemTags.SAPLINGS;
    
    /**
     * Check if an ItemStack is a valid fuel item
     * @param stack The ItemStack to check
     * @return true if the item can be used as fuel
     */
    public static boolean isFuel(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        Item item = stack.getItem();
        
        // Check all fuel types
        boolean isFuel = item == Items.CHARCOAL || 
               item == Items.COAL ||
               item == ModItems.COAL_COKE.get() ||
               item == Items.STICK ||
               item == ModItems.REDSTONE_FUEL.get() ||
               item == ModItems.PITCH.get() ||
               item == ModItems.TAR.get() ||
               stack.is(ROUGH_PLANKS) ||
               stack.is(SMOOTH_PLANKS) ||
               stack.is(POSTS) ||
               stack.is(SAPLINGS) ||
               // Legacy check for rough_oak_plank using proper registry access
               isRoughOakPlank(item);
        
        return isFuel;
    }

    /**
     * Fuel whitelist for the kiln/furnace-kiln/blast-furnace-kiln machines.
     * These are your "industrial" heaters, so coal coke is allowed here.
     */
    public static boolean isKilnFuel(ItemStack stack) {
        return isFuel(stack);
    }

    /**
     * Fuel whitelist for the oven (food cooking). Coal coke is explicitly disallowed.
     */
    public static boolean isOvenFuel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.COAL_COKE.get()) return false;
        return isFuel(stack);
    }

    /**
     * Fuel whitelist for the fire pit (charcoal + early cooking). Coal coke is explicitly disallowed.
     */
    public static boolean isFirePitFuel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.COAL_COKE.get()) return false;
        return isFuel(stack);
    }
    
    /**
     * Check if an item is rough_oak_plank using proper registry access
     */
    private static boolean isRoughOakPlank(Item item) {
        ResourceLocation itemName = Registry.ITEM.getKey(item);
        return itemName != null && itemName.toString().equals("materia:rough_oak_plank");
    }
    
    /**
     * Get the burn time for a fuel ItemStack
     * @param stack The fuel ItemStack
     * @return burn time in ticks, or 0 if not fuel
     */
    public static int getFuelTime(ItemStack stack) {
        if (!isFuel(stack)) {
            return 0;
        }
        
        Item item = stack.getItem();
        if (item == Items.CHARCOAL || item == Items.COAL) {
            return 1600; // Same as vanilla charcoal/coal (8 smelts)
        } else if (item == ModItems.COAL_COKE.get()) {
            return 3200; // Double vanilla charcoal/coal (16 smelts)
        } else if (item == ModItems.REDSTONE_FUEL.get()) {
            return 2400; // 50% better than charcoal (12 smelts)
        } else if (item == ModItems.PITCH.get() || item == ModItems.TAR.get()) {
            return 2000; // Between charcoal and redstone_fuel (10 smelts)
        } else if (item == Items.STICK) {
            return 100; // Vanilla stick burn time (0.5 smelts)
        } else if (stack.is(ROUGH_PLANKS)) {
            return 400; // Basic wood fuel (2 smelts)
        } else if (stack.is(SMOOTH_PLANKS)) {
            return 600; // Processed wood fuel (3 smelts)
        } else if (stack.is(POSTS)) {
            return 300; // Like vanilla slabs (1.5 smelts)
        } else if (stack.is(SAPLINGS)) {
            return 100; // Vanilla sapling burn time (0.5 smelts)
        } else if (isRoughOakPlank(item)) {
            return 400; // Legacy support for rough_oak_plank
        }
        
        return 0;
    }
}
