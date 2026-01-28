package com.torr.materia.events;

import com.torr.materia.ModItems;
import com.torr.materia.ModSounds;
import com.torr.materia.materia;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CraftingSoundHandler {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent e) {
        // Scan crafting grid for hammer stone
        for (int i = 0; i < e.getInventory().getContainerSize(); i++) {
            ItemStack ingredient = e.getInventory().getItem(i);
            if (!ingredient.isEmpty() && ingredient.getItem() == ModItems.HAMMER_STONE.get()) {
                // Play custom hammer-knap sound
                e.getPlayer().level.playSound(
                        null,
                        e.getPlayer().blockPosition(),
                        ModSounds.HAMMER_KNAP.get(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f);
                break;
            } else if (!ingredient.isEmpty() && ingredient.getItem() == ModItems.HAND_AXE.get()) {
                // Play custom axe-craft sound
                e.getPlayer().level.playSound(
                        null,
                        e.getPlayer().blockPosition(),
                        ModSounds.AXE_CRAFT.get(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f);
            } else if (!ingredient.isEmpty() && ingredient.getItem() == ModItems.FLINT_KNIFE.get()) {
                // Play custom flint-craft sound
                e.getPlayer().level.playSound(
                        null,
                        e.getPlayer().blockPosition(),
                        ModSounds.FLINT_CRAFT.get(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f);
                break;
            } else if (!ingredient.isEmpty() && ingredient.getItem() == ModItems.BRONZE_SAW.get()) {
                // Play custom saw crafting sound
                e.getPlayer().level.playSound(
                        null,
                        e.getPlayer().blockPosition(),
                        ModSounds.SAW_CRAFT.get(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f);
                break;
            } else if (!ingredient.isEmpty() && ingredient.getItem() == ModItems.BOW_DRILL.get()) {
                // Play custom bow-drill crafting sound
                e.getPlayer().level.playSound(
                        null,
                        e.getPlayer().blockPosition(),
                        ModSounds.BOW_DRILL.get(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f);
                break;
            }
        }
    }
}