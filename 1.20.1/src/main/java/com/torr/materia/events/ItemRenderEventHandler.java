package com.torr.materia.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.torr.materia.materia;
import com.torr.materia.capability.HotMetalCapability;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT)
public class ItemRenderEventHandler {
    
    private static final ResourceLocation HOT_OVERLAY = new ResourceLocation(materia.MOD_ID, "textures/gui/hot.png");

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        
        // Only handle container screens (inventories, GUIs, etc.)
        if (screen instanceof AbstractContainerScreen<?> containerScreen) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            
            // Render overlays for all slots
            for (Slot slot : containerScreen.getMenu().slots) {
                ItemStack stack = slot.getItem();
                if (!stack.isEmpty()) {
                    stack.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY).ifPresent(hotCapability -> {
                        if (hotCapability.isHot()) {
                            renderHotOverlay(guiGraphics, containerScreen.getGuiLeft() + slot.x,
                                           containerScreen.getGuiTop() + slot.y, hotCapability.getHeatLevel());
                        }
                    });
                }
            }
        }
    }
    
    private static void renderHotOverlay(GuiGraphics guiGraphics, int x, int y, float heatLevel) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, HOT_OVERLAY);
        
        // Full opacity - no fading
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // Calculate height based on heat level (shrinks from bottom to top)
        int fullHeight = 16;
        int currentHeight = (int)(fullHeight * heatLevel);
        
        // Only render if there's something to show
        if (currentHeight > 0) {
            // Draw from the top of the item slot, with height proportional to heat level
            // This creates a "fuel gauge" effect that empties from bottom to top
            guiGraphics.blit(HOT_OVERLAY, x, y + (fullHeight - currentHeight), 0, 0, 16, currentHeight, 16, 16);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
