package com.torr.materia.events;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import com.torr.materia.capability.GlassPipeCapability;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles steel pipe interactions for glass blowing
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class SteelPipeInteractionHandler {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        handleGlassBlowing(event);
    }
    
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        handleGlassBlowing(event);
    }
    
    private static void handleGlassBlowing(PlayerInteractEvent event) {
        ItemStack heldItem = event.getItemStack();
        
        // Check if player is holding steel pipe with hot glass
        if (heldItem.getItem() == ModItems.STEEL_PIPE.get()) {
            var pipeCapOptional = heldItem.getCapability(GlassPipeCapability.GLASS_PIPE_CAPABILITY);
            if (pipeCapOptional.isPresent()) {
                var pipeCap = pipeCapOptional.resolve().get();
                
                if (pipeCap.hasHotGlass()) {
                    // Remove glass from pipe and create bottle
                    ItemStack glass = pipeCap.removeGlass();
                    if (!glass.isEmpty()) {
                        ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                        
                        // Try to give bottle to player
                        if (!event.getEntity().getInventory().add(bottle)) {
                            // Drop if inventory is full
                            event.getEntity().drop(bottle, false);
                        }
                        
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
