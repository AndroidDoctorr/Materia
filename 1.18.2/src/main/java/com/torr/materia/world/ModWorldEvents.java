package com.torr.materia.world;

import com.torr.materia.ModBlocks;
import com.torr.materia.world.feature.ModPlacedFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "materia")
public class ModWorldEvents {
    
    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        // Add tin gravel ore to biomes during loading - alluvial/placer deposits
        if (isRiverBiome(event) || isBeachBiome(event)) {
            // Rivers and beaches get higher rate (alluvial tin deposits)
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.GRAVEL_TIN_ORE_RIVER_PLACED.getHolder().get());
        } else {
            // Other biomes get normal rate
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.GRAVEL_TIN_ORE_PLACED.getHolder().get());
        }

        // Add earth generation under topsoil (all biomes)
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.EARTH_SUBSOIL_PLACED.getHolder().get());

        // Extra surface earth for river biomes
        if (isRiverBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.SURFACE_EARTH_RIVER_PLACED.getHolder().get());
        }
        
        // Add rock generation based on biome type
        if (isRockyBiome(event)) {
            // Rocky biomes (mountains, hills, badlands) get higher rates
            // Move surface rocks to UNDERGROUND_DECORATION to spawn before trees
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION,
                    ModPlacedFeatures.SURFACE_ROCK_ROCKY_PLACED.getHolder().get());
            
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION,
                    ModPlacedFeatures.CAVE_ROCK_ROCKY_PLACED.getHolder().get());
            
            // Add limestone surface biome replacement - creates limestone cliff regions in rocky biomes
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.LIMESTONE_SURFACE_BIOME_PLACED.getHolder().get());
        } else {
            // Normal biomes get standard rates
            // Move surface rocks to UNDERGROUND_DECORATION to spawn before trees
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION,
                    ModPlacedFeatures.SURFACE_ROCK_PLACED.getHolder().get());
            
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION,
                    ModPlacedFeatures.CAVE_ROCK_PLACED.getHolder().get());
        }

        // Add bauxite surface clumps to warm/wet biomes (e.g., jungle, savanna)
        if (isWarmWetSurfaceBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.BAUXITE_PATCH_PLACED.getHolder().get());
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.BAUXITE_PATCH_ON_ORE_PLACED.getHolder().get());
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.BAUXITE_ORE_BLOB_PLACED.getHolder().get());
        }

        // Add indigo patches to tropical/subtropical biomes
        if (isTropicalBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.INDIGO_PLACED.getHolder().get());
        }

        // Add rubber trees to tropical biomes (jungles, savannas)
        if (isTropicalBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.RUBBER_TREE_PLACED.getHolder().get());
        }

        // Add olive trees to temperate biomes (plains, forests, etc.)
        if (isTemperateBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.OLIVE_TREE_PLACED.getHolder().get());
        }

        // Add wild grape vines to forest biomes (temporarily all forests for testing)
        if (isTemperateForestBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_GRAPE_VINE_PLACED.getHolder().get());
        }

        // Add wild hops vines to forest biomes
        if (isTemperateForestBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_HOPS_VINE_PLACED.getHolder().get());
        }

        // Add wild wisteria vines to forest biomes
        if (isTemperateForestBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_WISTERIA_VINE_PLACED.getHolder().get());
        }

        // Add wild crop patches to grassy biomes
        if (isGrassyBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_FLAX_PLACED.getHolder().get());

            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_SQUASH_PLACED.getHolder().get());

            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_BEANS_PLACED.getHolder().get());

            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_PEPPERS_PLACED.getHolder().get());

            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.WILD_CORN_PLACED.getHolder().get());
        }

        // Add saltpeter sand and sandstone to desert biomes
        if (isDesertBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.SALTPETER_SAND_PLACED.getHolder().get());
            
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.SALTPETER_SANDSTONE_PLACED.getHolder().get());
        }

        // Add murex shell patches to beach biomes
        if (isBeachBiome(event)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.MUREX_SHELL_PLACED.getHolder().get());
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModPlacedFeatures.CLAM_PLACED.getHolder().get());
        }

        // Add ore replacements for surface exposure
        // Malachite replaces surface copper ore
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.MALACHITE_ORE_PLACED.getHolder().get());

        // Surface iron replaces exposed iron ore  
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.SURFACE_IRON_ORE_PLACED.getHolder().get());

        // Ochre and red ochre replacing clay
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.OCHRE_CLAY_PLACED.getHolder().get());
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.RED_OCHRE_CLAY_PLACED.getHolder().get());

        // Magnetite spawning - emerald-like rarity, more common in mountains
        if (isRockyBiome(event)) {
            // Rocky biomes (mountains, hills) get higher magnetite rates like emerald
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.MAGNETITE_ORE_ROCKY_PLACED.getHolder().get());
        } else {
            // Other biomes get standard (low) rates
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    ModPlacedFeatures.MAGNETITE_ORE_PLACED.getHolder().get());
        }

        // Sphalerite (zinc ore) spawning - common near surface and in caves, realistic geological placement
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.SPHALERITE_ORE_PLACED.getHolder().get());
        
        // Additional cave-focused sphalerite for realistic underground zinc deposits
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.SPHALERITE_CAVE_PLACED.getHolder().get());

        // Sulfur ore spawning - near lava pools and in tuff veins
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.SULFUR_ORE_PLACED.getHolder().get());
        
        // Additional sulfur near lava level - more common around lava pools
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.SULFUR_ORE_LAVA_PLACED.getHolder().get());

        // Add small marble and limestone veins - safe sizes but common enough to create clusters
        // Single small veins with higher spawn rates create large clustered regions safely
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.MARBLE_VEIN_RARE_PLACED.getHolder().get());
        
        // Add occasional surface marble - small but more frequent
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.MARBLE_VEIN_SURFACE_PLACED.getHolder().get());
        
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.LIMESTONE_VEIN_RARE_PLACED.getHolder().get());

        // Add test marble and limestone veins - very common for debugging
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.MARBLE_TEST_PLACED.getHolder().get());
        
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.LIMESTONE_TEST_PLACED.getHolder().get());
    }
    
    private static boolean isRiverBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        
        // Check for river biomes by name
        return biomeName.equals(Biomes.RIVER.location()) || 
               biomeName.equals(Biomes.FROZEN_RIVER.location()) ||
               biomeName.getPath().contains("river");
    }
    
    private static boolean isRockyBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        
        // Check for rocky/mountainous biomes
        return biomeName.equals(Biomes.WINDSWEPT_HILLS.location()) ||
               biomeName.equals(Biomes.WINDSWEPT_FOREST.location()) ||
               biomeName.equals(Biomes.WINDSWEPT_GRAVELLY_HILLS.location()) ||
               biomeName.equals(Biomes.BADLANDS.location()) ||
               biomeName.equals(Biomes.ERODED_BADLANDS.location()) ||
               biomeName.equals(Biomes.WOODED_BADLANDS.location()) ||
               biomeName.equals(Biomes.STONY_SHORE.location()) ||
               // Check for common keywords in biome names
               biomeName.getPath().contains("mountain") ||
               biomeName.getPath().contains("hills") ||
               biomeName.getPath().contains("badlands") ||
               biomeName.getPath().contains("mesa") ||
               biomeName.getPath().contains("stone") ||
               biomeName.getPath().contains("rocky") ||
               biomeName.getPath().contains("windswept");
    }
    
    private static boolean isTropicalBiome(BiomeLoadingEvent event) {
        var name = event.getName();
        if (name == null) return false;
        String p = name.getPath();
        // Remove beaches from tropical biomes - rubber trees shouldn't spawn on beaches
        return (p.contains("jungle") || p.contains("savanna") || p.contains("bamboo")) &&
               !p.contains("beach") && !p.contains("shore") && !p.contains("coast");
    }
    
    private static boolean isGrassyBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        String path = biomeName.getPath();
        // Plains, savanna, meadow, jungle, forest, etc.
        return path.contains("plains") || path.contains("savanna") || path.contains("meadow") || path.contains("forest") || path.contains("jungle") || path.contains("grassland");
    }
    
    private static boolean isBeachBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        String path = biomeName.getPath();
        // Check for beach biomes
        return path.contains("beach") || path.contains("shore") || path.contains("coast");
    }
    
    private static boolean isDesertBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        String path = biomeName.getPath();
        // Check for desert biomes and sandy biomes
        return path.contains("desert") || path.contains("sandy") || 
               biomeName.equals(Biomes.DESERT.location());
    }
    
    private static boolean isTemperateBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        String path = biomeName.getPath();
        // Check for temperate biomes (not tropical, not cold/frozen, not rocky) - expanded for more olive tree spawning
        return (path.contains("plains") || path.contains("forest") || path.contains("oak") || path.contains("birch") ||
                path.contains("meadow") || path.contains("sunflower") || path.equals("the_void") == false) &&
               !path.contains("jungle") && !path.contains("savanna") && !path.contains("bamboo") &&
               !path.contains("cold") && !path.contains("frozen") && !path.contains("snow") && !path.contains("ice") &&
               !path.contains("mountain") && !path.contains("hills") && !path.contains("badlands") &&
               !path.contains("desert") && !path.contains("beach") && !path.contains("shore") &&
               !path.contains("ocean") && !path.contains("river") && !path.contains("swamp") &&
               !path.contains("mushroom") && !path.contains("nether") && !path.contains("end");
    }
    
    private static boolean isTemperateForestBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        String path = biomeName.getPath();
        // Check specifically for forest biomes in temperate zones
        return path.contains("forest") &&
               !path.contains("jungle") && !path.contains("bamboo") &&
               !path.contains("cold") && !path.contains("frozen") && !path.contains("snow") && !path.contains("ice") &&
               !path.contains("mountain") && !path.contains("hills");
    }

    private static boolean isWarmWetSurfaceBiome(BiomeLoadingEvent event) {
        var biomeName = event.getName();
        if (biomeName == null) return false;
        String path = biomeName.getPath();

        if (isRockyBiome(event) || isDesertBiome(event)) return false;
        if (path.contains("cold") || path.contains("snow") || path.contains("frozen") || path.contains("ice")) return false;
        if (path.contains("mountain") || path.contains("hills") || path.contains("windswept") || path.contains("peak")) return false;
        if (path.contains("ocean") || path.contains("river") || path.contains("beach") || path.contains("shore") || path.contains("coast")) return false;

        boolean nameMatch = path.contains("jungle") || path.contains("savanna") || path.contains("rainforest")
                || path.contains("tropical") || path.contains("monsoon");

        var climate = event.getClimate();
        boolean climateMatch = false;
        if (climate != null) {
            climateMatch = climate.temperature >= 0.8f && climate.downfall >= 0.6f;
        }

        return nameMatch || climateMatch;
    }
} 