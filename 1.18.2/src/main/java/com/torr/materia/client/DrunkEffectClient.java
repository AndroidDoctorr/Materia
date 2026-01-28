package com.torr.materia.client;

import com.torr.materia.ModEffects;
import com.torr.materia.materia;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT)
public class DrunkEffectClient {
    
    private static float drunkIntensity = 0.0F;
    private static float targetDrunkIntensity = 0.0F;
    private static final float DRUNK_FADE_SPEED = 0.02F;

    @SubscribeEvent
    public static void onFOVSetup(EntityViewRenderEvent.FieldOfView event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        
        if (player == null) return;
        
        // Update target intensity based on drunk effect
        if (player.hasEffect(ModEffects.DRUNK.get())) {
            int duration = player.getEffect(ModEffects.DRUNK.get()).getDuration();
            int amplifier = player.getEffect(ModEffects.DRUNK.get()).getAmplifier();
            
            // Calculate intensity based on duration and amplifier
            float effectStrength = Math.min(1.0F, (1200 - duration) / 300.0F); // Ramp up over 15 seconds
            targetDrunkIntensity = (0.3F + amplifier * 0.2F) * effectStrength;
        } else {
            targetDrunkIntensity = 0.0F;
        }
        
        // Smooth interpolation
        float intensityDifference = targetDrunkIntensity - drunkIntensity;
        if (Math.abs(intensityDifference) > 0.001F) {
            drunkIntensity += intensityDifference * DRUNK_FADE_SPEED;
        } else {
            drunkIntensity = targetDrunkIntensity;
        }
        
        // Apply FOV zoom effect
        if (drunkIntensity > 0.01F) {
            // Slight zoom in effect with wobble
            float wobble = (float) Math.sin(System.currentTimeMillis() * 0.003) * 0.05F * drunkIntensity;
            float zoomFactor = 1.0F + (drunkIntensity * 0.15F) + wobble;
            event.setFOV(event.getFOV() * zoomFactor);
        }
    }

    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        
        if (player == null || drunkIntensity <= 0.01F) return;
        
        // Add camera wobble/shake effect
        long currentTime = System.currentTimeMillis();
        
        // Yaw wobble (left-right)
        float yawWobble = (float) Math.sin(currentTime * 0.002) * 2.0F * drunkIntensity;
        // Pitch wobble (up-down)
        float pitchWobble = (float) Math.sin(currentTime * 0.0015) * 1.5F * drunkIntensity;
        // Roll wobble (tilt)
        float rollWobble = (float) Math.sin(currentTime * 0.0025) * 1.0F * drunkIntensity;
        
        event.setYaw(event.getYaw() + yawWobble);
        event.setPitch(event.getPitch() + pitchWobble);
        event.setRoll(event.getRoll() + rollWobble);
    }

}
