package com.torr.materia.network;

import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SheepColorSyncPacket {
    private final int entityId;
    private final String colorName;

    public SheepColorSyncPacket(int entityId, CustomSheepColor color) {
        this.entityId = entityId;
        this.colorName = color != null ? color.name() : "";
    }

    public SheepColorSyncPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.colorName = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeUtf(colorName);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Client-side handling
            if (context.getDirection().getReceptionSide().isClient()) {
                applyClient(entityId, colorName);
            }
        });
        context.setPacketHandled(true);
    }

    /**
     * Dedicated-server-safe: uses reflection so this class can load on server.
     */
    private static void applyClient(int entityId, String colorName) {
        try {
            Class<?> mcClass = Class.forName("net.minecraft.client.Minecraft");
            Object mc = mcClass.getMethod("getInstance").invoke(null);
            Object level = null;
            try {
                level = mcClass.getField("level").get(mc);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // fall through to declared-field attempt below
            }
            if (level == null) {
                try {
                    var f = mcClass.getDeclaredField("level");
                    f.setAccessible(true);
                    level = f.get(mc);
                } catch (Throwable ignored) {
                    level = null;
                }
            }
            if (level == null) return;

            // ClientLevel#getEntity(int)
            var getEntity = level.getClass().getMethod("getEntity", int.class);
            Object entityObj = getEntity.invoke(level, entityId);
            if (!(entityObj instanceof Entity entity)) return;
            if (!(entity instanceof Sheep sheep)) return;

            var capOptional = sheep.getCapability(CustomSheepColorCapability.CUSTOM_SHEEP_COLOR);
            if (!capOptional.isPresent()) return;
            var capability = capOptional.resolve().get();
            CustomSheepColor color = colorName.isEmpty() ? null : CustomSheepColor.valueOf(colorName);
            capability.setCustomColor(color);
        } catch (Throwable ignored) {
            // Ignore if anything fails (e.g. client classes not present).
        }
    }
}
