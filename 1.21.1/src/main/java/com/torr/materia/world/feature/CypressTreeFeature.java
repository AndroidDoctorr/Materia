package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CypressTreeFeature extends Feature<NoneFeatureConfiguration> {
    private static final int TREE_BLOCK_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS;

    private enum LayerStyle {
        NAKED,
        TIP,
        NARROW,
        CROSS,
        RING
    }

    public CypressTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        if (!level.getBlockState(origin.below()).isSolidRender(level, origin.below())) {
            return false;
        }

        int height = 12 + random.nextInt(4);
        int ringCount = 4 + random.nextInt(2);
        int crossCount = 2 + random.nextInt(2);
        int ringTop = ringCount;
        int crossTop = ringCount + crossCount;

        for (int y = 0; y < height; y++) {
            BlockPos center = origin.above(y);
            LayerStyle style = layerStyle(y, height, ringTop, crossTop);
            boolean trunk = y > 0 && (style == LayerStyle.CROSS || (y <= ringTop && style == LayerStyle.RING));
            placeLayer(level, center, style, trunk);
        }

        TreeLeafDistanceFix.refresh(level, origin, 1, height);
        return true;
    }

    private static LayerStyle layerStyle(int y, int height, int ringTop, int crossTop) {
        if (y == 0) {
            return LayerStyle.NAKED;
        }

        int fromTop = height - 1 - y;

        if (fromTop == 0) {
            return LayerStyle.TIP;
        }

        if (y <= ringTop) {
            return LayerStyle.RING;
        }

        if (y <= crossTop) {
            return LayerStyle.CROSS;
        }

        return LayerStyle.NARROW;
    }

    private static void placeLayer(WorldGenLevel level, BlockPos center, LayerStyle style, boolean trunk) {
        switch (style) {
            case NAKED -> placeLog(level, center);
            case TIP, NARROW -> placeLeaves(level, center);
            case CROSS, RING -> {
                if (trunk) {
                    placeLog(level, center);
                } else {
                    placeLeaves(level, center);
                }
                placeFoliage(level, center, style);
            }
        }
    }

    private static void placeFoliage(WorldGenLevel level, BlockPos center, LayerStyle style) {
        boolean crossOnly = style == LayerStyle.CROSS;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }
                if (crossOnly && dx != 0 && dz != 0) {
                    continue;
                }
                placeLeaves(level, center.offset(dx, 0, dz));
            }
        }
    }

    private static void placeLog(WorldGenLevel level, BlockPos pos) {
        if (level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced()) {
            BlockState log = ModBlocks.CYPRESS_LOG.get().defaultBlockState()
                    .setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
            level.setBlock(pos, log, TREE_BLOCK_FLAGS);
        }
    }

    private static void placeLeaves(WorldGenLevel level, BlockPos pos) {
        if (level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced()) {
            level.setBlock(pos, ModBlocks.CYPRESS_LEAVES.get().defaultBlockState(), TREE_BLOCK_FLAGS);
        }
    }
}
