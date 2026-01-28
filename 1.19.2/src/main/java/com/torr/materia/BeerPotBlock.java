package com.torr.materia;

import com.torr.materia.blockentity.BeerPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BeerPotBlock extends Block implements EntityBlock {
    protected static final VoxelShape POT_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water_level", 0, 3);

    public BeerPotBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATER_LEVEL, 3));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATER_LEVEL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        // Crucible: take a beer cup
        if (held.is(ModItems.CRUCIBLE.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BeerPotBlockEntity potEntity && potEntity.hasBeer()) {
                    ItemStack beerCup = new ItemStack(ModItems.BEER_CUP.get());
                    int next = potEntity.getBeerLevel() - 1;
                    potEntity.setBeerLevel(next);

                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerCup);
                    player.setItemInHand(hand, result);

                    if (next <= 0) {
                        level.setBlock(pos, ModBlocks.POT.get().defaultBlockState(), 3);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Glass bottle: take a beer bottle
        if (held.is(Items.GLASS_BOTTLE)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BeerPotBlockEntity potEntity && potEntity.hasBeer()) {
                    ItemStack beerBottle = new ItemStack(ModItems.BEER_BOTTLE.get());
                    int next = potEntity.getBeerLevel() - 1;
                    potEntity.setBeerLevel(next);

                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerBottle);
                    player.setItemInHand(hand, result);

                    if (next <= 0) {
                        level.setBlock(pos, ModBlocks.POT.get().defaultBlockState(), 3);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Empty bucket: take a full beer bucket (only when full)
        if (held.is(Items.BUCKET)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BeerPotBlockEntity potEntity && potEntity.getBeerLevel() == 3) {
                    potEntity.setBeerLevel(0);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack beerBucket = new ItemStack(ModItems.BEER_BUCKET.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerBucket);
                    player.setItemInHand(hand, result);
                    level.setBlock(pos, ModBlocks.POT.get().defaultBlockState(), 3);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Beer bottle: pour in (1 level)
        if (held.is(ModItems.BEER_BOTTLE.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BeerPotBlockEntity potEntity && potEntity.canAddBeer()) {
                    potEntity.setBeerLevel(potEntity.getBeerLevel() + 1);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                    ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                    player.setItemInHand(hand, result);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Beer cup: pour in (1 level)
        if (held.is(ModItems.BEER_CUP.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BeerPotBlockEntity potEntity && potEntity.canAddBeer()) {
                    potEntity.setBeerLevel(potEntity.getBeerLevel() + 1);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                    player.setItemInHand(hand, result);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Beer bucket: fill to full
        if (held.is(ModItems.BEER_BUCKET.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BeerPotBlockEntity potEntity && potEntity.canAddBeer()) {
                    potEntity.setBeerLevel(3);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                    ItemStack result = ItemUtils.createFilledResult(held, player, emptyBucket);
                    player.setItemInHand(hand, result);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Beer pot item: pour into an EMPTY placed beer pot (fills to full)
        if (held.is(ModItems.BEER_POT.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BeerPotBlockEntity potEntity) {
                    if (potEntity.getBeerLevel() == 0) {
                        potEntity.setBeerLevel(3);
                        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                        ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                        player.setItemInHand(hand, result);
                    } else {
                        player.displayClientMessage(Component.translatable("message.materia.pot.not_empty"), true);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BeerPotBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BeerPotBlockEntity potEntity) {
                potEntity.setBeerLevel(3);
            }
        }
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
}
