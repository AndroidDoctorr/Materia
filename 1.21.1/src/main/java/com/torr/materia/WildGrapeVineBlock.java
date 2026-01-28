package com.torr.materia;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;


public class WildGrapeVineBlock extends VineBlock {
    public static final BooleanProperty NORTH = VineBlock.NORTH;
    public static final BooleanProperty EAST = VineBlock.EAST;
    public static final BooleanProperty SOUTH = VineBlock.SOUTH;
    public static final BooleanProperty WEST = VineBlock.WEST;
    public static final BooleanProperty UP = VineBlock.UP;

    public WildGrapeVineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP);
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Only harvest on empty-hand interaction; otherwise let the item/default interaction run.
        if (!stack.isEmpty()) return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        InteractionResult result = handleUse(level, pos);
        if (result == InteractionResult.PASS) return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (result == InteractionResult.FAIL) return net.minecraft.world.ItemInteractionResult.FAIL;
        return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return handleUse(level, pos);
    }

    private InteractionResult handleUse(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            // Chance to drop grape seeds when right-clicked
            RandomSource random = level.getRandom();
            if (random.nextFloat() < 0.3f) { // 30% chance
                popResource(level, pos, new ItemStack(ModItems.GRAPE_SEEDS.get(), 1));
            }
            
            // Always drop plant fiber
            if (random.nextFloat() < 0.7f) { // 70% chance
                popResource(level, pos, new ItemStack(ModItems.PLANT_FIBER.get(), 1));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, 
                            net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        if (!level.isClientSide) {
            RandomSource random = level.getRandom();
            
            if (random.nextFloat() < 0.2f) {
                popResource(level, pos, new ItemStack(ModItems.GRAPE_SEEDS.get(), 1));
            }
            
            if (random.nextFloat() < 0.5f) {
                popResource(level, pos, new ItemStack(ModItems.PLANT_FIBER.get(), 1));
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return this.hasFaces(this.getUpdatedState(state, level, pos));
    }

    private BlockState getUpdatedState(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos northPos = pos.relative(Direction.NORTH);
        BlockPos eastPos = pos.relative(Direction.EAST);
        BlockPos southPos = pos.relative(Direction.SOUTH);
        BlockPos westPos = pos.relative(Direction.WEST);
        BlockPos upPos = pos.relative(Direction.UP);
        
        return state
                .setValue(NORTH, this.isAcceptableNeighbour(level, northPos, Direction.NORTH))
                .setValue(EAST, this.isAcceptableNeighbour(level, eastPos, Direction.EAST))
                .setValue(SOUTH, this.isAcceptableNeighbour(level, southPos, Direction.SOUTH))
                .setValue(WEST, this.isAcceptableNeighbour(level, westPos, Direction.WEST))
                .setValue(UP, this.isAcceptableNeighbour(level, upPos, Direction.UP));
    }

    private boolean isAcceptableNeighbour(LevelReader level, BlockPos pos, Direction direction) {
        BlockState blockState = level.getBlockState(pos);
        
        if (isLog(blockState)) {
            return Block.isFaceFull(blockState.getCollisionShape(level, pos), direction.getOpposite());
        }
        
        return false;
    }

    private boolean isLog(BlockState state) {
        return state.is(Blocks.OAK_LOG) || 
               state.is(Blocks.SPRUCE_LOG) || 
               state.is(Blocks.BIRCH_LOG) || 
               state.is(Blocks.JUNGLE_LOG) || 
               state.is(Blocks.ACACIA_LOG) || 
               state.is(Blocks.MANGROVE_LOG) ||
               state.is(Blocks.CRIMSON_STEM) ||
               state.is(Blocks.WARPED_STEM) ||
               state.is(Blocks.CRIMSON_HYPHAE) ||
               state.is(Blocks.WARPED_HYPHAE) ||
               state.is(Blocks.STRIPPED_CRIMSON_STEM) ||
               state.is(Blocks.STRIPPED_WARPED_STEM) ||
               state.is(Blocks.STRIPPED_CRIMSON_HYPHAE) ||
               state.is(Blocks.STRIPPED_WARPED_HYPHAE) ||
               state.is(Blocks.DARK_OAK_LOG) ||
               state.is(ModBlocks.RUBBER_TREE_LOG.get()) ||
               state.is(ModBlocks.OLIVE_TREE_LOG.get());
    }

    private boolean hasFaces(BlockState state) {
        return state.getValue(NORTH) || state.getValue(EAST) || 
               state.getValue(SOUTH) || state.getValue(WEST) || state.getValue(UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, 
                                LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (direction == Direction.DOWN) {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        } else {
            BlockState updatedState = this.getUpdatedState(state, level, currentPos);
            return this.hasFaces(updatedState) ? updatedState : Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }
}
