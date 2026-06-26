package com.torr.materia.block;

import com.torr.materia.world.tree.MapleTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class MapleSaplingBlock extends SaplingBlock {
    public MapleSaplingBlock(Properties properties) {
        super(MapleTreeGrower.GROWER, properties);
    }
}
