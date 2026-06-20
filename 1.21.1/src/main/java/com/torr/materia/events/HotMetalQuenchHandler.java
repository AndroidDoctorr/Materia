package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

/**
 * Drops of heatable metal cool instantly when submerged in vanilla water fluid.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class HotMetalQuenchHandler {

    private static final AABB ITEM_PROBE_BB = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D).inflate(3.08e7D);

    private HotMetalQuenchHandler() {}

    @SubscribeEvent
    public static void onLevelTickEnd(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.SERVER) {
            return;
        }
        if (!(event.level instanceof ServerLevel sl)) {
            return;
        }
        for (ItemEntity itemEntity : sl.getEntitiesOfClass(ItemEntity.class, ITEM_PROBE_BB)) {
            if (!itemEntity.isAlive() || itemEntity.getItem().isEmpty() || !itemEntity.isInWater()) {
                continue;
            }
            HotMetalStackingUtils.quenchHeatableIfHeated(itemEntity.getItem()).ifPresent(cooled -> {
                itemEntity.setItem(cooled);
                sl.playSound(null, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                        SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.6F,
                        1.9F + sl.random.nextFloat() * 0.12F);
            });
        }
    }
}
