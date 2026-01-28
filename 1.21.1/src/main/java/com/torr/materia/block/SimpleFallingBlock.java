package com.torr.materia.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SimpleFallingBlock extends FallingBlock {
    public static final MapCodec<SimpleFallingBlock> CODEC = simpleCodec(SimpleFallingBlock::new);

    public SimpleFallingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }
}

