package com.torr.materia.block;

import com.torr.materia.ModItems;
import com.torr.materia.config.materiaCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class HopsVineBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    private static final TagKey<Block> HOPS_VINE_SUPPORTS = BlockTags.create(
            net.minecraft.resources.ResourceLocation.tryParse("materia:hops_vine_supports")
    );

    public HopsVineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
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
        return ModItems.HOPS_SEEDS.get();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        // Seed-planted crop vines only survive on farmland.
        // Support structure growth is represented on support block states, not free-standing crop blocks.
        return belowState.getBlock() instanceof FarmBlock;
    }

    private boolean hasNearbySupport(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN) continue;
            BlockState checkState = level.getBlockState(pos.relative(direction));
            Block checkBlock = checkState.getBlock();
            if (checkState.is(HOPS_VINE_SUPPORTS)
                    || checkBlock instanceof TrellisBlock
                    || checkBlock instanceof PostBlock
                    || checkBlock instanceof JoistsBlock) {
                return true;
            }
        }
        return false;
    }

    private boolean attachToNearbySupports(ServerLevel level, BlockPos pos) {
        boolean attached = false;

        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN) continue;

            BlockPos checkPos = pos.relative(direction);
            BlockState checkState = level.getBlockState(checkPos);
            Block checkBlock = checkState.getBlock();

            if (!checkState.is(HOPS_VINE_SUPPORTS)
                    && !(checkBlock instanceof TrellisBlock)
                    && !(checkBlock instanceof PostBlock)
                    && !(checkBlock instanceof JoistsBlock)) continue;

            if (checkBlock instanceof TrellisBlock) {
                boolean hasOther = (checkState.getValue(TrellisBlock.HAS_GRAPE_VINE) || checkState.getValue(TrellisBlock.HAS_GRAPES))
                        || (checkState.getValue(TrellisBlock.HAS_WISTERIA_VINE) || checkState.getValue(TrellisBlock.HAS_WISTERIA_FLOWERS));
                if (!checkState.getValue(TrellisBlock.HAS_HOPS_VINE) && !hasOther) {
                    level.setBlock(checkPos, checkState.setValue(TrellisBlock.HAS_HOPS_VINE, true), 3);
                    attached = true;
                } else if (checkState.getValue(TrellisBlock.HAS_HOPS_VINE)) {
                    attached = true;
                }
            } else if (checkBlock instanceof PostBlock) {
                boolean hasOther = (checkState.getValue(PostBlock.HAS_GRAPE_VINE) || checkState.getValue(PostBlock.HAS_GRAPES))
                        || (checkState.getValue(PostBlock.HAS_WISTERIA_VINE) || checkState.getValue(PostBlock.HAS_WISTERIA_FLOWERS));
                if (!checkState.getValue(PostBlock.HAS_HOPS_VINE) && !hasOther) {
                    level.setBlock(checkPos, checkState.setValue(PostBlock.HAS_HOPS_VINE, true), 3);
                    attached = true;
                } else if (checkState.getValue(PostBlock.HAS_HOPS_VINE)) {
                    attached = true;
                }
            } else if (checkBlock instanceof JoistsBlock) {
                boolean hasOther = (checkState.getValue(JoistsBlock.HAS_GRAPE_VINE) || checkState.getValue(JoistsBlock.HAS_GRAPES))
                        || (checkState.getValue(JoistsBlock.HAS_WISTERIA_VINE) || checkState.getValue(JoistsBlock.HAS_WISTERIA_FLOWERS));
                if (!checkState.getValue(JoistsBlock.HAS_HOPS_VINE) && !hasOther) {
                    level.setBlock(checkPos, checkState.setValue(JoistsBlock.HAS_HOPS_VINE, true), 3);
                    attached = true;
                } else if (checkState.getValue(JoistsBlock.HAS_HOPS_VINE)) {
                    attached = true;
                }
            }
        }

        return attached;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = state.getValue(AGE);

            if (age < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, level, pos);
                if (random.nextInt((int) (25.0F / growthSpeed) + 1) == 0) {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 2);
                }
            } else if (age == this.getMaxAge()) {
                int chance = materiaCommonConfig.VINE_PLANT_SPREAD_CHANCE_PERCENT.get();
                if (chance > 0 && random.nextInt(100) < chance) {
                    attachToNearbySupports(level, pos);
                }
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}

