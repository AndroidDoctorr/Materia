package com.torr.materia.utils;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.capability.TongsCapability;
import com.torr.materia.events.HotMetalEventHandler;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
public class AnvilTongsUtils {
    
    /**
     * Handle tongs interaction with any anvil type
     * @param anvilEntity The anvil block entity
     * @param tongsStack The tongs item stack
     * @param isShiftKeyDown Whether shift is held
     * @return The interaction result
     */
    public static InteractionResult handleTongsInteraction(BlockEntity anvilEntity, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        
        // Check if tongs have any items
        if (tongsCap.isEmpty()) {
            return InteractionResult.PASS;
        }
        
        var anvilItemHandler = anvilEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!anvilItemHandler.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var itemHandler = anvilItemHandler.resolve().get();
        
        // Find all input slots for this anvil type
        int[] inputSlots = findInputSlots(itemHandler);
        if (inputSlots.length == 0) {
            return InteractionResult.PASS;
        }
        
        if (isShiftKeyDown) {
            // Place all items from tongs
            boolean placedAny = false;
            while (!tongsCap.isEmpty()) {
                ItemStack toPlace = tongsCap.removeItem();
                if (toPlace.isEmpty()) break;
                
                // Only place hot metal items
                if (!toPlace.is(HotMetalEventHandler.HEATABLE_METALS)) {
                    // Put it back if it's not a heatable metal
                    tongsCap.addItem(toPlace);
                    break;
                }
                
                ItemStack remaining = insertIntoAnySlot(itemHandler, inputSlots, toPlace);
                if (!remaining.isEmpty()) {
                    // Put back what couldn't be inserted
                    tongsCap.addItem(remaining);
                    break;
                } else {
                    placedAny = true;
                }
            }
            
            if (placedAny) {
                anvilEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        } else {
            // Place one item from tongs
            ItemStack toPlace = tongsCap.removeItem();
            if (!toPlace.isEmpty()) {
                // Only place hot metal items
                if (!toPlace.is(HotMetalEventHandler.HEATABLE_METALS)) {
                    // Put it back if it's not a heatable metal
                    tongsCap.addItem(toPlace);
                    return InteractionResult.PASS;
                }
                
                ItemStack remaining = insertIntoAnySlot(itemHandler, inputSlots, toPlace);
                if (!remaining.isEmpty()) {
                    // Put back what couldn't be inserted
                    tongsCap.addItem(remaining);
                } else {
                    anvilEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Find the input slots for different anvil types
     * Stone anvil: slot 1 (0=tool, 1=input)
     * Bronze anvil: slot 2 (0=tool1, 1=tool2, 2=input)
     * Iron anvil: slots 3-4 (0-2=tools, 3-4=inputs)
     */
    private static int[] findInputSlots(net.minecraftforge.items.IItemHandler itemHandler) {
        int slots = itemHandler.getSlots();
        
        if (slots == 2) {
            // 2 slots: Stone anvil (0=tool, 1=input)
            return new int[]{1};
        } else if (slots == 3) {
            // 3 slots: Bronze anvil (0=tool1, 1=tool2, 2=input)
            return new int[]{2};
        } else if (slots == 5) {
            // 5 slots: Iron anvil (0-2=tools, 3-4=inputs)
            return new int[]{3, 4};
        }
        
        // Fallback: assume last slot is input
        return slots > 0 ? new int[]{slots - 1} : new int[]{};
    }
    
    /**
     * Insert an item into any of the given slots, preferring slots that already contain the same item
     * Uses hot metal stacking logic for proper temperature averaging
     */
    private static ItemStack insertIntoAnySlot(net.minecraftforge.items.IItemHandler itemHandler, int[] slots, ItemStack toInsert) {
        ItemStack remaining = toInsert.copy();
        
        // First pass: try to stack with existing items using hot metal stacking logic
        for (int slot : slots) {
            ItemStack existing = itemHandler.getStackInSlot(slot);
            if (!existing.isEmpty()) {
                // Check if items can merge using hot metal stacking rules
                if (com.torr.materia.utils.HotMetalStackingUtils.canMergeHotStacks(existing, remaining)) {
                    // Calculate how much we can merge
                    int space = existing.getMaxStackSize() - existing.getCount();
                    int mergeAmount = Math.min(space, remaining.getCount());
                    
                    if (mergeAmount > 0) {
                        // Create a temporary stack with just the amount we're merging
                        ItemStack toMerge = remaining.copy();
                        toMerge.setCount(mergeAmount);
                        
                        // Merge the stacks
                        ItemStack merged = com.torr.materia.utils.HotMetalStackingUtils.mergeHotStacks(existing, toMerge);
                        
                        // Extract the existing item and insert the merged version
                        itemHandler.extractItem(slot, existing.getCount(), false);
                        ItemStack notInserted = itemHandler.insertItem(slot, merged, false);
                        
                        // If we couldn't insert everything back, we have a problem
                        if (!notInserted.isEmpty()) {
                            // Put the original back and fail
                            itemHandler.insertItem(slot, existing, false);
                            return remaining;
                        }
                        
                        // Reduce the remaining amount
                        remaining.shrink(mergeAmount);
                        
                        if (remaining.isEmpty()) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                // Also try normal item handler insertion for non-hot items
                else if (ItemStack.isSameItemSameComponents(existing, remaining)) {
                    remaining = itemHandler.insertItem(slot, remaining, false);
                    if (remaining.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        
        // Second pass: try to insert into empty slots
        for (int slot : slots) {
            ItemStack existing = itemHandler.getStackInSlot(slot);
            if (existing.isEmpty()) {
                remaining = itemHandler.insertItem(slot, remaining, false);
                if (remaining.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        return remaining;
    }
}
