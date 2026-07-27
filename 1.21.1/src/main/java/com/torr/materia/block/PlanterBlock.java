package com.torr.materia.block;

import com.torr.materia.blockentity.PlanterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PlanterBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 0, 16, 8, 8);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 8, 16, 8, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(0, 0, 0, 8, 8, 16);
    private static final VoxelShape SHAPE_EAST = Block.box(8, 0, 0, 16, 8, 16);

    public PlanterBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlanterBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                            InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = handleUse(level, pos, player, stack, hit);
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
        return handleUse(level, pos, player, ItemStack.EMPTY, hit);
    }

    private static InteractionResult handleUse(Level level, BlockPos pos, Player player, ItemStack stack,
                                               BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof PlanterBlockEntity planter)) {
            return InteractionResult.PASS;
        }

        boolean leftSlot = planter.isLeftSlot(pos, hit.getLocation());

        if (stack.isEmpty()) {
            Block removed = leftSlot ? planter.getLeftPlant() : planter.getRightPlant();
            if (removed == Blocks.AIR) {
                return InteractionResult.CONSUME;
            }
            if (!player.getAbilities().instabuild) {
                popResource(level, pos, new ItemStack(removed));
            }
            if (leftSlot) {
                planter.setLeftPlant(Blocks.AIR);
            } else {
                planter.setRightPlant(Blocks.AIR);
            }
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return InteractionResult.PASS;
        }
        Block plantBlock = blockItem.getBlock();
        if (!PlanterBlockEntity.accepts(plantBlock)) {
            return InteractionResult.PASS;
        }

        Block existing = leftSlot ? planter.getLeftPlant() : planter.getRightPlant();
        if (existing != Blocks.AIR) {
            return InteractionResult.PASS;
        }

        if (leftSlot) {
            planter.setLeftPlant(plantBlock);
        } else {
            planter.setRightPlant(plantBlock);
        }
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
            if (blockEntity instanceof PlanterBlockEntity planter) {
                dropPlant(level, pos, planter.getLeftPlant());
                dropPlant(level, pos, planter.getRightPlant());
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private static void dropPlant(Level level, BlockPos pos, Block plant) {
        if (plant != Blocks.AIR) {
            popResource(level, pos, new ItemStack(plant));
        }
    }
}
