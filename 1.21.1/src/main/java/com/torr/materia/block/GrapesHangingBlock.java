package com.torr.materia.block;

import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraft.util.RandomSource;

public class GrapesHangingBlock extends Block {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public GrapesHangingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 2;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < 2 && random.nextInt(5) == 0) { // 20% chance to grow
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
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
        
        // Can survive if attached to a trellis, post, or joists with grapes
        if (aboveBlock instanceof TrellisBlock) {
            return aboveState.getValue(TrellisBlock.HAS_GRAPES);
        } else if (aboveBlock instanceof PostBlock) {
            return aboveState.getValue(PostBlock.HAS_GRAPES);
        } else if (aboveBlock instanceof JoistsBlock) {
            return aboveState.getValue(JoistsBlock.HAS_GRAPES);
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
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                                                  InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = handleUse(state, level, pos);
        if (result == InteractionResult.PASS) return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (result == InteractionResult.FAIL) return net.minecraft.world.ItemInteractionResult.FAIL;
        return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return handleUse(state, level, pos);
    }

    private InteractionResult handleUse(BlockState state, Level level, BlockPos pos) {
        int age = state.getValue(AGE);
        
        // Only harvest if mature (age 2)
        if (age == 2) {
            if (!level.isClientSide) {
                RandomSource random = level.getRandom();
                
                // Drop grapes when harvested
                int grapeCount = 1 + random.nextInt(3); // 1-3 grapes
                for (int i = 0; i < grapeCount; i++) {
                    popResource(level, pos, new ItemStack(ModItems.GRAPES.get(), 1));
                }
                
                // Small chance to drop grape seeds
                if (random.nextFloat() < 0.3f) {
                    popResource(level, pos, new ItemStack(ModItems.GRAPE_SEEDS.get(), 1));
                }
                
                // Play harvest sound
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 
                              SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                
                // Revert to immature state
                level.setBlock(pos, state.setValue(AGE, 0), 2);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return InteractionResult.PASS;
    }
}

