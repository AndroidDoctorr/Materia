package com.torr.materia.client;

import com.torr.materia.materia;
import com.torr.materia.client.model.CartBodyModel;
import com.torr.materia.client.model.CartCoverModel;
import com.torr.materia.client.model.CartLanternModel;
import com.torr.materia.client.model.CartShieldModel;
import com.torr.materia.client.model.CartModel;
import com.torr.materia.client.model.ChariotModel;
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
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CartModel.LAYER, CartModel::createBodyLayer);
        event.registerLayerDefinition(CartBodyModel.LAYER, CartBodyModel::createBodyLayer);
        event.registerLayerDefinition(CartCoverModel.LAYER, CartCoverModel::createBodyLayer);
        event.registerLayerDefinition(CartLanternModel.LAYER, CartLanternModel::createBodyLayer);
        event.registerLayerDefinition(CartShieldModel.LAYER, CartShieldModel::createBodyLayer);
        event.registerLayerDefinition(ChariotModel.LAYER, ChariotModel::createBodyLayer);
    }
    
    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        // Attach our custom wool tint layer to the vanilla sheep renderer
        var renderer = event.getRenderer(EntityType.SHEEP);
        if (renderer instanceof SheepRenderer sheepRenderer) {
            sheepRenderer.addLayer(new CustomSheepFurLayer(sheepRenderer, event.getEntityModels()));
        }
    }
}
