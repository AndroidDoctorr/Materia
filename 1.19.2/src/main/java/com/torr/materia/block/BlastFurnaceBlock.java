package com.torr.materia.block;

import com.torr.materia.blockentity.KilnBlockEntity;
import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModItems;
import com.torr.materia.capability.GlassPipeCapability;
import com.torr.materia.events.HotMetalEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * A blast furnace block that reuses kiln logic but can smelt stone without chimney
 * and smelts faster than regular furnace. Always considered to have chimney and bellows.
 */
public class BlastFurnaceBlock extends com.torr.materia.KilnBlock {

    public BlastFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KilnBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof KilnBlockEntity kilnEntity) {
                ItemStack heldItem = player.getItemInHand(hand);
                
                // Check if player is holding steel pipe
                if (heldItem.getItem() == ModItems.STEEL_PIPE.get()) {
                    return handleSteelPipeInteraction(kilnEntity, heldItem, player, player.isShiftKeyDown());
                }
                
                // Open blast furnace GUI
                NetworkHooks.openScreen(((ServerPlayer)player), (KilnBlockEntity)entity, pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    
    /**
     * Handle interaction when player right-clicks blast furnace with steel pipe
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
        
        // Try to extract hot glass from blast furnace output slot (slot 2)
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
        return createTickerHelper(type, ModBlockEntities.KILN_BLOCK_ENTITY.get(), KilnBlockEntity::tick);
    }
}
