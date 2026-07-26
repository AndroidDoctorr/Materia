package com.torr.materia.menu;

import com.torr.materia.ModMenuTypes;
import com.torr.materia.entity.CartEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CartMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = 0;
    public static final int CRAFT_SLOT_START = 1;
    public static final int CRAFT_SLOT_END = 10;
    public static final int CART_SLOT_START = 10;
    public static final int CART_SLOT_END = 37;
    public static final int INV_SLOT_START = 37;
    public static final int INV_SLOT_END = 64;
    public static final int HOTBAR_SLOT_START = 64;
    public static final int HOTBAR_SLOT_END = 73;

    private static final int STORAGE_LEFT = 8;
    private static final int STORAGE_TOP = 20;
    private static final int CRAFT_LEFT = 176;
    private static final int CRAFT_TOP = 20;
    private static final int RESULT_X = 234;
    private static final int RESULT_Y = 38;
    private static final int PLAYER_LEFT = 8;
    private static final int PLAYER_TOP = 107;
    private static final int HOTBAR_Y = 165;

    private final CartEntity cart;
    private final CraftingContainer craftSlots = new CraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final Player player;

    public CartMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, getCart(playerInventory, data.readVarInt()));
    }

    public CartMenu(int windowId, Inventory playerInventory, CartEntity cart) {
        super(ModMenuTypes.CART_MENU.get(), windowId);
        this.cart = cart;
        this.player = playerInventory.player;

        if (cart.getLootTable() != null && !playerInventory.player.level().isClientSide()) {
            cart.unpackChestVehicleLootTable(playerInventory.player);
        }

        this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 0, RESULT_X, RESULT_Y));

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new Slot(this.craftSlots, col + row * 3, CRAFT_LEFT + col * 18, CRAFT_TOP + row * 18));
            }
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(cart, col + row * 9, STORAGE_LEFT + col * 18, STORAGE_TOP + row * 18));
            }
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, PLAYER_LEFT + col * 18, PLAYER_TOP + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, PLAYER_LEFT + col * 18, HOTBAR_Y));
        }
    }

    private static CartEntity getCart(Inventory inventory, int entityId) {
        Entity entity = inventory.player.level().getEntity(entityId);
        if (entity instanceof CartEntity cart) {
            return cart;
        }
        throw new IllegalStateException("Missing cart entity " + entityId);
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player,
            CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (!level.isClientSide()) {
            ItemStack resultStack = ItemStack.EMPTY;
            for (Recipe<?> recipe : level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, craftingContainer, level)) {
                if (recipe instanceof CraftingRecipe craftingRecipe) {
                    if (craftingRecipe.matches(craftingContainer, level)) {
                        resultStack = craftingRecipe.assemble(craftingContainer);
                        break;
                    }
                }
            }
            resultContainer.setItem(0, resultStack);
        }
    }

    @Override
    public void slotsChanged(Container container) {
        slotChangedCraftingGrid(this, player.level(), player, craftSlots, resultSlots);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, this.craftSlots);
    }

    public CartEntity getCart() {
        return this.cart;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.cart.isChestVehicleStillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        itemstack = stack.copy();

        if (index == RESULT_SLOT) {
            stack.getItem().onCraftedBy(stack, player.level(), player);
            if (!this.moveItemStackTo(stack, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, itemstack);
        } else if (index >= CRAFT_SLOT_START && index < CRAFT_SLOT_END) {
            if (!this.moveItemStackTo(stack, CART_SLOT_START, HOTBAR_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= CART_SLOT_START && index < CART_SLOT_END) {
            if (!this.moveItemStackTo(stack, INV_SLOT_START, HOTBAR_SLOT_END, false)) {
                if (!this.moveItemStackTo(stack, CRAFT_SLOT_START, CRAFT_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (index >= INV_SLOT_START && index < HOTBAR_SLOT_END) {
            if (!this.moveItemStackTo(stack, CART_SLOT_START, CART_SLOT_END, false)) {
                if (!this.moveItemStackTo(stack, CRAFT_SLOT_START, CRAFT_SLOT_END, false)) {
                    if (index < INV_SLOT_END) {
                        if (!this.moveItemStackTo(stack, HOTBAR_SLOT_START, HOTBAR_SLOT_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        if (index == RESULT_SLOT) {
            player.drop(stack, false);
        }

        return itemstack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }
}
