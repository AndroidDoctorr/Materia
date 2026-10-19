package com.torr.materia.compatibility;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Remaps registry IDs from older Materia builds when loading existing worlds.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LegacyRegistryMappings {

    private static final ResourceLocation FURNACE_KILN_BLOCK =
            new ResourceLocation(materia.MOD_ID, "furnace_kiln");
    private static final ResourceLocation FURNACE_KILN_MENU =
            new ResourceLocation(materia.MOD_ID, "furnace_kiln_menu");

    private LegacyRegistryMappings() {
    }

    @SubscribeEvent
    public static void onMissingBlocks(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings(materia.MOD_ID)) {
            if (FURNACE_KILN_BLOCK.equals(mapping.key)) {
                mapping.remap(ModBlocks.FORGE.get());
            }
        }
    }

    @SubscribeEvent
    public static void onMissingItems(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings(materia.MOD_ID)) {
            if (FURNACE_KILN_BLOCK.equals(mapping.key)) {
                mapping.remap(ModBlocks.FORGE.get().asItem());
            }
        }
    }

    @SubscribeEvent
    public static void onMissingMenus(RegistryEvent.MissingMappings<MenuType<?>> event) {
        for (RegistryEvent.MissingMappings.Mapping<MenuType<?>> mapping : event.getMappings(materia.MOD_ID)) {
            if (FURNACE_KILN_MENU.equals(mapping.key)) {
                mapping.remap(ModMenuTypes.FORGE_MENU.get());
            }
        }
    }
}
