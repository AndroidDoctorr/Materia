package com.torr.materia.network;

import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class NetworkHandler {
    private static final int PROTOCOL_VERSION = 1;
    
    public static final SimpleChannel INSTANCE = ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .acceptedVersions((status, remoteVersion) -> remoteVersion == PROTOCOL_VERSION)
            .simpleChannel();
    
    public static void register() {
        INSTANCE.messageBuilder(SheepColorSyncPacket.class)
                .encoder(SheepColorSyncPacket::encode)
                .decoder(SheepColorSyncPacket::new)
                .consumerMainThread(SheepColorSyncPacket::handle)
                .add();

        INSTANCE.messageBuilder(CannonStartAimPacket.class)
                .encoder(CannonStartAimPacket::encode)
                .decoder(CannonStartAimPacket::new)
                .consumerMainThread(CannonStartAimPacket::handle)
                .add();

        INSTANCE.messageBuilder(CannonUpdateAimPacket.class)
                .encoder(CannonUpdateAimPacket::encode)
                .decoder(CannonUpdateAimPacket::new)
                .consumerMainThread(CannonUpdateAimPacket::handle)
                .add();

        INSTANCE.messageBuilder(CannonFirePacket.class)
                .encoder(CannonFirePacket::encode)
                .decoder(CannonFirePacket::new)
                .consumerMainThread(CannonFirePacket::handle)
                .add();
    }
}
