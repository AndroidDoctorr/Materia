package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
/**
 * A spruce log that has been tapped for sap.
 * Can only be burned for ash, not charcoal.
 */
public class SappedSpruceLogBlock extends RotatedPillarBlock {

    public SappedSpruceLogBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(net.minecraft.world.level.block.SoundType.WOOD));
    }

    @Override
    public boolean isFlammable(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.core.Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.core.Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.core.Direction direction) {
        return 5;
    }
} 