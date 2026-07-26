package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Single-block metal finial (acorn) that should not be washed away by flowing water. */
public class MetalFinialCrossBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(5, 0, 5, 11, 16, 11);

    public MetalFinialCrossBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
