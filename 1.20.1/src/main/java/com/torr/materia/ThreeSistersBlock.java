package com.torr.materia;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;


public class ThreeSistersBlock extends CropBlock {
    // Properties for each crop type (0 = not present, 1-7 = growth stages)
    public static final IntegerProperty CORN_AGE = IntegerProperty.create("corn_age", 0, 7);
    public static final IntegerProperty BEANS_AGE = IntegerProperty.create("beans_age", 0, 7);
    public static final IntegerProperty SQUASH_AGE = IntegerProperty.create("squash_age", 0, 7);

    public ThreeSistersBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(CORN_AGE, 0)
                .setValue(BEANS_AGE, 0)
                .setValue(SQUASH_AGE, 0)
                .setValue(AGE, 0)); // Keep base AGE for compatibility
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CORN_AGE, BEANS_AGE, SQUASH_AGE, AGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // Check what type of seed is being planted
            if (itemStack.getItem() == ModItems.CORN.get()) {
                return plantCrop(state, level, pos, player, itemStack, CORN_AGE, "corn");
            } else if (itemStack.getItem() == ModItems.BEANS.get()) {
                return plantCrop(state, level, pos, player, itemStack, BEANS_AGE, "beans");
            } else if (itemStack.getItem() == ModItems.SQUASH_SEEDS.get()) {
                return plantCrop(state, level, pos, player, itemStack, SQUASH_AGE, "squash");
            }
        }
        
        return InteractionResult.PASS;
    }

    private InteractionResult plantCrop(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack, IntegerProperty ageProperty, String cropType) {
        // Check if this crop is already planted
        if (state.getValue(ageProperty) > 0) {
            return InteractionResult.FAIL; // Crop already present
        }

        // Plant the crop at age 1
        BlockState newState = state.setValue(ageProperty, 1);
        level.setBlock(pos, newState, 3);
        
        // Consume the seed item
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        
        return InteractionResult.SUCCESS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Each crop grows independently
        if (level.getRawBrightness(pos, 0) >= 9) {
            // Try to grow corn
            if (state.getValue(CORN_AGE) > 0 && state.getValue(CORN_AGE) < 7) {
                if (random.nextInt(3) == 0) { // Same growth rate as vanilla crops
                    int newCornAge = state.getValue(CORN_AGE) + 1;
                    level.setBlock(pos, state.setValue(CORN_AGE, newCornAge), 2);
                    
                    // Sync the upper corn block for tall stages
                    syncUpperCornBlock(level, pos, newCornAge);
                    
                    state = level.getBlockState(pos); // Update state reference
                }
            }
            
            // Try to grow beans
            if (state.getValue(BEANS_AGE) > 0 && state.getValue(BEANS_AGE) < 7) {
                if (random.nextInt(3) == 0) {
                    level.setBlock(pos, state.setValue(BEANS_AGE, state.getValue(BEANS_AGE) + 1), 2);
                    state = level.getBlockState(pos); // Update state reference
                }
            }
            
            // Try to grow squash
            if (state.getValue(SQUASH_AGE) > 0 && state.getValue(SQUASH_AGE) < 7) {
                if (random.nextInt(3) == 0) {
                    level.setBlock(pos, state.setValue(SQUASH_AGE, state.getValue(SQUASH_AGE) + 1), 2);
                }
            }
        }
    }

    private void syncUpperCornBlock(ServerLevel level, BlockPos pos, int cornAge) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        boolean shouldBeTall = cornAge >= 4;
        
        if (shouldBeTall) {
            if (aboveState.isAir()) {
                // Place the upper part using our custom Three Sisters corn upper block
                level.setBlock(abovePos, ModBlocks.THREE_SISTERS_CORN_UPPER.get().defaultBlockState()
                        .setValue(ThreeSistersCornUpperBlock.AGE, cornAge), 3);
            } else if (aboveState.getBlock() == ModBlocks.THREE_SISTERS_CORN_UPPER.get()) {
                // Update the existing upper block with new age
                if (aboveState.getValue(ThreeSistersCornUpperBlock.AGE) != cornAge) {
                    level.setBlock(abovePos, aboveState.setValue(ThreeSistersCornUpperBlock.AGE, cornAge), 2);
                }
            }
        } else {
            // Remove upper block if corn is not tall anymore (shouldn't happen in normal growth)
            if (aboveState.getBlock() == ModBlocks.THREE_SISTERS_CORN_UPPER.get()) {
                level.setBlock(abovePos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Must be on farmland
        return super.canSurvive(state, level, pos);
    }

    // Define our own shape array since SHAPE_BY_AGE is private
    private static final VoxelShape[] THREE_SISTERS_SHAPE_BY_AGE = new VoxelShape[]{
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),  // Age 0
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),  // Age 1
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),  // Age 2
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),  // Age 3
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), // Age 4
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), // Age 5
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), // Age 6
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)  // Age 7 (full grown)
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Use the tallest crop's shape for selection/visual bounds
        int cornAge = state.getValue(CORN_AGE);
        int beansAge = state.getValue(BEANS_AGE);
        int squashAge = state.getValue(SQUASH_AGE);
        
        // Use the tallest crop age for visual shape
        int maxAge = Math.max(Math.max(cornAge, beansAge), squashAge);
        if (maxAge == 0) {
            return THREE_SISTERS_SHAPE_BY_AGE[0]; // Empty block
        }
        
        return THREE_SISTERS_SHAPE_BY_AGE[Math.min(maxAge, 7)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Crops should have NO collision - players can walk through them
        return net.minecraft.world.phys.shapes.Shapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        // Tick if any crop is present and not fully grown
        return (state.getValue(CORN_AGE) > 0 && state.getValue(CORN_AGE) < 7) ||
               (state.getValue(BEANS_AGE) > 0 && state.getValue(BEANS_AGE) < 7) ||
               (state.getValue(SQUASH_AGE) > 0 && state.getValue(SQUASH_AGE) < 7);
    }

    // Helper methods to check what crops are present
    public boolean hasCorn(BlockState state) {
        return state.getValue(CORN_AGE) > 0;
    }

    public boolean hasBeans(BlockState state) {
        return state.getValue(BEANS_AGE) > 0;
    }

    public boolean hasSquash(BlockState state) {
        return state.getValue(SQUASH_AGE) > 0;
    }

    public int getCropCount(BlockState state) {
        int count = 0;
        if (hasCorn(state)) count++;
        if (hasBeans(state)) count++;
        if (hasSquash(state)) count++;
        return count;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player) {
        // If corn is tall, break the upper block too
        if (!level.isClientSide && state.getValue(CORN_AGE) >= 4) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            if (aboveState.getBlock() == ModBlocks.THREE_SISTERS_CORN_UPPER.get()) {
                level.setBlock(abovePos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, abovePos, Block.getId(aboveState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    // Override CropBlock methods that we don't use
    @Override
    protected net.minecraft.world.level.ItemLike getBaseSeedId() {
        return ModItems.CORN.get(); // Not really used since we handle planting manually
    }

    @Override
    public net.minecraft.world.level.block.state.properties.IntegerProperty getAgeProperty() {
        return AGE; // Use base AGE for compatibility, but we manage crops separately
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        // Handle bonemeal growth - grow all present crops
        if (state.getValue(CORN_AGE) > 0 && state.getValue(CORN_AGE) < 7) {
            int newCornAge = Math.min(7, state.getValue(CORN_AGE) + getBonemealAgeIncrease(level));
            state = state.setValue(CORN_AGE, newCornAge);
            if (level instanceof ServerLevel) {
                syncUpperCornBlock((ServerLevel)level, pos, newCornAge);
            }
        }
        
        if (state.getValue(BEANS_AGE) > 0 && state.getValue(BEANS_AGE) < 7) {
            state = state.setValue(BEANS_AGE, Math.min(7, state.getValue(BEANS_AGE) + getBonemealAgeIncrease(level)));
        }
        
        if (state.getValue(SQUASH_AGE) > 0 && state.getValue(SQUASH_AGE) < 7) {
            state = state.setValue(SQUASH_AGE, Math.min(7, state.getValue(SQUASH_AGE) + getBonemealAgeIncrease(level)));
        }
        
        level.setBlock(pos, state, 2);
    }
}
