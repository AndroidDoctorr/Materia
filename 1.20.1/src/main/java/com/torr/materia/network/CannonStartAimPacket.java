package com.torr.materia.network;

import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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
            if (context.getDirection() != null && context.getDirection().getReceptionSide().isClient()) {
                materia.LOGGER.info("CannonStartAimPacket received on client for pos {}", pos);
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    com.torr.materia.client.CannonAimClient.open(pos);
                });
            }
        });
        context.setPacketHandled(true);
    }
}

