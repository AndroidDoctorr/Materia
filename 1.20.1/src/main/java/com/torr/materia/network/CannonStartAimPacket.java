package com.torr.materia.network;

import com.torr.materia.client.screen.CannonAimScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
                Minecraft mc = Minecraft.getInstance();
                if (mc.level == null) return;
                mc.setScreen(new CannonAimScreen(pos));
            }
        });
        context.setPacketHandled(true);
    }
}

