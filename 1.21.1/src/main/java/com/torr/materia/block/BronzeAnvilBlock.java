package com.torr.materia.block;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.blockentity.BronzeAnvilBlockEntity;
import com.torr.materia.capability.TongsCapability;
import com.torr.materia.utils.AnvilTongsUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BronzeAnvilBlock extends BaseEntityBlock {
    public static final MapCodec<BronzeAnvilBlock> CODEC = simpleCodec(BronzeAnvilBlock::new);

    protected static final VoxelShape ANVIL_SHAPE = Block.box(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 16.0D);

    public BronzeAnvilBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ANVIL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ANVIL_SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ANVIL_SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof BronzeAnvilBlockEntity) {
                ((BronzeAnvilBlockEntity) blockentity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = handleUse(state, level, pos, player, hand, heldItem);
        if (result == InteractionResult.PASS) return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (result == InteractionResult.FAIL) return net.minecraft.world.ItemInteractionResult.FAIL;
        return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return handleUse(state, level, pos, player, InteractionHand.MAIN_HAND, player.getItemInHand(InteractionHand.MAIN_HAND));
    }

    private InteractionResult handleUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack heldItem) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof BronzeAnvilBlockEntity anvilEntity) {
                // Check if player is holding tongs
                var tongsCapOptional = heldItem.getCapability(TongsCapability.TONGS_CAPABILITY);
                if (tongsCapOptional.isPresent()) {
                    return AnvilTongsUtils.handleTongsInteraction(anvilEntity, heldItem, player.isShiftKeyDown());
                }
                
                // Normal anvil GUI interaction
                ((ServerPlayer) player).openMenu(anvilEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BronzeAnvilBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BRONZE_ANVIL_BLOCK_ENTITY.get(),
                BronzeAnvilBlockEntity::tick);
    }
} 