package com.torr.materia;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

import java.lang.reflect.Method;

public final class ModFlammables {
    private static Method setFlammableMethod;

    private ModFlammables() {}

    public static void register() {
        FireBlock fire = (FireBlock) Blocks.FIRE;

        registerLog(fire, ModBlocks.SAPPED_SPRUCE_LOG.get());
        registerLog(fire, ModBlocks.RUBBER_WOOD_LOG.get());
        registerLog(fire, ModBlocks.TAPPED_RUBBER_WOOD_LOG.get());
        registerLog(fire, ModBlocks.OLIVE_TREE_LOG.get());
        registerLog(fire, ModBlocks.PALM_LOG.get());
        registerLog(fire, ModBlocks.CYPRESS_LOG.get());
        registerLog(fire, ModBlocks.BAOBAB_LOG.get());

        registerLeaves(fire, ModBlocks.RUBBER_TREE_LEAVES.get());
        registerLeaves(fire, ModBlocks.OLIVE_TREE_LEAVES.get());
        registerLeaves(fire, ModBlocks.PALM_LEAVES.get());
        registerLeaves(fire, ModBlocks.CYPRESS_LEAVES.get());
        registerLeaves(fire, ModBlocks.BAOBAB_LEAVES.get());
    }

    private static void registerLog(FireBlock fire, Block block) {
        setFlammable(fire, block, 5, 5);
    }

    private static void registerLeaves(FireBlock fire, Block block) {
        setFlammable(fire, block, 30, 60);
    }

    private static void setFlammable(FireBlock fire, Block block, int encouragement, int flammability) {
        try {
            if (setFlammableMethod == null) {
                setFlammableMethod = FireBlock.class.getDeclaredMethod("setFlammable", Block.class, int.class, int.class);
                setFlammableMethod.setAccessible(true);
            }
            setFlammableMethod.invoke(fire, block, encouragement, flammability);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to register flammable block " + block, e);
        }
    }
}
