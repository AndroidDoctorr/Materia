package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class VanillaSpawnPointBlocker {
    @SubscribeEvent
    public static void onSetSpawn(PlayerSetSpawnEvent event) {
        BlockPos pos = event.getNewSpawn();
        if (pos == null) return;
        Player player = event.getEntity();
        BlockState state = player.level().getBlockState(pos);
        if (state.getBlock() == ModBlocks.BEDROLL.get()) {
            // Prevent setting spawn on bedrolls
            event.setCanceled(true);
        }
    }
}


