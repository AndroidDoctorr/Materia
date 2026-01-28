package com.torr.materia.menu;

import com.torr.materia.ModMenuTypes;
import com.torr.materia.blockentity.KilnBlockEntity;
import com.torr.materia.utils.FuelHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Menu for the blast furnace. Always behaves like an advanced kiln
 * (dual input) and is skinned by a separate screen.
 */
public class BlastFurnaceMenu extends AbstractContainerMenu {
    public final KilnBlockEntity blockEntity;
    private final ContainerData data;

    public BlastFurnaceMenu(int windowId, Inventory inv, FriendlyByteBuf extraData) {
        this(windowId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public BlastFurnaceMenu(int windowId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.BLAST_FURNACE_MENU.get(), windowId);
        checkContainerSize(inv, 4);
        this.blockEntity = (KilnBlockEntity) entity;
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            // Always expose two input slots like AdvancedKilnMenu
            this.addSlot(new SlotItemHandler(handler, 0, 47, 18));
            this.addSlot(new SlotItemHandler(handler, 3, 65, 18));
            // Fuel and output
            this.addSlot(new SlotItemHandler(handler, 1, 56, 54));
            this.addSlot(new SlotItemHandler(handler, 2, 116, 18));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public boolean hasFuel() {
        return data.get(2) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 24;
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledFuelProgress() {
        int fuelProgress = this.data.get(2);
        int maxFuelProgress = this.data.get(3);
        int fuelProgressSize = 14;
        return maxFuelProgress != 0 ? (fuelProgressSize * fuelProgress / maxFuelProgress) : 0;
    }

    // Slot management constants
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        int teInputA = TE_INVENTORY_FIRST_SLOT_INDEX + 0;  // handler slot 0
        int teInputB = TE_INVENTORY_FIRST_SLOT_INDEX + 1;  // handler slot 3
        int teFuel   = TE_INVENTORY_FIRST_SLOT_INDEX + 2;  // handler slot 1
        int teOutput = TE_INVENTORY_FIRST_SLOT_INDEX + 3;  // handler slot 2

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // From player inventory to TE
            if (FuelHelper.isFuel(sourceStack)) {
                // Try input B first (for steel smelting), then fuel slot
                if (!moveItemStackTo(sourceStack, teInputB, teInputB + 1, false)
                        && !moveItemStackTo(sourceStack, teFuel, teFuel + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Only into input slots (never into output)
                if (!moveItemStackTo(sourceStack, teInputA, teInputA + 1, false)
                        && !moveItemStackTo(sourceStack, teInputB, teInputB + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // From TE to player inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

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
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player, blockEntity.getBlockState().getBlock());
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
