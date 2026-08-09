package com.torr.materia.events;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Covered carts shelter occupants from phantoms (same idea as sleeping under a roof).
 * Uses entity join cancellation because {@code PlayerSpawnPhantomsEvent} is 1.20+ only.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CartPhantomHandler {

    private static final double SHELTER_RANGE = 32.0D;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            return;
        }
        if (!(event.getEntity() instanceof Phantom phantom)) {
            return;
        }
        double rangeSq = SHELTER_RANGE * SHELTER_RANGE;
        for (Player player : event.getLevel().players()) {
            if (!(player.getVehicle() instanceof CartEntity)) {
                continue;
            }
            CartEntity cart = (CartEntity) player.getVehicle();
            if (!cart.hasCover()) {
                continue;
            }
            if (player.distanceToSqr(phantom) <= rangeSq) {
                event.setCanceled(true);
                return;
            }
        }
    }
}
