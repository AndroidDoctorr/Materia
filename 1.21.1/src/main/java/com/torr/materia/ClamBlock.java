package com.torr.materia;

import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.util.RandomSource;

import java.util.List;

public class ClamBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    private static final int NORMAL_SPREAD_CHANCE = 300;

    // Define a shape for the clam based on the JSON model
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D);

    public ClamBlock(Properties properties) {
        super(properties.randomTicks()); // enable random ticks
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Only reproduce if this clam is underwater (waterlogged)
        if (!state.getValue(WATERLOGGED)) return;
        
        // Check if we're at least 1 block deep underwater
        BlockPos abovePos = pos.above();
        FluidState aboveFluid = level.getFluidState(abovePos);
        if (aboveFluid.getType() != Fluids.WATER) return; // need water above for depth
        
        int chance = NORMAL_SPREAD_CHANCE;
        if (random.nextInt(chance) != 0) return; // no spread this tick

        // Try up to 8 random target positions within 4Ã—2Ã—4 area
        for (int attempt = 0; attempt < 8; attempt++) {
            int dx = random.nextInt(9) - 4; // -4..4
            int dz = random.nextInt(9) - 4;
            int dy = random.nextInt(3) - 1; // -1..1 (allow slight vertical variance)
            if (dx == 0 && dz == 0) continue; // skip self

            BlockPos target = pos.offset(dx, dy, dz);
            BlockPos sandPos = target.below();
            BlockState sandState = level.getBlockState(sandPos);
            if (!sandState.is(net.minecraft.world.level.block.Blocks.SAND)) continue;

            // The block currently occupying target position
            BlockState targetState = level.getBlockState(target);
            FluidState targetFluid = level.getFluidState(target);

            boolean waterAbove = targetFluid.getType() == Fluids.WATER;
            boolean airAbove = targetState.isAir();
            
            if (!waterAbove) continue;

            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            boolean tooClose = false;
            for (int x = -2; x <= 2 && !tooClose; x++) {
                for (int z = -2; z <= 2 && !tooClose; z++) {
                    if (x == 0 && z == 0) continue;
                    cursor.set(pos.getX()+x, pos.getY(), pos.getZ()+z);
                    if (level.getBlockState(cursor).getBlock() == this) tooClose = true;
                }
            }
            if (tooClose) continue;

            BlockState newState = defaultBlockState()
                    .setValue(FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
                    .setValue(WATERLOGGED, waterAbove);
            level.setBlock(target, newState, 3);
            if (waterAbove) {
                level.scheduleTick(target, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
            break;
        }
    }

    // 1.20.x: getDrops signature changed (LootParams.Builder). We rely on the base implementation.
}
