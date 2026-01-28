package com.torr.materia.block;

import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraft.util.RandomSource;

public class WisteriaHangingBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public WisteriaHangingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();
        
        // Can survive if attached to a trellis, post, or joists with wisteria vines
        if (aboveBlock instanceof TrellisBlock) {
            return aboveState.getValue(TrellisBlock.HAS_WISTERIA_VINE);
        } else if (aboveBlock instanceof PostBlock) {
            return aboveState.getValue(PostBlock.HAS_WISTERIA_VINE);
        } else if (aboveBlock instanceof JoistsBlock) {
            return aboveState.getValue(JoistsBlock.HAS_WISTERIA_VINE);
        }
        
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, 
                                 LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            if (!this.canSurvive(state, level, currentPos)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                                InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            RandomSource random = level.getRandom();
            
            // Small chance to drop wisteria seeds when interacted with
            if (random.nextFloat() < 0.2f) {
                popResource(level, pos, new ItemStack(ModItems.WISTERIA_SEEDS.get(), 1));
            }
            
            // Chance to drop plant fiber
            if (random.nextFloat() < 0.5f) {
                popResource(level, pos, new ItemStack(ModItems.PLANT_FIBER.get(), 1));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
