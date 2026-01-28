package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.network.NetworkHandler;
import com.torr.materia.network.SheepColorSyncPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class VanillaDyePassHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        // Only main hand
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!(event.getTarget() instanceof Sheep sheep)) return;

        Item item = event.getItemStack().getItem();
        if (!isVanillaDye(item)) return; // Only care about vanilla dyes

        if (!event.getWorld().isClientSide) {
            var capOpt = sheep.getCapability(CustomSheepColorCapability.CUSTOM_SHEEP_COLOR);
            capOpt.ifPresent(cap -> {
                if (cap.getCustomColor() != null) {
                    // Clear custom color so rendering falls back to vanilla
                    cap.setCustomColor(null);
                    // Sync to tracking clients
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> sheep), new SheepColorSyncPacket(sheep.getId(), null));
                }
            });
        }
        // Do NOT cancel; let vanilla handle dyeing
    }

    private static boolean isVanillaDye(Item item) {
        return item == Items.WHITE_DYE || item == Items.ORANGE_DYE || item == Items.MAGENTA_DYE ||
               item == Items.LIGHT_BLUE_DYE || item == Items.YELLOW_DYE || item == Items.LIME_DYE ||
               item == Items.PINK_DYE || item == Items.GRAY_DYE || item == Items.LIGHT_GRAY_DYE ||
               item == Items.CYAN_DYE || item == Items.PURPLE_DYE || item == Items.BLUE_DYE ||
               item == Items.BROWN_DYE || item == Items.GREEN_DYE || item == Items.RED_DYE ||
               item == Items.BLACK_DYE;
    }
}
