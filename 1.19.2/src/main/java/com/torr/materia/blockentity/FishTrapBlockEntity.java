package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class FishTrapBlockEntity extends BlockEntity {

    private static final int MAX_STORED = 4;
    private static final int TICKS_PER_CATCH = 20 * 15; // ~15 seconds per fish, ~1 minute to fill

    private int storedFish;
    private int progressTicks;

    public FishTrapBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FISH_TRAP_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FishTrapBlockEntity be) {
        if (level.isClientSide) return;

        if (be.storedFish >= MAX_STORED) return;
        if (!isValidLocation(level, pos)) {
            // Slow decay/reset if moved out of water etc.
            if (be.progressTicks > 0) be.progressTicks--;
            return;
        }

        be.progressTicks++;
        if (be.progressTicks >= TICKS_PER_CATCH) {
            be.progressTicks = 0;
            be.storedFish = Math.min(MAX_STORED, be.storedFish + 1);
            be.setChanged();
        }
    }

    private static boolean isValidLocation(Level level, BlockPos pos) {
        // Delegate to block placement rules for consistency
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof com.torr.materia.block.FishTrapBlock)) return false;
        return ((com.torr.materia.block.FishTrapBlock) state.getBlock()).canSurvive(state, level, pos);
    }

    public int getStoredFish() {
        return storedFish;
    }

    public int extractAll() {
        int out = this.storedFish;
        this.storedFish = 0;
        this.setChanged();
        return out;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.storedFish = tag.getInt("StoredFish");
        this.progressTicks = tag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("StoredFish", this.storedFish);
        tag.putInt("Progress", this.progressTicks);
    }
}


