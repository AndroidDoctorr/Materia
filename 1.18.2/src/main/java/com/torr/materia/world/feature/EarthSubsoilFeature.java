package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Replaces dirt and clay below a preserved topsoil layer with earth across an entire chunk column.
 */
public class EarthSubsoilFeature extends Feature<NoneFeatureConfiguration> {
    /** Surface block only (grass/podzol/etc.) — about 1m of topsoil including the surface. */
    private static final int TOPSOIL_DEPTH = 1;

    public EarthSubsoilFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockState earth = ModBlocks.EARTH.get().defaultBlockState();
        boolean placedAny = false;

        int chunkMinX = origin.getX() & ~15;
        int chunkMinZ = origin.getZ() & ~15;

        for (int x = chunkMinX; x < chunkMinX + 16; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + 16; z++) {
                if (processColumn(level, x, z, earth)) {
                    placedAny = true;
                }
            }
        }

        return placedAny;
    }

    private static boolean processColumn(WorldGenLevel level, int x, int z, BlockState earth) {
        int surfaceAirY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        if (surfaceAirY <= level.getMinBuildHeight() + TOPSOIL_DEPTH) {
            return false;
        }

        BlockPos surfacePos = new BlockPos(x, surfaceAirY - 1, z);
        BlockState surfaceState = level.getBlockState(surfacePos);
        if (!isSoilSurface(surfaceState) || !surfaceState.getFluidState().isEmpty()) {
            return false;
        }

        Holder<Biome> biome = level.getBiome(surfacePos);
        if (biome.is(Biomes.LUSH_CAVES) || isLushCaveSurface(surfaceState)) {
            return false;
        }

        boolean placedAny = false;
        int firstReplaceY = surfaceAirY - TOPSOIL_DEPTH - 1;
        for (int y = firstReplaceY; y >= level.getMinBuildHeight(); y--) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);
            if (isReplaceableSubsoil(state)) {
                level.setBlock(pos, earth, 2);
                placedAny = true;
            } else if (isSubsoilStop(state) || state.isAir()) {
                break;
            }
        }

        return placedAny;
    }

    private static boolean isLushCaveSurface(BlockState state) {
        return state.is(Blocks.MOSS_BLOCK)
                || state.is(Blocks.MOSS_CARPET)
                || state.is(Blocks.AZALEA)
                || state.is(Blocks.FLOWERING_AZALEA);
    }

    private static boolean isSoilSurface(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.MYCELIUM)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.ROOTED_DIRT);
    }

    private static boolean isReplaceableSubsoil(BlockState state) {
        return state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.CLAY);
    }

    private static boolean isSubsoilStop(BlockState state) {
        return state.is(Blocks.BEDROCK)
                || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(Blocks.SAND)
                || state.is(Blocks.RED_SAND)
                || state.is(Blocks.GRAVEL)
                || state.is(Blocks.SANDSTONE)
                || state.is(Blocks.RED_SANDSTONE);
    }
}
