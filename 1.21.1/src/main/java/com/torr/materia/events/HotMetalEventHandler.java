package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.capability.HotMetalCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class HotMetalEventHandler {
    
	public static final TagKey<Item> HEATABLE_METALS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "heatable_metals"));

    public static void heatMetalItem(ItemStack stack) {
        if (stack.is(HEATABLE_METALS)) {
            stack.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY).ifPresent(hotCapability -> {
                hotCapability.heat();
            });
        }
    }
}
