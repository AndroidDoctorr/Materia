package com.torr.materia.block;

import com.torr.materia.world.tree.FigTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;

public class FigTreeSaplingBlock extends SaplingBlock {
    public FigTreeSaplingBlock(Properties properties) {
        super(new FigTreeGrower(), properties);
    }
}
