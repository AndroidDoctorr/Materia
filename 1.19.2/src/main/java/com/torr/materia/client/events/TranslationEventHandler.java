package com.torr.materia.client.events;

import com.torr.materia.materia;
import com.torr.materia.client.translation.ConditionalTranslationManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TranslationEventHandler {
    
    @SubscribeEvent
    public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        ConditionalTranslationManager manager = ConditionalTranslationManager.getInstance();
        
        if (manager.shouldUseFlemishTranslations()) {
            manager.loadFlemishTranslations();
        }
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ConditionalTranslationManager.getInstance().clearTranslations();
    }
}
