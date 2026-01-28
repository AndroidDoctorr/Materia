package com.torr.materia;

import com.torr.materia.blockentity.DryingRackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class DryingRackBlock extends Block implements EntityBlock {
    public static final BooleanProperty DRYING = BooleanProperty.create("drying");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE_X = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_Z = Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);

    public DryingRackBlock(Properties props) {
        super(props);
        registerDefaultState(
                stateDefinition.any().setValue(DRYING, false).setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DRYING, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof DryingRackBlockEntity rack))
            return InteractionResult.PASS;

        var handler = rack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (handler == null)
            return InteractionResult.PASS;

        ItemStack leatherSlot = handler.getStackInSlot(0);
        ItemStack meatSlot = handler.getStackInSlot(1);
        
        // Determine what mode we're in
        boolean hasLeather = !leatherSlot.isEmpty();
        boolean hasMeat = !meatSlot.isEmpty();
        
        // If player is holding meat and we have leather, or holding leather and we have meat, deny
        if ((isRawMeat(held) && hasLeather) || (held.is(net.minecraft.world.item.Items.LEATHER) && hasMeat)) {
            return InteractionResult.PASS;
        }
        
        // Handle leather slot (slot 0)
        if (leatherSlot.isEmpty() && !hasMeat) {
            // insert leather -> show stretched (only if no meat)
            if (held.is(net.minecraft.world.item.Items.LEATHER)) {
                if (!level.isClientSide) {
                    ItemStack one = new ItemStack(ModItems.LEATHER_STRETCHED.get());
                    handler.insertItem(0, one, false);
                    held.shrink(1);
                    level.setBlock(pos, state.setValue(DRYING, true), 3);
                    rack.setChanged();
                    level.sendBlockUpdated(pos, state, state.setValue(DRYING, true), 3);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else if (!leatherSlot.isEmpty()) {
            // extract leather item from the rack
            if (!level.isClientSide) {
                ItemStack extracted = handler.extractItem(0, 1, false);
                if (!extracted.isEmpty()) {
                    // If it's finished tanned leather, give the player the final item
                    if (extracted.is(ModItems.TANNED_LEATHER_STRETCHED.get())) {
                        ItemStack out = new ItemStack(ModItems.TANNED_LEATHER.get());
                        if (!player.addItem(out))
                            player.drop(out, false);
                    } else if (extracted.is(ModItems.LEATHER_STRETCHED.get())) {
                        ItemStack out = new ItemStack(Items.LEATHER);
                        if (!player.addItem(out))
                            player.drop(out, false);
                    } else {
                        // Otherwise give back the original item (leather or stretched leather)
                        if (!player.addItem(extracted))
                            player.drop(extracted, false);
                    }
                    level.setBlock(pos, state.setValue(DRYING, false), 3);
                    rack.resetCook();
                    rack.setChanged();
                    level.sendBlockUpdated(pos, state, state.setValue(DRYING, false), 3);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        
        // Handle meat slot (slot 1) - only if no leather
        if (!hasLeather) {
            if (meatSlot.isEmpty() || (meatSlot.getCount() < 2 && meatSlot.is(held.getItem()))) {
                // insert raw meat for jerky making (up to 2 pieces)
                if (isRawMeat(held)) {
                    if (!level.isClientSide) {
                        ItemStack existing = handler.getStackInSlot(1);
                        if (existing.isEmpty()) {
                            // First meat
                            ItemStack meatToInsert = held.copy();
                            meatToInsert.setCount(1);
                            handler.insertItem(1, meatToInsert, false);
                        } else if (existing.getCount() < 2 && existing.is(held.getItem())) {
                            // Second meat of same type - extract existing and insert with count 2
                            handler.extractItem(1, existing.getCount(), false);
                            ItemStack newStack = existing.copy();
                            newStack.setCount(2);
                            handler.insertItem(1, newStack, false);
                        } else {
                            return InteractionResult.PASS; // Can't add more
                        }
                        held.shrink(1);
                        level.setBlock(pos, state.setValue(DRYING, true), 3);
                        rack.setChanged();
                        level.sendBlockUpdated(pos, state, state.setValue(DRYING, true), 3);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            } else if (!meatSlot.isEmpty()) {
                // extract meat/jerky from the rack
                if (!level.isClientSide) {
                    ItemStack extracted = handler.extractItem(1, 1, false);
                    if (!extracted.isEmpty()) {
                        if (!player.addItem(extracted))
                            player.drop(extracted, false);
                        // Turn off drying if rack is now empty
                        if (handler.getStackInSlot(1).isEmpty()) {
                            level.setBlock(pos, state.setValue(DRYING, false), 3);
                            rack.resetCook();
                        }
                        rack.setChanged();
                        level.sendBlockUpdated(pos, state, state.setValue(DRYING, false), 3);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
    
    private boolean isRawMeat(ItemStack stack) {
        return stack.is(net.minecraft.world.item.Items.PORKCHOP) ||
               stack.is(net.minecraft.world.item.Items.BEEF) ||
               stack.is(net.minecraft.world.item.Items.MUTTON);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (state.getValue(DRYING) && random.nextInt(20) == 0) {
            level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, 0, 0.02, 0);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DryingRackBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        return level.isClientSide ? null : (l, p, s, be) -> {
            if (be instanceof DryingRackBlockEntity rack)
                rack.tick();
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == net.minecraft.core.Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == net.minecraft.core.Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == net.minecraft.core.Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }
}
