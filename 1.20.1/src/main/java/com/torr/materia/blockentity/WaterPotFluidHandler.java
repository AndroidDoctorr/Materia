package com.torr.materia.blockentity;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Exposes water-pot fill level as Forge water for fluid pipes (1000 mB per visual level, max 3000).
 */
public final class WaterPotFluidHandler implements IFluidHandler {

    public static final int MB_PER_LEVEL = 1000;
    public static final int MAX_LEVEL = 3;
    public static final int CAPACITY_MB = MB_PER_LEVEL * MAX_LEVEL;

    private final WaterPotBlockEntity be;

    public WaterPotFluidHandler(WaterPotBlockEntity be) {
        this.be = be;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if (tank != 0) {
            return FluidStack.EMPTY;
        }
        int lvl = be.getWaterLevel();
        if (lvl <= 0) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(Fluids.WATER, lvl * MB_PER_LEVEL);
    }

    @Override
    public int getTankCapacity(int tank) {
        return tank == 0 ? CAPACITY_MB : 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return tank == 0 && !stack.isEmpty() && stack.getFluid().isSame(Fluids.WATER);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.getFluid().isSame(Fluids.WATER)) {
            return 0;
        }
        int level = be.getWaterLevel();
        if (level >= MAX_LEVEL) {
            return 0;
        }
        int spaceMb = (MAX_LEVEL - level) * MB_PER_LEVEL;
        int moveMb = Math.min(spaceMb, resource.getAmount());
        int addLevels = moveMb / MB_PER_LEVEL;
        if (addLevels <= 0) {
            return 0;
        }
        if (action.execute()) {
            be.setWaterLevel(level + addLevels);
            be.setChanged();
            if (be.getLevel() != null) {
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        }
        return addLevels * MB_PER_LEVEL;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.getFluid().isSame(Fluids.WATER)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        int level = be.getWaterLevel();
        if (level <= 0 || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }
        int haveMb = level * MB_PER_LEVEL;
        int moveMb = Math.min(haveMb, maxDrain);
        int removeLevels = moveMb / MB_PER_LEVEL;
        if (removeLevels <= 0) {
            return FluidStack.EMPTY;
        }
        if (action.execute()) {
            be.setWaterLevel(level - removeLevels);
            be.setChanged();
            if (be.getLevel() != null) {
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        }
        return new FluidStack(Fluids.WATER, removeLevels * MB_PER_LEVEL);
    }
}
