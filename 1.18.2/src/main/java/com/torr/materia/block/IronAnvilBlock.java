package com.torr.materia.block;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.blockentity.IronAnvilBlockEntity;
import com.torr.materia.capability.TongsCapability;
import com.torr.materia.utils.AnvilTongsUtils;
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
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class IronAnvilBlock extends BaseEntityBlock {
    protected static final VoxelShape ANVIL_SHAPE = Block.box(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 16.0D);

    public IronAnvilBlock(Properties properties) {
        super(properties);
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
            if (blockentity instanceof IronAnvilBlockEntity) {
                ((IronAnvilBlockEntity) blockentity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof IronAnvilBlockEntity anvilEntity) {
                ItemStack heldItem = player.getItemInHand(hand);
                
                // Check if player is holding tongs
                var tongsCapOptional = heldItem.getCapability(TongsCapability.TONGS_CAPABILITY);
                if (tongsCapOptional.isPresent()) {
                    return AnvilTongsUtils.handleTongsInteraction(anvilEntity, heldItem, player.isShiftKeyDown());
                }
                
                // Normal anvil GUI interaction
                NetworkHooks.openGui((ServerPlayer) player, (IronAnvilBlockEntity) entity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IronAnvilBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.IRON_ANVIL_BLOCK_ENTITY.get(),
                IronAnvilBlockEntity::tick);
    }
} 