package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.capability.HotMetalCapability;
import com.torr.materia.capability.TongsCapability;
import com.torr.materia.capability.GlassPipeCapability;
import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CapabilityEventHandler {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        
        // Check if this item can be heated (we'll implement tag checking later)
        if (canBeHeated(stack)) {
            event.addCapability(new ResourceLocation(materia.MOD_ID, "hot_metal"), 
                new HotMetalCapability.Provider());
        }
        
        // Check if this item is tongs
        if (isTongs(stack)) {
            event.addCapability(new ResourceLocation(materia.MOD_ID, "tongs"), 
                new TongsCapability.Provider());
        }
        
        // Check if this item is a steel pipe
        if (isSteelPipe(stack)) {
            event.addCapability(new ResourceLocation(materia.MOD_ID, "glass_pipe"), 
                new GlassPipeCapability.Provider());
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesToEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Sheep) {
            event.addCapability(CustomSheepColorCapability.ID, 
                new CustomSheepColorCapability.Provider());
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HotMetalCapability.class);
        event.register(TongsCapability.class);
        event.register(GlassPipeCapability.class);
        event.register(CustomSheepColorCapability.class);
    }

    private static final TagKey<Item> HEATABLE_METALS = ItemTags.create(new ResourceLocation(materia.MOD_ID, "heatable_metals"));

    private static boolean canBeHeated(ItemStack stack) {
        // Explicitly check for glass puck first
        if (stack.getItem() == ModItems.GLASS_PUCK.get()) {
            return true;
        }
        // Then check tag
        return stack.is(HEATABLE_METALS);
    }
    
    private static boolean isTongs(ItemStack stack) {
        return stack.getItem() == ModItems.WOOD_TONGS.get() ||
               stack.getItem() == ModItems.BRONZE_TONGS.get() ||
               stack.getItem() == ModItems.IRON_TONGS.get() ||
               stack.getItem() == ModItems.STEEL_TONGS.get();
    }
    
    private static boolean isSteelPipe(ItemStack stack) {
        return stack.getItem() == ModItems.STEEL_PIPE.get();
    }
}
