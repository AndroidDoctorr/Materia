package com.torr.materia.network;

import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SheepColorSyncPacket {
    private final int entityId;
    private final String colorName;

    public SheepColorSyncPacket(int entityId, CustomSheepColor color) {
        this.entityId = entityId;
        this.colorName = color != null ? color.name() : "";
    }

    public SheepColorSyncPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.colorName = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeUtf(colorName);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Client-side handling
            if (context.getDirection().getReceptionSide().isClient()) {
                Entity entity = Minecraft.getInstance().level.getEntity(entityId);
                if (entity instanceof Sheep sheep) {
                    var capOptional = sheep.getCapability(CustomSheepColorCapability.CUSTOM_SHEEP_COLOR);
                    if (capOptional.isPresent()) {
                        var capability = capOptional.resolve().get();
                        CustomSheepColor color = colorName.isEmpty() ? null : CustomSheepColor.valueOf(colorName);
                        capability.setCustomColor(color);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
