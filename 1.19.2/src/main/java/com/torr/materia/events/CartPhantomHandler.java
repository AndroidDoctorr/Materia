package com.torr.materia.events;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Covered carts shelter occupants from phantoms (same idea as sleeping under a roof).
 * Uses entity join cancellation because {@code PlayerSpawnPhantomsEvent} is 1.20+ only.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CartPhantomHandler {

    private static final double PHANTOM_BLOCK_RANGE = 64.0D;
    private static final double PHANTOM_BLOCK_RANGE_SQ = PHANTOM_BLOCK_RANGE * PHANTOM_BLOCK_RANGE;

    public static boolean isShelteredInCoveredCart(Player player) {
        if (!(player.getVehicle() instanceof CartEntity)) {
            return false;
        }
        CartEntity cart = (CartEntity) player.getVehicle();
        return cart.hasCover();
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Phantom phantom)) {
            return;
        }
        if (isNearShelteredCartRider(phantom)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if (!(event.getEntity() instanceof Phantom)) {
            return;
        }
        LivingEntity newTarget = event.getNewTarget();
        if (newTarget instanceof Player player && isShelteredInCoveredCart(player)) {
            event.setCanceled(true);
        }
    }

    /** Clears targets phantoms picked up before the player took shelter. */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level.isClientSide) {
            return;
        }
        if (!isShelteredInCoveredCart(event.player) || event.player.tickCount % 20 != 0) {
            return;
        }
        Player player = event.player;
        for (Phantom phantom : player.level.getEntitiesOfClass(Phantom.class, player.getBoundingBox().inflate(PHANTOM_BLOCK_RANGE))) {
            if (phantom.getTarget() == player) {
                phantom.setTarget(null);
            }
        }
    }

    private static boolean isNearShelteredCartRider(Phantom phantom) {
        for (Player player : phantom.level.players()) {
            if (isShelteredInCoveredCart(player) && player.distanceToSqr(phantom) <= PHANTOM_BLOCK_RANGE_SQ) {
                return true;
            }
        }
        return false;
    }
}
