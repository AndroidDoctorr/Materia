package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Fence-style stone balustrade segments with custom multipart models.
 */
public class BalustradeBlock extends FenceBlock {
    private static final VoxelShape POST = Block.box(4, 0, 4, 12, 16, 12);
    private static final VoxelShape NORTH = Block.box(4, 0, 0, 12, 16, 12);
    private static final VoxelShape SOUTH = Block.box(4, 0, 4, 12, 16, 16);
    private static final VoxelShape WEST = Block.box(0, 0, 4, 12, 16, 12);
    private static final VoxelShape EAST = Block.box(4, 0, 4, 16, 16, 12);

    public BalustradeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = POST;
        if (state.getValue(BlockStateProperties.NORTH)) {
            shape = Shapes.or(shape, NORTH);
        }
        if (state.getValue(BlockStateProperties.SOUTH)) {
            shape = Shapes.or(shape, SOUTH);
        }
        if (state.getValue(BlockStateProperties.WEST)) {
            shape = Shapes.or(shape, WEST);
        }
        if (state.getValue(BlockStateProperties.EAST)) {
            shape = Shapes.or(shape, EAST);
        }
        return shape;
    }
}
