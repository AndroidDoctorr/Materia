package com.torr.materia.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Capability for steel pipes to hold hot glass
 */
public class GlassPipeCapability implements INBTSerializable<CompoundTag> {
    public static final Capability<GlassPipeCapability> GLASS_PIPE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private ItemStack storedGlass = ItemStack.EMPTY;
    private static final int MAX_CAPACITY = 1; // Steel pipe can only hold one glass puck at a time

    /**
     * Add a glass puck to the pipe
     * @param stack The glass puck to add
     * @return The remaining stack that couldn't be added
     */
    public ItemStack addGlass(ItemStack stack) {
        if (stack.isEmpty() || !storedGlass.isEmpty()) {
            return stack; // Can't add if pipe already has glass or stack is empty
        }
        
        // Only accept glass pucks
        if (stack.getItem() != com.torr.materia.ModItems.GLASS_PUCK.get()) {
            return stack;
        }
        
        if (stack.getCount() >= 1) {
            storedGlass = stack.copy();
            storedGlass.setCount(1);
            
            ItemStack remaining = stack.copy();
            remaining.shrink(1);
            return remaining.isEmpty() ? ItemStack.EMPTY : remaining;
        }
        
        return stack;
    }
    
    /**
     * Remove the glass puck from the pipe
     * @return The removed glass puck, or empty stack if none available
     */
    public ItemStack removeGlass() {
        if (storedGlass.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack result = storedGlass.copy();
        storedGlass = ItemStack.EMPTY;
        return result;
    }
    
    /**
     * Check if pipe has glass
     */
    public boolean hasGlass() {
        return !storedGlass.isEmpty();
    }
    
    /**
     * Get the stored glass for display purposes
     */
    public ItemStack getStoredGlass() {
        return storedGlass.copy();
    }
    
    /**
     * Check if the stored glass is hot
     */
    public boolean hasHotGlass() {
        if (storedGlass.isEmpty()) {
            return false;
        }
        
        return storedGlass.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY)
                .map(HotMetalCapability::isHot)
                .orElse(false);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (!storedGlass.isEmpty()) {
            CompoundTag glassTag = new CompoundTag();
            storedGlass.save(glassTag);
            tag.put("StoredGlass", glassTag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        storedGlass = ItemStack.EMPTY;
        
        if (nbt.contains("StoredGlass")) {
            CompoundTag glassTag = nbt.getCompound("StoredGlass");
            storedGlass = ItemStack.of(glassTag);
        }
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final GlassPipeCapability capability = new GlassPipeCapability();
        private final LazyOptional<GlassPipeCapability> optional = LazyOptional.of(() -> capability);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == GLASS_PIPE_CAPABILITY ? optional.cast() : LazyOptional.empty();
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
