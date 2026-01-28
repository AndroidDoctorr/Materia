package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.utils.FuelHelper;
import com.torr.materia.menu.FirePitMenu;
import com.torr.materia.recipe.FirePitRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import java.util.Optional;

public class FirePitBlockEntity extends BlockEntity implements MenuProvider {
    
    private final ItemStackHandler itemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 1) {
                return FuelHelper.isFirePitFuel(stack);
            }
            return super.isItemValid(slot, stack);
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public FirePitBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIRE_PIT_BLOCK_ENTITY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FirePitBlockEntity.this.progress;
                    case 1 -> FirePitBlockEntity.this.maxProgress;
                    case 2 -> FirePitBlockEntity.this.fuelTime;
                    case 3 -> FirePitBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FirePitBlockEntity.this.progress = value;
                    case 1 -> FirePitBlockEntity.this.maxProgress = value;
                    case 2 -> FirePitBlockEntity.this.fuelTime = value;
                    case 3 -> FirePitBlockEntity.this.maxFuelTime = value;
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
        return new TranslatableComponent("container.fire_pit");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FirePitMenu(containerId, inventory, this, this.data);
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
        tag.putInt("fire_pit.progress", progress);
        tag.putInt("fire_pit.fuel_time", fuelTime);
        tag.putInt("fire_pit.max_fuel_time", maxFuelTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("fire_pit.progress");
        fuelTime = tag.getInt("fire_pit.fuel_time");
        maxFuelTime = tag.getInt("fire_pit.max_fuel_time");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, FirePitBlockEntity entity) {
        if (level.isClientSide()) {
            return;
        }

        boolean hasRecipe = hasRecipe(entity);
        boolean wasBurning = entity.fuelTime > 0;

        if (entity.fuelTime > 0) {
            entity.fuelTime--;
        }

        if (hasRecipe) {
            if (!wasBurning && entity.canConsumeFuel()) {
                entity.consumeFuel();
            }

            if (entity.fuelTime > 0) {
                entity.progress++;
                if (entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                    entity.progress = 0;
                }
            } else {
                entity.progress = 0;
            }
        } else {
            entity.progress = 0;
        }

        boolean isBurning = entity.fuelTime > 0;
        if (wasBurning != isBurning) {
            level.setBlock(blockPos, blockState.setValue(com.torr.materia.FirePitBlock.LIT, isBurning), 3);
            entity.setChanged();
        }
    }

    private boolean canConsumeFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(1); // Slot 1 is fuel
        return isFuel(fuelStack) && !fuelStack.isEmpty();
    }

    private void consumeFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(1);
        if (isFuel(fuelStack)) {
            this.fuelTime = getFuelTime(fuelStack);
            this.maxFuelTime = this.fuelTime;
            fuelStack.shrink(1);
        }
    }

    private static boolean isFuel(ItemStack stack) {
        return FuelHelper.isFirePitFuel(stack);
    }

    private static int getFuelTime(ItemStack stack) {
        return FuelHelper.getFuelTime(stack);
    }

    private static void craftItem(FirePitBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<FirePitRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.FIRE_PIT_TYPE, inventory, level);

        if (recipe.isPresent()) {
            NonNullList<ItemStack> results = recipe.get().getResults();
            
            // Place results in output slots (slots 2-6)
            for (int i = 0; i < results.size() && i < 5; i++) {
                ItemStack result = results.get(i).copy();
                ItemStack outputSlot = entity.itemHandler.getStackInSlot(2 + i);
                
                if (outputSlot.isEmpty()) {
                    entity.itemHandler.setStackInSlot(2 + i, result);
                } else if (outputSlot.getItem() == result.getItem()) {
                    outputSlot.grow(result.getCount());
                }
            }

            entity.itemHandler.extractItem(0, 1, false); // Remove input item
        }
    }

    private static boolean hasRecipe(FirePitBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<FirePitRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.FIRE_PIT_TYPE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlots(inventory, recipe.get().getResults(), entity)
                && canInsertItemIntoOutputSlots(inventory, recipe.get().getResults(), entity);
    }

    private static boolean canInsertItemIntoOutputSlots(SimpleContainer inventory, NonNullList<ItemStack> results, FirePitBlockEntity entity) {
        for (int i = 0; i < results.size() && i < 5; i++) {
            ItemStack result = results.get(i);
            ItemStack outputSlot = entity.itemHandler.getStackInSlot(2 + i);
            
            if (!outputSlot.isEmpty() && !outputSlot.getItem().equals(result.getItem())) {
                return false;
            }
        }
        return true;
    }

    private static boolean canInsertAmountIntoOutputSlots(SimpleContainer inventory, NonNullList<ItemStack> results, FirePitBlockEntity entity) {
        for (int i = 0; i < results.size() && i < 5; i++) {
            ItemStack result = results.get(i);
            ItemStack outputSlot = entity.itemHandler.getStackInSlot(2 + i);
            
            if (!outputSlot.isEmpty() && outputSlot.getCount() + result.getCount() > outputSlot.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }
} 