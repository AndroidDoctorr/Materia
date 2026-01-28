package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Bronze pickaxe – first tool able to mine vanilla iron/copper ores
 * and any other blocks that need an iron-level tool in this mod.
 */
public class BronzePickaxeItem extends PickaxeItem {
    public BronzePickaxeItem(Properties properties) {
        // Use Bronze tier but with harvest level 2 (iron-level mining)
        // Can mine iron/copper ores but with bronze durability
        super(ModToolTiers.BRONZE, 1, -2.8F, properties);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        if (state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE)) {
            return false;
        }
        if (state.is(Blocks.DEEPSLATE) ||
            state.is(Blocks.GRANITE) || state.is(Blocks.DIORITE) || state.is(Blocks.ANDESITE) ||
            state.is(Blocks.DEEPSLATE_COPPER_ORE) || state.is(Blocks.TUFF)) {
            return false;
        }
        return super.isCorrectToolForDrops(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // Gate ultra-hard blocks for later tiers
        if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.ANCIENT_DEBRIS)) return 0.0F;
        // Stop progress on gated stones/ores
        if (state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE) ||
            state.is(Blocks.DEEPSLATE) ||
            state.is(Blocks.GRANITE) || state.is(Blocks.DIORITE) || state.is(Blocks.ANDESITE) ||
            state.is(Blocks.DEEPSLATE_COPPER_ORE) || state.is(Blocks.TUFF)) return 0.0F;
        if (state.is(com.torr.materia.ModBlocks.SURFACE_IRON_ORE.get())) return 1.0F; // slower surface iron
        return super.getDestroySpeed(stack, state);
    }
}

