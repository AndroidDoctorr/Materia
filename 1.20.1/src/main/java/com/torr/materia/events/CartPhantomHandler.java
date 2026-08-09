package com.torr.materia.events;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerSpawnPhantomsEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Covered carts shelter occupants from phantoms (same idea as sleeping under a roof).
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CartPhantomHandler {

    @SubscribeEvent
    public static void onPlayerSpawnPhantoms(PlayerSpawnPhantomsEvent event) {
        Player player = event.getEntity();
        if (player.getVehicle() instanceof CartEntity cart && cart.hasCover()) {
            event.setResult(Event.Result.DENY);
        }
    }
}
