package com.torr.materia.block;

import com.torr.materia.world.tree.PalmTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class PalmSaplingBlock extends SaplingBlock {
    public PalmSaplingBlock(Properties properties) {
        super(PalmTreeGrower.GROWER, properties);
    }
}
