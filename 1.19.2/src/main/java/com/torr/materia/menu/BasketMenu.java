package com.torr.materia.menu;

import com.torr.materia.BasketBlock;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.blockentity.BasketBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BasketMenu extends AbstractContainerMenu {
    private final BasketBlockEntity blockEntity;
    private final Level level;
    private final ContainerLevelAccess access;

    // Client constructor
    public BasketMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    // Main constructor
    public BasketMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.BASKET_MENU.get(), containerId);
        checkContainerSize(inv, 4);
        this.blockEntity = (BasketBlockEntity) entity;
        this.level = inv.player.level;
        this.access = ContainerLevelAccess.create(level, blockEntity.getBlockPos());

        // Add basket slots first (2x2 grid) - these will be slots 0-3
        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            // Top row (slots 0-1)
            this.addSlot(new SlotItemHandler(handler, 0, 71, 17));
            this.addSlot(new SlotItemHandler(handler, 1, 89, 17));
            
            // Bottom row (slots 2-3) 
            this.addSlot(new SlotItemHandler(handler, 2, 71, 35));
            this.addSlot(new SlotItemHandler(handler, 3, 89, 35));
        });

        // Add player inventory after basket slots
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            final int basketSlots = 4;
            final int playerInventoryStart = basketSlots;
            final int playerInventoryEnd = playerInventoryStart + 27; // 27 inventory slots
            final int playerHotbarStart = playerInventoryEnd;
            final int playerHotbarEnd = playerHotbarStart + 9; // 9 hotbar slots
            
            if (index < basketSlots) {
                // Moving from basket to player inventory
                // Try hotbar first, then main inventory
                if (!this.moveItemStackTo(itemstack1, playerHotbarStart, playerHotbarEnd, false)) {
                    if (!this.moveItemStackTo(itemstack1, playerInventoryStart, playerInventoryEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index < playerInventoryEnd) {
                // Moving from player main inventory to basket, then try hotbar
                if (!this.moveItemStackTo(itemstack1, 0, basketSlots, false)) {
                    if (!this.moveItemStackTo(itemstack1, playerHotbarStart, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index < playerHotbarEnd) {
                // Moving from player hotbar to basket, then try main inventory
                if (!this.moveItemStackTo(itemstack1, 0, basketSlots, false)) {
                    if (!this.moveItemStackTo(itemstack1, playerInventoryStart, playerInventoryEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.BASKET.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        // Close the basket when the GUI is closed
        if (!this.level.isClientSide) {
            if (this.blockEntity.getLevel().getBlockEntity(this.blockEntity.getBlockPos()) instanceof BasketBlockEntity) {
                BasketBlock basketBlock = (BasketBlock) ModBlocks.BASKET.get();
                basketBlock.closeBasket(this.level, this.blockEntity.getBlockPos(), 
                    this.level.getBlockState(this.blockEntity.getBlockPos()));
            }
        }
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
}
