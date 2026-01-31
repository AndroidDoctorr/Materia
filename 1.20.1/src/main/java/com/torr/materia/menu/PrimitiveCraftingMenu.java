package com.torr.materia.menu;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class PrimitiveCraftingMenu extends AbstractContainerMenu {
    public static final int CRAFTING_SLOTS = 6; // 3x2 grid
    public static final int RESULT_SLOT = 6;
    public static final int INV_SLOT_START = 7;
    public static final int INV_SLOT_END = 34;
    public static final int USE_ROW_SLOT_START = 34;
    public static final int USE_ROW_SLOT_END = 43;

    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 2);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    public PrimitiveCraftingMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, ContainerLevelAccess.NULL);
    }

    public PrimitiveCraftingMenu(int windowId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.PRIMITIVE_CRAFTING_MENU.get(), windowId);
        this.access = access;
        this.player = playerInventory.player;

        // Add the crafting result slot
        this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

        // Add the 3x2 crafting grid
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new Slot(this.craftSlots, col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        // Add player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Add player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, 
                                                CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (!level.isClientSide) {
            ItemStack resultStack = ItemStack.EMPTY;
            // Try to find a matching recipe
            for (Recipe<?> recipe : level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, craftingContainer, level)) {
                if (recipe instanceof CraftingRecipe craftingRecipe) {
                    if (craftingRecipe.matches(craftingContainer, level)) {
                        resultStack = craftingRecipe.assemble(craftingContainer, level.registryAccess());
                        break;
                    }
                }
            }
            resultContainer.setItem(0, resultStack);
            menu.setRemoteSlot(0, resultStack);

            // Sync the result slot to the client (broadcastChanges alone isn't enough for result slots)
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, resultStack));
            }
        }
    }

    @Override
    public void slotsChanged(Container container) {
        this.access.execute((level, blockPos) -> {
            slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots);
        });
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.craftSlots);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.PRIMITIVE_CRAFTING_TABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == RESULT_SLOT) {
                this.access.execute((level, blockPos) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, level, player);
                });
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= INV_SLOT_START && index < USE_ROW_SLOT_END) {
                if (!this.moveItemStackTo(itemstack1, 1, RESULT_SLOT, false)) {
                    if (index < INV_SLOT_END) {
                        if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (index == RESULT_SLOT) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }
} 