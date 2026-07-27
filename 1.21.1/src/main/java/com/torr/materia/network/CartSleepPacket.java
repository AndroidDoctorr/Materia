package com.torr.materia.network;

import com.torr.materia.menu.CartMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class CartSleepPacket {
    private final int cartId;

    public CartSleepPacket(int cartId) {
        this.cartId = cartId;
    }

    public CartSleepPacket(FriendlyByteBuf buf) {
        this.cartId = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.cartId);
    }

    public static void handle(CartSleepPacket msg, CustomPayloadEvent.Context context) {
        ServerPlayer sender = context.getSender();
        if (sender == null) {
            context.setPacketHandled(true);
            return;
        }

        if (sender.containerMenu instanceof CartMenu menu && menu.getCart().getId() == msg.cartId) {
            menu.getCart().trySleep(sender);
        }
        context.setPacketHandled(true);
    }
}
