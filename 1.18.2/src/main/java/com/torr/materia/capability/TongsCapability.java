package com.torr.materia.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TongsCapability implements INBTSerializable<CompoundTag> {
    public static final Capability<TongsCapability> TONGS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private List<ItemStack> storedItems = new ArrayList<>();
    private static final int MAX_CAPACITY = 64; // Maximum items tongs can hold

    /**
     * Add an item to the tongs
     * @param stack The item to add
     * @return The remaining stack that couldn't be added
     */
    public ItemStack addItem(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        
        int totalStored = getTotalItemCount();
        int spaceAvailable = MAX_CAPACITY - totalStored;
        
        if (spaceAvailable <= 0) {
            return stack.copy(); // No space, return the whole stack
        }
        
        int amountToAdd = Math.min(stack.getCount(), spaceAvailable);
        ItemStack toStore = stack.copy();
        toStore.setCount(amountToAdd);
        
        // Try to merge with existing stacks first
        for (ItemStack existing : storedItems) {
            if (canMerge(existing, toStore)) {
                int space = existing.getMaxStackSize() - existing.getCount();
                int mergeAmount = Math.min(space, toStore.getCount());
                existing.grow(mergeAmount);
                toStore.shrink(mergeAmount);
                
                if (toStore.isEmpty()) {
                    // If we need to return remaining, calculate it
                    if (amountToAdd < stack.getCount()) {
                        ItemStack remaining = stack.copy();
                        remaining.setCount(stack.getCount() - amountToAdd);
                        return remaining;
                    }
                    return ItemStack.EMPTY;
                }
            }
        }
        
        // Add remaining as new stack
        if (!toStore.isEmpty()) {
            storedItems.add(toStore);
        }
        
        // Return what couldn't be added
        if (amountToAdd < stack.getCount()) {
            ItemStack remaining = stack.copy();
            remaining.setCount(stack.getCount() - amountToAdd);
            return remaining;
        }
        
        return ItemStack.EMPTY;
    }
    
    /**
     * Remove one item from the tongs
     * @return The removed item, or empty stack if none available
     */
    public ItemStack removeItem() {
        if (storedItems.isEmpty()) return ItemStack.EMPTY;
        
        // Remove one item from the last stack
        ItemStack lastStack = storedItems.get(storedItems.size() - 1);
        ItemStack result = lastStack.copy();
        result.setCount(1);
        
        lastStack.shrink(1);
        if (lastStack.isEmpty()) {
            storedItems.remove(storedItems.size() - 1);
        }
        
        return result;
    }
    
    /**
     * Remove all items from the tongs
     * @return List of all removed items
     */
    public List<ItemStack> removeAllItems() {
        List<ItemStack> result = new ArrayList<>(storedItems);
        storedItems.clear();
        return result;
    }
    
    /**
     * Get the total number of individual items stored
     */
    public int getTotalItemCount() {
        return storedItems.stream().mapToInt(ItemStack::getCount).sum();
    }
    
    /**
     * Check if tongs are empty
     */
    public boolean isEmpty() {
        return storedItems.isEmpty();
    }
    
    /**
     * Get a copy of all stored items for display purposes
     */
    public List<ItemStack> getStoredItems() {
        return new ArrayList<>(storedItems);
    }
    
    private boolean canMerge(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() 
            && ItemStack.tagMatches(stack1, stack2)
            && stack1.getCount() < stack1.getMaxStackSize();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag itemList = new ListTag();
        
        for (ItemStack stack : storedItems) {
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                stack.save(itemTag);
                itemList.add(itemTag);
            }
        }
        
        tag.put("StoredItems", itemList);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        storedItems.clear();
        
        if (nbt.contains("StoredItems")) {
            ListTag itemList = nbt.getList("StoredItems", 10); // 10 = CompoundTag
            
            for (int i = 0; i < itemList.size(); i++) {
                CompoundTag itemTag = itemList.getCompound(i);
                ItemStack stack = ItemStack.of(itemTag);
                if (!stack.isEmpty()) {
                    storedItems.add(stack);
                }
            }
        }
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final TongsCapability capability = new TongsCapability();
        private final LazyOptional<TongsCapability> optional = LazyOptional.of(() -> capability);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == TONGS_CAPABILITY ? optional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return capability.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            capability.deserializeNBT(nbt);
        }

        public void invalidate() {
            optional.invalidate();
        }
    }
}
