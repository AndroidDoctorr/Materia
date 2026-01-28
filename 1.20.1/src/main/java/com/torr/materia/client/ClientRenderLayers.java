package com.torr.materia.client;

import com.torr.materia.materia;
import com.torr.materia.client.renderer.CustomSheepFurLayer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRenderLayers {
    
    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        // Attach our custom wool tint layer to the vanilla sheep renderer
        var renderer = event.getRenderer(EntityType.SHEEP);
        if (renderer instanceof SheepRenderer sheepRenderer) {
            sheepRenderer.addLayer(new CustomSheepFurLayer(sheepRenderer, event.getEntityModels()));
        }
    }
}
