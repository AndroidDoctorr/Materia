package com.torr.materia.world;

import com.torr.materia.materia;
import com.torr.materia.world.feature.ModPlacedFeatures;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Forge 1.18.2 does not load {@code data/materia/forge/biome_modifier/*} the way 1.19+ does.
 * Worldgen is injected here, mirroring the shared biome modifier JSON via biome tags.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class ModWorldEvents {

    private static final TagKey<Biome> OVERWORLD = tag("overworld");
    private static final TagKey<Biome> OVERWORLD_NON_RIVER = tag("overworld_non_river");
    private static final TagKey<Biome> OVERWORLD_NON_ROCKY = tag("overworld_non_rocky");
    private static final TagKey<Biome> OVERWORLD_LAND_NON_ROCKY = tag("overworld_land_non_rocky");
    private static final TagKey<Biome> ROCKY = tag("rocky");
    private static final TagKey<Biome> RIVER = tag("river");
    private static final TagKey<Biome> BEACH = tag("beach");
    private static final TagKey<Biome> DESERT = tag("desert");
    private static final TagKey<Biome> GRASSY = tag("grassy");
    private static final TagKey<Biome> PRAIRIE = tag("prairie");
    private static final TagKey<Biome> TEMPERATE = tag("temperate");
    private static final TagKey<Biome> TEMPERATE_BOREAL = tag("temperate_boreal");
    private static final TagKey<Biome> TEMPERATE_FOREST = tag("temperate_forest");
    private static final TagKey<Biome> SUBTROPICAL = tag("subtropical");
    private static final TagKey<Biome> TROPICAL = tag("tropical");
    private static final TagKey<Biome> WARM_WET_SURFACE = tag("warm_wet_surface");

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (matchesTag(event, OVERWORLD)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.EARTH_SUBSOIL_PLACED,
                    ModPlacedFeatures.MALACHITE_ORE_PLACED,
                    ModPlacedFeatures.SURFACE_IRON_ORE_PLACED,
                    ModPlacedFeatures.OCHRE_CLAY_PLACED,
                    ModPlacedFeatures.RED_OCHRE_CLAY_PLACED,
                    ModPlacedFeatures.SPHALERITE_ORE_PLACED,
                    ModPlacedFeatures.SPHALERITE_CAVE_PLACED,
                    ModPlacedFeatures.SULFUR_ORE_PLACED,
                    ModPlacedFeatures.SULFUR_ORE_LAVA_PLACED,
                    ModPlacedFeatures.MARBLE_VEIN_RARE_PLACED,
                    ModPlacedFeatures.MARBLE_VEIN_SURFACE_PLACED,
                    ModPlacedFeatures.LIMESTONE_VEIN_RARE_PLACED,
                    ModPlacedFeatures.MARBLE_TEST_PLACED,
                    ModPlacedFeatures.LIMESTONE_TEST_PLACED);
        }

        if (matchesTag(event, OVERWORLD_NON_RIVER)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.GRAVEL_TIN_ORE_PLACED);
        }
        if (matchesTag(event, RIVER)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.GRAVEL_TIN_ORE_RIVER_PLACED);
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SURFACE_EARTH_RIVER_PLACED);
        }

        if (matchesTag(event, OVERWORLD_LAND_NON_ROCKY)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacedFeatures.SURFACE_ROCK_PLACED);
        }
        if (matchesTag(event, OVERWORLD_NON_ROCKY)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacedFeatures.CAVE_ROCK_PLACED);
            add(event, GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MAGNETITE_ORE_PLACED);
        }
        if (matchesTag(event, ROCKY)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                    ModPlacedFeatures.SURFACE_ROCK_ROCKY_PLACED,
                    ModPlacedFeatures.CAVE_ROCK_ROCKY_PLACED);
            add(event, GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.LIMESTONE_SURFACE_BIOME_PLACED,
                    ModPlacedFeatures.MAGNETITE_ORE_ROCKY_PLACED);
        }

        if (matchesTag(event, WARM_WET_SURFACE)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.BAUXITE_ORE_BLOB_PLACED);
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.BAUXITE_PATCH_PLACED,
                    ModPlacedFeatures.BAUXITE_PATCH_ON_ORE_PLACED);
        }

        if (matchesTag(event, BEACH)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.MUREX_SHELL_PLACED,
                    ModPlacedFeatures.CLAM_PLACED);
        }
        if (matchesTag(event, DESERT)) {
            add(event, GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.SALTPETER_SAND_PLACED,
                    ModPlacedFeatures.SALTPETER_SANDSTONE_PLACED);
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.AGAVE_PLACED);
        }

        if (matchesTag(event, GRASSY)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_FLAX_PLACED,
                    ModPlacedFeatures.WILD_SQUASH_PLACED,
                    ModPlacedFeatures.WILD_BEANS_PLACED,
                    ModPlacedFeatures.WILD_PEPPERS_PLACED,
                    ModPlacedFeatures.WILD_CORN_PLACED,
                    ModPlacedFeatures.WILD_COTTON_PLACED);
        }

        if (matchesAny(event, RIVER, PRAIRIE, TEMPERATE)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WILD_RICE_PLACED);
        }
        if (matchesTag(event, PRAIRIE)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.BLUEBONNET_PLACED,
                    ModPlacedFeatures.PURPLE_CONEFLOWER_PLACED);
        }
        if (matchesAny(event, RIVER, TEMPERATE)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WHITE_LILY_PLACED);
        }
        if (matchesAny(event, DESERT, SUBTROPICAL)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.ESPARTO_PLACED,
                    ModPlacedFeatures.YUCCA_PLACED);
        }

        if (matchesTag(event, TEMPERATE_FOREST)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_GRAPE_VINE_PLACED,
                    ModPlacedFeatures.WILD_WISTERIA_VINE_PLACED,
                    ModPlacedFeatures.WILD_HOPS_VINE_PLACED);
        }
        if (matchesTag(event, SUBTROPICAL)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.OLIVE_TREE_PLACED,
                    ModPlacedFeatures.FIG_TREE_PLACED);
        }
        if (matchesAny(event, TEMPERATE, TEMPERATE_BOREAL, TEMPERATE_FOREST)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MAPLE_TREE_PLACED);
        }
        if (matchesAny(event, SUBTROPICAL, TEMPERATE_FOREST)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.CEDAR_TREE_PLACED,
                    ModPlacedFeatures.CYPRESS_TREE_PLACED,
                    ModPlacedFeatures.TEA_BUSH_PLACED);
        }
        if (matchesAny(event, SUBTROPICAL, TROPICAL)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.EUCALYPTUS_GROVE_PLACED);
        }
        if (matchesAny(event, TROPICAL, TEMPERATE_FOREST)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FUCHSIA_PLACED);
        }
        if (matchesAny(event, BEACH, TROPICAL)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALM_TREE_PLACED);
        }
        if (matchesAny(event, RIVER, WARM_WET_SURFACE)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.LOTUS_PLACED,
                    ModPlacedFeatures.REEDS_PLACED);
        }

        if (matchesTag(event, TROPICAL)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.BAOBAB_TREE_PLACED,
                    ModPlacedFeatures.HIBISCUS_PLACED,
                    ModPlacedFeatures.INDIGO_PLACED,
                    ModPlacedFeatures.RUBBER_TREE_PLACED,
                    ModPlacedFeatures.PLANTAIN_PLACED,
                    ModPlacedFeatures.RAINBOW_EUCALYPTUS_GROVE_PLACED,
                    ModPlacedFeatures.TARO_CROP_PLACED);
        }
        if (matchesAny(event, DESERT, TROPICAL)) {
            add(event, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MARIGOLD_PLACED);
        }
    }

    private static TagKey<Biome> tag(String path) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(materia.MOD_ID, path));
    }

    private static boolean matchesTag(BiomeLoadingEvent event, TagKey<Biome> tag) {
        ResourceLocation id = event.getName();
        if (id == null) {
            return false;
        }
        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, id);
        return ForgeRegistries.BIOMES.getHolder(key).map(holder -> holder.is(tag)).orElse(false);
    }

    @SafeVarargs
    private static boolean matchesAny(BiomeLoadingEvent event, TagKey<Biome>... tags) {
        for (TagKey<Biome> tag : tags) {
            if (matchesTag(event, tag)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    private static void add(BiomeLoadingEvent event, GenerationStep.Decoration step,
                            RegistryObject<PlacedFeature>... features) {
        for (RegistryObject<PlacedFeature> feature : features) {
            event.getGeneration().addFeature(step, feature.getHolder().get());
        }
    }
}
