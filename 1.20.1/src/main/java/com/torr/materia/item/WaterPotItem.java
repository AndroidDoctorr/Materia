package com.torr.materia.item;

import com.torr.materia.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WaterPotItem extends BlockItem {

    public WaterPotItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // If player is sneaking, use normal block placement behavior
        if (context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        }

        // Check if clicking on a water pot block first
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(blockpos);
        
        // If clicking on a water pot or brining vat, let the block handle the interaction
        if (clickedState.getBlock() instanceof com.torr.materia.WaterPotBlock) {
            return InteractionResult.PASS;
        }
        if (clickedState.getBlock() instanceof com.torr.materia.block.BriningVatBlock) {
            return InteractionResult.PASS;
        }

        // Try to place water next to the clicked block
        Direction direction = context.getClickedFace();
        BlockPos blockpos1 = blockpos.relative(direction);
        Player player = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();

        if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
            BlockState blockstate = level.getBlockState(blockpos1);

            // Try to place water
            if (blockstate.isAir()) {
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                } else {
                    // Place water and return empty pot
                    level.setBlock(blockpos1, Blocks.WATER.defaultBlockState(), 3);
                    level.playSound(null, blockpos1, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                    ItemStack emptyPot = new ItemStack(ModItems.POT.get());

                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, itemstack);
                    }

                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setItemInHand(context.getHand(), emptyPot);
                    } else {
                        if (!player.getInventory().add(emptyPot)) {
                            player.drop(emptyPot, false);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // If player is sneaking, use normal block placement behavior
        if (player.isShiftKeyDown()) {
            return super.use(level, player, hand);
        }

        // Handle right-clicking in air to place water
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);

            if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
                BlockState blockstate = level.getBlockState(blockpos1);

                // Try to place water
                if (blockstate.isAir()) {
                    if (level.isClientSide) {
                        return InteractionResultHolder.sidedSuccess(itemstack, true);
                    } else {
                        // Place water and return empty pot
                        level.setBlock(blockpos1, Blocks.WATER.defaultBlockState(), 3);
                        level.playSound(null, blockpos1, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                        ItemStack emptyPot = new ItemStack(ModItems.POT.get());

                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, itemstack);
                        }

                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            return InteractionResultHolder.sidedSuccess(emptyPot, false);
                        } else {
                            if (!player.getInventory().add(emptyPot)) {
                                player.drop(emptyPot, false);
                            }
                            return InteractionResultHolder.sidedSuccess(itemstack, false);
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.pass(itemstack);
    }
}