package com.torr.materia.compatibility;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

/**
 * Remaps registry IDs from older Materia builds when loading existing worlds.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LegacyRegistryMappings {

    private static final ResourceLocation FURNACE_KILN_BLOCK =
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "furnace_kiln");
    private static final ResourceLocation FURNACE_KILN_MENU =
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "furnace_kiln_menu");

    private LegacyRegistryMappings() {
    }

    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        if (event.getRegistry() == ForgeRegistries.BLOCKS) {
            event.getMappings(ForgeRegistries.BLOCKS.getRegistryKey(), FURNACE_KILN_BLOCK.toString())
                    .forEach(mapping -> mapping.remap(ModBlocks.FORGE.get()));
        } else if (event.getRegistry() == ForgeRegistries.ITEMS) {
            event.getMappings(ForgeRegistries.ITEMS.getRegistryKey(), FURNACE_KILN_BLOCK.toString())
                    .forEach(mapping -> mapping.remap(ModBlocks.FORGE.get().asItem()));
        } else if (event.getRegistry() == ForgeRegistries.MENU_TYPES) {
            event.getMappings(ForgeRegistries.MENU_TYPES.getRegistryKey(), FURNACE_KILN_MENU.toString())
                    .forEach(mapping -> mapping.remap(ModMenuTypes.FORGE_MENU.get()));
        }
    }
}
