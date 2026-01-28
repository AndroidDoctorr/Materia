package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.menu.BronzeAnvilMenu;
import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BronzeAnvilBlockEntity extends BlockEntity implements MenuProvider {
    
    // Bronze anvil has 2 tool slots + 1 input slot
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (slot == 0 || slot == 1) {
                // Tool slots - check for hammer or other tools
                return isValidTool(stack);
            } else if (slot == 2) {
                // Input slot - check for hot metals
                return isValidInput(stack);
            }
            return false;
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    
    // Anvil durability - bronze anvils are much more durable
    private static final int MAX_DURABILITY = 400; // Higher durability for bronze
    private int durability = MAX_DURABILITY;

    public BronzeAnvilBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRONZE_ANVIL_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.bronze_anvil");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return new BronzeAnvilMenu(windowId, playerInventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("durability", durability);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        durability = tag.getInt("durability");
        if (durability == 0) durability = MAX_DURABILITY; // Default for old saves
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, 
            new net.minecraft.world.SimpleContainer(itemHandler.getSlots()) {
                {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        setItem(i, itemHandler.getStackInSlot(i));
                    }
                }
            });
    }

    private boolean isValidTool(ItemStack stack) {
        // Accept all anvil tools: hammers, tongs, chisels, drawplates, bores, etc.
        String itemName = stack.getItem().toString().toLowerCase();
        return itemName.contains("hammer") || 
               itemName.contains("mallet") ||
               itemName.contains("tongs") ||
               itemName.contains("chisel") ||
               itemName.contains("drawplate") ||
               itemName.contains("bore") ||
               itemName.contains("awl") ||
               itemName.contains("file");
    }

    private boolean isValidInput(ItemStack stack) {
        // Accept items with hot metal capability
        return stack.getCapability(com.torr.materia.capability.HotMetalCapability.HOT_METAL_CAPABILITY)
            .map(cap -> cap.isHot()).orElse(false);
    }

    /**
     * Damage the anvil based on recipe complexity
     * @param recipeName The name of the recipe being crafted
     * @return true if anvil is still usable, false if it broke
     */
    public boolean damageAnvil(String recipeName) {
        int damage = getRecipeDamage(recipeName);
        durability -= damage;
        setChanged();
        
        if (durability <= 0) {
            breakAnvil();
            return false;
        }
        return true;
    }
    
    private int getRecipeDamage(String recipeName) {
        // Bronze anvils are much more durable, most recipes only cause 1 damage
        if (recipeName.contains("iron_anvil")) {
            return 15; // Making iron anvils is hard on bronze anvil
        } else if (recipeName.contains("block") || recipeName.contains("anvil")) {
            return 5; // Large items cause more damage
        } else {
            return 1; // Most recipes cause minimal damage
        }
    }
    
    private void breakAnvil() {
        if (level != null && !level.isClientSide) {
            // Play anvil breaking sound (same as vanilla)
            level.playSound(null, worldPosition, net.minecraft.sounds.SoundEvents.ANVIL_DESTROY, 
                net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            
            // Drop all items
            drops();
            
            // Remove the block
            level.removeBlock(worldPosition, false);
        }
    }
    
    public int getDurability() {
        return durability;
    }
    
    public int getMaxDurability() {
        return MAX_DURABILITY;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BronzeAnvilBlockEntity blockEntity) {
        if (level.isClientSide()) return;
        
        // Cool down hot metals in anvil every 40 ticks (2 seconds)
        if (level.getGameTime() % 40 == 0) {
            // Create a simple container view of the anvil inventory for cooling
            SimpleContainer tempContainer = new SimpleContainer(blockEntity.itemHandler.getSlots());
            for (int i = 0; i < blockEntity.itemHandler.getSlots(); i++) {
                tempContainer.setItem(i, blockEntity.itemHandler.getStackInSlot(i));
            }
            
            // Apply cooling
            HotMetalStackingUtils.convertCooledItems(tempContainer);
            
            // Copy back any changes
            for (int i = 0; i < blockEntity.itemHandler.getSlots(); i++) {
                blockEntity.itemHandler.setStackInSlot(i, tempContainer.getItem(i));
            }
        }
        
        // Process anvil recipes here if needed
        // For now, this is just a placeholder for future recipe processing
    }
}
