package com.torr.materia.network;

import com.torr.materia.block.CannonBlock;
import com.torr.materia.blockentity.CannonBlockEntity;
import com.torr.materia.util.CannonFireLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CannonFirePacket {
    private final BlockPos pos;
    private final float yaw;
    private final float pitch;

    public CannonFirePacket(BlockPos pos, float yaw, float pitch) {
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public CannonFirePacket(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        yaw = buf.readFloat();
        pitch = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer sender = context.getSender();
        if (sender == null) {
            context.setPacketHandled(true);
            return;
        }

        context.enqueueWork(() -> fire(sender));
        context.setPacketHandled(true);
    }

    private void fire(ServerPlayer player) {
        Level level = player.getLevel();
        if (player.blockPosition().distSqr(pos) > 64 * 64) return;

        BlockState state = level.getBlockState(pos);
        if (!state.hasProperty(CannonBlock.FACING)) return;

        var be = level.getBlockEntity(pos);
        if (!(be instanceof CannonBlockEntity cannonBe)) return;
        CannonFireLogic.fire(level, pos, state, cannonBe, player, yaw, pitch);
    }
}

