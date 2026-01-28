package com.torr.materia.client.translation;

import com.torr.materia.materia;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Provides custom item names based on player conditions
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ItemNameProvider {
    
    /**
     * Override item tooltips to show Flemish names
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.isEmpty() || event.getToolTip().isEmpty()) {
            return;
        }
        
        ConditionalTranslationManager manager = ConditionalTranslationManager.getInstance();
        if (!manager.shouldUseFlemishTranslations()) {
            return;
        }
        
        // Get the item's translation key
        Item item = itemStack.getItem();
        String translationKey = item.getDescriptionId();
        
        // Check if we have a Flemish translation for this item
        if (manager.hasFlemishTranslation(translationKey)) {
            String flemishName = manager.getTranslation(translationKey, translationKey);
            
            // Replace the first line (item name) with the Flemish translation
            event.getToolTip().set(0, Component.literal(flemishName));
        }
    }
}
