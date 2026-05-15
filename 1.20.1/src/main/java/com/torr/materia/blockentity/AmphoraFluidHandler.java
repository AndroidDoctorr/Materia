package com.torr.materia.blockentity;

import com.torr.materia.ModFluids;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Bridges amphora bottle counts to Forge {@link FluidStack} for pipe automation.
 * One bottle = {@value #MB_PER_BOTTLE} mB; max nine bottles.
 */
public final class AmphoraFluidHandler implements IFluidHandler {

    public static final int MB_PER_BOTTLE = 250;
    public static final int MAX_BOTTLES = 9;
    public static final int CAPACITY_MB = MB_PER_BOTTLE * MAX_BOTTLES;

    private final AmphoraBlockEntity be;

    public AmphoraFluidHandler(AmphoraBlockEntity be) {
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
        Fluid fluid = fluidForAmphora();
        if (fluid == null || be.getLiquidAmount() <= 0) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(fluid, be.getLiquidAmount() * MB_PER_BOTTLE);
    }

    @Override
    public int getTankCapacity(int tank) {
        return tank == 0 ? CAPACITY_MB : 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        if (tank != 0 || stack.isEmpty()) {
            return false;
        }
        String key = liquidKeyForFluid(stack.getFluid());
        if (key == null) {
            return false;
        }
        if (be.getLiquidAmount() <= 0) {
            return true;
        }
        if (be.hasWater()) {
            return "water".equals(key);
        }
        return key.equals(be.getLiquidType());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        }
        String key = liquidKeyForFluid(resource.getFluid());
        if (key == null) {
            return 0;
        }
        int spaceBottles = MAX_BOTTLES - be.getLiquidAmount();
        if (spaceBottles <= 0) {
            return 0;
        }
        if (be.getLiquidAmount() > 0) {
            if (be.hasWater() && !resource.getFluid().isSame(Fluids.WATER)) {
                return 0;
            }
            if (!be.hasWater() && !key.equals(be.getLiquidType())) {
                return 0;
            }
        }
        int wantMb = resource.getAmount();
        int wantBottles = (wantMb + MB_PER_BOTTLE - 1) / MB_PER_BOTTLE;
        int moveBottles = Math.min(spaceBottles, wantBottles);
        int moveMb = moveBottles * MB_PER_BOTTLE;
        if (action.execute()) {
            if (be.getLiquidAmount() == 0) {
                be.setLiquidType(key);
            }
            be.setLiquidAmount(be.getLiquidAmount() + moveBottles);
            be.setChanged();
            if (be.getLevel() != null) {
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        }
        return moveMb;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || be.getLiquidAmount() <= 0) {
            return FluidStack.EMPTY;
        }
        Fluid have = fluidForAmphora();
        if (have == null || !resource.getFluid().isSame(have)) {
            return FluidStack.EMPTY;
        }
        int maxBottles = be.getLiquidAmount();
        int wantBottles = (resource.getAmount() + MB_PER_BOTTLE - 1) / MB_PER_BOTTLE;
        int moveBottles = Math.min(maxBottles, wantBottles);
        int moveMb = moveBottles * MB_PER_BOTTLE;
        if (action.execute()) {
            be.setLiquidAmount(be.getLiquidAmount() - moveBottles);
            if (be.getLiquidAmount() <= 0) {
                be.setLiquidType("");
            }
            be.setChanged();
            if (be.getLevel() != null) {
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        }
        return new FluidStack(have, moveMb);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack current = getFluidInTank(0);
        if (current.isEmpty()) {
            return FluidStack.EMPTY;
        }
        int wantBottles = (maxDrain + MB_PER_BOTTLE - 1) / MB_PER_BOTTLE;
        int moveBottles = Math.min(be.getLiquidAmount(), wantBottles);
        int moveMb = moveBottles * MB_PER_BOTTLE;
        if (action.execute()) {
            be.setLiquidAmount(be.getLiquidAmount() - moveBottles);
            if (be.getLiquidAmount() <= 0) {
                be.setLiquidType("");
            }
            be.setChanged();
            if (be.getLevel() != null) {
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        }
        return new FluidStack(current.getFluid(), moveMb);
    }

    private Fluid fluidForAmphora() {
        if (be.hasWater()) {
            return Fluids.WATER;
        }
        if (!be.hasLiquid()) {
            return null;
        }
        return fluidForKey(be.getLiquidType());
    }

    private static Fluid fluidForKey(String liquidType) {
        if (liquidType == null || liquidType.isEmpty()) {
            return null;
        }
        return switch (liquidType) {
            case "water" -> Fluids.WATER;
            case "lava" -> Fluids.LAVA;
            case "milk" -> ForgeMod.MILK.get();
            case "wine" -> ModFluids.WINE_STILL.get();
            case "grape_juice" -> ModFluids.GRAPE_JUICE_STILL.get();
            case "olive_oil" -> ModFluids.OLIVE_OIL_STILL.get();
            case "vinegar" -> ModFluids.VINEGAR_STILL.get();
            case "beer" -> ModFluids.BEER_STILL.get();
            case "beer_mash" -> ModFluids.BEER_MASH_STILL.get();
            default -> null;
        };
    }

    /**
     * @return internal liquid key, or null if fluid is not supported
     */
    private static String liquidKeyForFluid(Fluid fluid) {
        if (fluid.isSame(Fluids.WATER)) {
            return "water";
        }
        if (fluid.isSame(Fluids.LAVA)) {
            return "lava";
        }
        if (fluid.isSame(ForgeMod.MILK.get())) {
            return "milk";
        }
        if (fluid.isSame(ModFluids.WINE_STILL.get())) {
            return "wine";
        }
        if (fluid.isSame(ModFluids.GRAPE_JUICE_STILL.get())) {
            return "grape_juice";
        }
        if (fluid.isSame(ModFluids.OLIVE_OIL_STILL.get())) {
            return "olive_oil";
        }
        if (fluid.isSame(ModFluids.VINEGAR_STILL.get())) {
            return "vinegar";
        }
        if (fluid.isSame(ModFluids.BEER_STILL.get())) {
            return "beer";
        }
        if (fluid.isSame(ModFluids.BEER_MASH_STILL.get())) {
            return "beer_mash";
        }
        return null;
    }
}
