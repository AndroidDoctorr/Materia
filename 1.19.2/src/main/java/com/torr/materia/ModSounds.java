package com.torr.materia;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, materia.MOD_ID);

    // Knapping stone/flint/bone
    public static final RegistryObject<SoundEvent> HAMMER_KNAP = SOUNDS.register(
            "item.hammer_stone.knap",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.hammer_stone.knap")));

    // Crafting with a hand axe
    public static final RegistryObject<SoundEvent> AXE_CRAFT = SOUNDS.register(
            "item.axe.craft",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.axe.craft")));

    // Crafting with a flint knife
    public static final RegistryObject<SoundEvent> FLINT_CRAFT = SOUNDS.register(
            "item.flint.craft",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.flint.craft")));

    // Starting a fire with a string bow
    public static final RegistryObject<SoundEvent> BOW_DRILL = SOUNDS.register(
            "item.bow_drill.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.bow_drill.use")));

    // Crafting or cutting with a saw
    public static final RegistryObject<SoundEvent> SAW_CRAFT = SOUNDS.register(
            "item.saw.craft",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.saw.craft")));

    // Maraca sound events
    public static final RegistryObject<SoundEvent> AIR_MARACA = SOUNDS.register(
            "item.air_maraca.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.air_maraca.use")));

    public static final RegistryObject<SoundEvent> EARTH_MARACA = SOUNDS.register(
            "item.earth_maraca.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.earth_maraca.use")));

    public static final RegistryObject<SoundEvent> FIRE_MARACA = SOUNDS.register(
            "item.fire_maraca.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.fire_maraca.use")));

    public static final RegistryObject<SoundEvent> WATER_MARACA = SOUNDS.register(
            "item.water_maraca.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.water_maraca.use")));

    public static final RegistryObject<SoundEvent> LIFE_MARACA = SOUNDS.register(
            "item.life_maraca.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.life_maraca.use")));

    // Flute sound events
    public static final RegistryObject<SoundEvent> AIR_FLUTE = SOUNDS.register(
            "item.air_flute.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.air_flute.use")));

    public static final RegistryObject<SoundEvent> EARTH_FLUTE = SOUNDS.register(
            "item.earth_flute.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.earth_flute.use")));

    public static final RegistryObject<SoundEvent> FIRE_FLUTE = SOUNDS.register(
            "item.fire_flute.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.fire_flute.use")));

    public static final RegistryObject<SoundEvent> WATER_FLUTE = SOUNDS.register(
            "item.water_flute.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.water_flute.use")));

    // Drum sound events
    public static final RegistryObject<SoundEvent> AIR_DRUMS = SOUNDS.register(
            "item.air_drums.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.air_drums.use")));

    public static final RegistryObject<SoundEvent> EARTH_DRUMS = SOUNDS.register(
            "item.earth_drums.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.earth_drums.use")));

    public static final RegistryObject<SoundEvent> FIRE_DRUMS = SOUNDS.register(
            "item.fire_drums.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.fire_drums.use")));

    public static final RegistryObject<SoundEvent> WATER_DRUMS = SOUNDS.register(
            "item.water_drums.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.water_drums.use")));

        
    public static final RegistryObject<SoundEvent> AIR_BASS = SOUNDS.register(
            "item.air_bass.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.air_bass.use")));

    public static final RegistryObject<SoundEvent> EARTH_BASS = SOUNDS.register(
            "item.earth_bass.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.earth_bass.use")));

    public static final RegistryObject<SoundEvent> FIRE_BASS = SOUNDS.register(
            "item.fire_bass.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.fire_bass.use")));

    public static final RegistryObject<SoundEvent> WATER_BASS = SOUNDS.register(
            "item.water_bass.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.water_bass.use")));

    public static final RegistryObject<SoundEvent> AIR_HARP = SOUNDS.register(
            "item.air_harp.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.air_harp.use")));

    public static final RegistryObject<SoundEvent> EARTH_HARP = SOUNDS.register(
            "item.earth_harp.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.earth_harp.use")));

    public static final RegistryObject<SoundEvent> FIRE_HARP = SOUNDS.register(
            "item.fire_harp.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.fire_harp.use")));

    public static final RegistryObject<SoundEvent> WATER_HARP = SOUNDS.register(
            "item.water_harp.use",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.water_harp.use")));

    public static final RegistryObject<SoundEvent> POTTERY_BREAK = SOUNDS.register(
            "item.pottery.break",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.pottery.break")));

    public static final RegistryObject<SoundEvent> WICKER_BASKET = SOUNDS.register(
            "item.basket.open",
            () -> new SoundEvent(new ResourceLocation(materia.MOD_ID, "item.basket.open")));
}
