package com.torr.materia.blockentity;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.utils.FuelHelper;
import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import com.torr.materia.capability.HotMetalCapability;
import com.torr.materia.menu.KilnMenu;
import com.torr.materia.recipe.KilnRecipe;
import com.torr.materia.recipe.AdvancedKilnRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class KilnBlockEntity extends BlockEntity implements MenuProvider {
    // Define fuel tags
    private static final TagKey<Item> ROUGH_PLANKS = ItemTags.create(new ResourceLocation("materia", "rough_planks"));
    private static final TagKey<Item> SMOOTH_PLANKS = ItemTags.create(new ResourceLocation("materia", "smooth_planks"));
    private static final TagKey<Item> POSTS = ItemTags.create(new ResourceLocation("materia", "posts"));
    
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            // If output slot (slot 2) changes, trigger immediate fuel consumption if recipe is now valid
            if (slot == 2 && level != null && !level.isClientSide) {
                if (hasRecipe(KilnBlockEntity.this) && fuelTime == 0 && canConsumeFuel()) {
                    consumeFuel();
                }
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Disallow inserting into output slot (slot 2)
            if (slot == 2) return false;
            return super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            // Block any external insertion into output slot
            if (slot == 2 && !stack.isEmpty()) return stack;
            return super.insertItem(slot, stack, simulate);
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 0;
    private boolean burningCoke = false;

    public KilnBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.KILN_BLOCK_ENTITY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> KilnBlockEntity.this.progress;
                    case 1 -> KilnBlockEntity.this.maxProgress;
                    case 2 -> KilnBlockEntity.this.fuelTime;
                    case 3 -> KilnBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> KilnBlockEntity.this.progress = value;
                    case 1 -> KilnBlockEntity.this.maxProgress = value;
                    case 2 -> KilnBlockEntity.this.fuelTime = value;
                    case 3 -> KilnBlockEntity.this.maxFuelTime = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.kiln");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        // Check if this is a blast furnace
        if (this.level != null && this.level.getBlockState(this.worldPosition).getBlock() == com.torr.materia.ModBlocks.BLAST_FURNACE_KILN.get()) {
            return new com.torr.materia.menu.BlastFurnaceMenu(containerId, inventory, this, this.data);
        }
        // If this BE is hosted by the furnace-kiln block, always use the furnace menu (dual-input)
        if (this.level != null && this.level.getBlockState(this.worldPosition).getBlock() == com.torr.materia.ModBlocks.FURNACE_KILN.get()) {
            return new com.torr.materia.menu.FurnaceKilnMenu(containerId, inventory, this, this.data);
        }
        // Otherwise pick between basic and advanced kiln
        if (hasChimney()) {
            return new com.torr.materia.menu.AdvancedKilnMenu(containerId, inventory, this, this.data);
        } else {
            return new com.torr.materia.menu.KilnMenu(containerId, inventory, this, this.data);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("kiln.progress", progress);
        tag.putInt("kiln.fuel_time", fuelTime);
        tag.putInt("kiln.max_fuel_time", maxFuelTime);
        tag.putBoolean("kiln.burning_coke", burningCoke);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        
        // Handle migration from 3-slot to 4-slot inventory
        CompoundTag inventoryTag = tag.getCompound("inventory");
        if (inventoryTag.contains("Size") && inventoryTag.getInt("Size") == 3) {
            // Migrate old 3-slot inventory to 4-slot
            CompoundTag newInventoryTag = new CompoundTag();
            newInventoryTag.putInt("Size", 4);
            
            // Copy existing slots 0, 1, 2
            for (int i = 0; i < 3; i++) {
                if (inventoryTag.contains("Slot" + i)) {
                    newInventoryTag.put("Slot" + i, inventoryTag.get("Slot" + i));
                }
            }
            // Initialize slot 3 as empty
            newInventoryTag.put("Slot3", new CompoundTag());
            
            itemHandler.deserializeNBT(newInventoryTag);
        } else {
            itemHandler.deserializeNBT(inventoryTag);
        }
        
        progress = tag.getInt("kiln.progress");
        fuelTime = tag.getInt("kiln.fuel_time");
        maxFuelTime = tag.getInt("kiln.max_fuel_time");
        burningCoke = tag.getBoolean("kiln.burning_coke");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    /**
     * Check if this kiln has a chimney on top
     */
    private boolean hasChimney() {
        if (level == null) return false;
        // Blast furnace behaves as if chimney is always present
        if (level.getBlockState(worldPosition).getBlock() == ModBlocks.BLAST_FURNACE_KILN.get()) return true;
        // Furnace-kiln requires a furnace chimney block above it
        if (level.getBlockState(worldPosition).getBlock() == ModBlocks.FURNACE_KILN.get()) {
            return hasFurnaceChimney();
        }
        BlockPos chimneyPos = worldPosition.above();
        return level.getBlockState(chimneyPos).is(ModBlocks.CHIMNEY.get());
    }

    /**
     * Check if this kiln has bellows adjacent to it
     */
    private boolean hasBellows() {
        if (level == null) return false;
        // Blast furnace and furnace-kiln behave as if bellows are always present
        if (level.getBlockState(worldPosition).getBlock() == ModBlocks.BLAST_FURNACE_KILN.get()) return true;
        if (level.getBlockState(worldPosition).getBlock() == ModBlocks.FURNACE_KILN.get()) return true;
        BlockPos[] adjacentPositions = {
            worldPosition.north(),
            worldPosition.south(),
            worldPosition.east(),
            worldPosition.west()
        };
        
        for (BlockPos pos : adjacentPositions) {
            if (level.getBlockState(pos).is(ModBlocks.BELLOWS.get())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if this furnace has a furnace chimney above it
     */
    private boolean hasFurnaceChimney() {
        if (level == null) return false;
        // Only furnace-kiln can have furnace chimneys
        if (level.getBlockState(worldPosition).getBlock() != ModBlocks.FURNACE_KILN.get()) return false;
        BlockPos chimneyPos = worldPosition.above();
        return level.getBlockState(chimneyPos).is(ModBlocks.FURNACE_CHIMNEY.get());
    }

    /**
     * Check if this is a blast furnace
     */
    private boolean isBlastFurnace() {
        if (level == null) return false;
        return level.getBlockState(worldPosition).getBlock() == ModBlocks.BLAST_FURNACE_KILN.get();
    }

    /**
     * Check if this is a furnace-type block (furnace or blast furnace)
     */
    private boolean isFurnaceType() {
        if (level == null) return false;
        return level.getBlockState(worldPosition).getBlock() == ModBlocks.FURNACE_KILN.get() ||
               level.getBlockState(worldPosition).getBlock() == ModBlocks.BLAST_FURNACE_KILN.get();
    }

    /**
     * Get the cooking time for this type of furnace/kiln
     */
    private int getCookingTime() {
        if (isBlastFurnace()) {
            return 100; // Blast furnace is twice as fast (100 vs 200)
        }
        return 200; // Default cooking time
    }

    /**
     * Get the temperature tier of this kiln setup
     * 1 = kiln alone, 2 = kiln + chimney, 3 = kiln + chimney + bellows
     */
    private int getTemperatureTier() {
        boolean chimney = hasChimney();
        boolean bellows = hasBellows();
        
        if (chimney && bellows) return 3;
        if (chimney) return 2;
        return 1;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, KilnBlockEntity entity) {
        if (level.isClientSide()) {
            return;
        }

        boolean hasRecipe = hasRecipe(entity);
        boolean wasBurning = entity.fuelTime > 0;

        if (entity.fuelTime > 0) {
            entity.fuelTime--;
        }

        if (hasRecipe) {
            // Start fuel if not burning and fuel is available
            if (entity.fuelTime == 0 && entity.canConsumeFuel()) {
                entity.consumeFuel();
            }

            // Update maxProgress based on block type
            entity.maxProgress = entity.getCookingTime();

            if (entity.fuelTime > 0) {
                entity.progress++;
                if (entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                    entity.progress = 0;
                }
            }
        } else {
            // Only reset progress if no recipe is valid
            entity.progress = 0;
        }

        // Keep items in output slot hot while kiln is burning
        if (entity.fuelTime > 0) {
            // Every 60 ticks (3 seconds), refresh heat on items in output slot
            if (level.getGameTime() % 60 == 0) {
                ItemStack outputStack = entity.itemHandler.getStackInSlot(2);
                if (!outputStack.isEmpty() && outputStack.is(com.torr.materia.events.HotMetalEventHandler.HEATABLE_METALS)) {
                    outputStack.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY).ifPresent(hotCap -> {
                        // Refresh the heat - reset the timer
                        hotCap.heat();
                    });
                }
            }
        }

        boolean isBurning = entity.fuelTime > 0;
        if (wasBurning != isBurning) {
            // Support both kiln and furnace_kiln blocks updating LIT property
            if (blockState.hasProperty(com.torr.materia.KilnBlock.LIT)) {
                level.setBlock(blockPos, blockState.setValue(com.torr.materia.KilnBlock.LIT, isBurning), 3);
            } else {
                level.setBlock(blockPos, blockState, 3);
            }
            entity.setChanged();
        }
    }

    private boolean canConsumeFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(1); // Slot 1 is fuel
        return isFuel(fuelStack) && !fuelStack.isEmpty();
    }

    private void consumeFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(1);
        if (isFuel(fuelStack)) {
            this.fuelTime = getFuelTime(fuelStack);
            this.maxFuelTime = this.fuelTime;
            this.burningCoke = fuelStack.getItem() == ModItems.COAL_COKE.get();
            fuelStack.shrink(1);
        }
    }

    private boolean hasCokeFuelAvailableOrBurning() {
        if (this.fuelTime > 0) {
            return this.burningCoke;
        }
        ItemStack fuelStack = this.itemHandler.getStackInSlot(1);
        return !fuelStack.isEmpty() && fuelStack.getItem() == ModItems.COAL_COKE.get();
    }

    private static boolean isFuel(ItemStack stack) {
        // Use the centralized fuel helper to ensure consistency
        return FuelHelper.isKilnFuel(stack);
    }

    private static int getFuelTime(ItemStack stack) {
        // Use the centralized fuel helper to ensure consistency
        return FuelHelper.getFuelTime(stack);
    }

    private static void craftItem(KilnBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        // First try advanced recipes (alloys)
        Optional<AdvancedKilnRecipe> advancedRecipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ADVANCED_KILN_TYPE.get(), inventory, level);

        if (advancedRecipe.isPresent()) {
            AdvancedKilnRecipe recipe = advancedRecipe.get();

            // Require coal coke for hot recipes
            if (recipe.requiresCokeFuel() && !entity.hasCokeFuelAvailableOrBurning()) {
                return;
            }
            
            // Check if chimney is required and present
            if (recipe.requiresChimney() && !entity.hasChimney()) {
                return; // Can't craft without chimney
            }
            
            // Check if bellows is required and present
            if (recipe.requiresBellows() && !entity.hasBellows()) {
                return; // Can't craft without bellows
            }
            
            // Special case: Steel smelting (raw iron + coal -> steel) requires furnace chimney or blast furnace
            if (recipe.getResultItem(level.registryAccess()).getItem() == net.minecraft.world.item.Items.IRON_INGOT) {
                if (!(entity.isBlastFurnace() || entity.hasFurnaceChimney())) {
                    return; // Steel requires furnace+chimney or blast furnace
                }
            }

            // Zinc evaporation: in furnace/blast furnace, or advanced kiln (chimney+bellows), raw zinc-only smelt produces nothing
            {
                ItemStack aItem = entity.itemHandler.getStackInSlot(0);
                ItemStack bItem = entity.itemHandler.getStackInSlot(3);
                boolean onlyRawZinc = !aItem.isEmpty() && aItem.getItem() == ModItems.RAW_ZINC.get() && bItem.isEmpty();
                if (onlyRawZinc && (entity.isFurnaceType() || entity.getTemperatureTier() == 3)) {
                    entity.itemHandler.extractItem(0, 1, false);
                    return;
                }
            }

            ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
            ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
            boolean outputInserted = false;
            
            if (outputSlot.isEmpty()) {
                entity.itemHandler.setStackInSlot(2, result);
                com.torr.materia.events.HotMetalEventHandler.heatMetalItem(result);
                outputInserted = true;
            } else if (outputSlot.getItem() == result.getItem()) {
                // Ensure new result is hot before merging so hot-hot merge/averaging works
                com.torr.materia.events.HotMetalEventHandler.heatMetalItem(result);
                ItemStack merged = HotMetalStackingUtils.tryMergeStacks(outputSlot, result);
                if (merged.getCount() != outputSlot.getCount()) {
                    entity.itemHandler.setStackInSlot(2, merged);
                    outputInserted = true;
                }
            }

            if (!outputInserted) {
                return; // Don't consume inputs if we couldn't place/merge the output
            }

            // Determine which slot contains copper and which contains zinc (or other)
            int slotA = 0;
            int slotB = 3;
            if (recipe.getResultItem(level.registryAccess()).getItem() == com.torr.materia.ModItems.BRASS_INGOT.get()) {
                // Need 1 zinc and 2 copper. Support either raw or ingot forms.
                boolean slotAIsCopper = isCopperItem(entity.itemHandler.getStackInSlot(slotA).getItem());
                int copperSlot = slotAIsCopper ? slotA : slotB;
                int zincSlot   = slotAIsCopper ? slotB : slotA;

                entity.itemHandler.extractItem(zincSlot, 1, false);   // remove 1 zinc
                entity.itemHandler.extractItem(copperSlot, 2, false); // remove 2 copper
            } else {
                // Default: consume 1 from each slot
                entity.itemHandler.extractItem(slotA, 1, false);
                entity.itemHandler.extractItem(slotB, 1, false);
            }
            return;
        }

        // Fall back to basic kiln recipes (never cook food in kiln)
        Optional<KilnRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.KILN_TYPE.get(), inventory, level);

        if (recipe.isPresent()) {
            KilnRecipe kilnRecipe = recipe.get();

            // Require coal coke for hot recipes
            if (kilnRecipe.requiresCokeFuel() && !entity.hasCokeFuelAvailableOrBurning()) {
                return;
            }

            // Block edible outputs from being produced in kiln
            if (kilnRecipe.getResultItem(level.registryAccess()).isEdible()) {
                return;
            }
            
            // Check if chimney is required and present
            if (kilnRecipe.requiresChimney() && !entity.hasChimney()) {
                return; // Can't craft without chimney
            }
            // Netherite scrap requires furnace-type high heat (blast furnace, or furnace-kiln with furnace chimney)
            if (kilnRecipe.getResultItem(level.registryAccess()).getItem() == Items.NETHERITE_SCRAP) {
                boolean allowed = entity.isBlastFurnace() || (entity.isFurnaceType() && entity.hasFurnaceChimney());
                if (!allowed) return;
            }
            // Gate wrought iron ingot to furnace or blast furnace with furnace chimney (for furnace)
            if (kilnRecipe.getResultItem(level.registryAccess()).getItem() == com.torr.materia.ModItems.WROUGHT_IRON_INGOT.get()) {
                boolean allowed = entity.isBlastFurnace() || (entity.isFurnaceType() && entity.hasFurnaceChimney());
                if (!allowed) return;
            }
            
            // Check if bellows is required and present
            if (kilnRecipe.requiresBellows() && !entity.hasBellows()) {
                return; // Can't craft without bellows
            }
            
            // Zinc evaporation: in furnace/blast furnace, or advanced kiln (chimney+bellows), raw zinc-only smelt produces nothing
            {
                ItemStack in = entity.itemHandler.getStackInSlot(0);
                if (!in.isEmpty() && in.getItem() == ModItems.RAW_ZINC.get()) {
                    if (entity.isFurnaceType() || entity.getTemperatureTier() == 3) {
                        entity.itemHandler.extractItem(0, 1, false);
                        return;
                    }
                }
            }

            ItemStack result = kilnRecipe.getResultItem(level.registryAccess()).copy();

            // In furnace types, never produce nuggets from kiln recipes; fall through to vanilla smelting instead
            if (entity.isFurnaceType() && isNuggetItem(result.getItem())) {
                // Do not craft via kiln recipe; allow vanilla smelting below
            } else {
            ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
            boolean outputInserted = false;
            
            if (outputSlot.isEmpty()) {
                entity.itemHandler.setStackInSlot(2, result);
                com.torr.materia.events.HotMetalEventHandler.heatMetalItem(result);
                outputInserted = true;
            } else if (outputSlot.getItem() == result.getItem()) {
                // Ensure new result is hot before merging so hot-hot merge/averaging works
                com.torr.materia.events.HotMetalEventHandler.heatMetalItem(result);
                ItemStack merged = HotMetalStackingUtils.tryMergeStacks(outputSlot, result);
                if (merged.getCount() != outputSlot.getCount()) {
                    entity.itemHandler.setStackInSlot(2, merged);
                    outputInserted = true;
                }
            }

            if (outputInserted) {
                entity.itemHandler.extractItem(0, 1, false); // Remove input item
                return;
            } else {
                return; // Do not consume input if output can't accept
            }
            }
        }
        
        // No kiln recipe found - check vanilla smelting recipes for heatable metals or stone smelting
        Optional<net.minecraft.world.item.crafting.SmeltingRecipe> smeltingRecipe = level.getRecipeManager()
                .getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING, inventory, level);
        
        if (smeltingRecipe.isPresent()) {
            net.minecraft.world.item.crafting.SmeltingRecipe smeltRecipe = smeltingRecipe.get();
            ItemStack result = smeltRecipe.getResultItem(level.registryAccess()).copy();
            ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);

            // Aluminum requires at least chimney tier in kilns; furnace types ignore this gate
            if (result.getItem() == ModItems.ALUMINUM_INGOT.get()) {
                if (!(entity.isFurnaceType() || entity.getTemperatureTier() >= 2)) {
                    return;
                }
            }
            
            // Only allow smelting if result is a heatable metal (like glass pucks)
            if (result.is(com.torr.materia.events.HotMetalEventHandler.HEATABLE_METALS)) {
                if (outputSlot.isEmpty()) {
                    entity.itemHandler.setStackInSlot(2, result);
                    com.torr.materia.events.HotMetalEventHandler.heatMetalItem(result);
                    entity.itemHandler.extractItem(0, 1, false);
                    return;
                } else if (outputSlot.getItem() == result.getItem()) {
                    // Ensure new result is hot before merging so hot-hot merge/averaging works
                    com.torr.materia.events.HotMetalEventHandler.heatMetalItem(result);
                    ItemStack merged = HotMetalStackingUtils.tryMergeStacks(outputSlot, result);
                    if (merged.getCount() != outputSlot.getCount()) {
                        entity.itemHandler.setStackInSlot(2, merged);
                        entity.itemHandler.extractItem(0, 1, false);
                        return;
                    }
                }
            }
            
            // Allow stone smelting for blast furnaces (no chimney needed) or furnaces with furnace chimney
            if ((entity.isBlastFurnace() || entity.hasFurnaceChimney()) && isStoneSmeltingRecipe(smeltRecipe)) {
                if (outputSlot.isEmpty()) {
                    entity.itemHandler.extractItem(0, 1, false);
                    entity.itemHandler.setStackInSlot(2, result);
                    return;
                } else if (outputSlot.getItem() == result.getItem() && outputSlot.getCount() < outputSlot.getMaxStackSize()) {
                    entity.itemHandler.extractItem(0, 1, false);
                    outputSlot.grow(result.getCount());
                    entity.itemHandler.setStackInSlot(2, outputSlot);
                    return;
                }
            }
        }
        
        // No recipe matched: if the input is a heatable metal, allow re-heating
        ItemStack input = entity.itemHandler.getStackInSlot(0);
        if (!input.isEmpty()) {
            // Prevent food from being re-heated in kiln
            if (input.isEdible()) return;
            // Copy one item to output for re-heat if it matches tag
            ItemStack copy = input.copy();
            copy.setCount(1);
            if (copy.is(com.torr.materia.events.HotMetalEventHandler.HEATABLE_METALS)) {
                ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
                if (outputSlot.isEmpty()) {
                    entity.itemHandler.setStackInSlot(2, copy);
                    com.torr.materia.events.HotMetalEventHandler.heatMetalItem(copy);
                    entity.itemHandler.extractItem(0, 1, false);
                } else if (outputSlot.getItem() == copy.getItem()) {
                    // Ensure the copy is hot before merging so hot-hot works
                    com.torr.materia.events.HotMetalEventHandler.heatMetalItem(copy);
                    ItemStack merged = HotMetalStackingUtils.tryMergeStacks(outputSlot, copy);
                    if (merged.getCount() != outputSlot.getCount()) {
                        entity.itemHandler.setStackInSlot(2, merged);
                        entity.itemHandler.extractItem(0, 1, false);
                    }
                }
            }
        }
    }

    private static boolean hasRecipe(KilnBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        // Check advanced recipes first
        Optional<AdvancedKilnRecipe> advancedRecipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ADVANCED_KILN_TYPE.get(), inventory, level);

        if (advancedRecipe.isPresent()) {
            // Zinc evaporation availability check for furnace/blast furnace or advanced kiln (tier 3)
            ItemStack aItem = entity.itemHandler.getStackInSlot(0);
            ItemStack bItem = entity.itemHandler.getStackInSlot(3);
            if (!aItem.isEmpty() && aItem.getItem() == ModItems.RAW_ZINC.get() && bItem.isEmpty()) {
                if (entity.isFurnaceType() || entity.getTemperatureTier() == 3) {
                    return true; // evaporate allowed
                }
            }
            AdvancedKilnRecipe recipe = advancedRecipe.get();

            // Require coal coke for hot recipes
            if (recipe.requiresCokeFuel() && !entity.hasCokeFuelAvailableOrBurning()) {
                return false;
            }
            
            // Check if chimney is required and present
            if (recipe.requiresChimney() && !entity.hasChimney()) {
                return false; // Can't craft without chimney
            }
            
            // Check if bellows is required and present
            if (recipe.requiresBellows() && !entity.hasBellows()) {
                return false; // Can't craft without bellows
            }
            
            // Special case: Steel smelting requires furnace chimney or blast furnace
            if (recipe.getResultItem(level.registryAccess()).getItem() == net.minecraft.world.item.Items.IRON_INGOT) {
                if (!(entity.isBlastFurnace() || entity.hasFurnaceChimney())) {
                    return false;
                }
            }

            ItemStack advancedResult = recipe.getResultItem(level.registryAccess());
            boolean outputOk = canInsertAmountIntoOutputSlot(advancedResult, entity)
                    && canInsertItemIntoOutputSlot(advancedResult, entity);
            if (advancedResult.getItem() == com.torr.materia.ModItems.BRASS_INGOT.get()) {
                // ensure at least 2 copper and 1 zinc across the two input slots (raw or ingot forms)
                ItemStack a = entity.itemHandler.getStackInSlot(0);
                ItemStack b = entity.itemHandler.getStackInSlot(3);
                int copperCount = (isCopperItem(a.getItem()) ? a.getCount() : 0) + (isCopperItem(b.getItem()) ? b.getCount() : 0);
                int zincCount = (isZincItem(a.getItem()) ? a.getCount() : 0) + (isZincItem(b.getItem()) ? b.getCount() : 0);
                return outputOk && copperCount >= 2 && zincCount >= 1;
            }
            return outputOk;
        }

        // Fall back to basic recipes (but never food)
        Optional<KilnRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.KILN_TYPE.get(), inventory, level);

        if (recipe.isPresent()) {
            // Zinc evaporation availability check for furnace/blast furnace or advanced kiln (tier 3)
            ItemStack in = entity.itemHandler.getStackInSlot(0);
            if (!in.isEmpty() && in.getItem() == ModItems.RAW_ZINC.get()) {
                if (entity.isFurnaceType() || entity.getTemperatureTier() == 3) {
                    return true; // evaporate allowed
                }
            }
            KilnRecipe kilnRecipe = recipe.get();

            // Require coal coke for hot recipes
            if (kilnRecipe.requiresCokeFuel() && !entity.hasCokeFuelAvailableOrBurning()) {
                return false;
            }

            // Prevent edible outputs
            if (kilnRecipe.getResultItem(level.registryAccess()).isEdible()) {
                return false;
            }
            
            // Check if chimney is required and present
            if (kilnRecipe.requiresChimney() && !entity.hasChimney()) {
                return false; // Can't craft without chimney
            }
            // Gate wrought iron ingot to furnace with furnace chimney only
            if (kilnRecipe.getResultItem(level.registryAccess()).getItem() == com.torr.materia.ModItems.WROUGHT_IRON_INGOT.get()) {
                return entity.isFurnaceType() && entity.hasFurnaceChimney()
                        && canInsertAmountIntoOutputSlot(kilnRecipe.getResultItem(level.registryAccess()), entity)
                        && canInsertItemIntoOutputSlot(kilnRecipe.getResultItem(level.registryAccess()), entity);
            }
            
            // Check if bellows is required and present
            if (kilnRecipe.requiresBellows() && !entity.hasBellows()) {
                return false; // Can't craft without bellows
            }
            
            // In furnace types, never allow kiln nugget recipes (ingots only)
            if (entity.isFurnaceType() && isNuggetItem(kilnRecipe.getResultItem(level.registryAccess()).getItem())) {
                // Skip kiln-based nugget results; allow vanilla smelting checks below
            } else {
                ItemStack basicResult = kilnRecipe.getResultItem(level.registryAccess());
                return canInsertAmountIntoOutputSlot(basicResult, entity)
                        && canInsertItemIntoOutputSlot(basicResult, entity);
            }
        }

        // Check vanilla smelting recipes for heatable metals or stone smelting
        Optional<net.minecraft.world.item.crafting.SmeltingRecipe> smeltingRecipe = level.getRecipeManager()
                .getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING, inventory, level);
        
        if (smeltingRecipe.isPresent()) {
            net.minecraft.world.item.crafting.SmeltingRecipe smeltRecipe = smeltingRecipe.get();
            ItemStack result = smeltRecipe.getResultItem(level.registryAccess());

            // Aluminum requires at least chimney tier in kilns; furnace types ignore this gate
            if (result.getItem() == ModItems.ALUMINUM_INGOT.get()) {
                if (!(entity.isFurnaceType() || entity.getTemperatureTier() >= 2)) {
                    return false;
                }
            }
            
            // Allow smelting if result is a heatable metal (like glass pucks)
            if (result.is(com.torr.materia.events.HotMetalEventHandler.HEATABLE_METALS)) {
                return canInsertAmountIntoOutputSlot(result, entity)
                        && canInsertItemIntoOutputSlot(result, entity);
            }
            
            // Allow stone smelting for blast furnaces (no chimney needed) or furnaces with furnace chimney
            if ((entity.isBlastFurnace() || entity.hasFurnaceChimney()) && isStoneSmeltingRecipe(smeltRecipe)) {
                return canInsertAmountIntoOutputSlot(result, entity)
                        && canInsertItemIntoOutputSlot(result, entity);
            }
        }

        // No recipe matched: allow re-heating in kiln if input is heatable metal and output can accept
        ItemStack input = entity.itemHandler.getStackInSlot(0);
        if (!input.isEmpty() && !input.isEdible() && input.is(com.torr.materia.events.HotMetalEventHandler.HEATABLE_METALS)) {
            ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
            return outputSlot.isEmpty() || (outputSlot.getItem() == input.getItem() && outputSlot.getCount() < outputSlot.getMaxStackSize());
        }

        return false;
    }

    private static boolean canInsertItemIntoOutputSlot(ItemStack result, KilnBlockEntity entity) {
        ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
        return outputSlot.isEmpty() || outputSlot.getItem().equals(result.getItem());
    }

    private static boolean canInsertAmountIntoOutputSlot(ItemStack result, KilnBlockEntity entity) {
        ItemStack outputSlot = entity.itemHandler.getStackInSlot(2);
        return outputSlot.isEmpty() || outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize();
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    /**
     * Check if a smelting recipe is for stone-type materials that should only work with furnace chimney
     */
    private static boolean isStoneSmeltingRecipe(net.minecraft.world.item.crafting.SmeltingRecipe recipe) {
        ItemStack input = recipe.getIngredients().get(0).getItems()[0];
        ItemStack result = recipe.getResultItem(net.minecraft.core.RegistryAccess.EMPTY);
        
        // Common stone smelting recipes
        return (input.getItem() == net.minecraft.world.item.Items.COBBLESTONE && result.getItem() == net.minecraft.world.item.Items.STONE) ||
               (input.getItem() == net.minecraft.world.item.Items.COBBLED_DEEPSLATE && result.getItem() == net.minecraft.world.item.Items.DEEPSLATE) ||
               (input.getItem() == net.minecraft.world.item.Items.BLACKSTONE && result.getItem() == net.minecraft.world.item.Items.POLISHED_BLACKSTONE) ||
               (input.getItem() == net.minecraft.world.item.Items.NETHERRACK && result.getItem() == net.minecraft.world.item.Items.NETHER_BRICK) ||
               (input.getItem() == net.minecraft.world.item.Items.STONE_BRICKS && result.getItem() == net.minecraft.world.item.Items.CRACKED_STONE_BRICKS) ||
               (input.getItem() == net.minecraft.world.item.Items.NETHER_BRICKS && result.getItem() == net.minecraft.world.item.Items.CRACKED_NETHER_BRICKS) ||
               (input.getItem() == net.minecraft.world.item.Items.POLISHED_BLACKSTONE_BRICKS && result.getItem() == net.minecraft.world.item.Items.CRACKED_POLISHED_BLACKSTONE_BRICKS) ||
               // High-temperature stone processing
               (input.getItem() == net.minecraft.world.item.Items.STONE && result.getItem() == net.minecraft.world.item.Items.SMOOTH_STONE) ||
               (input.getItem() == net.minecraft.world.item.Items.SANDSTONE && result.getItem() == net.minecraft.world.item.Items.SMOOTH_SANDSTONE) ||
               (input.getItem() == net.minecraft.world.item.Items.RED_SANDSTONE && result.getItem() == net.minecraft.world.item.Items.SMOOTH_RED_SANDSTONE) ||
               (input.getItem() == net.minecraft.world.item.Items.QUARTZ_BLOCK && result.getItem() == net.minecraft.world.item.Items.SMOOTH_QUARTZ);
    }

    private static boolean isNuggetItem(net.minecraft.world.item.Item item) {
        return item == net.minecraft.world.item.Items.IRON_NUGGET
                || item == net.minecraft.world.item.Items.GOLD_NUGGET
                || item == com.torr.materia.ModItems.COPPER_NUGGET.get()
                || item == com.torr.materia.ModItems.TIN_NUGGET.get()
                || item == com.torr.materia.ModItems.ZINC_NUGGET.get()
                || item == com.torr.materia.ModItems.BRASS_NUGGET.get()
                || item == com.torr.materia.ModItems.BRONZE_NUGGET.get()
                || item == com.torr.materia.ModItems.WROUGHT_IRON_NUGGET.get();
    }

    private static boolean isCopperItem(net.minecraft.world.item.Item item) {
        return item == net.minecraft.world.item.Items.COPPER_INGOT
                || item == net.minecraft.world.item.Items.RAW_COPPER
                || item == com.torr.materia.ModItems.COPPER_NUGGET.get();
    }

    private static boolean isZincItem(net.minecraft.world.item.Item item) {
        return item == com.torr.materia.ModItems.ZINC_INGOT.get()
                || item == com.torr.materia.ModItems.RAW_ZINC.get()
                || item == com.torr.materia.ModItems.ZINC_NUGGET.get();
    }
} 