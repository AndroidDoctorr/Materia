package com.torr.materia;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PotBlock extends Block {
    protected static final VoxelShape POT_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    public PotBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return POT_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return POT_SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return POT_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        // Convert empty pot into a beer pot (from crucible -> beer cup)
        if (held.is(ModItems.BEER_CUP.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.BEER_POT.get().defaultBlockState()
                        .setValue(BeerPotBlock.WATER_LEVEL, 1);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                // Replace beer cup with crucible
                ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a beer pot (from bottle -> empty bottle)
        if (held.is(ModItems.BEER_BOTTLE.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.BEER_POT.get().defaultBlockState()
                        .setValue(BeerPotBlock.WATER_LEVEL, 1);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a beer pot (from bucket -> empty bucket)
        if (held.is(ModItems.BEER_BUCKET.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.BEER_POT.get().defaultBlockState()
                        .setValue(BeerPotBlock.WATER_LEVEL, 3);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBucket);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a beer pot (from pot item -> empty pot item)
        if (held.is(ModItems.BEER_POT.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.BEER_POT.get().defaultBlockState()
                        .setValue(BeerPotBlock.WATER_LEVEL, 3);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a wine pot (from crucible -> wine cup)
        if (held.is(ModItems.WINE_CUP.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.WINE_POT.get().defaultBlockState()
                        .setValue(WinePotBlock.WATER_LEVEL, 1);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                // Replace wine cup with crucible
                ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a wine pot (from bottle -> empty bottle)
        if (held.is(ModItems.WINE_BOTTLE.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.WINE_POT.get().defaultBlockState()
                        .setValue(WinePotBlock.WATER_LEVEL, 1);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a wine pot (from bucket -> empty bucket)
        if (held.is(ModItems.WINE_BUCKET.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.WINE_POT.get().defaultBlockState()
                        .setValue(WinePotBlock.WATER_LEVEL, 3);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBucket);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a wine pot (from pot item -> empty pot item)
        if (held.is(ModItems.WINE_POT.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.WINE_POT.get().defaultBlockState()
                        .setValue(WinePotBlock.WATER_LEVEL, 3);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a water pot when used with a water cup
        if (held.is(ModItems.WATER_CUP.get())) {
            if (!level.isClientSide) {
                // Replace with water pot block, starting at level 1, not boiling
                BlockState newState = ModBlocks.WATER_POT.get().defaultBlockState()
                        .setValue(WaterPotBlock.WATER_LEVEL, 1)
                        .setValue(WaterPotBlock.BOILING, false);
                level.setBlock(pos, newState, 3);

                // Play pour sound
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                // Replace water cup with crucible
                ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Water bucket fills empty pot to full and converts to WaterPot
        if (held.is(Items.WATER_BUCKET)) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.WATER_POT.get().defaultBlockState()
                        .setValue(WaterPotBlock.WATER_LEVEL, 3)
                        .setValue(WaterPotBlock.BOILING, false);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBucket);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Water bottle fills empty pot by 1 level and converts to WaterPot
        if (held.is(Items.POTION) && PotionUtils.getPotion(held) == Potions.WATER) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.WATER_POT.get().defaultBlockState()
                        .setValue(WaterPotBlock.WATER_LEVEL, 1)
                        .setValue(WaterPotBlock.BOILING, false);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Sneak-use with a cauldron above: pull water into pot and convert
        if (player.isShiftKeyDown()) {
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.is(Blocks.CAULDRON) || aboveState.is(Blocks.WATER_CAULDRON)) {
                if (!level.isClientSide) {
                    int current = aboveState.is(Blocks.WATER_CAULDRON) ? aboveState.getValue(LayeredCauldronBlock.LEVEL) : 0;
                    if (current > 0) {
                        // Pull up to 3 levels and convert pot to water pot
                        int transfer = Math.min(3, current);
                        BlockState newCauldron = current - transfer == 0 ? Blocks.CAULDRON.defaultBlockState()
                                : aboveState.setValue(LayeredCauldronBlock.LEVEL, current - transfer);
                        level.setBlock(above, newCauldron, 3);

                        BlockState newPot = ModBlocks.WATER_POT.get().defaultBlockState()
                                .setValue(WaterPotBlock.WATER_LEVEL, transfer)
                                .setValue(WaterPotBlock.BOILING, false);
                        level.setBlock(pos, newPot, 3);
                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}