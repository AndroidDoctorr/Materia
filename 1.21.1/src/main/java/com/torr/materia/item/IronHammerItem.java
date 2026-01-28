package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DiggerItem;
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
import net.minecraft.world.item.ItemStack;
import com.torr.materia.ModItems;

public class IronHammerItem extends PickaxeItem {

    public IronHammerItem(Properties properties) {
        // Iron hammer: mid-tier, tougher than bronze
        super(ModToolTiers.WROUGHT_IRON, properties.attributes(DiggerItem.createAttributes(ModToolTiers.WROUGHT_IRON, 7.0F, -2.9F)));
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
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        // Disallow deepslate iron and the softer stones we gate at this tier
        if (state.is(Blocks.DEEPSLATE_IRON_ORE) ||
            state.is(Blocks.GRANITE) || state.is(Blocks.DIORITE) || state.is(Blocks.ANDESITE) ||
            state.is(Blocks.DEEPSLATE)) {
            return false;
        }
        // Allow all ores that bronze can handle plus normal iron ore and obsidian
        if (state.is(Blocks.IRON_ORE) || state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN)) {
            return true;
        }
        if (state.is(ModBlocks.MALACHITE.get()) || state.is(ModBlocks.MAGNETITE.get()) ||
            state.is(ModBlocks.SPHALERITE.get()) || state.is(ModBlocks.SURFACE_IRON_ORE.get())) {
            return true;
        }
        return super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (entity instanceof Player player && shouldActAsSilkTouch(player, state)) {
            // Drop the block itself instead of its normal drops
            if (!level.isClientSide) {
                level.destroyBlock(pos, false); // Don't drop normal items
                    stack.hurtAndBreak(1, entity, entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                // Damage the chisel in offhand too
                ItemStack offhandItem = player.getOffhandItem();
                if (offhandItem.getItem() instanceof BronzeChiselItem ||
                    offhandItem.getItem() instanceof IronChiselItem) {
                        offhandItem.hurtAndBreak(1, entity, EquipmentSlot.OFFHAND);
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
                stack.hurtAndBreak(1, entity, entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                return true;
            }
            // break stone without drops then spawn rocks (no cobblestone)
            if (state.is(Blocks.STONE)) {
                level.destroyBlock(pos, false);
                Block.popResource(level, pos, new ItemStack(ModBlocks.ROCK.get().asItem(), 4));
                stack.hurtAndBreak(1, entity, entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                return true;
            }
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN)) return 3.0F; // allow but slower
        if (state.is(Blocks.DEEPSLATE)) return 0.0F; // block breaking deepslate
        if (state.is(Blocks.DEEPSLATE_IRON_ORE)) return 0.0F;
        if (state.is(Blocks.ANCIENT_DEBRIS)) return 0.0F; // still gated
        if (state.is(ModBlocks.SURFACE_IRON_ORE.get())) return 1.5F; // slower surface iron
        if (state.is(Blocks.STONE)) return 2.0F; // faster than bronze hammer
        return super.getDestroySpeed(stack, state);
    }

    private boolean shouldActAsSilkTouch(Player player, BlockState state) {
        // Check if player has compatible chisel in offhand
        ItemStack offhandItem = player.getOffhandItem();
        
        // Iron hammer + Bronze chisel = bronze-level blocks
        if (offhandItem.getItem() instanceof BronzeChiselItem) {
            return isBasicStone(state);
        }
        
        // Iron hammer + Iron chisel = iron-level blocks  
        if (offhandItem.getItem() instanceof IronChiselItem) {
            return isIronLevelStone(state);
        }
        
        return false;
    }

    private boolean isIronLevelStone(BlockState state) {
        // Check if block can be mined with iron tools (harvest level 2) but exclude regular stone
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            // Include all bronze-level blocks except regular stone
            boolean isBronzeLevel = state.is(Blocks.COBBLESTONE) ||
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
