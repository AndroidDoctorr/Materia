package com.torr.materia.network;

import com.torr.materia.blockentity.CannonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class CannonUpdateAimPacket {
    private final BlockPos pos;
    private final float yaw;
    private final float pitch;

    public CannonUpdateAimPacket(BlockPos pos, float yaw, float pitch) {
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public CannonUpdateAimPacket(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        yaw = buf.readFloat();
        pitch = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
    }

    public static void handle(CannonUpdateAimPacket msg, CustomPayloadEvent.Context context) {
        ServerPlayer sender = context.getSender();
        if (sender == null) {
            context.setPacketHandled(true);
            return;
        }

        context.enqueueWork(() -> {
            var level = sender.level();
            if (sender.blockPosition().distSqr(msg.pos) > 64 * 64) return;
            var be = level.getBlockEntity(msg.pos);
            if (!(be instanceof CannonBlockEntity cannonBe)) return;

            cannonBe.setAimDegrees(msg.yaw, msg.pitch);
            level.sendBlockUpdated(msg.pos, cannonBe.getBlockState(), cannonBe.getBlockState(), 3);
        });

        context.setPacketHandled(true);
    }
}

