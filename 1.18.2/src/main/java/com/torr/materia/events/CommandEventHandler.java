package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.commands.HeatMetalCommand;
import com.torr.materia.commands.TestHeatCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CommandEventHandler {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        HeatMetalCommand.register(event.getDispatcher());
        TestHeatCommand.register(event.getDispatcher());
    }
}
