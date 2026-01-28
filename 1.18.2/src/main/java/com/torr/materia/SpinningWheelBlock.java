package com.torr.materia;

import com.torr.materia.ModItems;
import com.torr.materia.blockentity.SpinningWheelBlockEntity;
import com.torr.materia.utils.TextileUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

public class SpinningWheelBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty FRAME = IntegerProperty.create("frame", 0, 1);
    private static final int ANIMATION_TICKS = 5;

    // Define a shape for the spinning wheel based on the JSON model
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D);

    public SpinningWheelBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(FRAME, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, FRAME);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        // Only start the animation loop when the block is first placed.
        // State changes (frame toggles) should not enqueue additional ticks, or you'll get flicker.
        if (!level.isClientSide && oldState.getBlock() != state.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SpinningWheelBlockEntity spinningWheel) {
                spinningWheel.setNextAnimGameTime(level.getGameTime() + ANIMATION_TICKS);
            }
            level.scheduleTick(pos, this, ANIMATION_TICKS);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!state.hasProperty(FRAME)) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SpinningWheelBlockEntity spinningWheel) {
            long now = level.getGameTime();
            long nextAllowed = spinningWheel.getNextAnimGameTime();

            // If we got called early or multiple times for the same moment, don't double-flip.
            if (nextAllowed > 0 && now < nextAllowed) {
                int delay = (int) Math.max(1, Math.min(Integer.MAX_VALUE, nextAllowed - now));
                level.scheduleTick(pos, this, delay);
                return;
            }

            spinningWheel.setNextAnimGameTime(now + ANIMATION_TICKS);
        }

        int next = state.getValue(FRAME) == 0 ? 1 : 0;
        // Only need a client update + rerender for animation.
        level.setBlock(pos, state.setValue(FRAME, next), 2);
        level.scheduleTick(pos, this, ANIMATION_TICKS);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty() || held.getCount() < 2) return InteractionResult.PASS;

        Item outItem = TextileUtils.getStringItemForClump(held.getItem());
        if (outItem == null) return InteractionResult.PASS;

        if (!level.isClientSide) {
            int count = 2 + (level.random.nextFloat() < 0.10F ? 1 : 0);
            ItemStack out = new ItemStack(outItem, count);

            held.shrink(2);
            player.getInventory().placeItemBackInInventory(out);

            level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 0.8F, 1.1F);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return super.getDrops(state, builder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpinningWheelBlockEntity(pos, state);
    }
}
