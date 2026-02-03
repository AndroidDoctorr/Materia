package com.torr.materia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public class CannonStartAimPacket {
    private final BlockPos pos;

    public CannonStartAimPacket(BlockPos pos) {
        this.pos = pos;
    }

    public CannonStartAimPacket(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                openCannonAimScreenClient(pos);
            }
        });
        context.setPacketHandled(true);
    }

    /**
     * Dedicated-server-safe: uses reflection so this class can load on server.
     */
    private static void openCannonAimScreenClient(BlockPos pos) {
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

            Class<?> screenClass = Class.forName("com.torr.materia.client.screen.CannonAimScreen");
            Constructor<?> ctor = screenClass.getConstructor(BlockPos.class);
            Object screen = ctor.newInstance(pos);

            // Avoid referencing Screen type directly; pick setScreen(…) by name.
            for (var m : mcClass.getMethods()) {
                if (!m.getName().equals("setScreen")) continue;
                if (m.getParameterCount() != 1) continue;
                m.invoke(mc, screen);
                break;
            }
        } catch (Throwable ignored) {
            // If anything fails (missing client classes, bad mappings), just don't open the screen.
        }
    }
}

