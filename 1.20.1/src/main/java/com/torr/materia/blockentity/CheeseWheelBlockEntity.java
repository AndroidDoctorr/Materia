package com.torr.materia.blockentity;

import com.torr.materia.CheeseWheelBlock;
import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CheeseWheelBlockEntity extends BlockEntity {
    private static final int AGE_TIME_TICKS = 24000;

    private int ageTicks = 0;
    private int bites = 0; // 0..7

    public CheeseWheelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHEESE_WHEEL_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        syncStateFromNbtIfNeeded(level);

        BlockState state = getBlockState();
        if (state.is(ModBlocks.FRESH_CHEESE_WHEEL.get())) {
            ageTicks++;
            if (ageTicks >= AGE_TIME_TICKS) {
                int clampedBites = Math.min(7, Math.max(0, bites));
                BlockState aged = ModBlocks.AGED_CHEESE_WHEEL.get().defaultBlockState()
                    .setValue(CheeseWheelBlock.BITES, clampedBites);
                BlockPos pos = worldPosition;
                level.setBlock(pos, aged, 3);

                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof CheeseWheelBlockEntity wheel) {
                    wheel.setAgeTicks(AGE_TIME_TICKS);
                    wheel.setBites(clampedBites);
                }

                level.sendBlockUpdated(pos, aged, aged, 3);
            }
        }
    }

    private void syncStateFromNbtIfNeeded(Level level) {
        BlockState state = getBlockState();
        if (!state.hasProperty(CheeseWheelBlock.BITES)) return;
        int clamped = Math.min(7, Math.max(0, bites));
        if (state.getValue(CheeseWheelBlock.BITES) != clamped) {
            level.setBlock(worldPosition, state.setValue(CheeseWheelBlock.BITES, clamped), 2);
        }
    }

    public void setBites(int bites) {
        this.bites = Math.min(7, Math.max(0, bites));
        setChanged();
    }

    public void setAgeTicks(int ageTicks) {
        this.ageTicks = Math.max(0, ageTicks);
        setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ageTicks = tag.getInt("AgeTicks");
        bites = tag.getInt("Bites");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("AgeTicks", ageTicks);
        tag.putInt("Bites", bites);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag t = new CompoundTag();
        saveAdditional(t);
        return t;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}

