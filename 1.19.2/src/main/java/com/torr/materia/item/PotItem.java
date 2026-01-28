package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class PotItem extends BlockItem {

    public PotItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // If player is sneaking, use normal block placement behavior
        if (context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        }

        // Check if clicking on a fluid source for filling
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        Player player = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();

        // Check for water source blocks
        if (blockstate.is(Blocks.WATER)
                || (blockstate.getFluidState().getType() == Fluids.WATER && blockstate.getFluidState().isSource())) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                // Remove the water source block and fill pot
                level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                level.playSound(null, blockpos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                ItemStack filledPot = new ItemStack(ModItems.WATER_POT.get());

                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, filledPot);
                }

                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    player.setItemInHand(context.getHand(), filledPot);
                } else {
                    if (!player.getInventory().add(filledPot)) {
                        player.drop(filledPot, false);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        // Check for lava source blocks
        else if (blockstate.is(Blocks.LAVA)
                || (blockstate.getFluidState().getType() == Fluids.LAVA && blockstate.getFluidState().isSource())) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                // Remove the lava source block and fill pot
                level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                level.playSound(null, blockpos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
                ItemStack filledPot = new ItemStack(ModItems.LAVA_POT.get());

                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, filledPot);
                }

                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    player.setItemInHand(context.getHand(), filledPot);
                } else {
                    if (!player.getInventory().add(filledPot)) {
                        player.drop(filledPot, false);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        // Not a fluid source, so don't place block unless shift is held
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // If shift is held, allow normal block placement behaviour (no bucket logic)
        if (player.isShiftKeyDown()) {
            return super.use(level, player, hand);
        }

        // Ray-trace to find fluid source blocks (same as vanilla BucketItem)
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        // Try to fill with water
        if (state.is(Blocks.WATER)
                || (state.getFluidState().getType() == Fluids.WATER && state.getFluidState().isSource())) {
            return fillPot(level, player, hand, itemstack, pos, SoundEvents.BUCKET_FILL,
                    new ItemStack(ModItems.WATER_POT.get()));
        }

        // Try to fill with lava
        if (state.is(Blocks.LAVA)
                || (state.getFluidState().getType() == Fluids.LAVA && state.getFluidState().isSource())) {
            return fillPot(level, player, hand, itemstack, pos, SoundEvents.BUCKET_FILL_LAVA,
                    new ItemStack(ModItems.LAVA_POT.get()));
        }

        return InteractionResultHolder.pass(itemstack);
    }

    /**
     * Helper to remove the fluid source block, play sound, shrink stack and give
     * player the filled pot.
     */
    private InteractionResultHolder<ItemStack> fillPot(Level level, Player player, InteractionHand hand,
            ItemStack emptyStack,
            BlockPos pos, SoundEvent fillSound, ItemStack filledStack) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            level.playSound(null, pos, fillSound, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, filledStack);
            }

            emptyStack.shrink(1);
            if (emptyStack.isEmpty()) {
                player.setItemInHand(hand, filledStack);
            } else {
                if (!player.getInventory().add(filledStack)) {
                    player.drop(filledStack, false);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
}