package com.torr.materia.world.feature;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class WildWisteriaVineFeature extends Feature<NoneFeatureConfiguration> {
    
    public WildWisteriaVineFeature(com.mojang.serialization.Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        Random random = context.random();

        // Search for nearby logs to attach to in a larger area
        for (int x = -16; x <= 16; x++) {
            for (int y = -5; y <= 15; y++) {
                for (int z = -16; z <= 16; z++) {
                    BlockPos searchPos = pos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(searchPos);
                    
                    // Check if it's a log
                    if (isLog(blockState)) {
                        // Try to place vine on adjacent positions
                        if (tryPlaceVine(level, searchPos, random)) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private boolean tryPlaceVine(WorldGenLevel level, BlockPos logPos, Random random) {
        // Try all 4 horizontal directions around the log
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        
        for (Direction direction : directions) {
            if (random.nextFloat() < 0.8f) { // 80% chance per direction - increased for better success rate
                BlockPos vinePos = logPos.relative(direction);
                
                // Check if position is air and suitable for vine placement
                if (level.getBlockState(vinePos).isAir()) {
                    // Create vine state with proper facing
                    BlockState vineState = ModBlocks.WILD_WISTERIA_VINE.get().defaultBlockState()
                            .setValue(getPropertyForDirection(direction), true);
                    
                    level.setBlock(vinePos, vineState, 2);
                    
                    // Maybe place additional vine blocks hanging down
                    if (random.nextFloat() < 0.9f) { // 90% chance to hang down
                        int hangLength = random.nextInt(3) + 2; // 2-4 blocks (at least 2)
                        for (int i = 1; i <= hangLength; i++) {
                            BlockPos hangPos = vinePos.below(i);
                            if (level.getBlockState(hangPos).isAir()) {
                                BlockState hangVineState = ModBlocks.WILD_WISTERIA_VINE.get().defaultBlockState()
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
        switch (direction) {
            case NORTH: return VineBlock.SOUTH; // Vine faces south when attached to north side
            case SOUTH: return VineBlock.NORTH; // Vine faces north when attached to south side
            case EAST: return VineBlock.WEST;   // Vine faces west when attached to east side
            case WEST: return VineBlock.EAST;   // Vine faces east when attached to west side
            default: return VineBlock.NORTH;
        }
    }

    private boolean isLog(BlockState state) {
        return state.is(Blocks.OAK_LOG) || 
               state.is(Blocks.SPRUCE_LOG) || 
               state.is(Blocks.BIRCH_LOG) || 
               state.is(Blocks.JUNGLE_LOG) || 
               state.is(Blocks.ACACIA_LOG) || 
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
