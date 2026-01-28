package com.torr.materia;

import com.torr.materia.blockentity.MilkPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;

public class MilkPotBlock extends Block implements EntityBlock {
    protected static final VoxelShape POT_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water_level", 0, 3);

    public MilkPotBlock(Properties properties) {
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

        // Check if player is trying to use a crucible (fill it with milk)
        if (held.is(ModItems.CRUCIBLE.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MilkPotBlockEntity potEntity) {
                    if (potEntity.hasMilk()) {
                        // Convert crucible to milk cup and reduce milk level
                        ItemStack milkCup = new ItemStack(ModItems.MILK_CUP.get());
                        potEntity.setMilkLevel(potEntity.getMilkLevel() - 1);
                        
                        // Play milk fill sound
                        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        
                        // Replace crucible with milk cup in the same slot
                        ItemStack result = ItemUtils.createFilledResult(held, player, milkCup);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Glass bottle interactions: glass bottle takes 1 level, milk bottle adds 1 level
        if (held.is(Items.GLASS_BOTTLE)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MilkPotBlockEntity potEntity) {
                    if (potEntity.hasMilk()) {
                        potEntity.setMilkLevel(potEntity.getMilkLevel() - 1);

                        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack milkBottle = new ItemStack(ModItems.MILK_BOTTLE.get());
                        ItemStack result = ItemUtils.createFilledResult(held, player, milkBottle);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (held.is(ModItems.MILK_BOTTLE.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MilkPotBlockEntity potEntity) {
                    if (potEntity.canAddMilk()) {
                        potEntity.setMilkLevel(potEntity.getMilkLevel() + 1);

                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                        ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Bucket interactions: vanilla milk bucket fills pot to full, empty bucket takes full pot
        if (held.is(Items.MILK_BUCKET)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MilkPotBlockEntity potEntity) {
                    if (potEntity.canAddMilk()) {
                        // Fill to full
                        potEntity.setMilkLevel(3);

                        // Sound and replace with empty bucket
                        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                        ItemStack result = ItemUtils.createFilledResult(held, player, emptyBucket);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (held.is(Items.BUCKET)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MilkPotBlockEntity potEntity) {
                    if (potEntity.getMilkLevel() == 3) {
                        // Take a full bucket out
                        potEntity.setMilkLevel(0);

                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
                        ItemStack result = ItemUtils.createFilledResult(held, player, milkBucket);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Check if player is trying to use a milk pot block (transfer milk)
        if (held.is(ModBlocks.MILK_POT.get().asItem())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MilkPotBlockEntity targetPotEntity) {
                    if (targetPotEntity.canAddMilk()) {
                        // Transfer 1 milk level from milk pot to this pot
                        targetPotEntity.setMilkLevel(targetPotEntity.getMilkLevel() + 1);
                        
                        // Play milk pour sound
                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        
                        // Convert milk pot to empty pot
                        ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                        ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Check if player is trying to use a milk cup (add milk to pot)
        if (held.is(ModItems.MILK_CUP.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MilkPotBlockEntity potEntity) {
                    if (potEntity.canAddMilk()) {
                        // Convert milk cup to crucible and increase milk level
                        ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                        potEntity.setMilkLevel(potEntity.getMilkLevel() + 1);
                        
                        // Play milk pour sound
                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        
                        // Replace milk cup with crucible in the same slot
                        ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MilkPotBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        // When a milk pot block is placed, it should start with full milk (level 3)
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MilkPotBlockEntity potEntity) {
                potEntity.setMilkLevel(3);
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
