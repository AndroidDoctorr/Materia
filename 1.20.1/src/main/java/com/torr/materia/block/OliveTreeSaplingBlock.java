package com.torr.materia.block;

import com.torr.materia.world.tree.OliveTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class OliveTreeSaplingBlock extends SaplingBlock {
    public OliveTreeSaplingBlock(Properties properties) {
        super(new OliveTreeGrower(), properties);
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos);
    }
}
