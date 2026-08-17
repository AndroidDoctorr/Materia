package com.torr.materia.client;

import com.torr.materia.entity.CartEntity;
import com.torr.materia.entity.ChariotEntity;
import net.minecraft.world.entity.Entity;
import com.torr.materia.materia;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CartMountHealthOverlay {

    private static final ResourceLocation GUI_ICONS =
            ResourceLocation.withDefaultNamespace("textures/gui/icons.png");

    @SubscribeEvent
    public static void onAddGuiOverlayLayers(AddGuiOverlayLayersEvent event) {
        event.getLayeredDraw().addAbove(
                ForgeLayeredDraw.VANILLA_ROOT,
                ForgeLayeredDraw.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "cart_mount_health"),
                CartMountHealthOverlay::render);
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        float ratio = resolveMountHealthRatio(minecraft.player.getVehicle());
        if (ratio < 0.0F) {
            return;
        }

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
