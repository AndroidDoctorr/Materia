package com.torr.materia.menu;

import com.torr.materia.ModMenuTypes;
import com.torr.materia.blockentity.StoneAnvilBlockEntity;
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
import net.minecraftforge.items.SlotItemHandler;

import com.torr.materia.recipe.StoneAnvilRecipe;
import com.torr.materia.ModRecipes;
import net.minecraft.world.SimpleContainer;
import java.util.List;
import java.util.stream.Collectors;

public class StoneAnvilMenu extends AbstractContainerMenu {
    public final StoneAnvilBlockEntity blockEntity;
    private final Level level;

    public StoneAnvilMenu(int windowId, Inventory inv, FriendlyByteBuf extraData) {
        this(windowId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public StoneAnvilMenu(int windowId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.STONE_ANVIL_MENU.get(), windowId);
        checkContainerSize(inv, 2);
        blockEntity = (StoneAnvilBlockEntity) entity;
        this.level = inv.player.level;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            // Tool slot (slot 0)
            this.addSlot(new SlotItemHandler(handler, 0, 56, 54));
            // Input slot (slot 1)
            this.addSlot(new SlotItemHandler(handler, 1, 56, 18 ));
        });
    }

    // Recipe list (server authoritative, but client can recompute based on slots)
    public List<StoneAnvilRecipe> getAvailableRecipes() {
        // Read slots from TE
        ItemStack tool = blockEntity.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .map(h -> h.getStackInSlot(0)).orElse(ItemStack.EMPTY);
        ItemStack metal = blockEntity.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .map(h -> h.getStackInSlot(1)).orElse(ItemStack.EMPTY);

        // Only allow hot input
        boolean hot = metal.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY)
                .map(c -> c.isHot()).orElse(false);
        if (!hot || metal.isEmpty()) return java.util.Collections.emptyList();

        // Filter all stone anvil recipes by tool+metal and sort deterministically so UI indices match server
        return this.level.getRecipeManager().getAllRecipesFor(ModRecipes.STONE_ANVIL_TYPE.get()).stream()
                .map(r -> (StoneAnvilRecipe) r)
                .filter(r -> r.matchesStacks(metal, tool))
                .sorted(java.util.Comparator.comparing(r -> net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(r.getResultItem().getItem())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < 36) {
            // This is a vanilla container slot so merge the stack into the tile inventory (2 TE slots: 36-37)
            if (!moveItemStackTo(sourceStack, 36, 38, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 38) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.err.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    // Called by screen when a recipe index is clicked
    @Override
    public boolean clickMenuButton(Player player, int id) {
        List<StoneAnvilRecipe> recipes = getAvailableRecipes();
        if (id < 0 || id >= recipes.size()) return false;

        StoneAnvilRecipe recipe = recipes.get(id);

        // Validate slots
        var opt = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (!opt.isPresent()) return false;
        var handler = opt.resolve().get();
        ItemStack tool = handler.getStackInSlot(0);
        ItemStack input = handler.getStackInSlot(1);
        ItemStack output = ItemStack.EMPTY; // no output slot

        boolean hot = input.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY)
                .map(c -> c.isHot()).orElse(false);
        if (!hot || input.isEmpty() || tool.isEmpty()) return false;
        if (!recipe.matchesStacks(input, tool)) return false;
        
        // Check if we have enough items to craft
        if (input.getCount() < recipe.getIngredient().getCount()) return false;

        ItemStack result = recipe.getResultItem();
        // deliver to player only

        // Consume input first
        handler.extractItem(1, recipe.getIngredient().getCount(), false);

        // Damage tool (respect container item behavior if present)
        // If tool defines container item (like StoneHammerItem), replace stack with its container item
        if (tool.hasCraftingRemainingItem()) {
            ItemStack container = tool.getCraftingRemainingItem();
            handler.extractItem(0, 1, false);
            if (!container.isEmpty()) {
                // Extra wear for stone hammers
                if (container.getItem() instanceof com.torr.materia.item.StoneHammerItem) {
                    container.hurtAndBreak(2, player, p -> {});
                }
                handler.insertItem(0, container, false);
            }
        } else {
            int amount = (tool.getItem() instanceof com.torr.materia.item.StoneHammerItem) ? 3 : 1;
            tool.hurtAndBreak(amount, player, p -> {});
        }

        // Deliver result: prefer putting in player's main hand, then inventory, else drop
        ItemStack toGive = result.copy();
        if (player.getMainHandItem().isEmpty()) {
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, toGive);
        } else if (player.getInventory().add(toGive)) {
            // added to inventory
        } else {
            player.drop(toGive, false);
        }

        // Now damage the anvil last so result is never lost on break
        String recipeName = recipe.getId().getPath();
        if (!blockEntity.damageAnvil(recipeName)) {
            // Anvil broke; inputs were already consumed and output was granted
            return true;
        }

        return true;
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
