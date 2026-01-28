package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.block.TimerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TimerBlockEntity extends BlockEntity {
    private int ticksRemaining;

    public TimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TIMER_BLOCK_ENTITY.get(), pos, state);
        this.ticksRemaining = TimerBlock.intervalToTicks(state.getValue(TimerBlock.INTERVAL));
    }

    public static void serverTick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, TimerBlockEntity be) {
        if (level.isClientSide) return;
        if (be.ticksRemaining > 0) {
            be.ticksRemaining--;
        }
        if (be.ticksRemaining == 0) {
            // Emit a 1-tick pulse by toggling OUTPUT true for this tick
            boolean currentlyOn = state.getValue(TimerBlock.OUTPUT);
            if (!currentlyOn) {
                BlockState newState = state.setValue(TimerBlock.OUTPUT, true);
                level.setBlock(pos, newState, 3);
                // notify both self and the output neighbor on rising edge
                level.updateNeighborsAt(pos, newState.getBlock());
                level.updateNeighborsAt(pos.relative(newState.getValue(TimerBlock.FACING)), newState.getBlock());
            } else {
                BlockState newState = state.setValue(TimerBlock.OUTPUT, false);
                level.setBlock(pos, newState, 3);
                // notify both self and the output neighbor on falling edge
                level.updateNeighborsAt(pos, newState.getBlock());
                level.updateNeighborsAt(pos.relative(newState.getValue(TimerBlock.FACING)), newState.getBlock());
                // reset countdown
                be.ticksRemaining = TimerBlock.intervalToTicks(state.getValue(TimerBlock.INTERVAL));
            }
        }
    }

    public void resetCountdownForInterval(int interval) {
        this.ticksRemaining = TimerBlock.intervalToTicks(interval);
    }
}


