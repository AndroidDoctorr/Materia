package com.torr.materia.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.entity.CartEntity;
import com.torr.materia.entity.ChariotEntity;
import net.minecraft.world.entity.Entity;
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
        if (minecraft.player == null) {
            return;
        }
        float ratio = resolveMountHealthRatio(minecraft.player.getVehicle());
        if (ratio < 0.0F) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int left = width / 2 - 91;
        int top = height - 32 + 3;
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

    private static float resolveMountHealthRatio(Entity vehicle) {
        if (vehicle instanceof CartEntity cart) {
            return cart.getHealthRatio();
        }
        if (vehicle instanceof ChariotEntity chariot) {
            return chariot.getHealthRatio();
        }
        return -1.0F;
    }
}
