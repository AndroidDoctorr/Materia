package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Bed-like screen dimming while resting in a cart (pose driven by {@link CartSleepVisuals}).
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT)
public class CartSleepOverlay {

    @SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        float alpha = CartSleepVisuals.getOverlayAlpha();
        if (alpha <= 0.0F) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !(minecraft.player.getVehicle() instanceof CartEntity)) {
            return;
        }
        PoseStack poseStack = event.getMatrixStack();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int color = Mth.floor(alpha * 255.0F) << 24;
        GuiComponent.fill(poseStack, 0, 0, width, height, color);
    }
}
