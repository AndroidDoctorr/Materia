package com.torr.materia.menu;

import com.torr.materia.ModMenuTypes;
import com.torr.materia.blockentity.BronzeAnvilBlockEntity;
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
import com.torr.materia.recipe.BronzeAnvilRecipe;
import com.torr.materia.ModRecipes;
import java.util.List;
import java.util.stream.Collectors;
import com.torr.materia.recipe.BronzeAnvilRecipe;
import com.torr.materia.ModRecipes;
import java.util.List;
import java.util.stream.Collectors;

public class BronzeAnvilMenu extends AbstractContainerMenu {
    public final BronzeAnvilBlockEntity blockEntity;
    private final Level level;

    public BronzeAnvilMenu(int windowId, Inventory inv, FriendlyByteBuf extraData) {
        this(windowId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public BronzeAnvilMenu(int windowId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.BRONZE_ANVIL_MENU.get(), windowId);
        checkContainerSize(inv, 3);
        blockEntity = (BronzeAnvilBlockEntity) entity;
        this.level = inv.player.level;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            // Tool slot 1 (slot 0)
            this.addSlot(new SlotItemHandler(handler, 0, 44, 54));
            // Tool slot 2 (slot 1)
            this.addSlot(new SlotItemHandler(handler, 1, 68, 54));
            // Input slot (slot 2)
            this.addSlot(new SlotItemHandler(handler, 2, 56, 18));
        });
    }

    public List<BronzeAnvilRecipe> getAvailableRecipes() {
        var opt = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (!opt.isPresent()) return java.util.Collections.emptyList();
        var handler = opt.resolve().get();
        ItemStack tool0 = handler.getStackInSlot(0);
        ItemStack tool1 = handler.getStackInSlot(1);
        ItemStack metal = handler.getStackInSlot(2);
        boolean hot = metal.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY).map(c->c.isHot()).orElse(false);
        if (metal.isEmpty() || !hot) return java.util.Collections.emptyList();
        return this.level.getRecipeManager().getAllRecipesFor(ModRecipes.BRONZE_ANVIL_TYPE).stream()
                .map(r->(BronzeAnvilRecipe)r)
                .filter(r->r.matchesStacks(metal, tool0, tool1))
                .sorted(java.util.Comparator.comparing(r -> net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(r.getResultItem().getItem())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        List<BronzeAnvilRecipe> recipes = getAvailableRecipes();
        if (id < 0 || id >= recipes.size()) return false;
        var opt = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (!opt.isPresent()) return false;
        var h = opt.resolve().get();
        ItemStack tool0 = h.getStackInSlot(0);
        ItemStack tool1 = h.getStackInSlot(1);
        ItemStack metal = h.getStackInSlot(2);
        if (metal.isEmpty() || tool0.isEmpty()) return false; // Only require first tool
        boolean hot = metal.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY).map(c->c.isHot()).orElse(false);
        if (!hot) return false;
        BronzeAnvilRecipe r = recipes.get(id);
        if (!r.matchesStacks(metal, tool0, tool1)) return false;
        
        // Check if we have enough items to craft
        if (metal.getCount() < r.getIngredient().getCount()) return false;

        // Consume input first
        h.extractItem(2, r.getIngredient().getCount(), false);

        // Damage tools (handle container items properly)
        damageToolSlot(h, 0, tool0, player);
        
        // Damage second tool if present
        if (!tool1.isEmpty()) {
            damageToolSlot(h, 1, tool1, player);
        }

        // Give result to player
        ItemStack result = r.getResultItem();
        ItemStack give = result.copy();
        
        // Cool items that should naturally cool from the crafting process
        if (shouldCoolAfterCrafting(give)) {
            give = com.torr.materia.utils.HotMetalStackingUtils.createCooledVersion(give);
        }
        
        if (player.getMainHandItem().isEmpty()) {
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, give);
        } else if (!player.getInventory().add(give)) {
            player.drop(give, false);
        }

        // Damage the anvil last so the crafted item is never lost
        String recipeName = r.getId().getPath();
        blockEntity.damageAnvil(recipeName);
        return true;
    }
    
    private void damageToolSlot(net.minecraftforge.items.IItemHandler handler, int slot, ItemStack tool, net.minecraft.world.entity.player.Player player) {
        if (tool.hasContainerItem()) {
            // Use container item system (like hammers, tongs, etc.)
            ItemStack container = tool.getContainerItem();
            handler.extractItem(slot, 1, false);
            if (!container.isEmpty()) {
                // Extra wear for stone tools
                if (container.getItem() instanceof com.torr.materia.item.StoneHammerItem) {
                    container.hurtAndBreak(2, player, p -> {});
                }
                handler.insertItem(slot, container, false);
            }
        } else {
            // Use direct durability damage (fallback)
            int amount = (tool.getItem() instanceof com.torr.materia.item.StoneHammerItem) ? 3 : 1;
            tool.hurtAndBreak(amount, player, p -> {});
        }
    }
    
    private boolean shouldCoolAfterCrafting(ItemStack stack) {
        String itemName = stack.getItem().toString().toLowerCase();
        // Wire drawing and small part crafting naturally cools the metal
        return itemName.contains("wire") || 
               itemName.contains("nugget") || 
               itemName.contains("nail") || 
               itemName.contains("needle") ||
               itemName.contains("rivet") ||
               itemName.contains("ring") ||
               itemName.contains("scale");
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < 36) {
            // TE has 3 slots (0-2)
            if (!moveItemStackTo(sourceStack, 36, 39, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 39) {
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
