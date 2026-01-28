package com.torr.materia.events;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class BedrollSpawnHandler {

    @SubscribeEvent
    public static void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (event.isForced()) { // Only interested in forced spawn points (beds)
            Player player = event.getEntity();
            Level level = player.getLevel();
            BlockPos spawnPos = event.getNewSpawn();

            if (spawnPos != null && level.getBlockState(spawnPos).is(ModBlocks.BEDROLL.get())) {
                // If the new spawn point is a bedroll, cancel the event
                event.setCanceled(true);
            }
        }
    }
}
