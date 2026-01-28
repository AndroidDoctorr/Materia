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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Random;

public class MurexShellBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty VARIETY = IntegerProperty.create("variety", 0, 2); // 0=brandaris, 1=trunculus, 2=haemastoma
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty DEAD = BooleanProperty.create("dead");
    
    private static final int NORMAL_SPREAD_CHANCE = 100;
    // debug constant removed

    // Define a simple shape for the shell
    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D);

    public MurexShellBlock(Properties properties) {
        super(properties.randomTicks()); // enable random ticks
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(VARIETY, 0)
                .setValue(WATERLOGGED, false)
                .setValue(DEAD, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, VARIETY, WATERLOGGED, DEAD);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        ItemStack stack = context.getItemInHand();
        
        // Read variety and dead state from item NBT
        int variety = 0; // default
        boolean dead = false; // default
        
        if (stack.hasTag() && stack.getTag().contains("BlockStateTag")) {
            net.minecraft.nbt.CompoundTag blockStateTag = stack.getTag().getCompound("BlockStateTag");
            if (blockStateTag.contains("variety")) {
                variety = Integer.parseInt(blockStateTag.getString("variety"));
            }
            if (blockStateTag.contains("dead")) {
                dead = blockStateTag.getString("dead").equals("true");
            }
        }
        
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(VARIETY, variety)
                .setValue(DEAD, dead);
    }
    
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        // Check if shell is still out of water after 5 minutes
        if (!state.getValue(WATERLOGGED) && !state.getValue(DEAD)) {
            // Kill the shell
            level.setBlock(pos, state.setValue(DEAD, true), 3);
        }
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
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        // Check if shell should die from being out of water
        if (!state.getValue(WATERLOGGED) && !state.getValue(DEAD)) {
            // Schedule death check in 5 minutes (6000 ticks)
            level.scheduleTick(pos, this, 6000);
        }
        
        // Dead shells don't reproduce
        if (state.getValue(DEAD)) return;
        
        // Only reproduce if this shell is underwater (waterlogged)
        if (!state.getValue(WATERLOGGED)) return;
        
        /*
        // Check if we're at least 1 block deep underwater
        BlockPos abovePos = pos.above();
        FluidState aboveFluid = level.getFluidState(abovePos);
        if (aboveFluid.getType() != Fluids.WATER) return; // need water above for depth
        */

        int chance = NORMAL_SPREAD_CHANCE;
        if (random.nextInt(chance) != 0) return; // no spread this tick

        // Try up to 8 random target positions within 4×2×4 area
        for (int attempt = 0; attempt < 8; attempt++) {
            int dx = random.nextInt(9) - 4; // -4..4
            int dz = random.nextInt(9) - 4;
            int dy = random.nextInt(3) - 1; // -1..1 (allow slight vertical variance)
            if (dx == 0 && dz == 0) continue; // skip self

            BlockPos target = pos.offset(dx, dy, dz);
            BlockPos sandPos = target.below();
            BlockState sandState = level.getBlockState(sandPos);
            if (!sandState.is(net.minecraft.world.level.block.Blocks.SAND)) continue;

            BlockState targetState = level.getBlockState(target);
            FluidState targetFluid = level.getFluidState(target);

            boolean waterAbove = targetFluid.getType() == Fluids.WATER;
            boolean airAbove = targetState.isAir();
            
            if (!waterAbove) continue;

            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        
            int neighborCount = 0;
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && z == 0) continue;
                    cursor.set(pos.getX()+x, pos.getY(), pos.getZ()+z);
                    boolean isShell = level.getBlockState(cursor).getBlock() == this;
                    if (isShell) neighborCount++;
                }
            }
            boolean canSpawn = random.nextInt(neighborCount + 1) == 0;
            if (!canSpawn) continue;

            BlockState newState = defaultBlockState()
                    .setValue(FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
                    .setValue(VARIETY, state.getValue(VARIETY))
                    .setValue(WATERLOGGED, waterAbove)
                    .setValue(DEAD, false);
            level.setBlock(target, newState, 3);
            if (waterAbove) {
                level.scheduleTick(target, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
            break;
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        // Save block state to NBT for creative mode pick block
        net.minecraft.nbt.CompoundTag tag = stack.getOrCreateTag();
        net.minecraft.nbt.CompoundTag blockStateTag = new net.minecraft.nbt.CompoundTag();
        blockStateTag.putString("variety", String.valueOf(state.getValue(VARIETY)));
        blockStateTag.putString("dead", String.valueOf(state.getValue(DEAD)));
        tag.put("BlockStateTag", blockStateTag);
        return stack;
    }
    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return super.getDrops(state, builder);
    }
}
