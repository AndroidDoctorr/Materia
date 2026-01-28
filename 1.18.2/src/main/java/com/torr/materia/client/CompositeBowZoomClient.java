package com.torr.materia.client;

import com.torr.materia.materia;
import com.torr.materia.item.CompositeBowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT)
public class CompositeBowZoomClient {
    
    private static float smoothZoomFactor = 1.0F;
    private static float targetZoomFactor = 1.0F;
    private static final float ZOOM_SPEED = 0.15F;

    @SubscribeEvent
    public static void onFOVSetup(EntityViewRenderEvent.FieldOfView event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        
        if (player == null) return;
        
        targetZoomFactor = 1.0F;
        
        if (player.isUsingItem()) {
            ItemStack useItem = player.getUseItem();
            if (useItem.getItem() instanceof CompositeBowItem) {
                int ticksUsed = useItem.getUseDuration() - player.getUseItemRemainingTicks();
                float drawProgress = CompositeBowItem.getPowerForTime(ticksUsed);
                
                if (drawProgress > 0.1F) {
                    targetZoomFactor = 1.0F - (drawProgress * 0.25F);
                }
            }
        }
        
        float zoomDifference = targetZoomFactor - smoothZoomFactor;
        if (Math.abs(zoomDifference) > 0.001F) {
            smoothZoomFactor += zoomDifference * ZOOM_SPEED;
        } else {
            smoothZoomFactor = targetZoomFactor;
        }
        
        if (smoothZoomFactor < 0.99F) {
            event.setFOV(event.getFOV() * smoothZoomFactor);
        }
    }
}
