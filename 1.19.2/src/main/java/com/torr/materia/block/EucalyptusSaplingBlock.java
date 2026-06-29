package com.torr.materia.block;

import com.torr.materia.world.tree.EucalyptusTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class EucalyptusSaplingBlock extends SaplingBlock {
    public EucalyptusSaplingBlock(Properties properties) {
        super(new EucalyptusTreeGrower(), properties);
    }
}
