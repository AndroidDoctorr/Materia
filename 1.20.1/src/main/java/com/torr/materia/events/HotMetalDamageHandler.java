package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.capability.HotMetalCapability;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class HotMetalDamageHandler {
    
    // Damage players every 20 ticks (1 second) when holding hot metal
    // Different damage frequencies:
    // - Main/Off hand: Every 1 second (most dangerous)
    // - Hotbar: Every 3 seconds (moderately dangerous) 
    // - Main inventory: Every 5 seconds (least dangerous, but still harmful)
    private static final int DAMAGE_INTERVAL = 20;
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        Player player = event.player;
        if (player.level().isClientSide()) {
            return; // Only process on server
        }
        
        // Only check every DAMAGE_INTERVAL ticks to avoid excessive processing
        if (player.tickCount % DAMAGE_INTERVAL != 0) {
            return;
        }
        
        boolean isHoldingHotMetal = false;
        
        // Check main hand
        ItemStack mainHand = player.getMainHandItem();
        if (isHotMetalItem(mainHand)) {
            isHoldingHotMetal = true;
        }
        
        // Check off hand
        ItemStack offHand = player.getOffhandItem();
        if (isHotMetalItem(offHand)) {
            isHoldingHotMetal = true;
        }
        
        // Check entire inventory for hot metal items (not in hands)
        // Hotbar items are more dangerous (closer to body), main inventory items less so
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack inventoryItem = player.getInventory().getItem(i);
            if (isHotMetalItem(inventoryItem)) {
                if (i < 9) {
                    // Hotbar items: moderate damage frequency
                    if (player.tickCount % (DAMAGE_INTERVAL * 3) == 0) {
                        isHoldingHotMetal = true;
                        break;
                    }
                } else {
                    // Main inventory items: less frequent damage (further from body)
                    if (player.tickCount % (DAMAGE_INTERVAL * 5) == 0) {
                        isHoldingHotMetal = true;
                        break;
                    }
                }
            }
        }
        
        if (isHoldingHotMetal) {
            // Deal fire damage to the player
            player.hurt(player.damageSources().inFire(), 1.0F);
            
            // Set player on fire briefly for visual effect
            player.setSecondsOnFire(2);
            
            // Spawn smoke particles around player's hands (server-side for multiplayer)
            if (player.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 3; i++) {
                    double x = player.getX() + (player.getRandom().nextDouble() - 0.5) * 0.8;
                    double y = player.getY() + player.getEyeHeight() - 0.3;
                    double z = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 0.8;
                    serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0, 0.05, 0, 0.0);
                }
            }
            
            // Show warning message occasionally  
            if (player.tickCount % (DAMAGE_INTERVAL * 4) == 0) {
                player.displayClientMessage(Component.literal("Â§cHot metal burns you! Use tongs!"), true);
            }
            
            // Play hurt sound occasionally
            if (player.tickCount % (DAMAGE_INTERVAL * 2) == 0) {
                player.level().playSound(null, player.blockPosition(), 
                    SoundEvents.PLAYER_HURT_ON_FIRE, SoundSource.PLAYERS, 0.3F, 1.0F);
            }
        }
    }
    
    /**
     * Check if an item is a hot metal item
     */
    private static boolean isHotMetalItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        // Check if item has hot metal capability and is currently hot
        var hotCapOptional = stack.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
        return hotCapOptional.map(cap -> cap.isHot()).orElse(false);
    }
}
