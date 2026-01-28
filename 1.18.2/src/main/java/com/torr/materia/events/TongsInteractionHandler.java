package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.blockentity.KilnBlockEntity;
import com.torr.materia.capability.TongsCapability;
import com.torr.materia.utils.TongsEmptyingUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import com.torr.materia.blockentity.WaterPotBlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import com.torr.materia.events.HotMetalEventHandler;
import com.torr.materia.blockentity.BronzeAnvilBlockEntity;
import com.torr.materia.blockentity.IronAnvilBlockEntity;
import com.torr.materia.blockentity.StoneAnvilBlockEntity;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class TongsInteractionHandler {
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        // Only handle main hand interactions
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        
        ItemStack heldItem = event.getItemStack();
        
        // Check if player is holding tongs
        var tongsCapOptional = heldItem.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return;
        }
        
        var level = event.getWorld();
        var pos = event.getPos();
        var player = event.getPlayer();
        boolean isShiftKeyDown = player.isShiftKeyDown();
        
        if (level.isClientSide()) {
            return; // Only handle on server
        }
        
        var blockEntity = level.getBlockEntity(pos);
        
        // New: Allow extracting multiple items from furnaces/blast furnaces with shift-right-click
        if (blockEntity instanceof AbstractFurnaceBlockEntity furnaceEntity) {
            InteractionResult result = tryExtractFromFurnace(furnaceEntity, heldItem, isShiftKeyDown);
            if (result == InteractionResult.SUCCESS) {
                event.setCanceled(true);
                event.setCancellationResult(result);
                return;
            }
        }
        
        // New: Also support kiln extraction here so sneaking doesn't bypass block use
        if (blockEntity instanceof KilnBlockEntity kilnEntity) {
            InteractionResult result = tryExtractFromKiln(kilnEntity, heldItem, isShiftKeyDown);
            if (result == InteractionResult.SUCCESS) {
                event.setCanceled(true);
                event.setCancellationResult(result);
                return;
            }
        }
        
        // For the remaining interactions (chest/water pot), only proceed if tongs actually have items
        var tongsCap = tongsCapOptional.resolve().get();
        if (tongsCap.isEmpty()) {
            return; // Nothing to empty or quench
        }
        
        // New: Handle placing into anvils via utility, including shift placement
        if (blockEntity instanceof BronzeAnvilBlockEntity || blockEntity instanceof IronAnvilBlockEntity || blockEntity instanceof StoneAnvilBlockEntity) {
            InteractionResult result = com.torr.materia.utils.AnvilTongsUtils.handleTongsInteraction((BlockEntity) blockEntity, heldItem, isShiftKeyDown);
            if (result == InteractionResult.SUCCESS) {
                event.setCanceled(true);
                event.setCancellationResult(result);
                return;
            }
        }
        
        // Handle different block types
        if (blockEntity instanceof ChestBlockEntity chestEntity) {
            // Empty into chest
            InteractionResult result = TongsEmptyingUtils.tryEmptyIntoChest(chestEntity, heldItem, isShiftKeyDown);
            
            if (result == InteractionResult.SUCCESS) {
                // Cancel the event to prevent default block interaction (like opening chest GUI)
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        } else if (blockEntity instanceof WaterPotBlockEntity waterPotEntity) {
            // Quench hot items in water pot
            InteractionResult result = TongsEmptyingUtils.tryQuenchInWaterPot(waterPotEntity, heldItem, isShiftKeyDown);
            
            if (result == InteractionResult.SUCCESS) {
                // Cancel the event to prevent default block interaction
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        // Only handle main hand interactions
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        
        ItemStack heldItem = event.getItemStack();
        
        // Check if player is holding tongs
        var tongsCapOptional = heldItem.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        if (tongsCap.isEmpty()) {
            return; // No items to empty
        }
        
        var player = event.getPlayer();
        boolean isShiftKeyDown = player.isShiftKeyDown();
        
        // Don't eject to inventory on shift-click - that should interact with blocks
        if (isShiftKeyDown) {
            return;
        }
        
        if (player.level.isClientSide()) {
            return; // Only handle on server
        }
        
        // Right-clicking in air (non-shift) - empty cold items into player inventory
        InteractionResult result = TongsEmptyingUtils.emptyIntoPlayerInventory(player, heldItem, false);
        
        if (result == InteractionResult.SUCCESS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        // Only handle main hand interactions
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        
        ItemStack heldItem = event.getItemStack();
        
        // Check if player is holding tongs
        var tongsCapOptional = heldItem.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        if (tongsCap.isEmpty()) {
            return; // No items to empty
        }
        
        var player = event.getPlayer();
        boolean isShiftKeyDown = player.isShiftKeyDown();
        
        // Don't eject to inventory on shift-click - that should interact with blocks
        if (isShiftKeyDown) {
            return;
        }
        
        if (player.level.isClientSide()) {
            return; // Only handle on server
        }
        
        // Right-clicking with tongs (non-shift, fallback for air clicking) - empty cold items into player inventory
        InteractionResult result = TongsEmptyingUtils.emptyIntoPlayerInventory(player, heldItem, false);
        
        if (result == InteractionResult.SUCCESS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    // Helpers
    private static InteractionResult tryExtractFromFurnace(AbstractFurnaceBlockEntity furnaceEntity, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        var tongsCap = tongsCapOptional.resolve().get();
        var furnaceItemHandler = furnaceEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (!furnaceItemHandler.isPresent()) {
            return InteractionResult.PASS;
        }
        var itemHandler = furnaceItemHandler.resolve().get();
        // Vanilla furnace output is slot 2
        ItemStack outputStack = itemHandler.getStackInSlot(2);
        if (outputStack.isEmpty()) {
            return InteractionResult.PASS;
        }
        // Only extract heatable metal items
        if (!outputStack.is(HotMetalEventHandler.HEATABLE_METALS)) {
            return InteractionResult.PASS;
        }
        if (isShiftKeyDown) {
            // Extract as many as possible
            ItemStack remaining = tongsCap.addItem(outputStack.copy());
            if (remaining.getCount() < outputStack.getCount()) {
                // Update furnace output with what couldn't be taken
                itemHandler.extractItem(2, outputStack.getCount(), false);
                if (!remaining.isEmpty()) {
                    itemHandler.insertItem(2, remaining, false);
                }
                furnaceEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        } else {
            // Extract one item
            if (outputStack.getCount() > 0) {
                ItemStack single = outputStack.copy();
                single.setCount(1);
                ItemStack leftover = tongsCap.addItem(single);
                if (leftover.isEmpty()) {
                    itemHandler.extractItem(2, 1, false);
                    furnaceEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult tryExtractFromKiln(KilnBlockEntity kilnEntity, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        var tongsCap = tongsCapOptional.resolve().get();
        var kilnItemHandler = kilnEntity.getItemHandler();
        // Output slot is 2 in kiln
        ItemStack outputStack = kilnItemHandler.getStackInSlot(2);
        if (outputStack.isEmpty()) {
            return InteractionResult.PASS;
        }
        // Only extract hot metal items
        if (!outputStack.is(HotMetalEventHandler.HEATABLE_METALS)) {
            return InteractionResult.PASS;
        }
        if (isShiftKeyDown) {
            ItemStack remaining = tongsCap.addItem(outputStack.copy());
            if (remaining.getCount() < outputStack.getCount()) {
                kilnItemHandler.setStackInSlot(2, remaining.isEmpty() ? ItemStack.EMPTY : remaining);
                kilnEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        } else {
            if (outputStack.getCount() > 0) {
                ItemStack singleItem = outputStack.copy();
                singleItem.setCount(1);
                ItemStack remaining = tongsCap.addItem(singleItem);
                if (remaining.isEmpty()) {
                    outputStack.shrink(1);
                    kilnItemHandler.setStackInSlot(2, outputStack);
                    kilnEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
