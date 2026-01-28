package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModItems;
import com.torr.materia.block.CokeOvenBlock;
import com.torr.materia.menu.CokeOvenMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CokeOvenBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 2) return false; // output
            if (slot == 0) return isCokable(stack); // input
            if (slot == 1) return isCokeOvenFuel(stack); // fuel
            return super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == 2 && !stack.isEmpty()) return stack;
            return super.insertItem(slot, stack, simulate);
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public CokeOvenBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COKE_OVEN_BLOCK_ENTITY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CokeOvenBlockEntity.this.progress;
                    case 1 -> CokeOvenBlockEntity.this.maxProgress;
                    case 2 -> CokeOvenBlockEntity.this.fuelTime;
                    case 3 -> CokeOvenBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CokeOvenBlockEntity.this.progress = value;
                    case 1 -> CokeOvenBlockEntity.this.maxProgress = value;
                    case 2 -> CokeOvenBlockEntity.this.fuelTime = value;
                    case 3 -> CokeOvenBlockEntity.this.maxFuelTime = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.coke_oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CokeOvenMenu(containerId, inventory, this, this.data);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("coke_oven.progress", progress);
        tag.putInt("coke_oven.fuel_time", fuelTime);
        tag.putInt("coke_oven.max_fuel_time", maxFuelTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("coke_oven.progress");
        fuelTime = tag.getInt("coke_oven.fuel_time");
        maxFuelTime = tag.getInt("coke_oven.max_fuel_time");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CokeOvenBlockEntity entity) {
        if (level.isClientSide()) return;

        boolean hasRecipe = hasRecipe(entity);
        boolean wasBurning = entity.fuelTime > 0;

        if (entity.fuelTime > 0) {
            entity.fuelTime--;
        }

        if (hasRecipe) {
            if (entity.fuelTime == 0 && entity.canConsumeFuel()) {
                entity.consumeFuel();
            }

            if (entity.fuelTime > 0) {
                entity.progress++;
                if (entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                    entity.progress = 0;
                }
            }
        } else {
            entity.progress = 0;
        }

        boolean isBurning = entity.fuelTime > 0;
        if (wasBurning != isBurning) {
            level.setBlock(blockPos, blockState.setValue(CokeOvenBlock.LIT, isBurning), 3);
            entity.setChanged();
        }
    }

    private boolean canConsumeFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(1);
        return isCokeOvenFuel(fuelStack) && !fuelStack.isEmpty();
    }

    private void consumeFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(1);
        if (!isCokeOvenFuel(fuelStack)) return;

        this.fuelTime = getFuelTime(fuelStack);
        this.maxFuelTime = this.fuelTime;
        fuelStack.shrink(1);
    }

    private static int getFuelTime(ItemStack stack) {
        // Coke oven only accepts coal/charcoal fuel
        Item item = stack.getItem();
        if (item == Items.COAL || item == Items.CHARCOAL) return 1600;
        return 0;
    }

    private static void craftItem(CokeOvenBlockEntity entity) {
        ItemStack input = entity.itemHandler.getStackInSlot(0);
        if (!isCokable(input)) return;

        ItemStack result = new ItemStack(ModItems.COAL_COKE.get());
        ItemStack output = entity.itemHandler.getStackInSlot(2);

        if (output.isEmpty()) {
            entity.itemHandler.setStackInSlot(2, result);
            entity.itemHandler.extractItem(0, 1, false);
            return;
        }

        if (output.getItem() == result.getItem() && output.getCount() < output.getMaxStackSize()) {
            output.grow(1);
            entity.itemHandler.extractItem(0, 1, false);
        }
    }

    private static boolean hasRecipe(CokeOvenBlockEntity entity) {
        ItemStack input = entity.itemHandler.getStackInSlot(0);
        if (!isCokable(input)) return false;

        ItemStack output = entity.itemHandler.getStackInSlot(2);
        if (output.isEmpty()) return true;

        return output.getItem() == ModItems.COAL_COKE.get() && output.getCount() < output.getMaxStackSize();
    }

    private static boolean isCokable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item == Items.COAL;
    }

    private static boolean isCokeOvenFuel(ItemStack stack) {
        return isCokable(stack);
    }
}

