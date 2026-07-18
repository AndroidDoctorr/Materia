package com.torr.materia.block;

import com.torr.materia.ModItems;
import com.torr.materia.ModSounds;
import com.torr.materia.item.RoofTilesBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class RoofTilesBlock extends Block {
    public static final int COVER_TERRACOTTA = 0;
    public static final int COVER_COPPER = 1;
    public static final int COVER_SHINGLE = 2;
    public static final int SHINGLE_MAX_STAGE = 4;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 8);
    public static final BooleanProperty THATCH = BooleanProperty.create("thatch");
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final IntegerProperty COVER_TYPE = IntegerProperty.create("cover_type", 0, 2);
    public static final IntegerProperty OXIDATION = IntegerProperty.create("oxidation", 0, 3);

    private static final float COPPER_OXIDATION_CHANCE = 0.05688889F;
    private static final float PLAYER_CRUSHED_CHANCE = 0.20F;
    private static final float PLAYER_VANISH_CHANCE = 0.12F;
    private static final float VIOLENT_OBLITERATE_CHANCE = 0.5F;

    private enum TileBreakCause {
        PLAYER,
        CANNONBALL,
        EXPLOSION
    }

    private static VoxelShape buildRidgeWedge(Direction ridge) {
        VoxelShape shape = Shapes.empty();
        for (int i = 0; i < 16; i++) {
            int height = i + 1;
            switch (ridge) {
                case SOUTH -> shape = Shapes.or(shape, Block.box(0, 0, i, 16, height, i + 1));
                case NORTH -> shape = Shapes.or(shape, Block.box(0, 0, 15 - i, 16, height, 15 - i + 1));
                case EAST -> shape = Shapes.or(shape, Block.box(i, 0, 0, i + 1, height, 16));
                case WEST -> shape = Shapes.or(shape, Block.box(15 - i, 0, 0, 16 - i, height, 16));
                default -> {
                }
            }
        }
        return shape;
    }

    private static VoxelShape shapeForState(BlockState state) {
        Direction ridge = state.getValue(FACING).getOpposite();
        StairsShape shape = state.getValue(SHAPE);
        VoxelShape result = buildRidgeWedge(ridge);
        if (shape == StairsShape.STRAIGHT) {
            return result;
        }

        Direction crossRidge = switch (shape) {
            case INNER_LEFT, OUTER_LEFT -> ridge.getClockWise();
            case INNER_RIGHT, OUTER_RIGHT -> ridge.getCounterClockWise();
            default -> null;
        };
        if (crossRidge == null) {
            return result;
        }
        VoxelShape cross = buildRidgeWedge(crossRidge);
        return switch (shape) {
            case INNER_LEFT, INNER_RIGHT -> Shapes.or(result, cross);
            case OUTER_LEFT, OUTER_RIGHT -> Shapes.join(result, cross, BooleanOp.AND);
            default -> result;
        };
    }

    public RoofTilesBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STAGE, 0)
                .setValue(THATCH, false)
                .setValue(SHAPE, StairsShape.STRAIGHT)
                .setValue(COVER_TYPE, COVER_TERRACOTTA)
                .setValue(OXIDATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE, THATCH, SHAPE, COVER_TYPE, OXIDATION);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(COVER_TYPE) == COVER_COPPER && state.getValue(OXIDATION) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(COVER_TYPE) != COVER_COPPER || state.getValue(OXIDATION) >= 3) {
            return;
        }
        if (random.nextFloat() < COPPER_OXIDATION_CHANCE) {
            level.setBlock(pos, state.setValue(OXIDATION, state.getValue(OXIDATION) + 1), 3);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int stage = 0;
        boolean thatch = false;
        int coverType = COVER_TERRACOTTA;
        int oxidation = 0;
        ItemStack stack = context.getItemInHand();
        if (stack.getItem() instanceof RoofTilesBlockItem) {
            stage = RoofTilesBlockItem.placementStage(stack);
            thatch = RoofTilesBlockItem.placementThatch(stack);
            coverType = RoofTilesBlockItem.placementCoverType(stack);
            oxidation = RoofTilesBlockItem.placementOxidation(stack);
        }
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        BlockState state = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STAGE, stage)
                .setValue(THATCH, thatch)
                .setValue(COVER_TYPE, coverType)
                .setValue(OXIDATION, oxidation)
                .setValue(SHAPE, StairsShape.STRAIGHT);
        return orientFromNeighbors(state, context.getLevel(), pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(SHAPE, computeShape(state, level, pos));
        }
        return state;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            refreshShapeNeighbors(level, pos);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeForState(state);
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = handleUse(state, level, pos, held);
        if (result == InteractionResult.PASS) {
            return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (result == InteractionResult.FAIL) {
            return net.minecraft.world.ItemInteractionResult.FAIL;
        }
        return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private InteractionResult handleUse(BlockState state, Level level, BlockPos pos, ItemStack held) {
        int coverType = state.getValue(COVER_TYPE);
        int stage = state.getValue(STAGE);
        boolean thatch = state.getValue(THATCH);

        if (coverType == COVER_COPPER) {
            return InteractionResult.PASS;
        }

        if (coverType == COVER_SHINGLE) {
            if (stage >= SHINGLE_MAX_STAGE || !held.is(ModItems.SHINGLE.get())) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
                refreshShapeNeighbors(level, pos);
                held.shrink(1);
                playWoodScrape(level, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stage == 0 && !thatch) {
            if (held.is(ModItems.COPPER_PLATE.get())) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state
                            .setValue(COVER_TYPE, COVER_COPPER)
                            .setValue(OXIDATION, 0)
                            .setValue(STAGE, 0), 3);
                    refreshShapeNeighbors(level, pos);
                    held.shrink(1);
                    playCopperPlace(level, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
            if (held.is(ModItems.SHINGLE.get())) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state
                            .setValue(COVER_TYPE, COVER_SHINGLE)
                            .setValue(STAGE, 1), 3);
                    refreshShapeNeighbors(level, pos);
                    held.shrink(1);
                    playWoodScrape(level, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        if (stage >= 8) {
            return InteractionResult.PASS;
        }

        if (held.is(ModItems.BUNDLE.get())) {
            if (stage > 0) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide) {
                BlockState updated = !thatch
                        ? state.setValue(THATCH, true)
                        : state.setValue(STAGE, 8);
                level.setBlock(pos, updated, 3);
                refreshShapeNeighbors(level, pos);
                held.shrink(1);
                playThatchPlace(level, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (thatch || !held.is(ModItems.TERRACOTTA_ROOF_TILE.get())) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
            refreshShapeNeighbors(level, pos);
            held.shrink(1);
            playPotteryScrape(level, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public static void onCannonballImpact(ServerLevel level, BlockPos pos, BlockState state, boolean iron) {
        handleViolentImpact(level, pos, state, TileBreakCause.CANNONBALL, iron);
    }

    private static void handleViolentImpact(ServerLevel level, BlockPos pos, BlockState state, TileBreakCause cause, boolean alwaysObliterate) {
        int coverType = state.getValue(COVER_TYPE);
        if (coverType == COVER_COPPER || alwaysObliterate) {
            destroyRoofViolently(level, pos, state, cause, level.getRandom());
            return;
        }

        boolean thatch = state.getValue(THATCH);
        int stage = state.getValue(STAGE);
        RandomSource random = level.getRandom();
        boolean obliterate = random.nextFloat() < VIOLENT_OBLITERATE_CHANCE;

        if (obliterate) {
            destroyRoofViolently(level, pos, state, cause, random);
            return;
        }

        if (coverType == COVER_SHINGLE) {
            if (stage <= 0) {
                return;
            }
            int lost = violentTileLoss(stage, random);
            dropShingleStacks(level, pos, random, lost, cause);
            level.setBlock(pos, state.setValue(STAGE, stage - lost), 3);
            refreshShapeNeighbors(level, pos);
            playWoodScrape(level, pos);
            level.levelEvent(2001, pos, Block.getId(state));
            return;
        }

        if (thatch) {
            if (stage >= 8) {
                level.setBlock(pos, state.setValue(STAGE, 0), 3);
            } else if (stage == 0) {
                level.setBlock(pos, state.setValue(THATCH, false), 3);
            }
            refreshShapeNeighbors(level, pos);
            playThatchBreak(level, pos);
            level.levelEvent(2001, pos, Block.getId(state));
            return;
        }

        if (stage <= 0) {
            return;
        }

        int tilesLost = violentTileLoss(stage, random);
        dropTileStacks(level, pos, random, tilesLost, cause);
        level.setBlock(pos, state.setValue(STAGE, stage - tilesLost), 3);
        refreshShapeNeighbors(level, pos);
        playPotteryBreak(level, pos);
        level.levelEvent(2001, pos, Block.getId(state));
    }

    private static void destroyRoofViolently(ServerLevel level, BlockPos pos, BlockState state, TileBreakCause cause, RandomSource random) {
        int coverType = state.getValue(COVER_TYPE);
        boolean thatch = state.getValue(THATCH);
        int stage = state.getValue(STAGE);

        if (coverType == COVER_COPPER) {
            if (cause == TileBreakCause.PLAYER) {
                Block.popResource(level, pos, new ItemStack(ModItems.COPPER_PLATE.get()));
            }
            playCopperBreak(level, pos);
        } else if (coverType == COVER_SHINGLE && stage > 0) {
            dropShingleStacks(level, pos, random, stage, cause);
            playWoodScrape(level, pos);
        } else if (!thatch && stage > 0) {
            dropTileStacks(level, pos, random, stage, cause);
            playPotteryBreak(level, pos);
        } else if (thatch) {
            playThatchBreak(level, pos);
        }

        level.levelEvent(2001, pos, Block.getId(state));
        level.removeBlock(pos, false);
    }

    private static int violentTileLoss(int stage, RandomSource random) {
        if (stage < 3) {
            return stage;
        }
        return Math.min(stage, 3 + random.nextInt(stage - 2));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && !state.is(newState.getBlock()) && hasRoofCovering(state)) {
            switch (state.getValue(COVER_TYPE)) {
                case COVER_COPPER -> playCopperBreak(level, pos);
                case COVER_SHINGLE -> playWoodScrape(level, pos);
                default -> {
                    if (state.getValue(THATCH)) {
                        playThatchBreak(level, pos);
                    } else {
                        playPotteryBreak(level, pos);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            refreshShapeNeighbors(level, pos);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(ModItems.ROOF_FRAME.get()));

        TileBreakCause cause = builder.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS) != null
                ? TileBreakCause.EXPLOSION
                : TileBreakCause.PLAYER;

        int coverType = state.getValue(COVER_TYPE);
        boolean thatch = state.getValue(THATCH);
        int stage = state.getValue(STAGE);

        if (coverType == COVER_COPPER) {
            if (cause == TileBreakCause.PLAYER) {
                drops.add(new ItemStack(ModItems.COPPER_PLATE.get()));
            }
            return drops;
        }

        if (coverType == COVER_SHINGLE) {
            if (cause == TileBreakCause.PLAYER && stage > 0) {
                drops.add(new ItemStack(ModItems.SHINGLE.get(), stage));
            }
            return drops;
        }

        if (thatch) {
            if (cause == TileBreakCause.PLAYER) {
                if (stage >= 8) {
                    drops.add(new ItemStack(ModItems.BUNDLE.get(), 2));
                } else {
                    drops.add(new ItemStack(ModItems.BUNDLE.get()));
                }
            }
            return drops;
        }

        if (stage > 0) {
            RandomSource random = builder.getLevel().getRandom();
            drops.addAll(tileStacksForCount(stage, random, cause));
        }
        return drops;
    }

    private static boolean hasRoofCovering(BlockState state) {
        return switch (state.getValue(COVER_TYPE)) {
            case COVER_COPPER -> true;
            case COVER_SHINGLE -> state.getValue(STAGE) > 0;
            default -> state.getValue(THATCH) || state.getValue(STAGE) > 0;
        };
    }

    private static StairsShape computeShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction ridge = state.getValue(FACING);
        Direction descent = ridge.getOpposite();

        BlockState ridgeNeighbor = level.getBlockState(pos.relative(ridge));
        if (isCompatibleRoof(ridgeNeighbor)) {
            StairsShape shape = shapeForNeighbor(ridge, ridge, ridgeNeighbor.getValue(FACING));
            if (shape != StairsShape.STRAIGHT) {
                return shape;
            }
        }

        BlockState descentNeighbor = level.getBlockState(pos.relative(descent));
        if (isCompatibleRoof(descentNeighbor)) {
            StairsShape shape = shapeForNeighbor(ridge, descent, descentNeighbor.getValue(FACING));
            if (shape != StairsShape.STRAIGHT) {
                return shape;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static StairsShape shapeForNeighbor(Direction ridge, Direction side, Direction neighborRidge) {
        if (neighborRidge.getAxis() == ridge.getAxis()) {
            return StairsShape.STRAIGHT;
        }

        if (side == ridge) {
            return neighborRidge == ridge.getCounterClockWise()
                    ? StairsShape.INNER_RIGHT
                    : StairsShape.INNER_LEFT;
        }

        if (side == ridge.getOpposite()) {
            return neighborRidge == ridge.getCounterClockWise()
                    ? StairsShape.OUTER_RIGHT
                    : StairsShape.OUTER_LEFT;
        }

        return StairsShape.STRAIGHT;
    }

    private static BlockState orientFromNeighbors(BlockState state, BlockGetter level, BlockPos pos) {
        return state.setValue(SHAPE, computeShape(state, level, pos));
    }

    private static boolean isCompatibleRoof(BlockState other) {
        return other.getBlock() instanceof RoofTilesBlock;
    }

    private static void refreshShapeNeighbors(Level level, BlockPos pos) {
        refreshShapeAt(level, pos);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockState(neighborPos).getBlock() instanceof RoofTilesBlock) {
                refreshShapeAt(level, neighborPos);
            }
        }
    }

    private static void refreshShapeAt(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof RoofTilesBlock)) {
            return;
        }
        StairsShape shape = computeShape(state, level, pos);
        if (state.getValue(SHAPE) != shape) {
            level.setBlock(pos, state.setValue(SHAPE, shape), 3);
        }
    }

    private static void playPotteryScrape(Level level, BlockPos pos) {
        level.playSound(null, pos, ModSounds.POTTERY_SCRAPE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void playPotteryBreak(Level level, BlockPos pos) {
        level.playSound(null, pos, ModSounds.POTTERY_BREAK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void playThatchPlace(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundType.GRASS.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void playThatchBreak(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundType.GRASS.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void playCopperPlace(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    private static void playCopperBreak(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.6F, 1.1F);
    }

    private static void playWoodScrape(Level level, BlockPos pos) {
        level.playSound(null, pos, ModSounds.WOOD_SCRAPE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void dropTileStacks(ServerLevel level, BlockPos pos, RandomSource random, int count, TileBreakCause cause) {
        for (ItemStack stack : tileStacksForCount(count, random, cause)) {
            Block.popResource(level, pos, stack);
        }
    }

    private static void dropShingleStacks(ServerLevel level, BlockPos pos, RandomSource random, int count, TileBreakCause cause) {
        if (cause != TileBreakCause.PLAYER) {
            return;
        }
        Block.popResource(level, pos, new ItemStack(ModItems.SHINGLE.get(), count));
    }

    private static List<ItemStack> tileStacksForCount(int count, RandomSource random, TileBreakCause cause) {
        List<ItemStack> stacks = new ArrayList<>();
        if (cause == TileBreakCause.CANNONBALL || cause == TileBreakCause.EXPLOSION) {
            int maxCrushed = Math.max(1, count / 3);
            int crushedCount = random.nextInt(maxCrushed + 1);
            for (int i = 0; i < crushedCount; i++) {
                stacks.add(new ItemStack(ModItems.CRUSHED_CERAMIC.get()));
            }
            return stacks;
        }

        for (int i = 0; i < count; i++) {
            float roll = random.nextFloat();
            if (roll < PLAYER_VANISH_CHANCE) {
                continue;
            }
            if (roll < PLAYER_VANISH_CHANCE + PLAYER_CRUSHED_CHANCE) {
                stacks.add(new ItemStack(ModItems.CRUSHED_CERAMIC.get()));
            } else {
                stacks.add(new ItemStack(ModItems.TERRACOTTA_ROOF_TILE.get()));
            }
        }
        return stacks;
    }
}
