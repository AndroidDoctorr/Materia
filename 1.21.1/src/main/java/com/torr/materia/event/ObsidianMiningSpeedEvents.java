package com.torr.materia.event;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ObsidianMiningSpeedEvents {
    private ObsidianMiningSpeedEvents() {}

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        var state = event.getState();
        if (!(state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(ModBlocks.OBSIDIAN_SLAB.get()))) {
            return;
        }

        // Keep wrought iron pickaxe viable (~1s). Make vanilla tiers faster than wrought iron for progression.
        ItemStack tool = event.getEntity().getMainHandItem();

        float minSpeed;
        if (tool.is(Items.NETHERITE_PICKAXE)) {
            minSpeed = 120.0F;
        } else if (tool.is(Items.DIAMOND_PICKAXE)) {
            minSpeed = 110.0F;
        } else if (tool.is(Items.IRON_PICKAXE)) {
            minSpeed = 90.0F;
        } else {
            return;
        }

        // Don't slow down other sources of speed (effects/enchantments/custom tools)
        event.setNewSpeed(Math.max(event.getNewSpeed(), minSpeed));
    }
}

