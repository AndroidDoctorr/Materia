package com.torr.materia.utils;

import com.torr.materia.blockentity.KilnBlockEntity;
import com.torr.materia.capability.TongsCapability;
import com.torr.materia.capability.HotMetalCapability;
import com.torr.materia.events.HotMetalEventHandler;
import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import com.torr.materia.blockentity.WaterPotBlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

public class TongsEmptyingUtils {
    
    /**
     * Try to empty tongs into a kiln
     */
    public static InteractionResult tryEmptyIntoKiln(KilnBlockEntity kilnEntity, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        if (tongsCap.isEmpty()) {
            return InteractionResult.PASS;
        }
        
        // Try to insert items into kiln input slot (slot 0)
        boolean insertedAny = false;
        var kilnItemHandler = kilnEntity.getItemHandler();
        
        if (isShiftKeyDown) {
            // Empty all items
            while (!tongsCap.isEmpty()) {
                ItemStack toInsert = tongsCap.removeItem();
                if (toInsert.isEmpty()) break;
                
                ItemStack remaining = kilnItemHandler.insertItem(0, toInsert, false);
                if (!remaining.isEmpty()) {
                    // Put back what couldn't be inserted
                    tongsCap.addItem(remaining);
                    break;
                } else {
                    insertedAny = true;
                }
            }
        } else {
            // Empty one item
            ItemStack toInsert = tongsCap.removeItem();
            if (!toInsert.isEmpty()) {
                ItemStack remaining = kilnItemHandler.insertItem(0, toInsert, false);
                if (!remaining.isEmpty()) {
                    // Put back what couldn't be inserted
                    tongsCap.addItem(remaining);
                } else {
                    insertedAny = true;
                }
            }
        }
        
        if (insertedAny) {
            kilnEntity.setChanged();
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Try to empty tongs into a chest
     */
    public static InteractionResult tryEmptyIntoChest(ChestBlockEntity chestEntity, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        if (tongsCap.isEmpty()) {
            return InteractionResult.PASS;
        }
        
        var chestCapOptional = chestEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (!chestCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var chestItemHandler = chestCapOptional.resolve().get();
        boolean insertedAny = false;
        
        if (isShiftKeyDown) {
            // Empty all items
            while (!tongsCap.isEmpty()) {
                ItemStack toInsert = tongsCap.removeItem();
                if (toInsert.isEmpty()) break;
                
                // Try to insert into any available chest slot
                ItemStack remaining = insertIntoContainer(chestItemHandler, toInsert);
                if (!remaining.isEmpty()) {
                    // Put back what couldn't be inserted
                    tongsCap.addItem(remaining);
                    break;
                } else {
                    insertedAny = true;
                }
            }
        } else {
            // Empty one item
            ItemStack toInsert = tongsCap.removeItem();
            if (!toInsert.isEmpty()) {
                ItemStack remaining = insertIntoContainer(chestItemHandler, toInsert);
                if (!remaining.isEmpty()) {
                    // Put back what couldn't be inserted
                    tongsCap.addItem(remaining);
                } else {
                    insertedAny = true;
                }
            }
        }
        
        if (insertedAny) {
            chestEntity.setChanged();
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Empty tongs into player inventory (only cold items!)
     */
    public static InteractionResult emptyIntoPlayerInventory(Player player, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        if (tongsCap.isEmpty()) {
            return InteractionResult.PASS;
        }
        
        // Check if any items are hot - don't allow hot items into player inventory
        if (HotMetalStackingUtils.hasHotItems(tongsStack)) {
            // Send a message to the player that hot items can't be handled safely
            player.displayClientMessage(Component.literal("These items are too hot to handle safely!"), true);
            return InteractionResult.FAIL;
        }
        
        boolean droppedAny = false;
        
        if (isShiftKeyDown) {
            // Empty all cold items
            List<ItemStack> allItems = tongsCap.removeAllItems();
            for (ItemStack stack : allItems) {
                if (!stack.isEmpty()) {
                    if (!player.getInventory().add(stack)) {
                        // Drop what couldn't fit
                        player.drop(stack, false);
                    }
                    droppedAny = true;
                }
            }
        } else {
            // Empty one cold item
            ItemStack toTransfer = tongsCap.removeItem();
            if (!toTransfer.isEmpty()) {
                if (!player.getInventory().add(toTransfer)) {
                    // Drop what couldn't fit
                    player.drop(toTransfer, false);
                }
                droppedAny = true;
            }
        }
        
        return droppedAny ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
    
    /**
     * Generic method to insert an item into a container
     */
    private static ItemStack insertIntoContainer(net.minecraftforge.items.IItemHandler itemHandler, ItemStack stack) {
        ItemStack remaining = stack.copy();
        
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            remaining = itemHandler.insertItem(i, remaining, false);
            if (remaining.isEmpty()) {
                break;
            }
        }
        
        return remaining;
    }
    
    /**
     * Try to quench hot items in tongs by placing them in a water pot
     */
    public static InteractionResult tryQuenchInWaterPot(WaterPotBlockEntity waterPotEntity, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        if (tongsCap.isEmpty()) {
            return InteractionResult.PASS;
        }
        
        var waterPotItemHandler = waterPotEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (!waterPotItemHandler.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var itemHandler = waterPotItemHandler.resolve().get();
        
        // Check if water pot has space (slot 0 should be empty for quenching)
        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            return InteractionResult.PASS; // Water pot is occupied
        }
        
        boolean quenchedAny = false;
        
        if (isShiftKeyDown) {
            // Quench all hot items from tongs
            while (!tongsCap.isEmpty()) {
                ItemStack toQuench = tongsCap.removeItem();
                if (toQuench.isEmpty()) break;
                
                // Only quench hot heatable metals
                if (!toQuench.is(HotMetalEventHandler.HEATABLE_METALS)) {
                    // Put back non-heatable items
                    tongsCap.addItem(toQuench);
                    break;
                }
                
                // Check if item is hot
                var hotCapOptional = toQuench.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
                boolean isHot = hotCapOptional.map(cap -> cap.isHot()).orElse(false);
                
                if (isHot) {
                    // Create cooled version
                    ItemStack cooledStack = HotMetalStackingUtils.createCooledVersion(toQuench);
                    
                    // Try to insert into water pot temporarily for the quenching effect
                    ItemStack remaining = itemHandler.insertItem(0, cooledStack, false);
                    if (remaining.isEmpty()) {
                        quenchedAny = true;
                        // Immediately extract the cooled item back to tongs
                        ItemStack extractedCooled = itemHandler.extractItem(0, cooledStack.getCount(), false);
                        tongsCap.addItem(extractedCooled);
                    } else {
                        // Couldn't quench, put original back
                        tongsCap.addItem(toQuench);
                        break;
                    }
                } else {
                    // Item is already cold, put it back
                    tongsCap.addItem(toQuench);
                }
            }
        } else {
            // Quench one hot item from tongs
            if (!tongsCap.isEmpty()) {
                ItemStack toQuench = tongsCap.removeItem();
                
                // Only quench hot heatable metals
                if (!toQuench.is(HotMetalEventHandler.HEATABLE_METALS)) {
                    // Put back non-heatable items
                    tongsCap.addItem(toQuench);
                    return InteractionResult.PASS;
                }
                
                // Check if item is hot
                var hotCapOptional = toQuench.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
                boolean isHot = hotCapOptional.map(cap -> cap.isHot()).orElse(false);
                
                if (isHot) {
                    // Create cooled version
                    ItemStack cooledStack = HotMetalStackingUtils.createCooledVersion(toQuench);
                    
                    // Try to insert into water pot temporarily for the quenching effect
                    ItemStack remaining = itemHandler.insertItem(0, cooledStack, false);
                    if (remaining.isEmpty()) {
                        quenchedAny = true;
                        // Immediately extract the cooled item back to tongs
                        ItemStack extractedCooled = itemHandler.extractItem(0, cooledStack.getCount(), false);
                        tongsCap.addItem(extractedCooled);
                    } else {
                        // Couldn't quench, put original back
                        tongsCap.addItem(toQuench);
                    }
                } else {
                    // Item is already cold, put it back
                    tongsCap.addItem(toQuench);
                }
            }
        }
        
        if (quenchedAny) {
            // Play quenching sound and spawn steam particles
            Level level = waterPotEntity.getLevel();
            if (level != null && !level.isClientSide()) {
                BlockPos pos = waterPotEntity.getBlockPos();
                
                // Play extinguish sound
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.7F, 1.0F);
                
                // Spawn steam particles on the server side (they will sync to client)
                for (int i = 0; i < 8; i++) {
                    double x = pos.getX() + 0.3 + level.random.nextDouble() * 0.4;
                    double y = pos.getY() + 0.8;
                    double z = pos.getZ() + 0.3 + level.random.nextDouble() * 0.4;
                    double motionY = 0.1D + level.random.nextDouble() * 0.05D;
                    level.addParticle(ParticleTypes.CLOUD, x, y, z, 0, motionY, 0);
                }
            }
            
            waterPotEntity.setChanged();
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }
}
