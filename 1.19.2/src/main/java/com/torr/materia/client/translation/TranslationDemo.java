package com.torr.materia.client.translation;

import com.torr.materia.materia;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Demonstration class showing how the conditional translation system works
 */
@OnlyIn(Dist.CLIENT)
public class TranslationDemo {
    
    /**
     * Example method showing how to get conditional translations
     */
    public static void demonstrateTranslations() {
        ConditionalTranslationManager manager = ConditionalTranslationManager.getInstance();
        
        // Example translation keys from your mod
        String[] exampleKeys = {
            "item.materia.pebble",
            "item.materia.rock", 
            "item.materia.flint_knife",
            "block.materia.primitive_crafting_table",
            "block.materia.fire_pit",
            "container.primitive_crafting_table"
        };
        
        materia.LOGGER.info("=== Translation Demo ===");
        materia.LOGGER.info("Player: {}", 
            Minecraft.getInstance().player != null ? 
            Minecraft.getInstance().player.getName().getString() : "None");
        materia.LOGGER.info("Should use Flemish: {}", manager.shouldUseFlemishTranslations());
        
        for (String key : exampleKeys) {
            String defaultTranslation = key; // Fallback to key if no translation
            String conditionalTranslation = manager.getTranslation(key, defaultTranslation);
            
            materia.LOGGER.info("{} -> {}", key, conditionalTranslation);
        }
        materia.LOGGER.info("=== End Demo ===");
    }
    
    /**
     * Get a translated component that respects the conditional translation system
     */
    public static Component getTranslatedComponent(String translationKey) {
        ConditionalTranslationManager manager = ConditionalTranslationManager.getInstance();
        String translation = manager.getTranslation(translationKey, translationKey);
        return Component.literal(translation);
    }
    
    /**
     * Check if the current player should see Flemish translations
     */
    public static boolean isUsingFlemishTranslations() {
        return ConditionalTranslationManager.getInstance().shouldUseFlemishTranslations();
    }
}
