package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;

/**
 * Wrought iron pickaxe â€“ bridges bronze tools and the steel (vanilla iron) tier.
 */
public class IronPickaxeItem extends PickaxeItem {
    public IronPickaxeItem(Properties properties) {
        super(ModToolTiers.WROUGHT_IRON, properties.attributes(DiggerItem.createAttributes(ModToolTiers.WROUGHT_IRON, 1.0F, -2.8F)));
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        if (state.is(net.minecraft.world.level.block.Blocks.OBSIDIAN) || state.is(net.minecraft.world.level.block.Blocks.CRYING_OBSIDIAN)) {
            return true;
        }
        if (state.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)) {
            return false;
        }
        return super.isCorrectToolForDrops(stack, state);
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

