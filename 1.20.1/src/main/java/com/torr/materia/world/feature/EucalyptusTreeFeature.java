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

public class EucalyptusTreeFeature extends Feature<NoneFeatureConfiguration> {
    private static final int TREE_BLOCK_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS;

    public EucalyptusTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return placeTree(context.level(), context.origin(), context.random(),
                ModBlocks.EUCALYPTUS_LOG.get(), ModBlocks.EUCALYPTUS_LEAVES.get());
    }

    public static boolean placeTree(WorldGenLevel level, BlockPos origin, RandomSource random, Block logBlock, Block leavesBlock) {
        if (!level.getBlockState(origin.below()).isSolidRender(level, origin.below())) {
            return false;
        }

        BlockState leafState = leavesBlock.defaultBlockState();

        int height = 14 + random.nextInt(7);
        BlockPos.MutableBlockPos pos = origin.mutable();

        for (int i = 0; i < height; i++) {
            if (!level.isEmptyBlock(pos)) {
                return false;
            }
            placeLog(level, pos, logBlock, Direction.Axis.Y);
            pos.move(Direction.UP);
        }

        int canopyCount = 3 + random.nextInt(2);
        int maxReach = 2;
        for (int canopy = 0; canopy < canopyCount; canopy++) {
            float heightFraction = 0.38f + canopy * 0.16f + random.nextFloat() * 0.06f;
            int trunkY = Math.min(height - 2, Math.max(2, Math.round(height * heightFraction)));

            int offsetX = pickOffset(random);
            int offsetZ = pickOffset(random);
            if (offsetX == 0 && offsetZ == 0) {
                offsetX = random.nextBoolean() ? 1 : -1;
            }

            BlockPos trunkPos = origin.above(trunkY);
            BlockPos branchTip = trunkPos.offset(offsetX, 0, offsetZ);
            if (offsetX != 0 || offsetZ != 0) {
                placeBranch(level, trunkPos, branchTip, logBlock);
            }

            int radius = Math.max(1, 3 - canopy / 2 + random.nextInt(2));
            maxReach = Math.max(maxReach, Math.abs(offsetX) + radius);
            maxReach = Math.max(maxReach, Math.abs(offsetZ) + radius);
            placeBranchCanopy(level, branchTip, leafState, radius, random);
        }

        placeBranchCanopy(level, origin.above(height - 1), leafState, 1 + random.nextInt(2), random);

        TreeLeafDistanceFix.refresh(level, origin, maxReach + 1, height + 3);
        return true;
    }

    private static int pickOffset(RandomSource random) {
        return switch (random.nextInt(3)) {
            case 0 -> -1;
            case 1 -> 0;
            default -> 1;
        };
    }

    private static void placeBranch(WorldGenLevel level, BlockPos from, BlockPos to, Block logBlock) {
        BlockPos current = from;
        while (!current.equals(to)) {
            int dx = Integer.signum(to.getX() - current.getX());
            int dz = Integer.signum(to.getZ() - current.getZ());
            Direction.Axis axis = dx != 0 ? Direction.Axis.X : Direction.Axis.Z;
            BlockPos next = current.offset(dx, 0, dz);
            placeLog(level, next, logBlock, axis);
            current = next;
        }
    }

    /** Vertical leaf puff above the branch tip. */
    private static void placeBranchCanopy(WorldGenLevel level, BlockPos branchTip, BlockState leafState, int radius, RandomSource random) {
        BlockPos canopyBase = branchTip.above(1);
        placeLeaves(level, canopyBase, leafState);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                if (x * x + z * z > radius * radius + random.nextInt(2)) {
                    continue;
                }
                placeLeaves(level, canopyBase.offset(x, 0, z), leafState);
                if (random.nextFloat() < 0.55f) {
                    placeLeaves(level, canopyBase.offset(x, 1, z), leafState);
                }
            }
        }
    }

    private static void placeLog(WorldGenLevel level, BlockPos pos, Block logBlock, Direction.Axis axis) {
        if (level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced()) {
            level.setBlock(pos, logBlock.defaultBlockState()
                    .setValue(BlockStateProperties.AXIS, axis), TREE_BLOCK_FLAGS);
        }
    }

    private static void placeLeaves(WorldGenLevel level, BlockPos pos, BlockState leafState) {
        if (level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced()) {
            level.setBlock(pos, leafState, TREE_BLOCK_FLAGS);
        }
    }
}
