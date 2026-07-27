package com.torr.materia.block;

import com.torr.materia.blockentity.UrnBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class UrnBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 14, 14);

    public UrnBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new UrnBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                            InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = handleUse(level, pos, player, stack);
        if (result == InteractionResult.PASS) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (result == InteractionResult.FAIL) {
            return ItemInteractionResult.FAIL;
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        return handleUse(level, pos, player, ItemStack.EMPTY);
    }

    private static InteractionResult handleUse(Level level, BlockPos pos, Player player, ItemStack stack) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof UrnBlockEntity urn)) {
            return InteractionResult.PASS;
        }

        if (stack.isEmpty()) {
            Block removed = urn.getPlant();
            if (removed == Blocks.AIR) {
                return InteractionResult.CONSUME;
            }
            if (!player.getAbilities().instabuild) {
                popResource(level, pos, new ItemStack(removed));
            }
            urn.setPlant(Blocks.AIR);
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return InteractionResult.PASS;
        }
        Block plantBlock = blockItem.getBlock();
        if (!UrnBlockEntity.accepts(plantBlock)) {
            return InteractionResult.PASS;
        }
        if (urn.getPlant() != Blocks.AIR) {
            return InteractionResult.PASS;
        }

        urn.setPlant(plantBlock);
        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof UrnBlockEntity urn && urn.getPlant() != Blocks.AIR) {
                popResource(level, pos, new ItemStack(urn.getPlant()));
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
