package com.torr.materia.events;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CartSleepHandler {

    private static final int CART_SLEEP_DELAY_TICKS = 100;
    private static final Map<UUID, Integer> playerSleepDelay = new ConcurrentHashMap<>();

    public static void beginCartSleep(Player player, CartEntity cart) {
        if (player.level.isClientSide) {
            return;
        }
        playerSleepDelay.put(player.getUUID(), CART_SLEEP_DELAY_TICKS);
    }

    private static boolean isSleepingOnCart(Player player) {
        return player.getVehicle() instanceof CartEntity;
    }

    @SubscribeEvent
    public static void onSleepingLocationCheck(SleepingLocationCheckEvent event) {
        if (event.getEntity() instanceof Player player && isSleepingOnCart(player)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onSleepingTimeCheck(SleepingTimeCheckEvent event) {
        Player player = event.getEntity();
        if (isSleepingOnCart(player) && CartEntity.canSleepAt(player.level)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = event.player;
        if (player == null || player.level.isClientSide) {
            return;
        }
        Integer ticks = playerSleepDelay.get(player.getUUID());
        if (ticks == null) {
            return;
        }
        if (!player.isSleeping() || !isSleepingOnCart(player)) {
            playerSleepDelay.remove(player.getUUID());
            return;
        }
        ticks -= 1;
        if (ticks <= 0) {
            if (player.level instanceof ServerLevel serverLevel) {
                long currentTime = serverLevel.getDayTime();
                long timeToMorning = 24000L - (currentTime % 24000L);
                if (timeToMorning > 12000L) {
                    timeToMorning = 24000L - timeToMorning;
                }
                serverLevel.setDayTime(currentTime + timeToMorning);
            }
            playerSleepDelay.remove(player.getUUID());
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.stopSleepInBed(false, false);
            }
        } else {
            playerSleepDelay.put(player.getUUID(), ticks);
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity().level.isClientSide) {
            return;
        }
        playerSleepDelay.remove(event.getEntity().getUUID());
    }
}
