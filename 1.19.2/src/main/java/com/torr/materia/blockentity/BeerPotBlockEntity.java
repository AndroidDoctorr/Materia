package com.torr.materia.blockentity;

import com.torr.materia.BeerPotBlock;
import com.torr.materia.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BeerPotBlockEntity extends BlockEntity {

    private int beerLevel = 0; // 0 = empty, 3 = full

    public BeerPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BEER_POT_BLOCK_ENTITY.get(), pos, state);
        if (state.hasProperty(BeerPotBlock.WATER_LEVEL)) {
            this.beerLevel = state.getValue(BeerPotBlock.WATER_LEVEL);
        }
    }

    public int getBeerLevel() {
        return beerLevel;
    }

    public void setBeerLevel(int newLevel) {
        this.beerLevel = Math.max(0, Math.min(3, newLevel));
        setChanged();
        if (level != null) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(BeerPotBlock.WATER_LEVEL)) {
                BlockState next = currentState.setValue(BeerPotBlock.WATER_LEVEL, this.beerLevel);
                level.setBlock(worldPosition, next, 3);
                level.sendBlockUpdated(worldPosition, currentState, next, 3);
            }
        }
    }

    public boolean hasBeer() {
        return beerLevel > 0;
    }

    public boolean canAddBeer() {
        return beerLevel < 3;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("beerLevel", beerLevel);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        beerLevel = tag.getInt("beerLevel");
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
