package com.torr.materia.block;

import com.torr.materia.world.tree.CypressTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class CypressSaplingBlock extends SaplingBlock {
    public CypressSaplingBlock(Properties properties) {
        super(new CypressTreeGrower(), properties);
    }
}
