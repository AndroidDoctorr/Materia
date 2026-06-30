package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.material.Fluids;

public class TallPlantClusterFeature extends Feature<SimpleBlockConfiguration> {

    public TallPlantClusterFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        Block block = context.config().toPlace().getState(random, origin).getBlock();

        if (!(block instanceof DoublePlantBlock)) {
            return false;
        }

        BlockPos[] offsets;
        if (random.nextBoolean()) {
            offsets = new BlockPos[]{
                    BlockPos.ZERO,
                    new BlockPos(0, 0, -1),
                    new BlockPos(0, 0, 1),
                    new BlockPos(1, 0, 0),
                    new BlockPos(-1, 0, 0)
            };
        } else {
            offsets = new BlockPos[]{
                    BlockPos.ZERO,
                    new BlockPos(1, 0, 1),
                    new BlockPos(1, 0, -1),
                    new BlockPos(-1, 0, 1),
                    new BlockPos(-1, 0, -1)
            };
        }

        boolean placedAny = false;
        for (BlockPos offset : offsets) {
            if (tryPlaceDoublePlant(level, origin.offset(offset), block)) {
                placedAny = true;
            }
        }
        return placedAny;
    }

    private boolean tryPlaceDoublePlant(WorldGenLevel level, BlockPos pos, Block block) {
        BlockPos above = pos.above();
        if (!level.isEmptyBlock(pos) || !level.isEmptyBlock(above)) {
            return false;
        }

        BlockState lower = block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER);
        if (!lower.canSurvive(level, pos)) {
            return false;
        }

        DoublePlantBlock.placeAt(level, lower, pos, 2);
        return true;
    }
}
