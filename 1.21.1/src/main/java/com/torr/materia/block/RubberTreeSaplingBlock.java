package com.torr.materia.block;

import com.torr.materia.world.tree.RubberTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RubberTreeSaplingBlock extends SaplingBlock {
    public RubberTreeSaplingBlock(Properties properties) {
        super(RubberTreeGrower.GROWER, properties);
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos);
    }
}
