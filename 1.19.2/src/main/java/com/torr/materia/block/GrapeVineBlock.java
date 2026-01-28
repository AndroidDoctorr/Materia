package com.torr.materia.block;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import com.torr.materia.config.materiaCommonConfig;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.RandomSource;

public class GrapeVineBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
    
    // Tag for grape vine supports
    private static final TagKey<Block> GRAPE_VINE_SUPPORTS = BlockTags.create(
        net.minecraft.resources.ResourceLocation.tryParse("materia:grape_vine_supports")
    );
    
    // We don't need a mapping since we're using the same blocks with different states
    
    // Shapes for different growth stages
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
        Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D),  // Age 0 - small plant
        Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),  // Age 1 - growing vine
        Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D), // Age 2 - larger vine
        Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)  // Age 3 - mature vine with grapes
    };

    public GrapeVineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected net.minecraft.world.level.ItemLike getBaseSeedId() {
        return ModItems.GRAPE_SEEDS.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(AGE)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // No collision shape - players can walk through grape vines like other crops
        return Shapes.empty();
    }
    
    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return getShape(state, level, pos, CollisionContext.empty());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Check if planted on farmland (for initial growth)
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        
        if (belowState.getBlock() instanceof FarmBlock) {
            return true;
        }
        
        // Check if attached to a support structure
        return hasNearbySupport(level, pos);
    }

    private boolean hasNearbySupport(LevelReader level, BlockPos pos) {
        // Check all horizontal directions and up for support blocks
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN) continue;
            
            BlockPos checkPos = pos.relative(direction);
            BlockState checkState = level.getBlockState(checkPos);
            
            if (checkState.is(GRAPE_VINE_SUPPORTS)) {
                return true;
            }
        }
        return false;
    }
    
    // Attach vine to nearby support structures by changing them to grape vine variants
    private boolean attachToNearbySupports(ServerLevel level, BlockPos pos) {
        boolean attached = false;
        boolean preventOverlap = materiaCommonConfig.VINES_PREVENT_GRAPE_WISTERIA_OVERLAP.get();
        
        // Check all horizontal directions and up for support blocks
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN) continue;
            
            BlockPos checkPos = pos.relative(direction);
            BlockState checkState = level.getBlockState(checkPos);
            Block checkBlock = checkState.getBlock();
            
            if (checkState.is(GRAPE_VINE_SUPPORTS)) {
                // Check if the support block can have grape vines
                if (checkBlock instanceof TrellisBlock) {
                    boolean hasWisteria = preventOverlap
                            && (checkState.getValue(TrellisBlock.HAS_WISTERIA_VINE) || checkState.getValue(TrellisBlock.HAS_WISTERIA_FLOWERS));
                    if (!checkState.getValue(TrellisBlock.HAS_GRAPE_VINE) && !hasWisteria) {
                        // Set the trellis to have grape vines
                        BlockState newState = checkState.setValue(TrellisBlock.HAS_GRAPE_VINE, true);
                        level.setBlock(checkPos, newState, 3);
                        attached = true;
                    } else if (checkState.getValue(TrellisBlock.HAS_GRAPE_VINE)) {
                        // Already has grape vines, still counts as attached
                        attached = true;
                    }
                } else if (checkBlock instanceof PostBlock) {
                    boolean hasWisteria = preventOverlap
                            && (checkState.getValue(PostBlock.HAS_WISTERIA_VINE) || checkState.getValue(PostBlock.HAS_WISTERIA_FLOWERS));
                    if (!checkState.getValue(PostBlock.HAS_GRAPE_VINE) && !hasWisteria) {
                        // Set the post to have grape vines
                        BlockState newState = checkState.setValue(PostBlock.HAS_GRAPE_VINE, true);
                        level.setBlock(checkPos, newState, 3);
                        attached = true;
                    } else if (checkState.getValue(PostBlock.HAS_GRAPE_VINE)) {
                        // Already has grape vines, still counts as attached
                        attached = true;
                    }
                } else if (checkBlock instanceof JoistsBlock) {
                    boolean hasWisteria = preventOverlap
                            && (checkState.getValue(JoistsBlock.HAS_WISTERIA_VINE) || checkState.getValue(JoistsBlock.HAS_WISTERIA_FLOWERS));
                    if (!checkState.getValue(JoistsBlock.HAS_GRAPE_VINE) && !hasWisteria) {
                        // Set the joists to have grape vines
                        BlockState newState = checkState.setValue(JoistsBlock.HAS_GRAPE_VINE, true);
                        level.setBlock(checkPos, newState, 3);
                        attached = true;
                    } else if (checkState.getValue(JoistsBlock.HAS_GRAPE_VINE)) {
                        // Already has grape vines, still counts as attached
                        attached = true;
                    }
                }
            }
        }
        
        return attached;
    }
    
    // Check if there are any support structures with grape vines nearby
    private boolean hasNearbyGrapeVineSupport(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN) continue;
            
            BlockPos checkPos = pos.relative(direction);
            BlockState checkState = level.getBlockState(checkPos);
            Block checkBlock = checkState.getBlock();
            
            if (checkState.is(GRAPE_VINE_SUPPORTS)) {
                if (checkBlock instanceof TrellisBlock && checkState.getValue(TrellisBlock.HAS_GRAPE_VINE)) {
                    return true;
                } else if (checkBlock instanceof PostBlock && checkState.getValue(PostBlock.HAS_GRAPE_VINE)) {
                    return true;
                } else if (checkBlock instanceof JoistsBlock && checkState.getValue(JoistsBlock.HAS_GRAPE_VINE)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = state.getValue(AGE);
            
            // Normal growth
            if (age < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, level, pos);
                if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 2);
                }
            }
            // When mature, try to spread to nearby support blocks
            else if (age == this.getMaxAge()) {
                int chance = materiaCommonConfig.VINE_PLANT_SPREAD_CHANCE_PERCENT.get();
                if (chance > 0 && random.nextInt(100) < chance) {
                    attachToNearbySupports(level, pos);
                }
            }
        }
    }
    
    // Grape vines only survive on farmland
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, 
                                LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

}
