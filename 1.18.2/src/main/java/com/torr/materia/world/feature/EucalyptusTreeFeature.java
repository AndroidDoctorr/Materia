package com.torr.materia.world.feature;

import com.mojang.serialization.Codec;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
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
        return placeTree(context.level(), context.origin(), context.random(), false);
    }

    public static boolean placeTree(WorldGenLevel level, BlockPos origin, Random random, boolean rainbow) {
        if (!level.getBlockState(origin.below()).isSolidRender(level, origin.below())) {
            return false;
        }

        Block logBlock = rainbow ? ModBlocks.RAINBOW_EUCALYPTUS_LOG.get() : ModBlocks.EUCALYPTUS_LOG.get();
        Block leavesBlock = ModBlocks.EUCALYPTUS_LEAVES.get();

        int height = 14 + random.nextInt(7);
        BlockPos.MutableBlockPos pos = origin.mutable();

        for (int i = 0; i < height; i++) {
            if (!canReplace(level, pos)) {
                return false;
            }
            level.setBlock(pos, logBlock.defaultBlockState()
                    .setValue(BlockStateProperties.AXIS, Direction.Axis.Y), TREE_BLOCK_FLAGS);
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
            BlockPos canopyCenter = trunkPos.offset(offsetX, 0, offsetZ);
            if (offsetX != 0 || offsetZ != 0) {
                placeBranch(level, trunkPos, canopyCenter, logBlock);
            }

            int radius = Math.max(1, 3 - canopy / 2 + random.nextInt(2));
            maxReach = Math.max(maxReach, Math.abs(offsetX) + radius);
            maxReach = Math.max(maxReach, Math.abs(offsetZ) + radius);
            placeFlatCanopy(level, canopyCenter, leavesBlock, radius, random);
            if (random.nextFloat() < 0.65f) {
                placeFlatCanopy(level, canopyCenter.above(1), leavesBlock, Math.max(1, radius - 1), random);
            }
        }

        placeFlatCanopy(level, origin.above(height - 1), leavesBlock, 1 + random.nextInt(2), random);

        TreeLeafDistanceFix.refresh(level, origin, maxReach + 1, height + 3);
        return true;
    }

    private static int pickOffset(Random random) {
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
            if (canReplace(level, next)) {
                level.setBlock(next, logBlock.defaultBlockState()
                        .setValue(BlockStateProperties.AXIS, axis), TREE_BLOCK_FLAGS);
            }
            current = next;
        }
    }

    private static void placeFlatCanopy(WorldGenLevel level, BlockPos center, Block leavesBlock, int radius, Random random) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z > radius * radius + random.nextInt(2)) {
                    continue;
                }
                BlockPos leafPos = center.offset(x, 0, z);
                if (canReplace(level, leafPos)) {
                    level.setBlock(leafPos, leavesBlock.defaultBlockState()
                            .setValue(LeavesBlock.DISTANCE, 1)
                            .setValue(LeavesBlock.PERSISTENT, false), TREE_BLOCK_FLAGS);
                }
            }
        }
    }

    private static boolean canReplace(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.getMaterial().isReplaceable();
    }
}
