package com.torr.materia.menu;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.blockentity.AmphoraBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AmphoraMenu extends AbstractContainerMenu {
    private final AmphoraBlockEntity blockEntity;
    private final Level level;
    private final ContainerLevelAccess access;
    /** Number of block slots at the start of {@link AbstractContainerMenu#slots} when the item capability is present (6), else 0. */
    private final int amphoraSlotCount;
    
    // Custom slot that prevents amphorae from being placed
    public static class AmphoraSlot extends SlotItemHandler {
        private final AmphoraBlockEntity amphoraEntity;
        
        public AmphoraSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, AmphoraBlockEntity amphoraEntity) {
            super(itemHandler, index, xPosition, yPosition);
            this.amphoraEntity = amphoraEntity;
        }
        
        @Override
        public boolean mayPlace(ItemStack stack) {
            // Prevent amphorae from being placed
            if (!stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
                if (blockItem.getBlock() instanceof com.torr.materia.AmphoraBlock) {
                    return false;
                }
            }
            
            // Don't allow placing items in fermenting amphorae
            if (amphoraEntity.isFermenting()) {
                return false;
            }
            
            // Only check if amphora is in liquid mode (which prevents solid items)
            if (amphoraEntity.getStorageMode() == com.torr.materia.blockentity.AmphoraBlockEntity.MODE_LIQUID) {
                return false;
            }
            
            return super.mayPlace(stack);
        }
        
        @Override
        public boolean mayPickup(Player player) {
            // Don't allow taking items from fermenting amphorae
            if (amphoraEntity.isFermenting()) {
                return false;
            }
            return super.mayPickup(player);
        }
        
        @Override
        public boolean allowModification(Player player) {
            // Don't allow any modifications to fermenting amphorae
            if (amphoraEntity.isFermenting()) {
                return false;
            }
            return super.allowModification(player);
        }
    }

    // Client constructor
    public AmphoraMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    // Main constructor
    public AmphoraMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.AMPHORA_MENU.get(), containerId);
        checkContainerSize(inv, 6);
        this.blockEntity = (AmphoraBlockEntity) entity;
        this.level = inv.player.level();
        this.access = ContainerLevelAccess.create(level, blockEntity.getBlockPos());

        int slotsBeforeAmphora = this.slots.size();
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            // Top row (slots 0-1)
            this.addSlot(new AmphoraSlot(handler, 0, 71, 17, this.blockEntity));
            this.addSlot(new AmphoraSlot(handler, 1, 89, 17, this.blockEntity));
            
            // Middle row (slots 2-3) 
            this.addSlot(new AmphoraSlot(handler, 2, 71, 35, this.blockEntity));
            this.addSlot(new AmphoraSlot(handler, 3, 89, 35, this.blockEntity));
            
            // Bottom row (slots 4-5)
            this.addSlot(new AmphoraSlot(handler, 4, 71, 53, this.blockEntity));
            this.addSlot(new AmphoraSlot(handler, 5, 89, 53, this.blockEntity));
        });
        this.amphoraSlotCount = this.slots.size() - slotsBeforeAmphora;

        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        
        // Store original count to detect if anything moved
        int originalCount = sourceStack.getCount();

        int totalSlots = this.slots.size();

        if (this.amphoraSlotCount > 0 && index < this.amphoraSlotCount) {
            if (!moveItemStackTo(sourceStack, this.amphoraSlotCount, totalSlots, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= this.amphoraSlotCount && index < totalSlots) {
            if (sourceStack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
                if (blockItem.getBlock() instanceof com.torr.materia.AmphoraBlock) {
                    return ItemStack.EMPTY;
                }
            }

            if (this.amphoraSlotCount <= 0 || blockEntity.isFermenting()
                    || blockEntity.getStorageMode() == AmphoraBlockEntity.MODE_LIQUID) {
                return ItemStack.EMPTY;
            }

            if (!moveItemStackTo(sourceStack, 0, this.amphoraSlotCount, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        // Check if anything actually moved
        if (sourceStack.getCount() == originalCount) {
            return ItemStack.EMPTY; // Nothing moved, don't process further
        }

        // Update slot state
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        // Don't allow GUI access to fermenting amphorae
        if (blockEntity.isFermenting()) {
            return false;
        }
        return stillValid(this.access, player, ModBlocks.AMPHORA.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public AmphoraBlockEntity getBlockEntity() {
        return this.blockEntity;
    }
}

