package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * Thatch roofs use vanilla stair geometry but need a sealed occlusion cube so rain/skylight
 * do not pass through voxel gaps on inner-corner and upside-down layouts.
 */
public class ThatchSlopeBlock extends StairBlock {

    public ThatchSlopeBlock(Supplier<BlockState> baseStateSupplier, Properties properties) {
        super(baseStateSupplier, properties);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }
}
