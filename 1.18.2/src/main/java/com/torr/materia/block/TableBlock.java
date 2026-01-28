package com.torr.materia.block;

import com.torr.materia.blockentity.TableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TableBlock extends Block implements EntityBlock {
    // Table shape - full block minus the top part (table surface is at y=14)
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<TableShape> SHAPE_PROPERTY = EnumProperty.create("shape", TableShape.class);

    public enum TableShape implements StringRepresentable {
        SINGLE("single"),
        END("end"),
        MIDDLE("middle"),
        CORNER("corner");

        private final String name;

        TableShape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public TableBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(SHAPE_PROPERTY, TableShape.SINGLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE_PROPERTY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getConnectedState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, 
            LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        // Only update if the neighbor change is horizontal
        if (direction.getAxis().isHorizontal()) {
            return this.getConnectedState(state, level, pos);
        }
        return state;
    }

    private BlockState getConnectedState(BlockState state, LevelAccessor level, BlockPos pos) {
        // Check all four horizontal directions for table neighbors
        boolean north = isTable(level.getBlockState(pos.north()));
        boolean south = isTable(level.getBlockState(pos.south()));
        boolean east = isTable(level.getBlockState(pos.east()));
        boolean west = isTable(level.getBlockState(pos.west()));

        // Count connections
        int connections = (north ? 1 : 0) + (south ? 1 : 0) + (east ? 1 : 0) + (west ? 1 : 0);

        if (connections == 0) {
            // No neighbors - single table
            return state.setValue(SHAPE_PROPERTY, TableShape.SINGLE).setValue(FACING, Direction.NORTH);
        } else if (connections == 1) {
            // One neighbor - end piece
            // Facing should point AWAY from the neighbor (where the legs are)
            Direction facing;
            if (north) facing = Direction.SOUTH;  // Neighbor to north, legs point south
            else if (south) facing = Direction.NORTH;  // Neighbor to south, legs point north
            else if (east) facing = Direction.WEST;  // Neighbor to east, legs point west
            else facing = Direction.EAST;  // Neighbor to west, legs point east
            
            return state.setValue(SHAPE_PROPERTY, TableShape.END).setValue(FACING, facing);
        } else if (connections == 2) {
            // Two neighbors - could be middle (opposite sides) or corner (adjacent sides)
            if ((north && south) || (east && west)) {
                // Opposite sides - middle piece
                Direction facing = (north && south) ? Direction.NORTH : Direction.EAST;
                return state.setValue(SHAPE_PROPERTY, TableShape.MIDDLE).setValue(FACING, facing);
            } else {
                // Adjacent sides - corner piece
                Direction facing;
                if (north && east) facing = Direction.NORTH;       // NE corner
                else if (east && south) facing = Direction.EAST;   // SE corner
                else if (south && west) facing = Direction.SOUTH;  // SW corner
                else facing = Direction.WEST;                      // NW corner
                
                return state.setValue(SHAPE_PROPERTY, TableShape.CORNER).setValue(FACING, facing);
            }
        } else {
            // Three or four neighbors - use middle piece with arbitrary facing
            // Choose facing based on which sides are connected
            Direction facing = north ? Direction.NORTH : (south ? Direction.SOUTH : Direction.EAST);
            return state.setValue(SHAPE_PROPERTY, TableShape.MIDDLE).setValue(FACING, facing);
        }
    }

    private boolean isTable(BlockState state) {
        return state.getBlock() instanceof TableBlock;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        BlockEntity be = level.getBlockEntity(pos);
        
        if (!(be instanceof TableBlockEntity table))
            return InteractionResult.PASS;

        var handler = table.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (handler == null)
            return InteractionResult.PASS;

        ItemStack tableStack = handler.getStackInSlot(0);
        
        if (tableStack.isEmpty()) {
            // Try to place item on table
            if (!held.isEmpty()) {
                if (!level.isClientSide) {
                    ItemStack toPlace = held.copy();
                    toPlace.setCount(1);
                    ItemStack remainder = handler.insertItem(0, toPlace, false);
                    
                    if (remainder.isEmpty()) {
                        // Item was successfully placed
                        held.shrink(1);
                        table.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else {
            // Try to take item from table
            if (held.isEmpty() || (held.getItem() == tableStack.getItem() && held.getCount() < held.getMaxStackSize())) {
                if (!level.isClientSide) {
                    ItemStack extracted = handler.extractItem(0, 1, false);
                    if (!extracted.isEmpty()) {
                        if (held.isEmpty()) {
                            player.setItemInHand(hand, extracted);
                        } else {
                            held.grow(1);
                        }
                        table.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TableBlockEntity(pos, state);
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
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TableBlockEntity table) {
                // Drop the item when the table is broken
                ItemStack stack = table.getRenderStack();
                if (!stack.isEmpty()) {
                    popResource(level, pos, stack);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
