package com.torr.materia.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

/**
 * Solid bauxite ore block (as opposed to surface chunks).
 */
public class FullBauxiteBlock extends Block {
    public FullBauxiteBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(3.0f, 3.0f)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops());
    }
}

