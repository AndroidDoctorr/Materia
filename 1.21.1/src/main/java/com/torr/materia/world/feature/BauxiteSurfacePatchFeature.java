package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Places a small group of loose bauxite blocks directly on top of exposed bauxite ore.
 * Meant to be run where ore is surface-exposed to visually cap the vein.
 */
public class BauxiteSurfacePatchFeature extends Feature<NoneFeatureConfiguration> {
    private final Block looseBlock;
    private final Block oreBlock;

    public BauxiteSurfacePatchFeature(Codec<NoneFeatureConfiguration> codec, Block looseBlock, Block oreBlock) {
        super(codec);
        this.looseBlock = looseBlock;
        this.oreBlock = oreBlock;
    }

    @Override
    public boolean place(net.minecraft.world.level.levelgen.feature.FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();

        // Check if origin is air and below is exposed ore
        BlockPos below = origin.below();
        BlockState belowState = level.getBlockState(below);
        if (!level.isEmptyBlock(origin)) return false;
        if (!belowState.is(oreBlock)) return false;

        // Place a small cross pattern of loose bauxite (up to 5 blocks) atop the ore
        BlockState loose = looseBlock.defaultBlockState();
        placeIfAir(level, origin, loose);
        placeIfAir(level, origin.east(), loose);
        placeIfAir(level, origin.west(), loose);
        placeIfAir(level, origin.north(), loose);
        placeIfAir(level, origin.south(), loose);
        return true;
    }

    private void placeIfAir(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (level.isEmptyBlock(pos)) {
            // ensure the block below is solid so chunks don't float
            BlockPos below = pos.below();
            if (!level.getBlockState(below).isAir()) {
                level.setBlock(pos, state, 2);
            }
        }
    }
}

