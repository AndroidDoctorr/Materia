package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import com.torr.materia.block.PalmLeafFacing;
import com.torr.materia.block.PalmLeafShape;
import com.torr.materia.block.PalmLeavesBlock;
import com.torr.materia.block.PalmLogBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class PalmTreeFeature extends Feature<NoneFeatureConfiguration> {
    private static final int TREE_BLOCK_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS;
    private static final int[][] CARDINAL_DIRECTIONS = {
            {0, -1},
            {1, 0},
            {0, 1},
            {-1, 0}
    };

    public PalmTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        Random random = context.random();

        if (!level.getBlockState(origin.below()).isSolidRender(level, origin.below())) {
            return false;
        }

        int height = 8 + random.nextInt(4);
        int flatReach = 3 + random.nextInt(2);
        BlockPos.MutableBlockPos pos = origin.mutable();

        for (int i = 0; i < height; i++) {
            if (!level.isEmptyBlock(pos)) {
                return false;
            }
            boolean upper = i >= height - 3;
            BlockState log = ModBlocks.PALM_LOG.get().defaultBlockState()
                    .setValue(BlockStateProperties.AXIS, Direction.Axis.Y)
                    .setValue(PalmLogBlock.UPPER, upper);
            level.setBlock(pos, log, TREE_BLOCK_FLAGS);
            pos.move(Direction.UP);
        }

        BlockPos top = origin.above(height - 1);

        for (int[] direction : CARDINAL_DIRECTIONS) {
            PalmLeafFacing facing = PalmLeafFacing.fromOffset(direction[0], direction[1]);
            placeLeaves(level, top.offset(direction[0], 0, direction[1]), facing, PalmLeafShape.SLOPED);

            for (int distance = 2; distance <= flatReach; distance++) {
                placeLeaves(level, top.offset(direction[0] * distance, 0, direction[1] * distance), facing, PalmLeafShape.FLAT);
            }
        }

        TreeLeafDistanceFix.refresh(level, origin, flatReach, height);
        return true;
    }

    private static void placeLeaves(WorldGenLevel level, BlockPos pos, PalmLeafFacing facing, PalmLeafShape shape) {
        if (level.isEmptyBlock(pos)) {
            level.setBlock(pos, ModBlocks.PALM_LEAVES.get().defaultBlockState()
                    .setValue(PalmLeavesBlock.FACING, facing)
                    .setValue(PalmLeavesBlock.SHAPE, shape), TREE_BLOCK_FLAGS);
        }
    }
}
