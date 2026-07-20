package com.torr.materia.block;

import com.torr.materia.blockentity.MosaicBlockEntity;
import com.torr.materia.ModItems;
import com.torr.materia.mosaic.MosaicHarvest;
import com.torr.materia.mosaic.MosaicHitUtil;
import com.torr.materia.mosaic.MosaicItemData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Single-sided mosaic canvas. The painted face is the one toward the player at placement
 * (e.g. top face when placing on the ground while looking down).
 */
public class MosaicBlock extends BaseEntityBlock {

    /** Outward direction of the one editable face. */
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());

    public MosaicBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction paintFace = context.getNearestLookingDirection().getOpposite();
        return defaultBlockState().setValue(FACING, paintFace);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MosaicBlockEntity(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        MosaicBlockEntity mosaic = blockEntity instanceof MosaicBlockEntity entity ? entity : null;
        return MosaicItemData.createDrop(state, mosaic, true);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (!level.isClientSide) {
            MosaicBlockEntity mosaic = blockEntity instanceof MosaicBlockEntity entity ? entity : null;
            boolean preservePaint = MosaicHarvest.preservesPaint(tool, player);
            ItemStack drop = MosaicItemData.createDrop(state, mosaic, preservePaint);
            if (!drop.isEmpty()) {
                Block.popResource(level, pos, drop);
            }
        }
        level.removeBlock(pos, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        if (held.is(ModItems.SLAKED_LIME.get())) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof MosaicBlockEntity mosaic) || !mosaic.hasAnyPaint()) {
                return InteractionResult.PASS;
            }
            mosaic.clearCanvas();
            mosaic.setChanged();
            syncBlockEntity(level, pos, state, mosaic);
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 0.9F, 0.85F);
            return InteractionResult.CONSUME;
        }

        if (!held.is(ModItems.MOSAIC_STYLUS.get())) {
            return InteractionResult.PASS;
        }
        if (hit.getDirection() != state.getValue(FACING)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof MosaicBlockEntity mosaic)) {
            return InteractionResult.PASS;
        }

        MosaicHitUtil.FacePixel pixel = MosaicHitUtil.fromHit(pos, hit);
        mosaic.cyclePixel(pixel, player.isShiftKeyDown());
        mosaic.setChanged();
        syncBlockEntity(level, pos, state, mosaic);
        level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.35F, 1.4F);
        return InteractionResult.CONSUME;
    }

    private static void syncBlockEntity(Level level, BlockPos pos, BlockState state, MosaicBlockEntity mosaic) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(mosaic);
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                if (serverPlayer.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < 4096.0D) {
                    serverPlayer.connection.send(packet);
                }
            }
        }
    }
}
