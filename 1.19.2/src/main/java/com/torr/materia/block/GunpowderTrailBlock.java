package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.Level;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraft.util.RandomSource;

public class GunpowderTrailBlock extends Block {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    // 0 = unlit, 1..3 = burning animation stages
    public static final IntegerProperty BURN = IntegerProperty.create("burn", 0, 3);
    // Direction the fire entered from (used for directional burn visuals)
    public static final DirectionProperty FROM = DirectionProperty.create("from", Direction.Plane.HORIZONTAL);

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);
    private static final int BURN_TICK_DELAY = 7; // ~1s total across stages (3 * 7 = 21 ticks)

    public GunpowderTrailBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(BURN, 0)
                .setValue(FROM, Direction.NORTH));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        return belowState.isFaceSturdy(level, below, Direction.UP);
    }

    public BlockState updateConnections(LevelAccessor level, BlockPos pos, BlockState state) {
        boolean north = isConnectable(level.getBlockState(pos.north()));
        boolean east = isConnectable(level.getBlockState(pos.east()));
        boolean south = isConnectable(level.getBlockState(pos.south()));
        boolean west = isConnectable(level.getBlockState(pos.west()));

        return state
                .setValue(NORTH, north)
                .setValue(EAST, east)
                .setValue(SOUTH, south)
                .setValue(WEST, west);
    }

    private static boolean isConnectable(BlockState state) {
        return state.getBlock() instanceof GunpowderTrailBlock || state.is(Blocks.TNT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return updateConnections(context.getLevel(), context.getClickedPos(), this.defaultBlockState());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        // Once burning starts, lock-in the shape so it doesn't visually change as neighbors disappear
        if (state.getValue(BURN) != 0) {
            return state;
        }

        BlockState updated = updateConnections(level, pos, state);

        // If we come into contact with fire, instantly light
        if (updated.getValue(BURN) == 0 && isIgnitionSource(neighborState)) {
            if (level instanceof ServerLevel serverLevel) {
                ignite(serverLevel, pos, updated, direction);
                return serverLevel.getBlockState(pos);
            }
        }

        return updated;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            // If water replaces this block, drop 1 gunpowder (wash-away behavior)
            if (newState.getFluidState().getType() == Fluids.WATER) {
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(Items.GUNPOWDER));
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, BURN, FROM);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        // Flint & steel OR BowDrillItem (it extends FlintAndSteelItem)
        if (held.getItem() instanceof FlintAndSteelItem) {
            if (!level.isClientSide() && state.getValue(BURN) == 0) {
                Direction from = player.getDirection().getOpposite();
                ignite((ServerLevel) level, pos, state, from);

                if (!player.getAbilities().instabuild) {
                    held.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Rain extinguishes burning gunpowder
        if (level.isRainingAt(pos.above())) {
            if (state.getValue(BURN) != 0) {
                level.setBlock(pos, state.setValue(BURN, 0), Block.UPDATE_ALL);
            }
            return;
        }

        int burn = state.getValue(BURN);
        if (burn == 0) {
            return;
        }

        // While burning, occasionally ignite nearby flammable blocks (kept fairly rare)
        if (random.nextInt(16) == 0) {
            tryIgniteAdjacentFlammables(level, pos, random);
        }

        if (burn < 3) {
            level.setBlock(pos, state.setValue(BURN, burn + 1), Block.UPDATE_ALL);
            level.scheduleTick(pos, this, BURN_TICK_DELAY);
            return;
        }

        // burn == 3: spread to adjacent trails, then consume this segment
        igniteConnected(level, pos, state);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
    }

    private static boolean isIgnitionSource(BlockState state) {
        if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) return true;
        if (state.getBlock() instanceof BaseFireBlock) return true;
        return state.getFluidState().getType() == Fluids.LAVA;
    }

    private void ignite(ServerLevel level, BlockPos pos, BlockState state, Direction from) {
        if (state.getValue(BURN) != 0) return;

        // Lock in connections at ignition time so burning keeps the original shape
        BlockState locked = updateConnections(level, pos, state);

        BlockState lit = locked
                .setValue(BURN, 1)
                .setValue(FROM, from);

        level.setBlock(pos, lit, Block.UPDATE_ALL);
        level.scheduleTick(pos, this, BURN_TICK_DELAY);
    }

    private void igniteConnected(ServerLevel level, BlockPos pos, BlockState state) {
        // Spread only along *locked* connections, and ignite TNT as a connectable endpoint.
        boolean anyTrailOutward = false;
        Direction from = state.getValue(FROM);

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            boolean connected = switch (dir) {
                case NORTH -> state.getValue(NORTH);
                case EAST -> state.getValue(EAST);
                case SOUTH -> state.getValue(SOUTH);
                case WEST -> state.getValue(WEST);
                default -> false;
            };
            if (!connected) continue;

            BlockPos target = pos.relative(dir);
            BlockState targetState = level.getBlockState(target);

            if (targetState.getBlock() instanceof GunpowderTrailBlock) {
                if (targetState.getValue(BURN) == 0) {
                    ignite(level, target, targetState, dir.getOpposite());
                }
                if (dir != from) {
                    anyTrailOutward = true;
                }
                continue;
            }

            if (targetState.is(Blocks.TNT)) {
                igniteTnt(level, target, targetState, dir.getOpposite());
            }
        }

        // If we didn't spread to any *other* trail segment besides the one we came from, we're the end.
        // Ignite "in front" (direction of propagation) so endpoints light whatever is ahead.
        if (!anyTrailOutward) {
            Direction forward = from.getOpposite();
            igniteEndpoint(level, pos, forward);
        }
    }

    private static void igniteTnt(ServerLevel level, BlockPos pos, BlockState state, Direction from) {
        if (state.getBlock() instanceof TntBlock tnt) {
            tnt.onCaughtFire(state, level, pos, from, null);
            level.removeBlock(pos, false);
        }
    }

    private static void igniteEndpoint(ServerLevel level, BlockPos pos, Direction forward) {
        BlockPos ahead = pos.relative(forward);
        BlockState aheadState = level.getBlockState(ahead);

        if (aheadState.is(Blocks.TNT)) {
            igniteTnt(level, ahead, aheadState, forward.getOpposite());
            return;
        }

        // Prefer lighting fire in the empty space "ahead" (same Y), otherwise on top of the block ahead.
        if (level.isEmptyBlock(ahead)) {
            BlockState fire = BaseFireBlock.getState(level, ahead);
            if (fire.canSurvive(level, ahead)) {
                level.setBlock(ahead, fire, Block.UPDATE_ALL);
            }
            return;
        }

        BlockPos top = ahead.above();
        if (level.isEmptyBlock(top)) {
            BlockState fire = BaseFireBlock.getState(level, top);
            if (fire.canSurvive(level, top)) {
                level.setBlock(top, fire, Block.UPDATE_ALL);
            }
        }
    }

    private static void tryIgniteAdjacentFlammables(ServerLevel level, BlockPos pos, RandomSource random) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighbor = level.getBlockState(neighborPos);

            // If the adjacent block can burn, try placing fire above it (most intuitive).
            if (neighbor.isFlammable(level, neighborPos, dir.getOpposite())) {
                BlockPos firePos = neighborPos.above();
                if (level.isEmptyBlock(firePos)) {
                    BlockState fire = BaseFireBlock.getState(level, firePos);
                    if (fire.canSurvive(level, firePos)) {
                        level.setBlock(firePos, fire, Block.UPDATE_ALL);
                        return;
                    }
                }
            }

            // Also try placing fire in adjacent air pockets (helps ignite fences/leaves/etc).
            if (level.isEmptyBlock(neighborPos)) {
                BlockState fire = BaseFireBlock.getState(level, neighborPos);
                if (fire.canSurvive(level, neighborPos)) {
                    level.setBlock(neighborPos, fire, Block.UPDATE_ALL);
                    return;
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int burn = state.getValue(BURN);
        if (burn == 0) return;

        // Sparks/embers (lava particles), scaled by burn stage (kept moderate)
        int count = 3 + burn * 2 + random.nextInt(4);
        double y = pos.getY() + 0.08;

        // Emit from center + along connected arms (or all directions if isolated)
        boolean n = state.getValue(NORTH);
        boolean e = state.getValue(EAST);
        boolean s = state.getValue(SOUTH);
        boolean w = state.getValue(WEST);
        boolean any = n || e || s || w;

        for (int i = 0; i < count; i++) {
            double x = pos.getX() + 0.5;
            double z = pos.getZ() + 0.5;

            // Bias particles towards connected directions to "trace" the trail
            int pick = random.nextInt(any ? 5 : 1);
            if (any && pick != 0) {
                Direction dir = switch (pick) {
                    case 1 -> Direction.NORTH;
                    case 2 -> Direction.EAST;
                    case 3 -> Direction.SOUTH;
                    case 4 -> Direction.WEST;
                    default -> Direction.NORTH;
                };

                boolean connected = switch (dir) {
                    case NORTH -> n;
                    case EAST -> e;
                    case SOUTH -> s;
                    case WEST -> w;
                    default -> false;
                };

                if (connected) {
                    double t = 0.15 + random.nextDouble() * 0.35;
                    x += dir.getStepX() * t;
                    z += dir.getStepZ() * t;
                }
            } else {
                x += (random.nextDouble() - 0.5) * 0.2;
                z += (random.nextDouble() - 0.5) * 0.2;
            }

            double vx = (random.nextDouble() - 0.5) * 0.02;
            double vy = 0.02 + random.nextDouble() * 0.04;
            double vz = (random.nextDouble() - 0.5) * 0.02;
            level.addParticle(ParticleTypes.LAVA, x, y, z, vx, vy, vz);
        }
    }
}

