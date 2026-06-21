package com.torr.materia.blockentity;

import com.torr.materia.TeaPotBlock;
import com.torr.materia.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * BlockEntity for Tea Pot. Tracks serving level.
 */
public class TeaPotBlockEntity extends BlockEntity {

    private int teaLevel = 0; // 0 = empty, 3 = full

    public TeaPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEA_POT_BLOCK_ENTITY.get(), pos, state);
        if (state.hasProperty(TeaPotBlock.WATER_LEVEL)) {
            this.teaLevel = state.getValue(TeaPotBlock.WATER_LEVEL);
        }
    }

    public int getTeaLevel() {
        return teaLevel;
    }

    public void setTeaLevel(int newLevel) {
        this.teaLevel = Math.max(0, Math.min(3, newLevel));
        setChanged();
        if (level != null) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(TeaPotBlock.WATER_LEVEL)) {
                BlockState next = currentState.setValue(TeaPotBlock.WATER_LEVEL, this.teaLevel);
                level.setBlock(worldPosition, next, 3);
                level.sendBlockUpdated(worldPosition, currentState, next, 3);
            }
        }
    }

    public boolean hasTea() {
        return teaLevel > 0;
    }

    public boolean canAddTea() {
        return teaLevel < 3;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("teaLevel", teaLevel);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        teaLevel = tag.getInt("teaLevel");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}
