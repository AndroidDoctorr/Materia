package com.torr.materia.capability;

import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomSheepColorCapability implements INBTSerializable<CompoundTag> {
    
    public static final Capability<CustomSheepColorCapability> CUSTOM_SHEEP_COLOR = 
            CapabilityManager.get(new CapabilityToken<>() {});
    
    public static final ResourceLocation ID = new ResourceLocation("materia", "custom_sheep_color");
    
    private CustomSheepColor customColor = null;
    
    public CustomSheepColor getCustomColor() {
        return customColor;
    }
    
    public void setCustomColor(CustomSheepColor color) {
        this.customColor = color;
    }
    
    public boolean hasCustomColor() {
        return customColor != null && customColor.isCustomColor();
    }
    
    public void clearCustomColor() {
        this.customColor = null;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (customColor != null) {
            tag.putString("CustomColor", customColor.name());
        }
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("CustomColor")) {
            try {
                this.customColor = CustomSheepColor.valueOf(tag.getString("CustomColor"));
            } catch (IllegalArgumentException e) {
                this.customColor = null;
            }
        } else {
            this.customColor = null;
        }
    }
    
    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final CustomSheepColorCapability capability = new CustomSheepColorCapability();
        private final LazyOptional<CustomSheepColorCapability> optional = LazyOptional.of(() -> capability);
        
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CUSTOM_SHEEP_COLOR) {
                return optional.cast();
            }
            return LazyOptional.empty();
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
