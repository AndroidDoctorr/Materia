package com.torr.materia.network;

import com.torr.materia.menu.CartMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer sender = context.getSender();
        if (sender == null) {
            context.setPacketHandled(true);
            return;
        }

        context.enqueueWork(() -> {
            if (sender.containerMenu instanceof CartMenu menu && menu.getCart().getId() == this.cartId) {
                menu.getCart().trySleep(sender);
            }
        });
        context.setPacketHandled(true);
    }
}
