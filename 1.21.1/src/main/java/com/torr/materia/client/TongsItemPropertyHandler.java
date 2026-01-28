package com.torr.materia.client;

import com.torr.materia.materia;
import com.torr.materia.ModItems;
import com.torr.materia.item.TongsItem;
import com.torr.materia.item.SteelPipeItem;
import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TongsItemPropertyHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register item property for bronze tongs
            ItemProperties.register(ModItems.BRONZE_TONGS.get(), 
                ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "state"), 
                (stack, level, entity, seed) -> {
                    if (stack.getItem() instanceof TongsItem tongsItem) {
                        String suffix = tongsItem.getModelSuffix(stack);
                        return switch (suffix) {
                            case "_ingot_hot" -> 1.0f;
                            case "_ingot_cold" -> 0.5f;
                            default -> 0.0f; // Empty tongs
                        };
                    }
                    return 0.0f;
                });
            
            // Register item property for iron tongs
            ItemProperties.register(ModItems.IRON_TONGS.get(), 
                ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "state"), 
                (stack, level, entity, seed) -> {
                    if (stack.getItem() instanceof TongsItem tongsItem) {
                        String suffix = tongsItem.getModelSuffix(stack);
                        return switch (suffix) {
                            case "_ingot_hot" -> 1.0f;
                            case "_ingot_cold" -> 0.5f;
                            default -> 0.0f; // Empty tongs
                        };
                    }
                    return 0.0f;
                });
            
            // Register item property for steel tongs
            ItemProperties.register(ModItems.STEEL_TONGS.get(), 
                ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "state"), 
                (stack, level, entity, seed) -> {
                    if (stack.getItem() instanceof TongsItem tongsItem) {
                        String suffix = tongsItem.getModelSuffix(stack);
                        return switch (suffix) {
                            case "_ingot_hot" -> 1.0f;
                            case "_ingot_cold" -> 0.5f;
                            default -> 0.0f; // Empty tongs
                        };
                    }
                    return 0.0f;
                });
            
            // Register item property for wood tongs
            ItemProperties.register(ModItems.WOOD_TONGS.get(), 
                ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "state"), 
                (stack, level, entity, seed) -> {
                    if (stack.getItem() instanceof TongsItem tongsItem) {
                        String suffix = tongsItem.getModelSuffix(stack);
                        return switch (suffix) {
                            case "_ingot_hot" -> 1.0f;
                            case "_ingot_cold" -> 0.5f;
                            default -> 0.0f; // Empty tongs
                        };
                    }
                    return 0.0f;
                });
            
            // Register item property for steel pipe
            ItemProperties.register(ModItems.STEEL_PIPE.get(), 
                ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "has_glass"), 
                (stack, level, entity, seed) -> {
                    if (stack.getItem() instanceof SteelPipeItem pipeItem) {
                        return pipeItem.hasHotGlass(stack) ? 1.0f : 0.0f;
                    }
                    return 0.0f;
                });
        });
    }
}
