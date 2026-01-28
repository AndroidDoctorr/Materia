package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.materia;
import com.torr.materia.capability.TongsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT)
public class TongsHotbarRenderer {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        // Only render after all GUI elements to ensure we're on top
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        PoseStack poseStack = event.getMatrixStack();
        Font font = minecraft.font;
        
        // Check each hotbar slot for tongs
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) continue;
            
            var tongsCapOptional = stack.getCapability(TongsCapability.TONGS_CAPABILITY);
            if (tongsCapOptional.isPresent()) {
                var tongsCap = tongsCapOptional.resolve().get();
                int itemCount = tongsCap.getTotalItemCount();
                
                if (itemCount > 0) {
                    // Calculate hotbar slot position
                    int screenWidth = minecraft.getWindow().getGuiScaledWidth();
                    int screenHeight = minecraft.getWindow().getGuiScaledHeight();
                    
                    // Standard hotbar positioning
                    int hotbarX = screenWidth / 2 - 91;
                    int hotbarY = screenHeight - 22;
                    
                    // Position for this slot
                    int slotX = hotbarX + i * 20 + 6;
                    int slotY = hotbarY + 6;
                    
                    // Render count in bottom-right of slot
                    String countText = String.valueOf(itemCount);
                    int textX = slotX + 17 - font.width(countText);
                    int textY = slotY + 9;
                    
                    // Draw text with shadow for better visibility
                    poseStack.pushPose();
                    poseStack.translate(0, 0, 200); // Render in front
                    font.drawShadow(poseStack, countText, textX, textY, 0xFFFFFF);
                    poseStack.popPose();
                }
            }
        }
    }
}
