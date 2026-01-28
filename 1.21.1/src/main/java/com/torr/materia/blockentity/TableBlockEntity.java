package com.torr.materia.blockentity;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TableBlockEntity extends BlockEntity {

    private final ItemStackHandler items = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            // Sync to client when items change
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Allow most items but exclude large blocks and entities
            return !stack.isEmpty() && stack.getMaxStackSize() > 1; // Basic filter
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1; // Only allow one item per table
        }
    };
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> items);

    public TableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TABLE_BLOCK_ENTITY.get(), pos, state);
    }

    public ItemStack getRenderStack() {
        return items.getStackInSlot(0);
    }

    public boolean isEmpty() {
        return items.getStackInSlot(0).isEmpty();
    }

    public ItemStack insertItem(ItemStack stack) {
        return items.insertItem(0, stack, false);
    }

    public ItemStack extractItem() {
        return items.extractItem(0, 1, false);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optional.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return optional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        items.deserializeNBT(provider, tag.getCompound("inv"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inv", items.serializeNBT(provider));
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
}
