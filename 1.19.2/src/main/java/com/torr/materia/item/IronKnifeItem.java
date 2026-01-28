package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class IronKnifeItem extends SwordItem {

    public IronKnifeItem(Properties properties) {
        // Iron-tier knife: better damage and slightly faster than bronze
        super(Tiers.IRON, 3, -1.6F, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        // Check if we're right-clicking a spruce log
        if (state.is(Blocks.SPRUCE_LOG)) {
            if (!level.isClientSide()) {
                // Convert to sapped spruce log
                level.setBlock(pos, ModBlocks.SAPPED_SPRUCE_LOG.get().defaultBlockState()
                        .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)), 3);
                
                // Drop 1-2 sap items
                int sapCount = level.random.nextInt(2) + 1; // 1 or 2
                ItemStack sapStack = new ItemStack(ModItems.SAP.get(), sapCount);
                net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                        level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sapStack);
                level.addFreshEntity(itemEntity);
                
                // Damage the knife
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                
                // Play sound
                level.playSound(null, pos, ModSounds.FLINT_CRAFT.get(), SoundSource.BLOCKS, 1.0F, 
                        level.random.nextFloat() * 0.4F + 0.8F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Check if we're right-clicking a rubber tree log
        if (state.is(ModBlocks.RUBBER_TREE_LOG.get())) {
            if (!level.isClientSide()) {
                // Convert to tapped rubber tree log
                level.setBlock(pos, ModBlocks.TAPPED_RUBBER_TREE_LOG.get().defaultBlockState()
                        .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)), 3);
                
                // Drop 1-2 latex items
                int latexCount = level.random.nextInt(2) + 1; // 1 or 2
                ItemStack latexStack = new ItemStack(ModItems.LATEX.get(), latexCount);
                net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                        level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, latexStack);
                level.addFreshEntity(itemEntity);
                
                // Damage the knife
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                
                // Play sound
                level.playSound(null, pos, ModSounds.FLINT_CRAFT.get(), SoundSource.BLOCKS, 1.0F, 
                        level.random.nextFloat() * 0.4F + 0.8F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.useOn(context);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return copy;
    }
}


