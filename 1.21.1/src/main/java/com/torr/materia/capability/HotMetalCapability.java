package com.torr.materia.capability;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HotMetalCapability implements INBTSerializable<CompoundTag> {
    public static final Capability<HotMetalCapability> HOT_METAL_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private long heatTime = 0;
    private long heatDuration = 1200; // 60 seconds (1200 ticks * 50ms = 60,000ms = 60s)

    public void heat() {
        this.heatTime = System.currentTimeMillis();
        // Ensure heat duration is set to the current default (fixes old capabilities)
        this.heatDuration = 1200;
    }

    public boolean isHot() {
        if (heatTime == 0) return false;
        return (System.currentTimeMillis() - heatTime) < (heatDuration * 50); // Convert to milliseconds
    }

    public void setHeatDuration(long duration) {
        this.heatDuration = duration;
    }

    public long getHeatTime() {
        return heatTime;
    }
    
    public long getHeatDuration() {
        return heatDuration;
    }

    public void setHeatTime(long time) {
        this.heatTime = time;
    }

    public float getHeatLevel() {
        if (heatTime == 0) return 0.0f;
        long elapsed = System.currentTimeMillis() - heatTime;
        long maxTime = heatDuration * 50;
        if (elapsed >= maxTime) return 0.0f;
        return 1.0f - ((float) elapsed / maxTime);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        // Only serialize heat data if the item is actually hot or has been heated
        if (heatTime > 0) {
            tag.putLong("heat_time", heatTime);
            tag.putLong("heat_duration", heatDuration);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        heatTime = nbt.getLong("heat_time");
        heatDuration = nbt.getLong("heat_duration");
    }
    
    /**
     * Create a weighted average of heat times for stacking
     */
    public static long averageHeatTimes(long time1, int count1, long time2, int count2) {
        if (time1 <= 0 && time2 <= 0) return 0;
        if (time1 <= 0) return time2;
        if (time2 <= 0) return time1;
        
        return (time1 * count1 + time2 * count2) / (count1 + count2);
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final HotMetalCapability capability = new HotMetalCapability();
        private final LazyOptional<HotMetalCapability> optional = LazyOptional.of(() -> capability);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == HOT_METAL_CAPABILITY ? optional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            return capability.serializeNBT(provider);
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            capability.deserializeNBT(provider, nbt);
        }

        public void invalidate() {
            optional.invalidate();
        }
    }
}
