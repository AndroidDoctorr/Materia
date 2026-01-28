package com.torr.materia.network;

import com.torr.materia.client.screen.CannonAimScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

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

    public static void handle(CannonStartAimPacket msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.isClientSide()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level == null) return;
                mc.setScreen(new CannonAimScreen(msg.pos));
            }
        });
        context.setPacketHandled(true);
    }
}

