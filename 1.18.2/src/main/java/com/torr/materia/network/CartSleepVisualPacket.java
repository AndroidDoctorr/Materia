package com.torr.materia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                applyClient(this.sleeping, this.sleepTicks);
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
