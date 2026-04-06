package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModRecipes;
import com.torr.materia.WaterPotBlock;
import com.torr.materia.recipe.WaterPotRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * BlockEntity for the Water Pot. Handles boiling detection and processes
 * {@code materia:water_pot} recipes loaded from JSON datapacks.
 */
public class WaterPotBlockEntity extends BlockEntity {

    private int boilTicks;
    private int cookTime;
    
    // Water level system (0 = empty, 3 = full)
    private int waterLevel = 0; // Start empty by default

    // Simple 1-slot inventory (input while boiling)
    private final ItemStackHandler items = new ItemStackHandler(1);
    private final LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> items);

    public WaterPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WATER_POT_BLOCK_ENTITY.get(), pos, state);
        // Initialize water level from block state
        if (state.hasProperty(WaterPotBlock.WATER_LEVEL)) {
            this.waterLevel = state.getValue(WaterPotBlock.WATER_LEVEL);
        }
    }

    /* ===================================================================== */
    /* TICK */
    /* ===================================================================== */
    public void tick() {
        if (level == null || level.isClientSide)
            return;

        boolean boilingNow = isCampfireBelow();
        boolean boilingBefore = getBlockState().getValue(WaterPotBlock.BOILING);

        // Sync blockstate when boiling starts / stops
        if (boilingNow != boilingBefore) {
            level.setBlock(worldPosition,
                    getBlockState().setValue(WaterPotBlock.BOILING, boilingNow), 3);
        }

        if (boilingNow) {
            boilTicks++;
            // Bubble-pop sound every second
            if (boilTicks % 20 == 0) {
                level.playSound(null, worldPosition, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
                        SoundSource.BLOCKS, 0.5F, 1F);
            }
        } else {
            boilTicks = 0;
        }

        // Always process recipe; individual recipes decide if boiling is required
        processRecipe(boilingNow);
    }

    /* ===================================================================== */
    /* RECIPE PROCESSING */
    /* ===================================================================== */
    private void processRecipe(boolean isBoiling) {
        ItemStack input = items.getStackInSlot(0);
        if (input.isEmpty()) {
            cookTime = 0;
            return;
        }

        SimpleContainer container = new SimpleContainer(input);
        Optional<WaterPotRecipe> recipeOpt = level.getRecipeManager()
                .getRecipeFor(ModRecipes.WATER_POT_TYPE.get(), container, level);

        if (recipeOpt.isEmpty()) {
            cookTime = 0;
            return;
        }
        WaterPotRecipe recipe = recipeOpt.get();

        if (recipe.requiresBoiling() && !isBoiling) {
            cookTime = 0;
            return;
        }
        if (input.getCount() < recipe.getIngredientCount()) {
            cookTime = 0;
            return;
        }

        cookTime++;
        if (cookTime < recipe.getCookingTime()) {
            return;
        }

        items.extractItem(0, recipe.getIngredientCount(), false);
        recipe.getResults().forEach(this::spawnOutput);
        if (recipe.consumesWater()) {
            level.setBlock(worldPosition, ModBlocks.POT.get().defaultBlockState(), 3);
        }

        cookTime = 0;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private void spawnOutput(ItemStack stack) {
        if (level == null || level.isClientSide) {
            return;
        }
        ItemEntity entity = new ItemEntity(level,
                worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5,
                stack);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }

    public ItemStack getRenderStack() {
        return items.getStackInSlot(0);
    }
    
    public int getWaterLevel() {
        return waterLevel;
    }
    
    public void setWaterLevel(int newLevel) {
        this.waterLevel = Math.max(0, Math.min(3, newLevel));
        setChanged();
        if (level != null) {
            // Update the block state to reflect the new water level
            BlockState currentState = getBlockState();
            BlockState newState = currentState.setValue(WaterPotBlock.WATER_LEVEL, this.waterLevel);
            level.setBlock(worldPosition, newState, 3);
            level.sendBlockUpdated(worldPosition, currentState, newState, 3);
        }
    }
    
    public boolean hasWater() {
        return waterLevel > 0;
    }
    
    public boolean canAddWater() {
        return waterLevel < 3;
    }

    /* ===================================================================== */
    /* UTILITIES */
    /* ===================================================================== */
    private boolean isCampfireBelow() {
        BlockState below = level.getBlockState(worldPosition.below());
        return below.is(Blocks.CAMPFIRE) && below.getValue(CampfireBlock.LIT);
    }

    /* ===================================================================== */
    /* CAPABILITY */
    /* ===================================================================== */
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemOptional.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inv", items.serializeNBT());
        tag.putInt("cook", cookTime);
        tag.putInt("waterLevel", waterLevel);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.deserializeNBT(tag.getCompound("inv"));
        cookTime = tag.getInt("cook");
        waterLevel = tag.getInt("waterLevel");
        
        // Sync water level with block state when loading
        if (level != null && !level.isClientSide) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(WaterPotBlock.WATER_LEVEL) && 
                currentState.getValue(WaterPotBlock.WATER_LEVEL) != waterLevel) {
                BlockState newState = currentState.setValue(WaterPotBlock.WATER_LEVEL, waterLevel);
                level.setBlock(worldPosition, newState, 3);
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
    
    @Override
    public void setLevel(net.minecraft.world.level.Level level) {
        super.setLevel(level);
        // Ensure water level is synced when added to world
        if (level != null && !level.isClientSide) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(WaterPotBlock.WATER_LEVEL) && 
                currentState.getValue(WaterPotBlock.WATER_LEVEL) != waterLevel) {
                BlockState newState = currentState.setValue(WaterPotBlock.WATER_LEVEL, waterLevel);
                level.setBlock(worldPosition, newState, 3);
            }
        }
    }
}