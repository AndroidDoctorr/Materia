package com.torr.materia.block;

import com.torr.materia.world.tree.RainbowEucalyptusTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class RainbowEucalyptusSaplingBlock extends SaplingBlock {
    public RainbowEucalyptusSaplingBlock(Properties properties) {
        super(new RainbowEucalyptusTreeGrower(), properties);
    }
}
