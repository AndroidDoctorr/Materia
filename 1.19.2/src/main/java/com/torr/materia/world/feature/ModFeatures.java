package com.torr.materia.world.feature;

import com.torr.materia.materia;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

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

    public static final RegistryObject<Feature<?>> PALM_TREE_FEATURE = FEATURES.register("palm_tree_feature",
            () -> new PalmTreeFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> CYPRESS_TREE_FEATURE = FEATURES.register("cypress_tree_feature",
            () -> new CypressTreeFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> EUCALYPTUS_TREE_FEATURE = FEATURES.register("eucalyptus_tree_feature",
            () -> new EucalyptusTreeFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> EUCALYPTUS_GROVE_FEATURE = FEATURES.register("eucalyptus_grove_feature",
            () -> new EucalyptusGroveFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE), false));

    public static final RegistryObject<Feature<?>> RAINBOW_EUCALYPTUS_GROVE_FEATURE = FEATURES.register("rainbow_eucalyptus_grove_feature",
            () -> new EucalyptusGroveFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE), true));

    public static final RegistryObject<Feature<?>> RAINBOW_EUCALYPTUS_TREE_FEATURE = FEATURES.register("rainbow_eucalyptus_tree_feature",
            () -> new RainbowEucalyptusTreeFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> TALL_PLANT_CLUSTER_FEATURE = FEATURES.register("tall_plant_cluster_feature",
            () -> new TallPlantClusterFeature(SimpleBlockConfiguration.CODEC));

    public static final RegistryObject<Feature<?>> REED_CLUSTER_FEATURE = FEATURES.register("reed_cluster_feature",
            () -> new ReedClusterFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> LOTUS_WATER_PATCH_FEATURE = FEATURES.register("lotus_water_patch_feature",
            () -> new LotusWaterPatchFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> TARO_PATCH_FEATURE = FEATURES.register("taro_patch_feature",
            () -> new TaroPatchFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> EARTH_SUBSOIL_FEATURE = FEATURES.register("earth_subsoil_feature",
            () -> new EarthSubsoilFeature(com.mojang.serialization.Codec.unit(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE)));

    public static final RegistryObject<Feature<?>> LOOSE_GROUND_BLOCK_FEATURE = FEATURES.register("loose_ground_block",
            () -> new LooseGroundBlockFeature(SimpleBlockConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
