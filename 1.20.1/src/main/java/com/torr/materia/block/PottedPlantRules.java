package com.torr.materia.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

/**
 * Rules for decorative planter and urn planting.
 */
public final class PottedPlantRules {

    public static final TagKey<Block> PLANTER_PLANTS = TagKey.create(
            Registries.BLOCK, new ResourceLocation("materia", "planter_plants"));
    public static final TagKey<Block> URN_PLANTS = TagKey.create(
            Registries.BLOCK, new ResourceLocation("materia", "urn_plants"));

    private PottedPlantRules() {
    }

    public static boolean canPlantInPlanter(Block block) {
        return block.builtInRegistryHolder().is(PLANTER_PLANTS);
    }

    public static boolean canPlantInUrn(Block block) {
        return block.builtInRegistryHolder().is(URN_PLANTS);
    }

    public static boolean isTallPlant(Block block) {
        return block instanceof DoublePlantBlock;
    }

    public static net.minecraft.world.level.block.state.BlockState displayState(Block block) {
        net.minecraft.world.level.block.state.BlockState state = block.defaultBlockState();
        if (block instanceof DoublePlantBlock) {
            return state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
        }
        return state;
    }

    public static net.minecraft.world.level.block.state.BlockState tallUpperState(Block block) {
        if (block instanceof DoublePlantBlock) {
            return block.defaultBlockState()
                    .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
        }
        return Blocks.AIR.defaultBlockState();
    }
}
