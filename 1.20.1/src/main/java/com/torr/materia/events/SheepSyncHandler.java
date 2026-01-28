package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.network.NetworkHandler;
import com.torr.materia.network.SheepColorSyncPacket;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class SheepSyncHandler {
    
    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        // Sync custom sheep colors when player starts tracking a sheep
        if (event.getTarget() instanceof Sheep sheep) {
            var capOptional = sheep.getCapability(CustomSheepColorCapability.CUSTOM_SHEEP_COLOR);
            if (capOptional.isPresent()) {
                var capability = capOptional.resolve().get();
                var customColor = capability.getCustomColor();
                
                if (customColor != null && customColor.isCustomColor()) {
                    SheepColorSyncPacket packet = new SheepColorSyncPacket(sheep.getId(), customColor);
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (net.minecraft.server.level.ServerPlayer) event.getEntity()), packet);
                }
            }
        }
    }
}
