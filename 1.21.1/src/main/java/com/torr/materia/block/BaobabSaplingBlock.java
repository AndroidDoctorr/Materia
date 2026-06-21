package com.torr.materia.block;

import com.torr.materia.world.tree.BaobabTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class BaobabSaplingBlock extends SaplingBlock {
    public BaobabSaplingBlock(Properties properties) {
        super(BaobabTreeGrower.GROWER, properties);
    }
}
