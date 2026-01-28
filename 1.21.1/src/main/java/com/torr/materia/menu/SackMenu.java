package com.torr.materia.menu;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.item.SackItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SackMenu extends AbstractContainerMenu {
    private static final int SLOT_COUNT = 4;
    private final InteractionHand hand;
    private final ItemStack sackStack;
    private final IItemHandler handler;

    public SackMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, extraData.readEnum(InteractionHand.class));
    }

    public SackMenu(int containerId, Inventory inv, InteractionHand hand) {
        super(ModMenuTypes.SACK_MENU.get(), containerId);
        this.hand = hand;
        this.sackStack = inv.player.getItemInHand(hand);
        this.handler = this.sackStack.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalStateException("Sack has no item handler"));

        addSackSlots();
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    private void addSackSlots() {
        this.addSlot(new SackSlot(handler, 0, 71, 17));
        this.addSlot(new SackSlot(handler, 1, 89, 17));
        this.addSlot(new SackSlot(handler, 2, 71, 35));
        this.addSlot(new SackSlot(handler, 3, 89, 35));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int playerInventoryStart = SLOT_COUNT;
            final int playerInventoryEnd = playerInventoryStart + 27;
            final int playerHotbarStart = playerInventoryEnd;
            final int playerHotbarEnd = playerHotbarStart + 9;

            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(itemstack1, playerHotbarStart, playerHotbarEnd, false)) {
                    if (!this.moveItemStackTo(itemstack1, playerInventoryStart, playerInventoryEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index < playerInventoryEnd) {
                if (!this.moveItemStackTo(itemstack1, 0, SLOT_COUNT, false)) {
                    if (!this.moveItemStackTo(itemstack1, playerHotbarStart, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index < playerHotbarEnd) {
                if (!this.moveItemStackTo(itemstack1, 0, SLOT_COUNT, false)) {
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
        ItemStack inHand = player.getItemInHand(this.hand);
        return !inHand.isEmpty() && ItemStack.isSameItemSameComponents(inHand, this.sackStack);
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

    private static class SackSlot extends SlotItemHandler {
        public SackSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(stack.getItem() instanceof SackItem);
        }
    }
}

