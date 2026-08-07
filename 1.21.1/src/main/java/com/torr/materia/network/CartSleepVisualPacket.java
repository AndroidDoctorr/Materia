package com.torr.materia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

/** Server → client: start or stop cart sleep visuals. */
public class CartSleepVisualPacket {

    private final boolean sleeping;
    private final int sleepTicks;

    public CartSleepVisualPacket(boolean sleeping, int sleepTicks) {
        this.sleeping = sleeping;
        this.sleepTicks = sleepTicks;
    }

    public CartSleepVisualPacket(FriendlyByteBuf buf) {
        this.sleeping = buf.readBoolean();
        this.sleepTicks = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.sleeping);
        buf.writeVarInt(this.sleepTicks);
    }

    public static void handle(CartSleepVisualPacket msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.isClientSide()) {
                applyClient(msg.sleeping, msg.sleepTicks);
            }
        });
        context.setPacketHandled(true);
    }

    private static void applyClient(boolean sleeping, int sleepTicks) {
        try {
            Class<?> clazz = Class.forName("com.torr.materia.client.CartSleepVisuals");
            if (sleeping) {
                clazz.getMethod("begin", int.class).invoke(null, sleepTicks);
            } else {
                clazz.getMethod("end").invoke(null);
            }
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
