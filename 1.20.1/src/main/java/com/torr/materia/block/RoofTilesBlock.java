package com.torr.materia.block;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ModSounds;
import com.torr.materia.item.RoofTilesBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class RoofTilesBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 8);
    public static final BooleanProperty THATCH = BooleanProperty.create("thatch");

    private static final float TILE_BREAK_CHANCE = 0.15F;

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 8, 16)
    );

    public RoofTilesBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STAGE, 0)
                .setValue(THATCH, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE, THATCH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int stage = 0;
        boolean thatch = false;
        ItemStack stack = context.getItemInHand();
        if (stack.getItem() instanceof RoofTilesBlockItem) {
            stage = RoofTilesBlockItem.placementStage(stack);
            thatch = RoofTilesBlockItem.placementThatch(stack);
        }
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STAGE, stage)
                .setValue(THATCH, thatch);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return handleUse(state, level, pos, player.getItemInHand(hand));
    }

    private InteractionResult handleUse(BlockState state, Level level, BlockPos pos, ItemStack held) {
        int stage = state.getValue(STAGE);
        boolean thatch = state.getValue(THATCH);

        if (stage >= 8) {
            return InteractionResult.PASS;
        }

        if (held.is(ModItems.BUNDLE.get())) {
            if (stage > 0 && !thatch) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide) {
                if (!thatch) {
                    level.setBlock(pos, state.setValue(THATCH, true), 3);
                } else {
                    level.setBlock(pos, state.setValue(STAGE, 8), 3);
                }
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
            held.shrink(1);
            playPotteryScrape(level, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public static void onCannonballImpact(ServerLevel level, BlockPos pos, BlockState state) {
        boolean thatch = state.getValue(THATCH);
        int stage = state.getValue(STAGE);

        if (thatch) {
            if (stage >= 8) {
                Block.popResource(level, pos, new ItemStack(ModItems.BUNDLE.get()));
                level.setBlock(pos, state.setValue(STAGE, 0), 3);
                playThatchBreak(level, pos);
            } else if (stage == 0) {
                Block.popResource(level, pos, new ItemStack(ModItems.BUNDLE.get()));
                level.setBlock(pos, state.setValue(THATCH, false), 3);
                playThatchBreak(level, pos);
            }
            level.levelEvent(2001, pos, Block.getId(state));
            return;
        }

        if (stage <= 0) {
            return;
        }

        RandomSource random = level.getRandom();
        int tilesLost = 1 + random.nextInt(Math.min(3, stage));
        int newStage = Math.max(0, stage - tilesLost);
        dropTiles(level, pos, random, tilesLost);
        level.setBlock(pos, state.setValue(STAGE, newStage), 3);
        playPotteryBreak(level, pos);
        level.levelEvent(2001, pos, Block.getId(state));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && !state.is(newState.getBlock()) && hasRoofCovering(state)) {
            if (state.getValue(THATCH)) {
                playThatchBreak(level, pos);
            } else {
                playPotteryBreak(level, pos);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(ModItems.ROOF_FRAME.get()));

        boolean thatch = state.getValue(THATCH);
        int stage = state.getValue(STAGE);

        if (thatch) {
            if (stage >= 8) {
                drops.add(new ItemStack(ModItems.BUNDLE.get(), 2));
            } else {
                drops.add(new ItemStack(ModItems.BUNDLE.get()));
            }
            return drops;
        }

        if (stage > 0) {
            RandomSource random = builder.getLevel().getRandom();
            drops.addAll(tileStacksForCount(stage, random));
        }
        return drops;
    }

    private static boolean hasRoofCovering(BlockState state) {
        if (state.getValue(THATCH)) {
            return true;
        }
        return state.getValue(STAGE) > 0;
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

    private static void dropTiles(ServerLevel level, BlockPos pos, RandomSource random, int count) {
        for (ItemStack stack : tileStacksForCount(count, random)) {
            Block.popResource(level, pos, stack);
        }
    }

    private static List<ItemStack> tileStacksForCount(int count, RandomSource random) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (random.nextFloat() < TILE_BREAK_CHANCE) {
                stacks.add(new ItemStack(ModItems.CRUSHED_CERAMIC.get()));
            } else {
                stacks.add(new ItemStack(ModItems.TERRACOTTA_ROOF_TILE.get()));
            }
        }
        return stacks;
    }
}
