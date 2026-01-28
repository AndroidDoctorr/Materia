package com.torr.materia.block;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
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

        // Sleep succeeded - schedule delayed dawn and cleanup using a simple timer
        if (level instanceof ServerLevel serverLevel) {
            new Thread(() -> {
                try {
                    Thread.sleep(5000); // 5 second delay
                    
                    // Execute on server thread
                    serverLevel.getServer().execute(() -> {
                        // Advance time to dawn
                        long currentTime = serverLevel.getDayTime();
                        long timeToMorning = 24000 - (currentTime % 24000);
                        if (timeToMorning > 12000) timeToMorning = 24000 - timeToMorning;
                        serverLevel.setDayTime(currentTime + timeToMorning);
                        
                        // Remove both bedroll blocks - calculate both positions correctly
                        Direction facing = state.getValue(FACING);
                        final BlockPos finalHeadPos;
                        final BlockPos finalFootPos;
                        if (part == BedPart.HEAD) {
                            // If we clicked the head, foot is in the opposite direction of facing
                            finalHeadPos = headPos;
                            finalFootPos = pos.relative(facing.getOpposite());
                        } else {
                            // If we clicked the foot, head is in the direction of facing
                            finalHeadPos = pos.relative(facing);
                            finalFootPos = pos;
                        }
                        
                        // Force remove both blocks
                        if (serverLevel.getBlockState(finalHeadPos).is(ModBlocks.BEDROLL.get())) {
                            serverLevel.setBlock(finalHeadPos, Blocks.AIR.defaultBlockState(), 11);
                        }
                        if (serverLevel.getBlockState(finalFootPos).is(ModBlocks.BEDROLL.get())) {
                            serverLevel.setBlock(finalFootPos, Blocks.AIR.defaultBlockState(), 11);
                        }
                        
                        // Give bedroll back to player
                        player.getInventory().add(new ItemStack(com.torr.materia.ModItems.BEDROLL.get()));
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        return InteractionResult.SUCCESS;
    }
}


