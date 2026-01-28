package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.MilkPotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * BlockEntity for the Milk Pot. Handles milk storage.
 */
public class MilkPotBlockEntity extends BlockEntity {
    
    // Milk level system (0 = empty, 3 = full)
    private int milkLevel = 0; // Start empty by default

    public MilkPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MILK_POT_BLOCK_ENTITY.get(), pos, state);
        // Initialize milk level from block state
        if (state.hasProperty(MilkPotBlock.WATER_LEVEL)) {
            this.milkLevel = state.getValue(MilkPotBlock.WATER_LEVEL);
        }
    }

    public int getMilkLevel() {
        return milkLevel;
    }
    
    public void setMilkLevel(int newLevel) {
        this.milkLevel = Math.max(0, Math.min(3, newLevel));
        setChanged();
        if (level != null) {
            // Update the block state to reflect the new milk level
            BlockState currentState = getBlockState();
            BlockState newState = currentState.setValue(MilkPotBlock.WATER_LEVEL, this.milkLevel);
            level.setBlock(worldPosition, newState, 3);
            level.sendBlockUpdated(worldPosition, currentState, newState, 3);
        }
    }
    
    public boolean hasMilk() {
        return milkLevel > 0;
    }
    
    public boolean canAddMilk() {
        return milkLevel < 3;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("milkLevel", milkLevel);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        milkLevel = tag.getInt("milkLevel");
        
        // Sync milk level with block state when loading
        if (level != null && !level.isClientSide) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(MilkPotBlock.WATER_LEVEL) && 
                currentState.getValue(MilkPotBlock.WATER_LEVEL) != milkLevel) {
                BlockState newState = currentState.setValue(MilkPotBlock.WATER_LEVEL, milkLevel);
                level.setBlock(worldPosition, newState, 3);
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        loadAdditional(tag, provider);
    }
    
    @Override
    public void setLevel(net.minecraft.world.level.Level level) {
        super.setLevel(level);
        // Ensure milk level is synced when added to world
        if (level != null && !level.isClientSide) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(MilkPotBlock.WATER_LEVEL) && 
                currentState.getValue(MilkPotBlock.WATER_LEVEL) != milkLevel) {
                BlockState newState = currentState.setValue(MilkPotBlock.WATER_LEVEL, milkLevel);
                level.setBlock(worldPosition, newState, 3);
            }
        }
    }
}
