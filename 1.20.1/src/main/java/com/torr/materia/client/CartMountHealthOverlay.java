package com.torr.materia.client;

import com.torr.materia.entity.CartEntity;
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
 * Shows cart durability as a horse-style jump bar while the local player is riding.
 * Only visible to the mounted player (client-side check).
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
        if (minecraft.player == null || !(minecraft.player.getVehicle() instanceof CartEntity cart)) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int left = width / 2 - 91;
        int top = height - 32 + 3;
        float ratio = Mth.clamp(cart.getCartHealth() / CartEntity.MAX_HEALTH, 0.0F, 1.0F);
        int filled = (int) (ratio * 183.0F);

        graphics.blit(GUI_ICONS, left, top, 0, 84, 182, 5);
        if (filled > 0) {
            graphics.blit(GUI_ICONS, left, top, 0, 89, filled, 5);
        }
    }
}
