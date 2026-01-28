package com.torr.materia.menu;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.blockentity.IronAnvilBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import com.torr.materia.recipe.IronAnvilRecipe;
import com.torr.materia.ModRecipes;
import java.util.List;
import java.util.stream.Collectors;

public class IronAnvilMenu extends AbstractContainerMenu {
    public final IronAnvilBlockEntity blockEntity;
    private final Level level;

    public IronAnvilMenu(int windowId, Inventory inv, FriendlyByteBuf extraData) {
        this(windowId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public IronAnvilMenu(int windowId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.IRON_ANVIL_MENU.get(), windowId);
        checkContainerSize(inv, 5);
        blockEntity = (IronAnvilBlockEntity) entity;
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            // Tool slot 1 (slot 0)
            this.addSlot(new SlotItemHandler(handler, 0, 34, 54));
            // Tool slot 2 (slot 1)
            this.addSlot(new SlotItemHandler(handler, 1, 56, 54));
            // Tool slot 3 (slot 2)
            this.addSlot(new SlotItemHandler(handler, 2, 78, 54));
            // Input slot 1 (slot 3)
            this.addSlot(new SlotItemHandler(handler, 3, 45, 18));
            // Input slot 2 (slot 4)
            this.addSlot(new SlotItemHandler(handler, 4, 67, 18));
        });
    }

    public List<IronAnvilRecipe> getAvailableRecipes() {
        var opt = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!opt.isPresent()) return java.util.Collections.emptyList();
        var h = opt.resolve().get();
        ItemStack tool0 = h.getStackInSlot(0);
        ItemStack tool1 = h.getStackInSlot(1);
        ItemStack tool2 = h.getStackInSlot(2);
        ItemStack a = h.getStackInSlot(3);
        ItemStack b = h.getStackInSlot(4);
        boolean hotA = a.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY).map(c->c.isHot()).orElse(false);
        boolean hotB = b.isEmpty() || b.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY).map(c->c.isHot()).orElse(false);
        if (a.isEmpty() || !hotA || !hotB) return java.util.Collections.emptyList();
        return this.level.getRecipeManager().getAllRecipesFor(ModRecipes.IRON_ANVIL_TYPE.get()).stream()
                .map(r->(IronAnvilRecipe)r)
                .filter(r->r.matchesStacks(a,b,tool0,tool1,tool2))
                .sorted(java.util.Comparator.comparing(r -> net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(r.getResultItem().getItem())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        List<IronAnvilRecipe> recipes = getAvailableRecipes();
        if (id < 0 || id >= recipes.size()) return false;
        var opt = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!opt.isPresent()) return false;
        var h = opt.resolve().get();

        ItemStack tool0 = h.getStackInSlot(0);
        ItemStack tool1 = h.getStackInSlot(1);
        ItemStack tool2 = h.getStackInSlot(2);
        ItemStack a = h.getStackInSlot(3);
        ItemStack b = h.getStackInSlot(4);

        // Temporarily relax validation - only require slot A and one tool
        if (a.isEmpty() || tool0.isEmpty()) return false;
        boolean hotA = a.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY).map(c->c.isHot()).orElse(false);
        boolean hotB = b.isEmpty() || b.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY).map(c->c.isHot()).orElse(false);
        if (!hotA || !hotB) return false;

        IronAnvilRecipe r = recipes.get(id);
        if (!r.matchesStacks(a,b,tool0,tool1,tool2)) return false;
        
        // Check if we have enough items to craft
        if (a.getCount() < r.getIngredientA().getCount()) return false;
        if (!r.getIngredientB().isEmpty() && !b.isEmpty() && b.getCount() < r.getIngredientB().getCount()) return false;

        // Consume inputs first
        h.extractItem(3, r.getIngredientA().getCount(), false);
        if (!b.isEmpty()) h.extractItem(4, r.getIngredientB().getCount(), false);

        // Damage tools (handle container items properly)
        damageToolSlot(h, 0, tool0, player);
        damageToolSlot(h, 1, tool1, player);
        damageToolSlot(h, 2, tool2, player);

        // Give result to player
        ItemStack result = r.getResultItem().copy();
        
        // Cool items that should naturally cool from the crafting process
        if (shouldCoolAfterCrafting(result)) {
            result = com.torr.materia.utils.HotMetalStackingUtils.createCooledVersion(result);
        }
        
        if (player.getMainHandItem().isEmpty()) {
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, result);
        } else if (!player.getInventory().add(result)) {
            player.drop(result, false);
        }
        
        // Damage the anvil last so the crafted item is never lost
        String recipeName = r.getId().getPath();
        blockEntity.damageAnvil(recipeName);
        return true;
    }
    
    private void damageToolSlot(net.minecraftforge.items.IItemHandler handler, int slot, ItemStack tool, net.minecraft.world.entity.player.Player player) {
        if (tool.hasCraftingRemainingItem()) {
            // Use crafting remaining item system (like buckets, etc.)
            ItemStack container = tool.getCraftingRemainingItem();
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
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, 36, 41, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 41) {
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
