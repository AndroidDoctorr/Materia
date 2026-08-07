package com.torr.materia.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT)
public class CartMountHealthOverlay {

    private static final ResourceLocation GUI_ICONS =
            new ResourceLocation("textures/gui/icons.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !(minecraft.player.getVehicle() instanceof CartEntity cart)) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int left = width / 2 - 91;
        int top = height - 32 + 3;
        float ratio = Mth.clamp(cart.getCartHealth() / CartEntity.MAX_HEALTH, 0.0F, 1.0F);
        int filled = (int) (ratio * 183.0F);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_ICONS);
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, -90.0D);
        GuiComponent.blit(poseStack, left, top, 0, 84, 182, 5, 256, 256);
        if (filled > 0) {
            GuiComponent.blit(poseStack, left, top, 0, 89, filled, 5, 256, 256);
        }
        poseStack.popPose();
    }
}
