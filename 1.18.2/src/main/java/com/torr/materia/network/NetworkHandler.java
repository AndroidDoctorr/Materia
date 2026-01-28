package com.torr.materia.network;

import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(materia.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, SheepColorSyncPacket.class, 
            SheepColorSyncPacket::encode, 
            SheepColorSyncPacket::new, 
            SheepColorSyncPacket::handle);

        INSTANCE.registerMessage(id++, CannonStartAimPacket.class,
            CannonStartAimPacket::encode,
            CannonStartAimPacket::new,
            CannonStartAimPacket::handle);
        INSTANCE.registerMessage(id++, CannonUpdateAimPacket.class,
            CannonUpdateAimPacket::encode,
            CannonUpdateAimPacket::new,
            CannonUpdateAimPacket::handle);
        INSTANCE.registerMessage(id++, CannonFirePacket.class,
            CannonFirePacket::encode,
            CannonFirePacket::new,
            CannonFirePacket::handle);
    }
}
