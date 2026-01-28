package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.utils.FuelHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import com.torr.materia.capability.HotMetalCapability;
import com.torr.materia.menu.KilnMenu;
import com.torr.materia.recipe.KilnRecipe;
import com.torr.materia.recipe.AdvancedKilnRecipe;
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
import java.util.Optional;

public class OvenBlockEntity extends BlockEntity implements MenuProvider {
    // Oven is for cooking food - no special fuel tags needed, uses standard fuel system
    
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 1) {
                return FuelHelper.isOvenFuel(stack);
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

    public OvenBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.OVEN_BLOCK_ENTITY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> OvenBlockEntity.this.progress;
                    case 1 -> OvenBlockEntity.this.maxProgress;
                    case 2 -> OvenBlockEntity.this.fuelTime;
                    case 3 -> OvenBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> OvenBlockEntity.this.progress = value;
                    case 1 -> OvenBlockEntity.this.maxProgress = value;
                    case 2 -> OvenBlockEntity.this.fuelTime = value;
                    case 3 -> OvenBlockEntity.this.maxFuelTime = value;
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
        return new TranslatableComponent("container.oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        // Check if chimney is present to determine which menu to return
        return new com.torr.materia.menu.OvenMenu(containerId, inventory, this, this.data);
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
        tag.putInt("oven.progress", progress);
        tag.putInt("oven.fuel_time", fuelTime);
        tag.putInt("oven.max_fuel_time", maxFuelTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        
        CompoundTag inventoryTag = tag.getCompound("inventory");
        if (inventoryTag.contains("Size") && inventoryTag.getInt("Size") == 4) {
            CompoundTag newInventoryTag = new CompoundTag();
            newInventoryTag.putInt("Size", 3);
            
            for (int i = 0; i < 3; i++) {
                if (inventoryTag.contains("Slot" + i)) {
                    newInventoryTag.put("Slot" + i, inventoryTag.get("Slot" + i));
                }
            }
            
            itemHandler.deserializeNBT(newInventoryTag);
        } else {
            itemHandler.deserializeNBT(inventoryTag);
        }
        
        progress = tag.getInt("oven.progress");
        fuelTime = tag.getInt("oven.fuel_time");
        maxFuelTime = tag.getInt("oven.max_fuel_time");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private int getTemperatureTier() {
        return 1;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, OvenBlockEntity entity) {
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

        // Add smoke particles when oven is lit and cooking
        if (entity.fuelTime > 0 && hasRecipe) {
            // Every 20 ticks (1 second), spawn smoke particles
            if (level.getGameTime() % 20 == 0) {
                double x = blockPos.getX() + 0.5;
                double y = blockPos.getY() + 1.0;
                double z = blockPos.getZ() + 0.5;
                level.addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE, x, y, z, 0.0, 0.1, 0.0);
            }
        }

        boolean isBurning = entity.fuelTime > 0;
        if (wasBurning != isBurning) {
            level.setBlock(blockPos, blockState.setValue(com.torr.materia.OvenBlock.LIT, isBurning), 3);
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
        // Use the centralized fuel helper to ensure consistency
        return FuelHelper.isOvenFuel(stack);
    }

    private static int getFuelTime(ItemStack stack) {
        // Use the centralized fuel helper to ensure consistency
        return FuelHelper.getFuelTime(stack);
    }

    private static void craftItem(OvenBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        // Look for oven recipes (food cooking)
        Optional<com.torr.materia.recipe.OvenRecipe> ovenRecipeOpt = level.getRecipeManager()
                .getRecipeFor(ModRecipes.OVEN_TYPE, inventory, level);

        if (ovenRecipeOpt.isPresent()) {
            com.torr.materia.recipe.OvenRecipe ovenRecipe = ovenRecipeOpt.get();
            
            ItemStack result = ovenRecipe.getResultItem().copy();
            ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
            
            if (outputSlot.isEmpty()) {
                entity.itemHandler.setStackInSlot(2, result);
            } else if (outputSlot.getItem() == result.getItem()) {
                outputSlot.grow(result.getCount());
            }

            entity.itemHandler.extractItem(0, 1, false); // Remove input item
        } else {
            // Fallback to vanilla smelting recipes ONLY if result is edible
            Optional<net.minecraft.world.item.crafting.SmeltingRecipe> smeltingRecipeOpt = level.getRecipeManager()
                    .getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING, inventory, level);
            if (smeltingRecipeOpt.isPresent()) {
                net.minecraft.world.item.crafting.SmeltingRecipe smeltRecipe = smeltingRecipeOpt.get();
                ItemStack result = smeltRecipe.getResultItem();
                if (result.isEdible()) {
                    ItemStack copy = result.copy();
                    ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
                    if (outputSlot.isEmpty()) {
                        entity.itemHandler.setStackInSlot(2, copy);
                    } else if (outputSlot.getItem() == copy.getItem()) {
                        outputSlot.grow(copy.getCount());
                    }
                    entity.itemHandler.extractItem(0, 1, false);
                }
            }
        }
    }

    private static boolean hasRecipe(OvenBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        // Check for oven recipes first
        Optional<com.torr.materia.recipe.OvenRecipe> ovenRecipeOpt = level.getRecipeManager()
                .getRecipeFor(ModRecipes.OVEN_TYPE, inventory, level);

        if (ovenRecipeOpt.isPresent()) {
            com.torr.materia.recipe.OvenRecipe ovenRecipe = ovenRecipeOpt.get();
            return canInsertAmountIntoOutputSlot(ovenRecipe.getResultItem(), entity)
                    && canInsertItemIntoOutputSlot(ovenRecipe.getResultItem(), entity);
        }

        // Fall back to vanilla smelting recipes
        Optional<net.minecraft.world.item.crafting.SmeltingRecipe> smeltingRecipeOpt = level.getRecipeManager()
                .getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING, inventory, level);

        if (smeltingRecipeOpt.isPresent()) {
            net.minecraft.world.item.crafting.SmeltingRecipe smeltRecipe = smeltingRecipeOpt.get();
            return canInsertAmountIntoOutputSlot(smeltRecipe.getResultItem(), entity)
                    && canInsertItemIntoOutputSlot(smeltRecipe.getResultItem(), entity);
        }

        return false;
    }

    private static boolean canInsertItemIntoOutputSlot(ItemStack result, OvenBlockEntity entity) {
        ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
        return outputSlot.isEmpty() || outputSlot.getItem().equals(result.getItem());
    }

    private static boolean canInsertAmountIntoOutputSlot(ItemStack result, OvenBlockEntity entity) {
        ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
        return outputSlot.isEmpty() || outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize();
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }
} 