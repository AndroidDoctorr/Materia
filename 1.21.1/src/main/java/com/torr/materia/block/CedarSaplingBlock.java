package com.torr.materia.block;

import com.torr.materia.world.tree.CedarTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class CedarSaplingBlock extends SaplingBlock {
    public CedarSaplingBlock(Properties properties) {
        super(CedarTreeGrower.GROWER, properties);
    }
}
