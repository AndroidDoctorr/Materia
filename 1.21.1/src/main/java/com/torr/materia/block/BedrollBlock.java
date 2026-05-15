package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

public class BedrollBlock extends BedBlock {

    private static final VoxelShape LOW_SLICE = box(0, 0, 0, 16, 3, 16);

    public BedrollBlock(Properties properties) {
        super(DyeColor.WHITE, properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LOW_SLICE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LOW_SLICE; // Low profile collision so you can stand on it briefly
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // Use block model JSON instead of the bed's entity renderer
        return RenderShape.MODEL;
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = handleUse(state, level, pos, player);
        if (result == InteractionResult.PASS) return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (result == InteractionResult.FAIL) return net.minecraft.world.ItemInteractionResult.FAIL;
        return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return handleUse(state, level, pos, player);
    }

    private InteractionResult handleUse(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // Make sure we're using the head block for sleep
        BedPart part = state.getValue(PART);
        BlockPos headPos = part == BedPart.HEAD ? pos : pos.relative(state.getValue(FACING));
        
        // Start sleep
        var sleepResult = player.startSleepInBed(headPos);
        if (sleepResult.left().isPresent()) {
            // Sleep failed
            Player.BedSleepingProblem problem = sleepResult.left().get();
            if (problem.getMessage() != null) {
                player.displayClientMessage(problem.getMessage(), true);
            }
            return InteractionResult.FAIL;
        }

        // Dawn, block removal, and returning the bedroll item are handled only by BedrollSleepHandler
        return InteractionResult.SUCCESS;
    }
}


