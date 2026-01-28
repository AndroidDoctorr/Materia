package com.torr.materia.utils;

import com.torr.materia.materia;
import com.torr.materia.capability.HotMetalCapability;
import com.torr.materia.capability.TongsCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HotMetalStackingUtils {
    
    private static final TagKey<Item> HEATABLE_METALS = ItemTags.create(new ResourceLocation(materia.MOD_ID, "heatable_metals"));
    
    /**
     * Attempt to add an item to a container with hot metal stacking support
     * @param container The container to add to
     * @param stack The stack to add
     * @return The remaining stack that couldn't be added
     */
    public static ItemStack addToContainer(Container container, ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        
        ItemStack remaining = stack.copy();
        
        // First pass: try to merge with existing stacks
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (remaining.isEmpty()) break;
            
            ItemStack existing = container.getItem(i);
            if (existing.isEmpty()) continue;
            
            ItemStack merged = tryMergeStacks(existing, remaining);
            if (!merged.isEmpty()) {
                int mergedAmount = merged.getCount() - existing.getCount();
                container.setItem(i, merged);
                remaining.shrink(mergedAmount);
            }
        }
        
        // Second pass: put remaining in empty slots
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (remaining.isEmpty()) break;
            
            if (container.getItem(i).isEmpty()) {
                container.setItem(i, remaining);
                remaining = ItemStack.EMPTY;
            }
        }
        
        return remaining;
    }
    
    /**
     * Try to merge two stacks, considering hot metal stacking rules
     * @param existing The existing stack in inventory
     * @param toAdd The stack being added
     * @return The merged stack if successful, or the original existing stack if no merge
     */
    public static ItemStack tryMergeStacks(ItemStack existing, ItemStack toAdd) {
        if (existing.isEmpty() || toAdd.isEmpty()) return existing;
        
        // First check normal stacking rules
        if (existing.getItem() == toAdd.getItem() && ItemStack.tagMatches(existing, toAdd)) {
            int space = existing.getMaxStackSize() - existing.getCount();
            if (space > 0) {
                ItemStack result = existing.copy();
                result.grow(Math.min(space, toAdd.getCount()));
                return result;
            }
        }
        
        // Then check hot metal stacking rules
        if (canMergeHotStacks(existing, toAdd)) {
            return mergeHotStacks(existing, toAdd);
        }
        
        return existing;
    }
    
    /**
     * Check if two hot metal stacks can be merged
     */
    public static boolean canMergeHotStacks(ItemStack stack1, ItemStack stack2) {
        // Must be same item type
        if (stack1.getItem() != stack2.getItem()) return false;
        
        // Both must be heatable metals
        if (!stack1.is(HEATABLE_METALS) || !stack2.is(HEATABLE_METALS)) return false;
        
        // Check available space
        if (stack1.getCount() >= stack1.getMaxStackSize()) return false;
        
        // Check if both have hot metal capabilities
        var cap1 = stack1.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
        var cap2 = stack2.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
        
        if (!cap1.isPresent() || !cap2.isPresent()) return false;
        
        var hot1 = cap1.resolve().get();
        var hot2 = cap2.resolve().get();
        
        // Both must be hot OR both must be cold
        return hot1.isHot() == hot2.isHot();
    }
    
    /**
     * Merge two hot metal stacks with temperature averaging
     */
    public static ItemStack mergeHotStacks(ItemStack existing, ItemStack toAdd) {
        if (!canMergeHotStacks(existing, toAdd)) return existing;
        
        int space = existing.getMaxStackSize() - existing.getCount();
        int mergeAmount = Math.min(space, toAdd.getCount());
        
        // Create result stack
        ItemStack result = existing.copy();
        result.grow(mergeAmount);
        
        // Average the temperatures
        var existingCap = existing.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
        var toAddCap = toAdd.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
        var resultCap = result.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
        
        if (existingCap.isPresent() && toAddCap.isPresent() && resultCap.isPresent()) {
            var hotExisting = existingCap.resolve().get();
            var hotToAdd = toAddCap.resolve().get();
            var hotResult = resultCap.resolve().get();
            
            // Calculate weighted average of heat times
            long timeExisting = hotExisting.getHeatTime();
            long timeToAdd = hotToAdd.getHeatTime();
            int countExisting = existing.getCount();
            int countToAdd = mergeAmount;
            
            long avgTime = HotMetalCapability.averageHeatTimes(timeExisting, countExisting, timeToAdd, countToAdd);
            hotResult.setHeatTime(avgTime);
        }
        
        return result;
    }
    
    /**
     * Create a cooled (regular) version of a hot metal item
     */
    public static ItemStack createCooledVersion(ItemStack hotStack) {
        // Create new stack without the hot metal capability
        ItemStack cooledStack = new ItemStack(hotStack.getItem(), hotStack.getCount());
        
        // Copy any other NBT data except the hot metal capability
        if (hotStack.hasTag()) {
            var tag = hotStack.getTag().copy();
            // Remove the hot metal capability data
            tag.remove("ForgeCaps");
            if (!tag.isEmpty()) {
                cooledStack.setTag(tag);
            }
        }
        
        return cooledStack;
    }
    
    /**
     * Check and convert cooled items in a container
     */
    public static void convertCooledItems(Container container) {
        convertCooledItemsSelective(container, false);
    }
    
    /**
     * Check and convert cooled items in a container with selective behavior
     * @param container The container to check
     * @param excludeTongs Whether to exclude items inside tongs from cooling
     */
    public static void convertCooledItemsSelective(Container container, boolean excludeTongs) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            
            if (stack.isEmpty() || !stack.is(HEATABLE_METALS)) {
                // Check if this is tongs with items - handle tongs cooling separately
                if (excludeTongs && stack.getCapability(TongsCapability.TONGS_CAPABILITY).isPresent()) {
                    // Process cooling for items inside tongs
                    processTongsCooling(stack);
                    continue;
                }
                continue;
            }
            
            final int slotIndex = i; // Make effectively final for lambda
            stack.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY).ifPresent(hotCap -> {
                // If item was heated but is now cold, convert back to regular version
                if (hotCap.getHeatTime() > 0 && !hotCap.isHot()) {
                    ItemStack cooledStack = createCooledVersion(stack);
                    if (!cooledStack.isEmpty()) {
                        container.setItem(slotIndex, cooledStack);
                    }
                }
            });
        }
    }
    
    /**
     * Process cooling for items inside tongs
     */
    private static void processTongsCooling(ItemStack tongsStack) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        boolean anyChanged = false;
        
        // Check each stored item and convert if cooled
        for (ItemStack stored : tongsCap.getStoredItems()) {
            if (stored.isEmpty() || !stored.is(HEATABLE_METALS)) {
                continue;
            }
            
            stored.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY).ifPresent(hotCap -> {
                // Items in tongs cool normally - we want this for the texture system
                if (hotCap.getHeatTime() > 0 && !hotCap.isHot()) {
                    // Convert to cooled version
                    ItemStack cooledStack = createCooledVersion(stored);
                    if (!cooledStack.isEmpty()) {
                        // Replace the hot item with cooled item in tongs
                        // This is a bit complex since we need to modify the tongs contents
                        // For now, we'll let the texture system handle the visual change
                        // and leave the actual item conversion for when it's removed from tongs
                    }
                }
            });
        }
    }
    
    /**
     * Get the hottest item from tongs (for texture determination)
     */
    public static boolean hasHotItems(ItemStack tongsStack) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return false;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        for (ItemStack stored : tongsCap.getStoredItems()) {
            var hotCapOptional = stored.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
            if (hotCapOptional.isPresent() && hotCapOptional.resolve().get().isHot()) {
                return true;
            }
        }
        return false;
    }
}
