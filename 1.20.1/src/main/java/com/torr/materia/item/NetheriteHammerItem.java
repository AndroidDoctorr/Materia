package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NetheriteHammerItem extends PickaxeItem {

    public NetheriteHammerItem(Properties properties) {
        // Netherite hammer: highest tier, toughest material
        super(Tiers.NETHERITE, 7, -2.9F, properties);
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
        // Allow all blocks that netherite can handle
        return true;
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
                if (offhandItem.getItem() instanceof IronChiselItem) {
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
        if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN)) return this.getTier().getSpeed(); // allow but slower
        if (state.is(Blocks.DEEPSLATE)) return this.getTier().getSpeed(); // allow deepslate for diamond hammer
        return super.getDestroySpeed(stack, state);
    }

    private boolean shouldActAsSilkTouch(Player player, BlockState state) {
        // Check if player has compatible chisel in offhand
        ItemStack offhandItem = player.getOffhandItem();

        // Netherite hammer + Iron chisel = steel-level blocks
        if (offhandItem.getItem() instanceof IronChiselItem) {
            return isSteelLevelStone(state);
        }

        return false;
    }

    private boolean isSteelLevelStone(BlockState state) {
        // Check if block can be mined with iron tools (harvest level 2) but exclude regular stone
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            // Include all steel-level blocks except regular stone
            boolean isSteelLevel = state.is(Blocks.COBBLESTONE) ||
                state.is(Blocks.STONE) ||
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
                state.is(Blocks.TUFF) ||
                state.is(Blocks.DRIPSTONE_BLOCK);

            return isSteelLevel;
        }
        return false;
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
                   state.is(Blocks.TUFF) ||
                   state.is(Blocks.DRIPSTONE_BLOCK);
        }
        return false;
    }
}
