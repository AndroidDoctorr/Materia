package com.torr.materia.world.feature;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

final class TreeLeafDistanceFix {
    private TreeLeafDistanceFix() {
    }

    static void refresh(WorldGenLevel level, BlockPos origin, int horizontalRadius, int height) {
        BlockPos.MutableBlockPos pos = origin.mutable();

        for (int pass = 0; pass < 8; pass++) {
            boolean changed = false;

            for (int y = 0; y < height; y++) {
                for (int dx = -horizontalRadius; dx <= horizontalRadius; dx++) {
                    for (int dz = -horizontalRadius; dz <= horizontalRadius; dz++) {
                        pos.set(origin.getX() + dx, origin.getY() + y, origin.getZ() + dz);
                        BlockState state = level.getBlockState(pos);

                        if (!isCustomLeaves(state)) {
                            continue;
                        }

                        int distance = computeDistance(level, pos);
                        if (distance != state.getValue(LeavesBlock.DISTANCE)) {
                            level.setBlock(pos, state.setValue(LeavesBlock.DISTANCE, distance), 2);
                            changed = true;
                        }
                    }
                }
            }

            if (!changed) {
                break;
            }
        }
    }

    private static int computeDistance(WorldGenLevel level, BlockPos pos) {
        int distance = 7;
        BlockPos.MutableBlockPos neighbor = pos.mutable();

        for (net.minecraft.core.Direction direction : net.minecraft.core.Direction.values()) {
            neighbor.setWithOffset(pos, direction);
            distance = Math.min(distance, getDistanceAt(level.getBlockState(neighbor)) + 1);
        }

        return distance;
    }

    private static int getDistanceAt(BlockState state) {
        if (state.hasProperty(LeavesBlock.DISTANCE)) {
            return state.getValue(LeavesBlock.DISTANCE);
        }
        return state.is(BlockTags.LOGS) ? 0 : 7;
    }

    private static boolean isCustomLeaves(BlockState state) {
        return state.is(ModBlocks.RUBBER_TREE_LEAVES.get())
                || state.is(ModBlocks.OLIVE_TREE_LEAVES.get())
                || state.is(ModBlocks.PALM_LEAVES.get())
                || state.is(ModBlocks.CYPRESS_LEAVES.get())
                || state.is(ModBlocks.BAOBAB_LEAVES.get())
                || state.is(ModBlocks.MAPLE_LEAVES.get())
                || state.is(ModBlocks.FIG_LEAVES.get())
                || state.is(ModBlocks.CEDAR_LEAVES.get())
                || state.is(ModBlocks.EUCALYPTUS_LEAVES.get());
    }
}
