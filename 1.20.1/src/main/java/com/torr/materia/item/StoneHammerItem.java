package com.torr.materia.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import com.torr.materia.ModItems;
import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;

public class StoneHammerItem extends PickaxeItem {

    public StoneHammerItem(Properties properties) {
        // Stone tier, high attack damage (5), slow swing speed (-3.4F)
        super(Tiers.STONE, 5, -3.4F, properties);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        return copy.getDamageValue() >= copy.getMaxDamage() ? ItemStack.EMPTY : copy;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        if (state.is(ModBlocks.LIMESTONE.get())) {
            return true;
        }
        if (state.is(Blocks.CALCITE)) {
            return true;
        }
        // Allow soft sulfur variants
        if (state.is(ModBlocks.SULFUR_ORE.get()) || state.is(ModBlocks.TUFF_SULFUR_ORE.get())) {
            return true;
        }
        // Allow malachite (copper), surface iron variant, and magnetite as special soft targets
        if (state.is(ModBlocks.MALACHITE.get()) || state.is(ModBlocks.MAGNETITE.get()) || state.is(ModBlocks.SURFACE_IRON_ORE.get())) {
            return true;
        }
        // Allow very soft, non-stone materials typically diggable by hand tools
        if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.COARSE_DIRT) ||
            state.is(Blocks.ROOTED_DIRT) || state.is(Blocks.MYCELIUM) || state.is(Blocks.PODZOL) ||
            state.is(Blocks.FARMLAND) || state.is(Blocks.SAND) || state.is(Blocks.RED_SAND) ||
            state.is(Blocks.GRAVEL) || state.is(Blocks.CLAY)) {
            return true;
        }
        // Everything else (stone, deepslate, vanilla ores, etc.) is disallowed
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(ModBlocks.MALACHITE.get())) {
            return super.getDestroySpeed(stack, state);
        }
        if (state.is(ModBlocks.LIMESTONE.get())) {
            return super.getDestroySpeed(stack, state);
        }
        if (state.is(Blocks.CALCITE)) {
            return super.getDestroySpeed(stack, state);
        }
        if (state.is(ModBlocks.SULFUR_ORE.get()) || state.is(ModBlocks.TUFF_SULFUR_ORE.get())) {
            return super.getDestroySpeed(stack, state);
        }
        // Prevent meaningful progress on stone-like or pickaxe-mineable blocks
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(Blocks.OBSIDIAN) || state.is(Blocks.DEEPSLATE)) {
            return 0.0F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide) {
            // Crush calcite to powder
            if (state.is(Blocks.CALCITE)) {
                level.destroyBlock(pos, false);
                Block.popResource(level, pos, new ItemStack(ModItems.CALCITE_POWDER.get(), 2));
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                return true;
            }
            // Crush stone into rocks instead of cobblestone
            if (state.is(Blocks.STONE)) {
                level.destroyBlock(pos, false);
                Block.popResource(level, pos, new ItemStack(ModBlocks.ROCK.get().asItem(), 4));
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                return true;
            }
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }
} 