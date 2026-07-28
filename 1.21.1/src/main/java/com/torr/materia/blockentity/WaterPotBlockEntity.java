package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModRecipes;
import com.torr.materia.WaterPotBlock;
import com.torr.materia.recipe.WaterPotRecipe;
import com.torr.materia.recipe.RecipeInputs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
    private LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> items);
    private LazyOptional<IFluidHandler> fluidCapability = LazyOptional.of(() -> new WaterPotFluidHandler(this));

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

        CraftingInput container = RecipeInputs.fromItemHandler(items, 1);
        Optional<WaterPotRecipe> recipeOpt = level.getRecipeManager()
                .getRecipeFor(ModRecipes.WATER_POT_TYPE.get(), container, level)
                .map(net.minecraft.world.item.crafting.RecipeHolder::value);

        if (recipeOpt.isEmpty()) {
            cookTime = 0;
            return;
        }
        WaterPotRecipe recipe = recipeOpt.get();

        if (recipe.requiresBoiling() && !isBoiling) {
            cookTime = 0;
            return;
        }
        if (recipe.requiresWater() && !hasWater()) {
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
        if (recipe.getResultBlock() != null) {
            transformToResultBlock(recipe.getResultBlock());
        } else if (recipe.consumesWater()) {
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

    private void transformToResultBlock(ResourceLocation blockId) {
        if (level == null || level.isClientSide) {
            return;
        }
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        if (block == null || block.defaultBlockState().isAir()) {
            return;
        }
        BlockState newState = block.defaultBlockState();
        if (newState.hasProperty(WaterPotBlock.WATER_LEVEL)) {
            newState = newState.setValue(WaterPotBlock.WATER_LEVEL, waterLevel);
        }
        level.setBlock(worldPosition, newState, 3);
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
        fluidCapability.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidCapability.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inv", items.serializeNBT(provider));
        tag.putInt("cook", cookTime);
        tag.putInt("waterLevel", waterLevel);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        items.deserializeNBT(provider, tag.getCompound("inv"));
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
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        loadAdditional(tag, provider);
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemOptional = LazyOptional.of(() -> items);
        fluidCapability = LazyOptional.of(() -> new WaterPotFluidHandler(this));
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