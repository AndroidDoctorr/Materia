package com.torr.materia.world.feature;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WildHopsVineFeature extends Feature<NoneFeatureConfiguration> {

    public WildHopsVineFeature(com.mojang.serialization.Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        // Search for nearby logs to attach to in a larger area
        for (int x = -16; x <= 16; x++) {
            for (int y = -5; y <= 15; y++) {
                for (int z = -16; z <= 16; z++) {
                    BlockPos searchPos = pos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(searchPos);

                    if (isLog(blockState)) {
                        if (tryPlaceVine(level, searchPos, random)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean tryPlaceVine(WorldGenLevel level, BlockPos logPos, RandomSource random) {
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        for (Direction direction : directions) {
            if (random.nextFloat() < 0.8f) {
                BlockPos vinePos = logPos.relative(direction);

                if (level.getBlockState(vinePos).isAir()) {
                    BlockState vineState = ModBlocks.WILD_HOPS_VINE.get().defaultBlockState()
                            .setValue(getPropertyForDirection(direction), true);

                    level.setBlock(vinePos, vineState, 2);

                    if (random.nextFloat() < 0.7f) {
                        int hangLength = random.nextInt(4) + 1;
                        for (int i = 1; i <= hangLength; i++) {
                            BlockPos hangPos = vinePos.below(i);
                            if (level.getBlockState(hangPos).isAir()) {
                                BlockState hangVineState = ModBlocks.WILD_HOPS_VINE.get().defaultBlockState()
                                        .setValue(getPropertyForDirection(direction), true);
                                level.setBlock(hangPos, hangVineState, 2);
                            } else {
                                break;
                            }
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private net.minecraft.world.level.block.state.properties.BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> VineBlock.SOUTH;
            case SOUTH -> VineBlock.NORTH;
            case EAST -> VineBlock.WEST;
            case WEST -> VineBlock.EAST;
            default -> VineBlock.NORTH;
        };
    }

    private boolean isLog(BlockState state) {
        return state.is(Blocks.OAK_LOG) ||
                state.is(Blocks.SPRUCE_LOG) ||
                state.is(Blocks.BIRCH_LOG) ||
                state.is(Blocks.JUNGLE_LOG) ||
                state.is(Blocks.ACACIA_LOG) ||
                state.is(Blocks.MANGROVE_LOG) ||
                state.is(Blocks.CRIMSON_STEM) ||
                state.is(Blocks.WARPED_STEM) ||
                state.is(Blocks.CRIMSON_HYPHAE) ||
                state.is(Blocks.WARPED_HYPHAE) ||
                state.is(Blocks.STRIPPED_CRIMSON_STEM) ||
                state.is(Blocks.STRIPPED_WARPED_STEM) ||
                state.is(Blocks.STRIPPED_CRIMSON_HYPHAE) ||
                state.is(Blocks.STRIPPED_WARPED_HYPHAE) ||
                state.is(Blocks.DARK_OAK_LOG) ||
                state.is(ModBlocks.RUBBER_TREE_LOG.get()) ||
                state.is(ModBlocks.OLIVE_TREE_LOG.get());
    }
}

