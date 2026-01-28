package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.WinePotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class WinePotBlockEntity extends BlockEntity {

    private int wineLevel = 0; // 0 = empty, 3 = full

    public WinePotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WINE_POT_BLOCK_ENTITY.get(), pos, state);
        if (state.hasProperty(WinePotBlock.WATER_LEVEL)) {
            this.wineLevel = state.getValue(WinePotBlock.WATER_LEVEL);
        }
    }

    public int getWineLevel() {
        return wineLevel;
    }

    public void setWineLevel(int newLevel) {
        this.wineLevel = Math.max(0, Math.min(3, newLevel));
        setChanged();
        if (level != null) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(WinePotBlock.WATER_LEVEL)) {
                BlockState next = currentState.setValue(WinePotBlock.WATER_LEVEL, this.wineLevel);
                level.setBlock(worldPosition, next, 3);
                level.sendBlockUpdated(worldPosition, currentState, next, 3);
            }
        }
    }

    public boolean hasWine() {
        return wineLevel > 0;
    }

    public boolean canAddWine() {
        return wineLevel < 3;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("wineLevel", wineLevel);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        wineLevel = tag.getInt("wineLevel");
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
}
