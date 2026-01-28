package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.blockentity.WaterPotBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class CrucibleItem extends Item {
    
    public CrucibleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        InteractionHand hand = context.getHand();
        
        if (player == null) return InteractionResult.PASS;
        
        BlockState blockState = level.getBlockState(blockPos);
        
        // Pour from crucible to cauldron is not allowed (crucible is empty), handled in WaterCup
        // Check if we're looking at a water source block
        if ((blockState.is(Blocks.WATER) || blockState.getFluidState().getType() == Fluids.WATER)
                && blockState.getFluidState().isSource()) {
            if (!level.isClientSide) {
                // Play water fill sound
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                
                // Convert crucible to water cup
                ItemStack result = new ItemStack(ModItems.WATER_CUP.get());
                player.awardStat(Stats.ITEM_USED.get(this));
                
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, result);
                }
                
                // Replace crucible with water cup in the same slot
                ItemStack finalResult = ItemUtils.createFilledResult(itemStack, player, result);
                player.setItemInHand(hand, finalResult);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        // Raycast to find what the player is looking at
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }
        
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);
            
            // Check if we're looking at a water source block
            if ((blockState.is(Blocks.WATER) || blockState.getFluidState().getType() == Fluids.WATER)
                    && blockState.getFluidState().isSource()) {
                if (!level.isClientSide) {
                    // Play water fill sound
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    
                    // Convert crucible to water cup
                    ItemStack result = new ItemStack(ModItems.WATER_CUP.get());
                    player.awardStat(Stats.ITEM_USED.get(this));
                    
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, result);
                    }
                    
                    return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(itemStack, player, result), level.isClientSide());
                }
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
            }
            
            // Fill crucible from water cauldron (take 1 layer)
            if (blockState.is(Blocks.WATER_CAULDRON)) {
                if (!level.isClientSide) {
                    int current = blockState.getValue(LayeredCauldronBlock.LEVEL);
                    if (current > 0) {
                        int newLevel = current - 1;
                        BlockState newState = newLevel == 0 ? Blocks.CAULDRON.defaultBlockState()
                                : blockState.setValue(LayeredCauldronBlock.LEVEL, newLevel);
                        level.setBlock(blockPos, newState, 3);
                        level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                        ItemStack result = new ItemStack(ModItems.WATER_CUP.get());
                        return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(itemStack, player, result), level.isClientSide());
                    }
                }
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
            }
        }
        
        return InteractionResultHolder.pass(itemStack);
    }
}
