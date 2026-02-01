package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Wrought iron pickaxe â€“ bridges bronze tools and the steel (vanilla iron) tier.
 */
public class IronPickaxeItem extends PickaxeItem {
    public IronPickaxeItem(Properties properties) {
        super(ModToolTiers.WROUGHT_IRON, properties.attributes(DiggerItem.createAttributes(ModToolTiers.WROUGHT_IRON, 1.0F, -2.8F)));
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        if (state.is(net.minecraft.world.level.block.Blocks.OBSIDIAN) ||
            state.is(net.minecraft.world.level.block.Blocks.CRYING_OBSIDIAN) ||
            state.is(com.torr.materia.ModBlocks.OBSIDIAN_SLAB.get())) {
            return true;
        }
        if (state.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)) {
            return false;
        }
        return super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        if (state.is(net.minecraft.world.level.block.Blocks.OBSIDIAN) ||
            state.is(net.minecraft.world.level.block.Blocks.CRYING_OBSIDIAN) ||
            state.is(com.torr.materia.ModBlocks.OBSIDIAN_SLAB.get())) {
            // Make obsidian workable with wrought iron (about ~1s without Efficiency)
            return 80.0F;
        }
        if (state.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)) {
            return 0.0F;
        }
        if (state.is(com.torr.materia.ModBlocks.SURFACE_IRON_ORE.get())) {
            return 1.0F; // slower surface iron
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, net.minecraft.world.level.block.state.BlockState state, BlockPos pos, LivingEntity entity) {
        // Force-drop obsidian when using the wrought iron pickaxe (bypasses vanilla diamond gating)
        if (!level.isClientSide &&
            (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(com.torr.materia.ModBlocks.OBSIDIAN_SLAB.get()))) {
            level.destroyBlock(pos, false);
            Block.popResource(level, pos, new ItemStack(state.getBlock().asItem()));
            stack.hurtAndBreak(1, entity, entity.getEquipmentSlotForItem(stack));
            return true;
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }
}

