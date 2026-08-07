package com.torr.materia.client;



import com.torr.materia.materia;

import net.minecraft.client.Minecraft;

import net.minecraft.world.entity.Pose;
import net.minecraft.sounds.SoundEvents;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.event.TickEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;



/**

 * Client-side cart sleep fade timer and sounds (driven by {@link com.torr.materia.network.CartSleepVisualPacket}).

 */

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class CartSleepVisuals {



    private static final int FADE_IN_TICKS = 30;

    private static final int FADE_OUT_TICKS = 20;



    private static boolean active;

    private static int totalTicks;

    private static int elapsedTicks;

    private static int fadeOutTicks;



    public static boolean isActive() {

        return active || fadeOutTicks > 0;

    }



    public static float getOverlayAlpha() {

        if (fadeOutTicks > 0) {

            return (float) fadeOutTicks / FADE_OUT_TICKS;

        }

        if (!active) {

            return 0.0F;

        }

        if (elapsedTicks < FADE_IN_TICKS) {

            return (float) elapsedTicks / FADE_IN_TICKS;

        }

        return 1.0F;

    }



    public static void begin(int sleepTicks) {

        active = true;

        totalTicks = Math.max(1, sleepTicks);

        elapsedTicks = 0;

        fadeOutTicks = 0;

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null) {

            minecraft.player.setForcedPose(Pose.SLEEPING);

            minecraft.player.playSound(SoundEvents.WOOL_STEP, 1.0F, 1.0F);

        }

    }



    public static void end() {

        if (!active && fadeOutTicks <= 0) {

            return;

        }

        active = false;

        fadeOutTicks = FADE_OUT_TICKS;

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null) {

            minecraft.player.setForcedPose(null);

            minecraft.player.playSound(SoundEvents.PLAYER_LEVELUP, 0.35F, 1.2F);

        }

    }



    @SubscribeEvent

    public static void onClientTick(TickEvent.ClientTickEvent event) {

        if (event.phase != TickEvent.Phase.END) {

            return;

        }

        if (active) {

            elapsedTicks++;

            if (elapsedTicks >= totalTicks) {

                end();

            }

        } else if (fadeOutTicks > 0) {

            fadeOutTicks--;

        }

    }

}

