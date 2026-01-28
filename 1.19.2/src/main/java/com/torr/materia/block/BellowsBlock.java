package com.torr.materia.block;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import java.util.Random;

/**
 * Bellows block that can be placed next to kilns to enable high-temperature smelting.
 * Detects if it's placed next to a kiln and provides enhanced smelting capabilities.
 */
public class BellowsBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape BELLOWS_SHAPE = Block.box(3.0D, 0.0D, 0.0D, 13.0D, 6.0D, 16.0D);

    public BellowsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * Check if this bellows is placed next to a kiln
     */
    public static boolean isNextToKiln(Level level, BlockPos bellowsPos) {
        BlockPos kilnPosEast = bellowsPos.east();
        BlockPos kilnPosWest = bellowsPos.west();
        BlockPos kilnPosNorth = bellowsPos.north();
        BlockPos kilnPosSouth = bellowsPos.south();
        BlockState kilnStateEast = level.getBlockState(kilnPosEast);
        BlockState kilnStateWest = level.getBlockState(kilnPosWest);
        BlockState kilnStateNorth = level.getBlockState(kilnPosNorth);
        BlockState kilnStateSouth = level.getBlockState(kilnPosSouth);
        boolean isEast = kilnStateEast.is(ModBlocks.KILN.get());
        boolean isWest = kilnStateWest.is(ModBlocks.KILN.get());
        boolean isNorth = kilnStateNorth.is(ModBlocks.KILN.get());
        boolean isSouth = kilnStateSouth.is(ModBlocks.KILN.get());
        return isEast || isWest || isNorth || isSouth;
    }

    /**
     * Get the kiln block adjacent to this bellows, or null if not on a kiln
     */
    public static BlockPos getAdjacentKiln(Level level, BlockPos bellowsPos) {
        BlockPos kilnPosEast = bellowsPos.east();
        BlockPos kilnPosWest = bellowsPos.west();
        BlockPos kilnPosNorth = bellowsPos.north();
        BlockPos kilnPosSouth = bellowsPos.south();
        BlockState kilnStateEast = level.getBlockState(kilnPosEast);
        BlockState kilnStateWest = level.getBlockState(kilnPosWest);
        BlockState kilnStateNorth = level.getBlockState(kilnPosNorth);
        BlockState kilnStateSouth = level.getBlockState(kilnPosSouth);
        if (kilnStateEast.is(ModBlocks.KILN.get())) {
            return kilnPosEast;
        } else if (kilnStateWest.is(ModBlocks.KILN.get())) {
            return kilnPosWest;
        } else if (kilnStateNorth.is(ModBlocks.KILN.get())) {
            return kilnPosNorth;
        } else if (kilnStateSouth.is(ModBlocks.KILN.get())) {
            return kilnPosSouth;
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BELLOWS_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BELLOWS_SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BELLOWS_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        // Check for adjacent kilns and face toward them
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (Direction direction : directions) {
            BlockPos adjacentPos = pos.relative(direction);
            if (level.getBlockState(adjacentPos).is(ModBlocks.KILN.get())) {
                // Face toward the kiln
                return this.defaultBlockState().setValue(FACING, direction);
            }
        }
        
        // No kiln found, face away from player (like furnace)
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
} 