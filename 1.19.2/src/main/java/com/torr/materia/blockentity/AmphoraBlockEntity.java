package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.menu.AmphoraMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class AmphoraBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    
    // Storage modes
    public static final int MODE_EMPTY = 0;      // Empty amphora - can become either type
    public static final int MODE_SOLID = 1;     // Solid item storage (6 slots)
    public static final int MODE_LIQUID = 2;    // Liquid storage (future implementation)
    
    // 6 slots for solid storage (3 rows of 2)
    private NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    private int storageMode = MODE_EMPTY;
    
    // Liquid storage variables
    private int liquidAmount = 0; // 0-9 (9 bottles = 3 buckets)
    private String liquidType = ""; // "water", "wine", "vinegar", etc.
    
    // Lid tracking
    private boolean hasLid = false;
    private boolean hasSealed = false; // true if sealed_lid, false if regular lid
    
    // Fermentation tracking
    private boolean isFermenting = false;
    private int fermentationProgress = 0; // Progress in ticks
    private static final int VINEGAR_FERMENTATION_TIME = 24000; // 20 minutes (20 ticks/sec * 60 sec * 20 min)
    private static final int WINE_FERMENTATION_TIME = 72000; // 60 minutes (sealed fermentation is slower)
    
    // Forge capabilities
    private LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    public AmphoraBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AMPHORA_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AmphoraBlockEntity blockEntity) {
        // Static tick method for functionality
        
        // Update blockstate if needed (for existing amphorae loaded from old saves)
        if (!level.isClientSide) {
            blockEntity.updateBlockStateIfNeeded(level, pos, state);
            
            // Handle fermentation (server-side only)
            blockEntity.tickFermentation();
        }
    }

    private void updateBlockStateIfNeeded(Level level, BlockPos pos, BlockState currentState) {
        // Check if the blockstate needs updating based on current contents and lid
        boolean expectedClosed = this.hasLid();
        com.torr.materia.AmphoraBlock.LiquidType expectedType = com.torr.materia.AmphoraBlock.LiquidType.EMPTY;
        
        if (!expectedClosed) { // Only show liquid types when not closed
            if (this.hasWater()) {
                expectedType = com.torr.materia.AmphoraBlock.LiquidType.WATER;
            } else if (this.hasLiquid()) {
                String liquidType = this.getLiquidType();
                switch (liquidType) {
                    case "wine":
                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.WINE;
                        break;
                    case "vinegar":
                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.VINEGAR;
                        break;
                    case "olive_oil":
                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.OLIVE_OIL;
                        break;
                    case "lava":
                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.LAVA;
                        break;
                    case "grape_juice":
                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.GRAPE_JUICE;
                        break;
                }
            }
        }
        
        // Check if update is needed
        boolean needsUpdate = false;
        if (currentState.hasProperty(com.torr.materia.AmphoraBlock.CLOSED)) {
            boolean currentClosed = currentState.getValue(com.torr.materia.AmphoraBlock.CLOSED);
            if (currentClosed != expectedClosed) {
                needsUpdate = true;
            }
        }
        if (currentState.hasProperty(com.torr.materia.AmphoraBlock.LIQUID_TYPE)) {
            com.torr.materia.AmphoraBlock.LiquidType currentType = currentState.getValue(com.torr.materia.AmphoraBlock.LIQUID_TYPE);
            if (currentType != expectedType) {
                needsUpdate = true;
            }
        }
        
        if (needsUpdate) {
            BlockState newState = currentState.setValue(com.torr.materia.AmphoraBlock.CLOSED, expectedClosed)
                                              .setValue(com.torr.materia.AmphoraBlock.LIQUID_TYPE, expectedType);
            level.setBlock(pos, newState, 3);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.materia.amphora");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new AmphoraMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        this.updateStorageMode();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = ContainerHelper.takeItem(this.items, slot);
        this.updateStorageMode();
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        // Prevent amphorae from being stored inside other amphorae
        if (!stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
            if (blockItem.getBlock() instanceof com.torr.materia.AmphoraBlock) {
                return; // Reject amphora items
            }
        }
        
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        
        // Set storage mode to solid when first item is added
        if (!stack.isEmpty() && this.storageMode == MODE_EMPTY) {
            this.storageMode = MODE_SOLID;
            this.setChanged();
        }
        
        this.updateStorageMode();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, 
                                      (double)this.worldPosition.getY() + 0.5D, 
                                      (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.storageMode = MODE_EMPTY;
        this.setChanged();
    }

    // WorldlyContainer implementation for automation
    @Override
    public int[] getSlotsForFace(Direction side) {
        // Allow automation from all sides for solid storage
        if (this.storageMode == MODE_SOLID) {
            return new int[]{0, 1, 2, 3, 4, 5};
        }
        return new int[0]; // No automation for empty or liquid mode
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        // Prevent amphorae from being stored inside other amphorae
        if (!stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
            if (blockItem.getBlock() instanceof com.torr.materia.AmphoraBlock) {
                return false; // Reject amphora items
            }
        }
        
        return this.storageMode == MODE_EMPTY || this.storageMode == MODE_SOLID;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return this.storageMode == MODE_SOLID;
    }

    // Storage mode management
    private void updateStorageMode() {
        if (this.isEmpty() && this.liquidAmount == 0) {
            this.storageMode = MODE_EMPTY;
        } else if (this.liquidAmount > 0) {
            this.storageMode = MODE_LIQUID;
        } else if (!this.isEmpty()) {
            this.storageMode = MODE_SOLID;
        }
        this.setChanged();
    }

    public int getStorageMode() {
        return this.storageMode;
    }

    public boolean canAcceptSolidItems() {
        return this.storageMode == MODE_EMPTY || this.storageMode == MODE_SOLID;
    }

    public boolean canAcceptLiquids() {
        return this.storageMode == MODE_EMPTY || this.storageMode == MODE_LIQUID;
    }

    // Liquid storage methods
    public int getLiquidAmount() {
        return this.liquidAmount;
    }

    public void setLiquidAmount(int amount) {
        this.liquidAmount = Math.max(0, Math.min(9, amount)); // Clamp between 0-9
        this.updateStorageMode();
        this.setChanged();
    }

    public String getLiquidType() {
        return this.liquidType;
    }

    public void setLiquidType(String type) {
        this.liquidType = type;
        this.setChanged();
    }

    public boolean hasLiquid() {
        return this.liquidAmount > 0 && !this.liquidType.isEmpty();
    }

    public boolean hasWater() {
        return this.hasLiquid() && "water".equals(this.liquidType);
    }

    public boolean hasGrapeJuice() {
        return this.hasLiquid() && "grape_juice".equals(this.liquidType);
    }

    public boolean hasOliveOil() {
        return this.hasLiquid() && "olive_oil".equals(this.liquidType);
    }

    public boolean hasVinegar() {
        return this.hasLiquid() && "vinegar".equals(this.liquidType);
    }

    public boolean hasWine() {
        return this.hasLiquid() && "wine".equals(this.liquidType);
    }

    public boolean hasMilk() {
        return this.hasLiquid() && "milk".equals(this.liquidType);
    }

    public boolean canAddLiquid(String liquidType) {
        if (this.storageMode == MODE_SOLID) return false;
        if (this.liquidAmount >= 9) return false; // Full
        if (this.liquidAmount == 0) return true; // Empty, can accept any liquid
        return this.liquidType.equals(liquidType); // Must match existing liquid type
    }

    public boolean canAddWater() {
        return canAddLiquid("water");
    }

    public boolean canAddGrapeJuice() {
        return canAddLiquid("grape_juice");
    }

    public boolean canAddOliveOil() {
        return canAddLiquid("olive_oil");
    }

    public boolean canAddVinegar() {
        return canAddLiquid("vinegar");
    }

    public boolean canAddWine() {
        return canAddLiquid("wine");
    }

    public boolean canAddMilk() {
        return canAddLiquid("milk");
    }

    public void addLiquid(String liquidType, int amount) {
        if (!canAddLiquid(liquidType)) return;
        
        if (this.liquidAmount == 0) {
            this.liquidType = liquidType;
            this.storageMode = MODE_LIQUID;
        }
        
        this.liquidAmount = Math.min(9, this.liquidAmount + amount);
        this.setChanged();
    }

    public void addWater(int amount) {
        addLiquid("water", amount);
    }

    public void addGrapeJuice(int amount) {
        addLiquid("grape_juice", amount);
    }

    public void addOliveOil(int amount) {
        addLiquid("olive_oil", amount);
    }

    public void addVinegar(int amount) {
        addLiquid("vinegar", amount);
    }

    public void addWine(int amount) {
        addLiquid("wine", amount);
    }

    public void addMilk(int amount) {
        addLiquid("milk", amount);
    }

    public boolean removeLiquid(int amount) {
        if (this.liquidAmount < amount) return false;
        
        this.liquidAmount -= amount;
        if (this.liquidAmount == 0) {
            this.liquidType = "";
            this.storageMode = MODE_EMPTY;
        }
        
        this.setChanged();
        return true;
    }
    
    // Lid management
    public boolean hasLid() {
        return this.hasLid;
    }
    
    public boolean hasSealed() {
        return this.hasSealed;
    }
    
    public void setLid(boolean sealed) {
        this.hasLid = true;
        this.hasSealed = sealed;
        
        // Start fermentation if we have grape juice
        if (this.hasGrapeJuice()) {
            this.startFermentation();
        }
        
        this.setChanged();
    }
    
    public void removeLid() {
        this.hasLid = false;
        this.hasSealed = false;
        this.setChanged();
    }
    
    public ItemStack getLidItem() {
        if (!this.hasLid) return ItemStack.EMPTY;
        return new ItemStack(this.hasSealed ? com.torr.materia.ModItems.SEALED_LID.get() : com.torr.materia.ModItems.LID.get());
    }
    
    // Fermentation management
    public boolean isFermenting() {
        return this.isFermenting;
    }
    
    public int getFermentationProgress() {
        return this.fermentationProgress;
    }
    
    public int getMaxFermentationTime() {
        return this.hasSealed ? WINE_FERMENTATION_TIME : VINEGAR_FERMENTATION_TIME;
    }
    
    public float getFermentationProgressPercent() {
        if (!this.isFermenting) return 0.0f;
        return (float) this.fermentationProgress / (float) getMaxFermentationTime();
    }
    
    public void startFermentation() {
        if (this.hasGrapeJuice() && this.hasLid()) {
            this.isFermenting = true;
            this.fermentationProgress = 0;
            this.setChanged();
            
            // Sync to client when fermentation starts
            if (this.level != null && !this.level.isClientSide) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }
    
    public void stopFermentation() {
        this.isFermenting = false;
        this.fermentationProgress = 0;
        this.setChanged();
    }
    
    public void tickFermentation() {
        if (!this.isFermenting) return;
        
        this.fermentationProgress++;
        
        // Check if fermentation is complete
        if (this.fermentationProgress >= getMaxFermentationTime()) {
            // Complete fermentation
            if (this.hasSealed) {
                // Sealed lid → wine
                this.liquidType = "wine";
            } else {
                // Regular lid → vinegar
                this.liquidType = "vinegar";
            }
            
            this.stopFermentation();
            // Note: liquid amount stays the same
            
            // Sync to client after completion
            if (this.level != null && !this.level.isClientSide) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
        
        // Sync fermentation progress to client every 100 ticks (5 seconds)
        if (this.fermentationProgress % 100 == 0) {
            this.setChanged();
            if (this.level != null && !this.level.isClientSide) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        } else {
            this.setChanged();
        }
    }

    // Client synchronization
    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
    
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }
    
    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public void onDataPacket(net.minecraft.network.Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    // NBT serialization
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        this.storageMode = tag.getInt("StorageMode");
        this.liquidAmount = tag.getInt("LiquidAmount");
        this.liquidType = tag.getString("LiquidType");
        this.hasLid = tag.getBoolean("HasLid");
        this.hasSealed = tag.getBoolean("HasSealed");
        this.isFermenting = tag.getBoolean("IsFermenting");
        this.fermentationProgress = tag.getInt("FermentationProgress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("StorageMode", this.storageMode);
        tag.putInt("LiquidAmount", this.liquidAmount);
        tag.putString("LiquidType", this.liquidType);
        tag.putBoolean("HasLid", this.hasLid);
        tag.putBoolean("HasSealed", this.hasSealed);
        tag.putBoolean("IsFermenting", this.isFermenting);
        tag.putInt("FermentationProgress", this.fermentationProgress);
    }

    // Forge capabilities
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.storageMode == MODE_SOLID) {
            if (side == null) {
                return handlers[0].cast();
            } else {
                return handlers[side.ordinal()].cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = SidedInvWrapper.create(this, Direction.values());
    }
}
