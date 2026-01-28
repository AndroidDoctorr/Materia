package com.torr.materia.client.translation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.torr.materia.materia;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages conditional translations based on player username
 */
@OnlyIn(Dist.CLIENT)
public class ConditionalTranslationManager {
    private static final ConditionalTranslationManager INSTANCE = new ConditionalTranslationManager();
    private static final Gson GSON = new Gson();
    
    private final Map<String, String> flemishTranslations = new HashMap<>();
    private boolean flemishTranslationsLoaded = false;
    
    public static ConditionalTranslationManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Load Flemish translations from the nl_be.json file
     */
    public void loadFlemishTranslations() {
        if (flemishTranslationsLoaded) {
            return;
        }
        
        try {
            Minecraft minecraft = Minecraft.getInstance();
            ResourceManager resourceManager = minecraft.getResourceManager();
            ResourceLocation flemishLangFile = new ResourceLocation(materia.MOD_ID, "lang/nl_be.json");
            
            try {
                Resource resource = resourceManager.getResource(flemishLangFile);
                try (InputStreamReader reader = new InputStreamReader(
                        resource.getInputStream(), StandardCharsets.UTF_8)) {
                    
                    JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
                    flemishTranslations.clear();
                    
                    for (Map.Entry<String, com.google.gson.JsonElement> entry : jsonObject.entrySet()) {
                        flemishTranslations.put(entry.getKey(), entry.getValue().getAsString());
                    }
                    
                    flemishTranslationsLoaded = true;
                    materia.LOGGER.info("Loaded {} Flemish translations", flemishTranslations.size());
                }
            } catch (Exception resourceException) {
                materia.LOGGER.warn("Flemish translation file not found: {}", flemishLangFile);
            }
        } catch (Exception e) {
            materia.LOGGER.error("Failed to load Flemish translations", e);
        }
    }
    
    /**
     * Check if the current player should use Flemish translations
     */
    public boolean shouldUseFlemishTranslations() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            String username = minecraft.player.getName().getString();
            return "Borginator".equals(username);
        }
        return false;
    }
    
    /**
     * Get a translation, using Flemish if conditions are met
     */
    public String getTranslation(String key, String defaultTranslation) {
        if (shouldUseFlemishTranslations()) {
            loadFlemishTranslations(); // Ensure translations are loaded
            String flemishTranslation = flemishTranslations.get(key);
            if (flemishTranslation != null) {
                return flemishTranslation;
            }
        }
        return defaultTranslation;
    }
    
    /**
     * Check if we have a Flemish translation for the given key
     */
    public boolean hasFlemishTranslation(String key) {
        loadFlemishTranslations();
        return flemishTranslations.containsKey(key);
    }
    
    /**
     * Clear loaded translations (useful for resource pack reloads)
     */
    public void clearTranslations() {
        flemishTranslations.clear();
        flemishTranslationsLoaded = false;
    }
}
