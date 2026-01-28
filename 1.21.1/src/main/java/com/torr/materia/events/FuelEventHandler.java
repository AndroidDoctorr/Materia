package com.torr.materia.events;

import com.torr.materia.materia;
import com.torr.materia.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class FuelEventHandler {
    
    // Define fuel tags
    private static final TagKey<Item> ROUGH_PLANKS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "rough_planks"));
    private static final TagKey<Item> SMOOTH_PLANKS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "smooth_planks"));
    private static final TagKey<Item> POSTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "posts"));
    private static final TagKey<Item> SAPLINGS = ItemTags.SAPLINGS;
    
    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        
        // Redstone Fuel - Premium fuel (16 smelts)
        if (item == ModItems.REDSTONE_FUEL.get()) {
            event.setBurnTime(2400); // 50% better than charcoal (12 smelts)
        }
        else if (item == ModItems.COAL_COKE.get()) {
            event.setBurnTime(3200); // Double vanilla charcoal/coal (16 smelts)
        }
        // Pitch and Tar - Good fuel sources (10 smelts each)
        else if (item == ModItems.PITCH.get() || item == ModItems.TAR.get()) {
            event.setBurnTime(2000); // Between charcoal and redstone_fuel
        }
        // Smooth Planks - Processed wood fuel (3 smelts each)
        else if (itemStack.is(SMOOTH_PLANKS)) {
            event.setBurnTime(600);
        }
        // Rough Planks - Basic wood fuel (2 smelts each)
        else if (itemStack.is(ROUGH_PLANKS)) {
            event.setBurnTime(400);
        }
        // Posts - Like vanilla slabs (1.5 smelts each)
        else if (itemStack.is(POSTS)) {
            event.setBurnTime(300);
        }
        // Sticks - Vanilla value (0.5 smelts)
        else if (item == Items.STICK) {
            event.setBurnTime(100);
        }
        // Saplings - Vanilla value (0.5 smelts)
        else if (itemStack.is(SAPLINGS)) {
            event.setBurnTime(100);
        }
    }
}
