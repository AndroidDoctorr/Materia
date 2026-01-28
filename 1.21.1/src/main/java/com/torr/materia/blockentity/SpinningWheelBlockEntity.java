package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SpinningWheelBlockEntity extends BlockEntity {
    private static final String TAG_NEXT_ANIM_TICK = "materia.spinning_wheel.next_anim_tick";

    /**
     * Guard against duplicate scheduled ticks causing double-flips (flicker).
     * We only allow the frame to change when level.getGameTime() >= nextAnimGameTime.
     */
    private long nextAnimGameTime;

    public SpinningWheelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPINNING_WHEEL_BLOCK_ENTITY.get(), pos, state);
    }

    public long getNextAnimGameTime() {
        return nextAnimGameTime;
    }

    public void setNextAnimGameTime(long nextAnimGameTime) {
        this.nextAnimGameTime = nextAnimGameTime;
        setChanged();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level instanceof ServerLevel serverLevel) {
            // Kick-start animation after chunk loads.
            serverLevel.scheduleTick(worldPosition, getBlockState().getBlock(), 1);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        nextAnimGameTime = tag.getLong(TAG_NEXT_ANIM_TICK);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putLong(TAG_NEXT_ANIM_TICK, nextAnimGameTime);
        super.saveAdditional(tag, provider);
    }
}

