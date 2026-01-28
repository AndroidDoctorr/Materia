package com.torr.materia.menu;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.blockentity.KilnBlockEntity;
import com.torr.materia.utils.FuelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class AdvancedKilnMenu extends AbstractContainerMenu {
    public final KilnBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private final boolean hasChimney;

    public AdvancedKilnMenu(int windowId, Inventory inv, FriendlyByteBuf extraData) {
        this(windowId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public AdvancedKilnMenu(int windowId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.ADVANCED_KILN_MENU.get(), windowId);
        checkContainerSize(inv, 4);
        blockEntity = (KilnBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        // Check if chimney is present (either kiln chimney or furnace chimney for furnace-kiln)
        BlockPos kilnPos = blockEntity.getBlockPos();
        boolean hasKilnChimney = level.getBlockState(kilnPos.above()).is(ModBlocks.CHIMNEY.get());
        boolean hasFurnaceChimney = level.getBlockState(kilnPos.above()).is(ModBlocks.FURNACE_CHIMNEY.get());
        this.hasChimney = hasKilnChimney || hasFurnaceChimney;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            if (hasChimney) {
                // Two input slots for alloying (slots 0 and 3)
                this.addSlot(new SlotItemHandler(handler, 0, 47, 18)); // Left input
                this.addSlot(new SlotItemHandler(handler, 3, 65, 18)); // Right input (separate slot)
            } else {
                // Single input slot for basic smelting
                this.addSlot(new SlotItemHandler(handler, 0, 56, 18));
            }
            // Fuel slot (slot 1)  
            this.addSlot(new SlotItemHandler(handler, 1, 56, 54));
            // Output slot (slot 2)
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

    public boolean hasChimney() {
        return hasChimney;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 24; // This is the width in pixels of our arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledFuelProgress() {
        int fuelProgress = this.data.get(2);
        int maxFuelProgress = this.data.get(3);
        int fuelProgressSize = 14; // Flame sprite height

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
    private static final int TE_INVENTORY_SLOT_COUNT = 4;  // 4 slots total

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot, so we're shift-clicking from player inventory
            
            // Smart fuel placement: if it's a fuel item, try fuel slot first
            if (FuelHelper.isFuel(sourceStack)) {
                // Try to place in fuel slot (slot 1) first
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 1, TE_INVENTORY_FIRST_SLOT_INDEX + 2, false)) {
                    // If fuel slot is full, try input slots, then output slot
                    if (hasChimney) {
                        // With chimney: slots 0 and 3 are input, slot 2 is output
                        if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + 1, false) &&
                            !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 3, TE_INVENTORY_FIRST_SLOT_INDEX + 4, false) &&
                            !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 2, TE_INVENTORY_FIRST_SLOT_INDEX + 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        // Without chimney: slot 0 is input, slot 2 is output
                        if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + 1, false) &&
                            !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 2, TE_INVENTORY_FIRST_SLOT_INDEX + 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else {
                // Not a fuel item, try input slots first, then output slot, then fuel slot as last resort
                if (hasChimney) {
                    // With chimney: try both input slots, then output, then fuel
                    if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + 1, false) &&
                        !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 3, TE_INVENTORY_FIRST_SLOT_INDEX + 4, false) &&
                        !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 2, TE_INVENTORY_FIRST_SLOT_INDEX + 3, false) &&
                        !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 1, TE_INVENTORY_FIRST_SLOT_INDEX + 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Without chimney: try input, then output, then fuel
                    if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + 1, false) &&
                        !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 2, TE_INVENTORY_FIRST_SLOT_INDEX + 3, false) &&
                        !moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 1, TE_INVENTORY_FIRST_SLOT_INDEX + 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        
        // If stack size == 0 (the entire stack was moved) set slot contents to null
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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
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