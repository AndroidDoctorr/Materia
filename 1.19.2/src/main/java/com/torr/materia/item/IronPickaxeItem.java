package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;

/**
 * Wrought iron pickaxe – bridges bronze tools and the steel (vanilla iron) tier.
 */
public class IronPickaxeItem extends PickaxeItem {
    public IronPickaxeItem(Properties properties) {
        super(ModToolTiers.WROUGHT_IRON, 1, -2.8F, properties);
    }

    @Override
    public boolean isCorrectToolForDrops(net.minecraft.world.level.block.state.BlockState state) {
        if (state.is(net.minecraft.world.level.block.Blocks.OBSIDIAN) || state.is(net.minecraft.world.level.block.Blocks.CRYING_OBSIDIAN)) {
            return true;
        }
        if (state.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)) {
            return false;
        }
        return super.isCorrectToolForDrops(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        if (state.is(net.minecraft.world.level.block.Blocks.OBSIDIAN) || state.is(net.minecraft.world.level.block.Blocks.CRYING_OBSIDIAN)) {
            return 3.0F; // slower but now mineable
        }
        if (state.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)) {
            return 0.0F;
        }
        if (state.is(com.torr.materia.ModBlocks.SURFACE_IRON_ORE.get())) {
            return 1.0F; // slower surface iron
        }
        return super.getDestroySpeed(stack, state);
    }
}

