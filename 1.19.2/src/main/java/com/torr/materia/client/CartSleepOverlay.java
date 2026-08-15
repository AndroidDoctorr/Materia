package com.torr.materia.client;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Bed-like screen dimming and sleeping player pose while resting in a cart.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CartSleepOverlay {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) {
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
        var poseStack = event.getPoseStack();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int color = Mth.floor(alpha * 255.0F) << 24;
        net.minecraft.client.gui.GuiComponent.fill(poseStack, 0, 0, width, height, color);
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        if (!(event.getEntity() instanceof Player player) || !(player.getVehicle() instanceof CartEntity)) {
            return;
        }
        if (player.getForcedPose() != Pose.SLEEPING && !CartSleepVisuals.isActive()) {
            return;
        }
        if (event.getRenderer().getModel() instanceof PlayerModel<?> model) {
            model.riding = false;
            model.crouching = false;
        }
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        float alpha = CartSleepVisuals.getOverlayAlpha();
        if (alpha <= 0.01F) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !(minecraft.player.getVehicle() instanceof CartEntity)) {
            return;
        }
        if (!minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }
        event.setPitch(event.getPitch() + 18.0F * alpha);
    }
}
