package com.torr.materia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tiers;

/**
 * Iron chisel used for detail work on metal. 
 * Survives crafting recipes while losing durability.
 * When held in main hand with iron hammer in offhand, acts like silk touch for iron-level stones.
 */
public class IronChiselItem extends Item {

    public IronChiselItem(Properties properties) {
        super(properties);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        return copy.getDamageValue() >= copy.getMaxDamage() ? ItemStack.EMPTY : copy;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (entity instanceof Player player && shouldActAsSilkTouch(player, state)) {
            // Drop the block itself instead of its normal drops
            if (!level.isClientSide) {
                level.destroyBlock(pos, false); // Don't drop normal items
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                // Damage the hammer in offhand too
                ItemStack offhandItem = player.getOffhandItem();
                if (offhandItem.getItem() instanceof IronHammerItem ||
                    offhandItem.getItem() instanceof SteelHammerItem) {
                    offhandItem.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(InteractionHand.OFF_HAND));
                }
                // Drop the block itself
                popResource(level, pos, new ItemStack(state.getBlock().asItem()));
            }
            return true;
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        // Only works as a tool when paired with hammer
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // Very slow mining speed to encourage using proper tools
        return 0.5F;
    }

    private boolean shouldActAsSilkTouch(Player player, BlockState state) {
        // Check if player has compatible hammer in offhand (iron or steel)
        ItemStack offhandItem = player.getOffhandItem();
        if (!(offhandItem.getItem() instanceof IronHammerItem ||
              offhandItem.getItem() instanceof SteelHammerItem)) {
            return false;
        }

        // Iron chisel works on iron-level stone blocks
        return isIronLevelStone(state);
    }

    private boolean isIronLevelStone(BlockState state) {
        // Check if block can be mined with iron tools (harvest level 2) but exclude regular stone
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            // Include all bronze-level blocks except regular stone
            boolean isBronzeLevel = state.is(Blocks.COBBLESTONE) ||
                                  state.is(Blocks.STONE_BRICKS) ||
                                  state.is(Blocks.MOSSY_STONE_BRICKS) ||
                                  state.is(Blocks.CRACKED_STONE_BRICKS) ||
                                  state.is(Blocks.CHISELED_STONE_BRICKS) ||
                                  state.is(Blocks.GRANITE) ||
                                  state.is(Blocks.DIORITE) ||
                                  state.is(Blocks.ANDESITE) ||
                                  state.is(Blocks.POLISHED_GRANITE) ||
                                  state.is(Blocks.POLISHED_DIORITE) ||
                                  state.is(Blocks.POLISHED_ANDESITE) ||
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
                                  
            // Iron-level specific blocks (require iron tools to harvest)
            boolean isIronLevel = state.is(Blocks.GOLD_ORE) ||
                                state.is(Blocks.DEEPSLATE_GOLD_ORE) ||
                                state.is(Blocks.REDSTONE_ORE) ||
                                state.is(Blocks.DEEPSLATE_REDSTONE_ORE) ||
                                state.is(Blocks.LAPIS_ORE) ||
                                state.is(Blocks.DEEPSLATE_LAPIS_ORE) ||
                                state.is(Blocks.DIAMOND_ORE) ||
                                state.is(Blocks.DEEPSLATE_DIAMOND_ORE) ||
                                state.is(Blocks.EMERALD_ORE) ||
                                state.is(Blocks.DEEPSLATE_EMERALD_ORE) ||
                                state.is(Blocks.NETHER_GOLD_ORE) ||
                                state.is(Blocks.NETHER_QUARTZ_ORE) ||
                                state.is(Blocks.GILDED_BLACKSTONE) ||
                                state.is(Blocks.BLACKSTONE) ||
                                state.is(Blocks.POLISHED_BLACKSTONE) ||
                                state.is(Blocks.POLISHED_BLACKSTONE_BRICKS) ||
                                state.is(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS) ||
                                state.is(Blocks.CHISELED_POLISHED_BLACKSTONE) ||
                                state.is(Blocks.BASALT) ||
                                state.is(Blocks.POLISHED_BASALT) ||
                                state.is(Blocks.SMOOTH_BASALT);
                                
            return isBronzeLevel || isIronLevel;
        }
        return false;
    }

    private static void popResource(Level level, BlockPos pos, ItemStack stack) {
        net.minecraft.world.level.block.Block.popResource(level, pos, stack);
    }
}
