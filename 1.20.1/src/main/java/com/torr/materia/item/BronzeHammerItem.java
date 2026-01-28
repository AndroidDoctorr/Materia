package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import com.torr.materia.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.BlockTags;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;


public class BronzeHammerItem extends PickaxeItem {

    public BronzeHammerItem(Properties properties) {
        // Bronze hammer: bronze-tier mining so it can harvest copper (vanilla) and mod soft ores
        super(ModToolTiers.BRONZE, 6, -3.1F, properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1); // damage by 1 each craft
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY; // breaks when max durability reached
        }
        return copy;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        // Disallow harder or gated blocks for bronze hammer
        if (state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE) ||
            state.is(ModBlocks.SURFACE_IRON_ORE.get()) || state.is(ModBlocks.MAGNETITE.get()) ||
            state.is(Blocks.DEEPSLATE_COPPER_ORE) || state.is(Blocks.TUFF) ||
            state.is(Blocks.GRANITE) || state.is(Blocks.DIORITE) || state.is(Blocks.ANDESITE) ||
            state.is(Blocks.DEEPSLATE)) {
            return false;
        }
        // Allow copper and gold,
        if (state.is(Blocks.COPPER_ORE) || state.is(Blocks.DEEPSLATE_COPPER_ORE) ||
            state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE)) {
            return true;
        }
        // Allow mod soft ores
        if (state.is(ModBlocks.SURFACE_IRON_ORE.get()) ||
            state.is(ModBlocks.MALACHITE.get()) ||
            state.is(ModBlocks.MAGNETITE.get()) ||
            state.is(ModBlocks.SPHALERITE.get())) {
            return true;
        }
        return super.isCorrectToolForDrops(state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (entity instanceof Player player && shouldActAsSilkTouch(player, state)) {
            // Drop the block itself instead of its normal drops
            if (!level.isClientSide) {
                level.destroyBlock(pos, false); // Don't drop normal items
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                // Damage the chisel in offhand too
                ItemStack offhandItem = player.getOffhandItem();
                if (offhandItem.getItem() instanceof BronzeChiselItem) {
                    offhandItem.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(InteractionHand.OFF_HAND));
                }
                // Drop the block itself
                Block.popResource(level, pos, new ItemStack(state.getBlock().asItem()));
            }
            return true;
        }

        if (!level.isClientSide) {
            // Crush calcite to powder
            if (state.is(Blocks.CALCITE)) {
                level.destroyBlock(pos, false);
                Block.popResource(level, pos, new ItemStack(ModItems.CALCITE_POWDER.get(), 2));
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                return true;
            }
            // break stone without drops then spawn rocks (no cobblestone)
            if (state.is(Blocks.STONE)) {
                level.destroyBlock(pos, false);
                Block.popResource(level, pos, new ItemStack(ModBlocks.ROCK.get().asItem(), 4));
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                return true;
            }
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.ANCIENT_DEBRIS)) {
            return 0.0F; // gate ultra-hard blocks
        }
        // Block bronze from progressing on harder stone/ores we don't want it to mine
        if (state.is(ModBlocks.SURFACE_IRON_ORE.get()) || state.is(ModBlocks.MAGNETITE.get()) ||
            state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE) ||
            state.is(Blocks.DEEPSLATE_COPPER_ORE) || state.is(Blocks.TUFF) ||
            state.is(Blocks.GRANITE) || state.is(Blocks.DIORITE) || state.is(Blocks.ANDESITE) ||
            state.is(Blocks.DEEPSLATE)) {
            return 0.0F;
        }
        // Ensure proper speed on allowed mod ores
        if (state.is(ModBlocks.SPHALERITE.get()) || state.is(ModBlocks.MALACHITE.get()) ||
            state.is(Blocks.COPPER_ORE) || state.is(Blocks.DEEPSLATE_COPPER_ORE) ||
            state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE)) {
            return this.getTier().getSpeed();
        }
        if (state.is(Blocks.STONE)) {
            return 1.5F; // slower speed when breaking stone with hammer
        }
        return super.getDestroySpeed(stack, state);
    }

    private boolean shouldActAsSilkTouch(Player player, BlockState state) {
        // Check if player has bronze chisel in offhand
        ItemStack offhandItem = player.getOffhandItem();
        if (!(offhandItem.getItem() instanceof BronzeChiselItem)) {
            return false;
        }

        // Check if the block is mineable with stone pickaxe level tools
        return isBasicStone(state);
    }

    private boolean isBasicStone(BlockState state) {
        // Check if block can be mined with stone tools (harvest level 1)
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            // These are blocks that require stone level or lower to mine
            return state.is(Blocks.STONE) || 
                   state.is(Blocks.COBBLESTONE) ||
                   state.is(Blocks.STONE_BRICKS) ||
                   state.is(Blocks.MOSSY_STONE_BRICKS) ||
                   state.is(Blocks.CRACKED_STONE_BRICKS) ||
                   state.is(Blocks.CHISELED_STONE_BRICKS) ||
                   state.is(Blocks.COAL_ORE) ||
                   state.is(Blocks.DEEPSLATE_COAL_ORE) ||
                   state.is(Blocks.COPPER_ORE) ||
                   state.is(Blocks.DEEPSLATE_COPPER_ORE) ||
                   state.is(Blocks.IRON_ORE) ||
                   state.is(Blocks.DEEPSLATE_IRON_ORE) ||
                   state.is(Blocks.SANDSTONE) ||
                   state.is(Blocks.RED_SANDSTONE) ||
                   state.is(Blocks.SMOOTH_SANDSTONE) ||
                   state.is(Blocks.SMOOTH_RED_SANDSTONE) ||
                   state.is(Blocks.DEEPSLATE) ||
                   state.is(Blocks.COBBLED_DEEPSLATE) ||
                   state.is(Blocks.POLISHED_DEEPSLATE) ||
                   state.is(Blocks.DEEPSLATE_BRICKS) ||
                   state.is(Blocks.DEEPSLATE_TILES) ||
                   state.is(Blocks.CHISELED_DEEPSLATE) ||
                   state.is(Blocks.CALCITE) ||
                   state.is(Blocks.DRIPSTONE_BLOCK);
        }
        return false;
    }
}
