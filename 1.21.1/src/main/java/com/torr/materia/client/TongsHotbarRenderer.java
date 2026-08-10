package com.torr.materia.client;

import com.torr.materia.materia;
import com.torr.materia.capability.TongsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Draws held-item counts for tongs on the hotbar.
 * <p>
 * Uses Forge's {@code AddGuiOverlayLayersEvent} when present (Forge 52.1.2+). On older
 * 1.21.1 Forge builds (e.g. 52.1.0) that event does not exist yet, so registration is
 * skipped and tongs still work without the overlay text.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TongsHotbarRenderer {

    private static final String OVERLAY_EVENT_CLASS =
            "net.minecraftforge.client.event.AddGuiOverlayLayersEvent";
    private static final String LAYERED_DRAW_CLASS =
            "net.minecraftforge.client.gui.overlay.ForgeLayeredDraw";

    @SubscribeEvent
    public static void onConstructMod(FMLConstructModEvent event) {
        registerOverlayIfSupported(FMLJavaModLoadingContext.get().getModEventBus());
    }

    static void registerOverlayIfSupported(IEventBus modEventBus) {
        try {
            Class<?> overlayEventClass = Class.forName(OVERLAY_EVENT_CLASS);
            Method addListener = IEventBus.class.getMethod("addListener", Class.class, Consumer.class);
            Consumer<Object> handler = TongsHotbarRenderer::onAddGuiOverlayLayers;
            addListener.invoke(modEventBus, overlayEventClass, handler);
        } catch (ClassNotFoundException ignored) {
            // Forge 52.1.0: no overlay registration API yet.
        } catch (ReflectiveOperationException e) {
            materia.LOGGER.warn("Failed to register tongs hotbar overlay listener", e);
        }
    }

    private static void onAddGuiOverlayLayers(Object event) {
        try {
            Object layeredDraw = event.getClass().getMethod("getLayeredDraw").invoke(event);
            Class<?> layeredDrawClass = Class.forName(LAYERED_DRAW_CLASS);
            ResourceLocation root = (ResourceLocation) layeredDrawClass.getField("VANILLA_ROOT").get(null);
            ResourceLocation hotbar = (ResourceLocation) layeredDrawClass.getField("HOTBAR").get(null);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "tongs_hotbar");

            BiConsumer<GuiGraphics, DeltaTracker> renderer = TongsHotbarRenderer::render;
            Method addAbove = findAddAbove(layeredDraw.getClass());
            if (addAbove == null) {
                materia.LOGGER.warn("Could not register tongs hotbar overlay: ForgeLayeredDraw.addAbove missing");
                return;
            }
            addAbove.invoke(layeredDraw, root, hotbar, id, renderer);
        } catch (ReflectiveOperationException e) {
            materia.LOGGER.warn("Failed to register tongs hotbar overlay", e);
        }
    }

    private static Method findAddAbove(Class<?> layeredDrawClass) {
        for (Method method : layeredDrawClass.getMethods()) {
            if (!"addAbove".equals(method.getName()) || method.getParameterCount() != 4) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (ResourceLocation.class.isAssignableFrom(params[0])
                    && ResourceLocation.class.isAssignableFrom(params[1])
                    && ResourceLocation.class.isAssignableFrom(params[2])) {
                return method;
            }
        }
        return null;
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }

        Font font = minecraft.font;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            var tongsCapOptional = stack.getCapability(TongsCapability.TONGS_CAPABILITY);
            if (tongsCapOptional.isPresent()) {
                var tongsCap = tongsCapOptional.resolve().get();
                int itemCount = tongsCap.getTotalItemCount();

                if (itemCount > 0) {
                    int screenWidth = minecraft.getWindow().getGuiScaledWidth();
                    int screenHeight = minecraft.getWindow().getGuiScaledHeight();

                    int hotbarX = screenWidth / 2 - 91;
                    int hotbarY = screenHeight - 22;

                    int slotX = hotbarX + i * 20 + 6;
                    int slotY = hotbarY + 6;

                    String countText = String.valueOf(itemCount);
                    int textX = slotX + 17 - font.width(countText);
                    int textY = slotY + 9;

                    graphics.pose().pushPose();
                    graphics.pose().translate(0, 0, 200);
                    graphics.drawString(font, countText, textX, textY, 0xFFFFFF, true);
                    graphics.pose().popPose();
                }
            }
        }
    }
}
