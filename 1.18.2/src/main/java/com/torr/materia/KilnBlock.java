package com.torr.materia;

import com.torr.materia.ModBlocks;
import com.torr.materia.blockentity.KilnBlockEntity;
import com.torr.materia.capability.TongsCapability;
import com.torr.materia.capability.GlassPipeCapability;
import com.torr.materia.events.HotMetalEventHandler;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;

public class KilnBlock extends BaseEntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    public KilnBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KilnBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof KilnBlockEntity) {
                ((KilnBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof KilnBlockEntity kilnEntity) {
                ItemStack heldItem = player.getItemInHand(hand);
                
                // Check if player is holding tongs
                var tongsCapOptional = heldItem.getCapability(TongsCapability.TONGS_CAPABILITY);
                if (tongsCapOptional.isPresent()) {
                    return handleTongsInteraction(kilnEntity, heldItem, player.isShiftKeyDown());
                }
                
                // Check if player is holding steel pipe
                if (heldItem.getItem() == ModItems.STEEL_PIPE.get()) {
                    return handleSteelPipeInteraction(kilnEntity, heldItem, player, player.isShiftKeyDown());
                }
                
                // Normal kiln GUI interaction
                // Check if chimney is present to determine which menu to open
                boolean hasChimney = level.getBlockState(pos.above()).is(ModBlocks.CHIMNEY.get());
                
                if (hasChimney) {
                    // Open advanced kiln menu
                    NetworkHooks.openGui(((ServerPlayer)player), (KilnBlockEntity)entity, pos);
                } else {
                    // Open basic kiln menu
                    NetworkHooks.openGui(((ServerPlayer)player), (KilnBlockEntity)entity, pos);
                }
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    
    /**
     * Handle interaction when player right-clicks kiln with tongs
     */
    private InteractionResult handleTongsInteraction(KilnBlockEntity kilnEntity, ItemStack tongsStack, boolean isShiftKeyDown) {
        var tongsCapOptional = tongsStack.getCapability(TongsCapability.TONGS_CAPABILITY);
        if (!tongsCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var tongsCap = tongsCapOptional.resolve().get();
        var kilnItemHandler = kilnEntity.getItemHandler();
        
        // Slot 2 is the output slot in KilnBlockEntity
        ItemStack outputStack = kilnItemHandler.getStackInSlot(2);
        
        if (outputStack.isEmpty()) {
            return InteractionResult.PASS; // Nothing to extract
        }
        
        // Only extract hot metal items
        if (!outputStack.is(HotMetalEventHandler.HEATABLE_METALS)) {
            return InteractionResult.PASS;
        }
        
        if (isShiftKeyDown) {
            // Extract all items
            ItemStack remaining = tongsCap.addItem(outputStack.copy());
            if (remaining.getCount() < outputStack.getCount()) {
                kilnItemHandler.setStackInSlot(2, remaining.isEmpty() ? ItemStack.EMPTY : remaining);
                kilnEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        } else {
            // Extract one item
            if (outputStack.getCount() > 0) {
                ItemStack singleItem = outputStack.copy();
                singleItem.setCount(1);
                
                ItemStack remaining = tongsCap.addItem(singleItem);
                if (remaining.isEmpty()) {
                    outputStack.shrink(1);
                    kilnItemHandler.setStackInSlot(2, outputStack);
                    kilnEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Handle interaction when player right-clicks kiln with steel pipe
     */
    private InteractionResult handleSteelPipeInteraction(KilnBlockEntity kilnEntity, ItemStack pipeStack, Player player, boolean isShiftKeyDown) {
        var pipeCapOptional = pipeStack.getCapability(GlassPipeCapability.GLASS_PIPE_CAPABILITY);
        if (!pipeCapOptional.isPresent()) {
            return InteractionResult.PASS;
        }
        
        var pipeCap = pipeCapOptional.resolve().get();
        var kilnItemHandler = kilnEntity.getItemHandler();
        
        // Check if pipe already has glass - if so, try to blow it
        if (pipeCap.hasGlass()) {
            return handleGlassBlowing(pipeCap, player);
        }
        
        // Try to extract hot glass from kiln output slot (slot 2)
        ItemStack outputStack = kilnItemHandler.getStackInSlot(2);
        
        if (outputStack.isEmpty()) {
            return InteractionResult.PASS; // Nothing to extract
        }
        
        // Only extract glass pucks
        if (outputStack.getItem() != ModItems.GLASS_PUCK.get()) {
            return InteractionResult.PASS;
        }
        
        // Only extract if glass is hot
        if (!outputStack.is(HotMetalEventHandler.HEATABLE_METALS)) {
            return InteractionResult.PASS;
        }
        
        // Extract one glass puck
        if (outputStack.getCount() > 0) {
            ItemStack singleGlass = outputStack.copy();
            singleGlass.setCount(1);
            
            ItemStack remaining = pipeCap.addGlass(singleGlass);
            if (remaining.isEmpty()) {
                outputStack.shrink(1);
                kilnItemHandler.setStackInSlot(2, outputStack);
                kilnEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Handle glass blowing when player right-clicks with pipe containing hot glass
     */
    private InteractionResult handleGlassBlowing(GlassPipeCapability pipeCap, Player player) {
        if (!pipeCap.hasHotGlass()) {
            return InteractionResult.PASS; // Glass is too cold to blow
        }
        
        // Remove glass from pipe and create bottle
        ItemStack glass = pipeCap.removeGlass();
        if (!glass.isEmpty()) {
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            
            // Try to give bottle to player
            if (!player.getInventory().add(bottle)) {
                // Drop if inventory is full
                player.drop(bottle, false);
            }
            
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.KILN_BLOCK_ENTITY.get(),
                KilnBlockEntity::tick);
    }
} 