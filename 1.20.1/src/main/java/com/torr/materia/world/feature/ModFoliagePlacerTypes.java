package com.torr.materia.world.feature;

import com.torr.materia.materia;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFoliagePlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES =
            DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, materia.MOD_ID);

    public static final RegistryObject<FoliagePlacerType<OliveFoliagePlacer>> OLIVE_FOLIAGE_PLACER =
            FOLIAGE_PLACER_TYPES.register("olive_foliage_placer", 
                    () -> new FoliagePlacerType<>(OliveFoliagePlacer.CODEC));

    public static void register(IEventBus eventBus) {
        FOLIAGE_PLACER_TYPES.register(eventBus);
    }
}
