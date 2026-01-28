package com.torr.materia.block;

import com.torr.materia.ModItems;
import com.torr.materia.config.materiaCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;

public class JoistsBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_GRAPE_VINE = BooleanProperty.create("has_grape_vine");
    public static final BooleanProperty HAS_GRAPES = BooleanProperty.create("has_grapes");
    public static final BooleanProperty HAS_HOPS_VINE = BooleanProperty.create("has_hops_vine");
    public static final BooleanProperty HAS_HOPS = BooleanProperty.create("has_hops");
    public static final BooleanProperty HAS_WISTERIA_VINE = BooleanProperty.create("has_wisteria_vine");
    public static final BooleanProperty HAS_WISTERIA_FLOWERS = BooleanProperty.create("has_wisteria_flowers");
    
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public JoistsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(HAS_GRAPE_VINE, false)
            .setValue(HAS_GRAPES, false)
            .setValue(HAS_HOPS_VINE, false)
            .setValue(HAS_HOPS, false)
            .setValue(HAS_WISTERIA_VINE, false)
            .setValue(HAS_WISTERIA_FLOWERS, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_GRAPE_VINE, HAS_GRAPES, HAS_HOPS_VINE, HAS_HOPS, HAS_WISTERIA_VINE, HAS_WISTERIA_FLOWERS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE;
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HAS_GRAPE_VINE) || state.getValue(HAS_HOPS_VINE) || state.getValue(HAS_WISTERIA_VINE);
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean preventOverlap = materiaCommonConfig.VINES_PREVENT_GRAPE_WISTERIA_OVERLAP.get();

        // Wisteria + grapes should never occupy the same block. If it happens (due to old saves / bugs),
        // resolve it deterministically to keep the world stable.
        if (preventOverlap && materiaCommonConfig.VINES_SELF_HEAL_SUPPORT_OVERLAP.get()) {
            boolean hasGrape = state.getValue(HAS_GRAPE_VINE) || state.getValue(HAS_GRAPES);
            boolean hasWisteria = state.getValue(HAS_WISTERIA_VINE) || state.getValue(HAS_WISTERIA_FLOWERS);
            if (hasGrape && hasWisteria) {
                BlockState fixed = state;
                if (state.getValue(HAS_GRAPES)) {
                    fixed = fixed.setValue(HAS_WISTERIA_VINE, false).setValue(HAS_WISTERIA_FLOWERS, false);
                } else if (state.getValue(HAS_WISTERIA_FLOWERS)) {
                    fixed = fixed.setValue(HAS_GRAPE_VINE, false).setValue(HAS_GRAPES, false);
                } else {
                    // Default: keep grape vine if neither has produced yet
                    fixed = fixed.setValue(HAS_WISTERIA_VINE, false).setValue(HAS_WISTERIA_FLOWERS, false);
                }
                level.setBlock(pos, fixed, 2);
                state = fixed;
            }
        }

        // Handle grape vines
        if (state.getValue(HAS_GRAPE_VINE)) {
            // Chance to grow grapes if we have vines but no grapes yet
            if (!state.getValue(HAS_GRAPES)) {
                if (random.nextInt(100) == 0) { // 1% chance per tick
                    level.setBlock(pos, state.setValue(HAS_GRAPES, true), 2);
                    return;
                }
            }
            
            // Try to place hanging grapes below if there's air and we have grapes
            if (state.getValue(HAS_GRAPES)) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);
                if (belowState.isAir()) {
                    if (random.nextInt(20) == 0) { // 5% chance per tick
                        level.setBlock(belowPos, com.torr.materia.ModBlocks.GRAPES_HANGING.get().defaultBlockState(), 3);
                    }
                }
            }
        }
        
        // Handle wisteria vines
        if (state.getValue(HAS_WISTERIA_VINE)) {
            // Chance to grow flowers if we have vines but no flowers yet
            if (!state.getValue(HAS_WISTERIA_FLOWERS)) {
                if (random.nextInt(100) == 0) { // 1% chance per tick
                    level.setBlock(pos, state.setValue(HAS_WISTERIA_FLOWERS, true), 2);
                    return;
                }
            }
            
            // Try to place hanging wisteria below if there's air
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            if (belowState.isAir()) {
                if (random.nextInt(20) == 0) { // 5% chance per tick
                    level.setBlock(belowPos, com.torr.materia.ModBlocks.WISTERIA_HANGING.get().defaultBlockState(), 3);
                }
            }
        }
        
        if (!state.getValue(HAS_GRAPE_VINE) && !state.getValue(HAS_WISTERIA_VINE)) {
            return;
        }
        
        int spreadChance = materiaCommonConfig.VINE_SUPPORT_CHAIN_SPREAD_CHANCE_PERCENT.get();
        if (spreadChance <= 0 || random.nextInt(100) >= spreadChance) {
            return;
        }
        
        // Check nearby positions for other support blocks
        for (Direction direction : Direction.values()) {
            for (int distance = 1; distance <= 3; distance++) {
                BlockPos checkPos = pos.relative(direction, distance);
                BlockState checkState = level.getBlockState(checkPos);
                Block checkBlock = checkState.getBlock();
                
                // Check if it's a support block that doesn't have vines yet
                if ((checkBlock instanceof TrellisBlock || checkBlock instanceof PostBlock || checkBlock instanceof JoistsBlock)) {
                    // Determine which type of vine to spread
                    if (state.getValue(HAS_GRAPE_VINE)) {
                        // Spread grape vines
                        if (checkBlock instanceof TrellisBlock
                                && !checkState.getValue(TrellisBlock.HAS_GRAPE_VINE)
                                && (!preventOverlap || !checkState.getValue(TrellisBlock.HAS_WISTERIA_VINE))) {
                            BlockState newState = checkState.setValue(TrellisBlock.HAS_GRAPE_VINE, true);
                            level.setBlock(checkPos, newState, 3);
                            return;
                        } else if (checkBlock instanceof PostBlock
                                && !checkState.getValue(PostBlock.HAS_GRAPE_VINE)
                                && (!preventOverlap || !checkState.getValue(PostBlock.HAS_WISTERIA_VINE))) {
                            BlockState newState = checkState.setValue(PostBlock.HAS_GRAPE_VINE, true);
                            level.setBlock(checkPos, newState, 3);
                            return;
                        } else if (checkBlock instanceof JoistsBlock
                                && !checkState.getValue(JoistsBlock.HAS_GRAPE_VINE)
                                && (!preventOverlap || !checkState.getValue(JoistsBlock.HAS_WISTERIA_VINE))) {
                            BlockState newState = checkState.setValue(JoistsBlock.HAS_GRAPE_VINE, true);
                            level.setBlock(checkPos, newState, 3);
                            return;
                        }
                    } else if (state.getValue(HAS_WISTERIA_VINE)) {
                        // Spread wisteria vines
                        if (checkBlock instanceof TrellisBlock
                                && !checkState.getValue(TrellisBlock.HAS_WISTERIA_VINE)
                                && (!preventOverlap || !checkState.getValue(TrellisBlock.HAS_GRAPE_VINE))) {
                            BlockState newState = checkState.setValue(TrellisBlock.HAS_WISTERIA_VINE, true);
                            level.setBlock(checkPos, newState, 3);
                            return;
                        } else if (checkBlock instanceof PostBlock
                                && !checkState.getValue(PostBlock.HAS_WISTERIA_VINE)
                                && (!preventOverlap || !checkState.getValue(PostBlock.HAS_GRAPE_VINE))) {
                            BlockState newState = checkState.setValue(PostBlock.HAS_WISTERIA_VINE, true);
                            level.setBlock(checkPos, newState, 3);
                            return;
                        } else if (checkBlock instanceof JoistsBlock
                                && !checkState.getValue(JoistsBlock.HAS_WISTERIA_VINE)
                                && (!preventOverlap || !checkState.getValue(JoistsBlock.HAS_GRAPE_VINE))) {
                            BlockState newState = checkState.setValue(JoistsBlock.HAS_WISTERIA_VINE, true);
                            level.setBlock(checkPos, newState, 3);
                            return;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                                InteractionHand hand, BlockHitResult hit) {
        // Harvest grapes
        if (state.getValue(HAS_GRAPE_VINE) && state.getValue(HAS_GRAPES)) {
            if (!level.isClientSide) {
                // Drop grapes (2-4)
                int grapeCount = 2 + level.random.nextInt(3);
                for (int i = 0; i < grapeCount; i++) {
                    popResource(level, pos, new ItemStack(ModItems.GRAPES.get()));
                }
                
                // Drop seeds (1-2)
                int seedCount = 1 + level.random.nextInt(2);
                for (int i = 0; i < seedCount; i++) {
                    popResource(level, pos, new ItemStack(ModItems.GRAPE_SEEDS.get()));
                }
                
                // Play harvest sound
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 
                              SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                
                // Remove grapes but keep the vines
                level.setBlock(pos, state.setValue(HAS_GRAPES, false), 2);
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        // Harvest wisteria (seeds and plant fiber, no fruit)
        if (state.getValue(HAS_WISTERIA_VINE) && state.getValue(HAS_WISTERIA_FLOWERS)) {
            if (!level.isClientSide) {
                // Drop seeds (1-3)
                int seedCount = 1 + level.random.nextInt(3);
                for (int i = 0; i < seedCount; i++) {
                    popResource(level, pos, new ItemStack(ModItems.WISTERIA_SEEDS.get()));
                }
                
                // Drop plant fiber (0-2)
                int fiberCount = level.random.nextInt(3);
                for (int i = 0; i < fiberCount; i++) {
                    popResource(level, pos, new ItemStack(ModItems.PLANT_FIBER.get()));
                }
                
                // Play harvest sound
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 
                              SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                
                // Remove flowers but keep the vines
                level.setBlock(pos, state.setValue(HAS_WISTERIA_FLOWERS, false), 2);
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return InteractionResult.PASS;
    }
}

