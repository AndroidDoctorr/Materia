package com.torr.materia.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;

/**
 * Steel hammer - highest tier metalworking hammer.
 * More durable than iron hammer.
 * Works with bronze chisel for bronze-level blocks and iron chisel for iron-level blocks.
 */
public class SteelHammerItem extends PickaxeItem {

    public SteelHammerItem(Properties properties) {
        // Steel hammer: best damage and speed, hardest material holds edge best
        super(Tiers.DIAMOND, 8, -2.7F, properties);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1); // damage by 1 each craft
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY; // breaks when max durability reached
        }
        return copy;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        // Force-drop obsidian when using the steel hammer (bypasses vanilla diamond gating)
        if (!level.isClientSide && (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(ModBlocks.OBSIDIAN_SLAB.get()))) {
            level.destroyBlock(pos, false);
            Block.popResource(level, pos, new ItemStack(state.getBlock().asItem()));
            stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
            return true;
        }

        if (entity instanceof Player player && shouldActAsSilkTouch(player, state)) {
            // Drop the block itself instead of its normal drops
            if (!level.isClientSide) {
                level.destroyBlock(pos, false); // Don't drop normal items
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                // Damage the chisel in offhand too
                ItemStack offhandItem = player.getOffhandItem();
                if (offhandItem.getItem() instanceof BronzeChiselItem ||
                    offhandItem.getItem() instanceof IronChiselItem) {
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

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(ModBlocks.OBSIDIAN_SLAB.get())) {
            return true;
        }
        // Steel hammer is the only hammer allowed to harvest deepslate
        if (state.is(Blocks.DEEPSLATE)) {
            return true;
        }
        return super.isCorrectToolForDrops(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(ModBlocks.OBSIDIAN_SLAB.get())) {
            // Make obsidian workable with steel (about ~1s without Efficiency)
            return 80.0F;
        }
        if (state.is(Blocks.DEEPSLATE)) return this.getTier().getSpeed(); // allow deepslate for steel hammer
        if (state.is(ModBlocks.SURFACE_IRON_ORE.get())) return 1.5F; // slower surface iron
        return super.getDestroySpeed(stack, state);
    }

    private boolean shouldActAsSilkTouch(Player player, BlockState state) {
        // Check if player has compatible chisel in offhand
        ItemStack offhandItem = player.getOffhandItem();
        
        // Steel hammer + Bronze chisel = bronze-level blocks
        if (offhandItem.getItem() instanceof BronzeChiselItem) {
            return isBasicStone(state);
        }
        
        // Steel hammer + Iron chisel = iron-level blocks  
        if (offhandItem.getItem() instanceof IronChiselItem) {
            return isIronLevelStone(state);
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
        }
        return false;
    }

    private boolean isIronLevelStone(BlockState state) {
        // Check if block can be mined with iron tools (harvest level 2)
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            // Include all bronze-level blocks
            boolean isBronzeLevel = state.is(Blocks.COBBLESTONE) ||
                                  state.is(Blocks.STONE) ||
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
}
