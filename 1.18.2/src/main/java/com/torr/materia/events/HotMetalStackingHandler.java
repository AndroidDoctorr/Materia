package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class HotMetalStackingHandler {
    
    /**
     * Check and convert cooled items back to regular versions
     * Now excludes items in kilns and tongs
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level.isClientSide) return;
        
        // Check every 20 ticks (1 second)
        if (event.player.tickCount % 20 != 0) return;
        
        Player player = event.player;
        
        // Only cool items in player inventory (excluding tongs contents)
        HotMetalStackingUtils.convertCooledItemsSelective(player.getInventory(), true);
    }
}
