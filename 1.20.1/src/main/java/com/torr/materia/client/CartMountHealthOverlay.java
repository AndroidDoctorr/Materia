package com.torr.materia.client;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.entity.ChariotEntity;
import net.minecraft.world.entity.Entity;
import com.torr.materia.materia;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Horse-style mount bar while riding a cart or chariot. Bar width is always the same;
 * fill shows current HP as a percentage of that vehicle's max health.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CartMountHealthOverlay {

    private static final ResourceLocation GUI_ICONS =
            new ResourceLocation("textures/gui/icons.png");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        float ratio = resolveMountHealthRatio(minecraft.player.getVehicle());
        if (ratio < 0.0F) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int left = width / 2 - 91;
        int top = height - 32 + 3;
        int filled = (int) (ratio * 183.0F);

        graphics.blit(GUI_ICONS, left, top, 0, 84, 182, 5);
        if (filled > 0) {
            graphics.blit(GUI_ICONS, left, top, 0, 89, filled, 5);
        }
    }

    /** Returns health ratio for cart/chariot mounts, or -1 when not applicable. */
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
