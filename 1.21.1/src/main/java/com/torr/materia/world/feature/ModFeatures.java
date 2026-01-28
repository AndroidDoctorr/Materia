package com.torr.materia.world.feature;

import com.torr.materia.materia;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, materia.MOD_ID);

    public static final RegistryObject<Feature<?>> MUREX_SHELL_FEATURE = FEATURES.register("murex_shell_feature",
            () -> new MurexShellFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> CLAM_FEATURE = FEATURES.register("clam_feature",
            () -> new ClamFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> WILD_GRAPE_VINE_FEATURE = FEATURES.register("wild_grape_vine_feature",
            () -> new WildGrapeVineFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> WILD_WISTERIA_VINE_FEATURE = FEATURES.register("wild_wisteria_vine_feature",
            () -> new WildWisteriaVineFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> WILD_HOPS_VINE_FEATURE = FEATURES.register("wild_hops_vine_feature",
            () -> new WildHopsVineFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
