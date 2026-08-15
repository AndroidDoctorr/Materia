package com.torr.materia.events;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import com.torr.materia.network.CartSleepVisualPacket;
import com.torr.materia.network.NetworkHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cart sleep while mounted: forced prone pose, no vanilla bed sleep (which dismounts and fights riding).
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CartSleepHandler {

    private static final int CART_SLEEP_DELAY_TICKS = 100;
    private static final Map<UUID, Integer> playerSleepDelay = new ConcurrentHashMap<>();
    private static final Map<UUID, UUID> playerToCart = new ConcurrentHashMap<>();

    public static boolean isCartSleeping(Player player) {
        return playerSleepDelay.containsKey(player.getUUID());
    }

    /** Works on client (forced pose sync) and server (sleep map). */
    public static boolean shouldSkipPassengerPositioning(Player player) {
        return player.getForcedPose() == Pose.SLEEPING || isCartSleeping(player);
    }

    public static void beginCartSleep(Player player, CartEntity cart) {
        if (player.level().isClientSide()) {
            return;
        }
        if (player.getVehicle() != cart) {
            return;
        }
        playerToCart.put(player.getUUID(), cart.getUUID());
        playerSleepDelay.put(player.getUUID(), CART_SLEEP_DELAY_TICKS);
        cart.applySleepBodyOrientation(player);
        player.setForcedPose(Pose.SLEEPING);
        sendVisuals(player, true, CART_SLEEP_DELAY_TICKS);
    }

    public static void endCartSleep(Player player) {
        playerSleepDelay.remove(player.getUUID());
        playerToCart.remove(player.getUUID());
        player.setForcedPose(null);
        sendVisuals(player, false, 0);
    }

    private static void sendVisuals(Player player, boolean sleeping, int sleepTicks) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new CartSleepVisualPacket(sleeping, sleepTicks));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = event.player;
        if (player == null || player.level().isClientSide()) {
            return;
        }
        if (!isCartSleeping(player)) {
            return;
        }
        if (!(player.getVehicle() instanceof CartEntity cart) || !cart.isAlive()) {
            endCartSleep(player);
            return;
        }
        UUID expectedCart = playerToCart.get(player.getUUID());
        if (expectedCart == null || cart.getUUID() != expectedCart) {
            endCartSleep(player);
            return;
        }

        player.setForcedPose(Pose.SLEEPING);

        Integer ticks = playerSleepDelay.get(player.getUUID());
        if (ticks == null) {
            return;
        }
        ticks -= 1;
        if (ticks <= 0) {
            if (player.level() instanceof ServerLevel serverLevel) {
                long currentTime = serverLevel.getDayTime();
                long timeToMorning = 24000L - (currentTime % 24000L);
                if (timeToMorning > 12000L) {
                    timeToMorning = 24000L - timeToMorning;
                }
                serverLevel.setDayTime(currentTime + timeToMorning);
            }
            endCartSleep(player);
        } else {
            playerSleepDelay.put(player.getUUID(), ticks);
        }
    }
}
