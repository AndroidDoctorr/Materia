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
    public static void onTextureStitch(TextureStitchEvent.Post event) {
        // 1.20.1 Forge exposes TextureStitchEvent.Post (no Pre). For now we rely on
        // textures being pulled in by normal usage; if any custom bed textures fail
        // to load, we'll revisit with the proper atlas/sprite registration hook.
        if (event.getAtlas().location().equals(Sheets.BED_SHEET)) {
            // no-op
            new ResourceLocation(materia.MOD_ID, "entity/bed/ochre");
        }
    }
}
