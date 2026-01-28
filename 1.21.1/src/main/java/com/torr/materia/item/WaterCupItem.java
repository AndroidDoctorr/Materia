package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.WaterPotBlock;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WaterCupItem extends Item {
    
    public WaterCupItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(ModItems.CRUCIBLE.get());
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
        
        // Pour water into cauldrons
        if (blockState.is(Blocks.CAULDRON) || blockState.is(Blocks.WATER_CAULDRON)) {
            if (!level.isClientSide) {
                int current = blockState.is(Blocks.WATER_CAULDRON) ? blockState.getValue(LayeredCauldronBlock.LEVEL) : 0;
                if (current < 3) {
                    int newLevel = current + 1;
                    BlockState newState = Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, newLevel);
                    level.setBlock(blockPos, newState, 3);
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Convert water cup to crucible
                    ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                    ItemStack result = ItemUtils.createFilledResult(itemStack, player, crucible);
                    player.setItemInHand(hand, result);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        // Check if we're looking at a water source block
        if (blockState.is(Blocks.WATER) && blockState.getFluidState().isSource()) {
            if (!level.isClientSide) {
                // Play water fill sound
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                
                // Water cup is already filled, just give the same water cup back
                player.awardStat(Stats.ITEM_USED.get(this));
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
            if (blockState.is(Blocks.WATER) && blockState.getFluidState().isSource()) {
                if (!level.isClientSide) {
                    // Play water fill sound
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    
                    // Give player a water cup (this item), consume crucible if that's what they're holding
                    ItemStack result = new ItemStack(ModItems.WATER_CUP.get());
                    player.awardStat(Stats.ITEM_USED.get(this));
                    
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, result);
                    }
                    
                    return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(itemStack, player, result), level.isClientSide());
                }
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
            }
            
            // Check if we're looking at a water pot block
            if (blockState.is(ModBlocks.WATER_POT.get())) {
                if (!level.isClientSide) {
                    BlockEntity blockEntity = level.getBlockEntity(blockPos);
                    if (blockEntity instanceof WaterPotBlockEntity waterPotEntity) {
                        // Try to fill water cup from water pot
                        // This will be handled by the WaterPotBlock's use method
                        return InteractionResultHolder.pass(itemStack);
                    }
                }
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
            }

            // Pour water into cauldrons
            if (blockState.is(Blocks.CAULDRON) || blockState.is(Blocks.WATER_CAULDRON)) {
                if (!level.isClientSide) {
                    int current = blockState.is(Blocks.WATER_CAULDRON) ? blockState.getValue(LayeredCauldronBlock.LEVEL) : 0;
                    if (current < 3) {
                        int newLevel = current + 1;
                        BlockState newState = Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, newLevel);
                        level.setBlock(blockPos, newState, 3);
                        level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                        ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                        return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(itemStack, player, crucible), level.isClientSide());
                    }
                }
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
            }
        }
        
        return InteractionResultHolder.pass(itemStack);
    }
}
