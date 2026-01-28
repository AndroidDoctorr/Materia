package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModItems;
import com.torr.materia.ModBlocks;
import com.torr.materia.WaterPotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * BlockEntity for the Water Pot. Handles boiling detection and simple in-place
 * recipes:
 * - Bone -> Glue
 * - Tanned Leather -> Hardened Leather
 * - Brandaris gland -> Boiled Brandaris Gland
 * - Trunculus gland -> Boiled Trunculus Gland
 * - Haemastoma gland -> Boiled Haemastoma Gland
 * - Oak Bark -> Boiled Bark
 * - Earth -> Clay Ball and Dirt
 * - Paper mixture -> Paper Pulp
 */
public class WaterPotBlockEntity extends BlockEntity {

    private static final int COOK_TIME_TOTAL = 160; // 8 seconds at 20 tps
    private static final TagKey<Item> EARTH_BLOCKS = ItemTags.create(new ResourceLocation(com.torr.materia.materia.MOD_ID, "earth_blocks"));

    private int boilTicks;
    private int cookTime;
    private final Random random = new Random();
    
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

        // Determine whether recipe needs boiling
        boolean requiresBoiling = input.is(Items.BONE) || input.is(ModItems.TANNED_LEATHER.get()) || 
                                 input.is(ModItems.MUREX_GLAND_BRANDARIS.get()) ||
                                 input.is(ModItems.MUREX_GLAND_TRUNCULUS.get()) ||
                                 input.is(ModItems.MUREX_GLAND_HAEMASTOMA.get()) ||
                                 input.is(ModItems.OAK_BARK.get());
        if (requiresBoiling && !isBoiling) {
            cookTime = 0;
            return;
        }

        cookTime++;
        if (cookTime < COOK_TIME_TOTAL) {
            return;
        }

        if (input.is(Items.BONE)) {
            // Bone  -> Glue
            items.extractItem(0, 1, false);
            spawnOutput(new ItemStack(ModItems.GLUE.get()));
        } else if (input.is(ModItems.TANNED_LEATHER.get())) {
            // Leather -> Hardened Leather
            items.extractItem(0, 1, false);
            spawnOutput(new ItemStack(ModItems.HARDENED_LEATHER.get()));
        } else if (input.is(ModItems.MUREX_GLAND_BRANDARIS.get())) {
            // Brandaris gland -> boiled brandaris gland
            items.extractItem(0, 1, false);
            spawnOutput(new ItemStack(ModItems.BOILED_MUREX_GLAND_BRANDARIS.get()));
        } else if (input.is(ModItems.MUREX_GLAND_TRUNCULUS.get())) {
            // Trunculus gland -> boiled trunculus gland
            items.extractItem(0, 1, false);
            spawnOutput(new ItemStack(ModItems.BOILED_MUREX_GLAND_TRUNCULUS.get()));
        } else if (input.is(ModItems.MUREX_GLAND_HAEMASTOMA.get())) {
            // Haemastoma gland -> boiled haemastoma gland
            items.extractItem(0, 1, false);
            spawnOutput(new ItemStack(ModItems.BOILED_MUREX_GLAND_HAEMASTOMA.get()));
        } else if (input.is(ModItems.OAK_BARK.get())) {
            // Oak bark -> boiled bark
            items.extractItem(0, 1, false);
            spawnOutput(new ItemStack(ModItems.BOILED_BARK.get()));
        } else if (input.is(EARTH_BLOCKS)) {
            // Need two earth blocks to separate
            if (input.getCount() < 2) {
                // Wait until we have two
                cookTime = 0;
                return;
            }
            items.extractItem(0, 2, false);
            spawnOutput(new ItemStack(Items.CLAY_BALL, 4));
            spawnOutput(new ItemStack(Items.DIRT));
            // Replace water pot with empty pot block
            level.setBlock(worldPosition, ModBlocks.POT.get().defaultBlockState(), 3);
        } else if (input.is(ModItems.PAPER_MIXTURE.get())) {
            // Paper mixture -> paper pulp
            items.extractItem(0, 1, false);
            spawnOutput(new ItemStack(ModItems.PAPER_PULP.get()));
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