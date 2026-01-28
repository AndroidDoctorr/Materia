package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.ModItems;
import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.entity.CustomSheepColor;
import com.torr.materia.network.NetworkHandler;
import com.torr.materia.network.SheepColorSyncPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class SheepDyeingHandler {
    
    /**
     * Get CustomSheepColor only for custom dyes, returns null for vanilla dyes
     */
    private static CustomSheepColor getCustomDyeOnly(Item dyeItem) {
        // Only check custom dyes, ignore vanilla ones completely
        if (dyeItem == ModItems.OCHRE.get()) return CustomSheepColor.OCHRE;
        if (dyeItem == ModItems.RED_OCHRE.get()) return CustomSheepColor.RED_OCHRE;
        if (dyeItem == ModItems.INDIGO_DYE.get()) return CustomSheepColor.INDIGO;
        if (dyeItem == ModItems.OLIVE_DYE.get()) return CustomSheepColor.OLIVE;
        if (dyeItem == ModItems.TYRIAN_PURPLE_DYE.get()) return CustomSheepColor.TYRIAN_PURPLE;
        if (dyeItem == ModItems.LAVENDER_DYE.get()) return CustomSheepColor.LAVENDER;
        if (dyeItem == ModItems.CHARCOAL_GRAY_DYE.get()) return CustomSheepColor.CHARCOAL_GRAY;
        if (dyeItem == ModItems.TAUPE_DYE.get()) return CustomSheepColor.TAUPE;
        
        return null; // Not a custom dye
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        // Only handle main hand interactions
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        
        // Check if we're interacting with a sheep
        if (!(event.getTarget() instanceof Sheep sheep)) {
            return;
        }
        
        // Check if player is holding a custom dye (not vanilla)
        ItemStack heldItem = event.getItemStack();
        CustomSheepColor dyeColor = getCustomDyeOnly(heldItem.getItem());
        
        
        // Only handle truly custom colors - let vanilla handle all vanilla colors
        if (dyeColor == null) {
            return; // Not a custom dye, let vanilla handle it
        }
        
        // Cancel the event to prevent normal dyeing for custom colors only
        event.setResult(Event.Result.ALLOW);
        event.setCanceled(true);
        
        if (!event.getLevel().isClientSide()) {
            // Get the sheep's custom color capability
            var capOptional = sheep.getCapability(CustomSheepColorCapability.CUSTOM_SHEEP_COLOR);
            if (capOptional.isPresent()) {
                var capability = capOptional.resolve().get();
                
                // Set the custom color
                capability.setCustomColor(dyeColor);
                
                // Set the sheep's vanilla color to the closest equivalent for basic functionality
                // This ensures the sheep behaves normally (breeding, etc.)
                if (dyeColor.getVanillaEquivalent() != null) {
                    sheep.setColor(dyeColor.getVanillaEquivalent());
                } else {
                    // For custom colors without vanilla equivalent, use a visible fallback color
                    net.minecraft.world.item.DyeColor fallbackColor = switch (dyeColor) {
                        case OCHRE -> net.minecraft.world.item.DyeColor.YELLOW;
                        case RED_OCHRE -> net.minecraft.world.item.DyeColor.RED;
                        case INDIGO -> net.minecraft.world.item.DyeColor.BLUE;
                        case OLIVE -> net.minecraft.world.item.DyeColor.GREEN;
                        case TYRIAN_PURPLE -> net.minecraft.world.item.DyeColor.PURPLE;
                        case LAVENDER -> net.minecraft.world.item.DyeColor.MAGENTA;
                        case CHARCOAL_GRAY -> net.minecraft.world.item.DyeColor.GRAY;
                        case TAUPE -> net.minecraft.world.item.DyeColor.BROWN;
                        default -> net.minecraft.world.item.DyeColor.WHITE;
                    };
                    sheep.setColor(fallbackColor);
                }
                
                // Consume the dye item
                if (!event.getEntity().isCreative()) {
                    heldItem.shrink(1);
                }
                
                // Play dyeing sound (use a sound that exists in 1.18.2)
                sheep.playSound(net.minecraft.sounds.SoundEvents.SHEEP_AMBIENT, 1.0F, 1.0F);
                
                // Sync to all nearby players
                SheepColorSyncPacket packet = new SheepColorSyncPacket(sheep.getId(), dyeColor);
                NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> sheep), packet);
            }
        }
    }
}
