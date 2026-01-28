package com.torr.materia.client;

import com.torr.materia.materia;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BedTextureStitcher {
    
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.BED_SHEET)) {
            // Register all custom bed textures
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/ochre"));
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/red_ochre"));
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/indigo"));
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/olive"));
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/tyrian_purple"));
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/lavender"));
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/charcoal_gray"));
            event.addSprite(new ResourceLocation(materia.MOD_ID, "entity/bed/taupe"));
        }
    }
}
