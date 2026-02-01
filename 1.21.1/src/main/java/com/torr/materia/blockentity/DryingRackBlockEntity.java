package com.torr.materia.blockentity;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import com.torr.materia.ModBlockEntities;

import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class DryingRackBlockEntity extends BlockEntity {

    private static final int DRY_TIME = 600; // 30 seconds
    private int dryTicks;

    private final ItemStackHandler items = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> items);

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide)
            return;
        
        // Check if we have items that can be dried (leather in slot 0 OR meat in slot 1)
        boolean shouldDryLeather = items.getStackInSlot(0).is(ModItems.LEATHER_STRETCHED.get());
        boolean shouldDryMeat = isRawMeat(items.getStackInSlot(1)) || items.getStackInSlot(1).is(ModItems.CLEAN_GUT.get());
        boolean isDrying = (shouldDryLeather || shouldDryMeat) && hasLitCampfireNearby();

        // Update blockstate if needed
        boolean currentlyDrying = getBlockState().getValue(com.torr.materia.DryingRackBlock.DRYING);
        if (isDrying != currentlyDrying) {
            level.setBlock(worldPosition, getBlockState().setValue(com.torr.materia.DryingRackBlock.DRYING, isDrying),
                    3);
        }

        if (isDrying) {
            dryTicks++;
            if (dryTicks >= DRY_TIME) {
                finishDrying();
            }
        } else {
            dryTicks = 0;
        }
    }

    private void finishDrying() {
        // Process leather in slot 0
        ItemStack leatherIn = items.getStackInSlot(0);
        if (!leatherIn.isEmpty() && leatherIn.is(ModItems.LEATHER_STRETCHED.get())) {
            ItemStack leatherOut = new ItemStack(ModItems.TANNED_LEATHER_STRETCHED.get());
            items.setStackInSlot(0, leatherOut);
        }
        
        // Process meat in slot 1 - convert all meat to jerky preserving count
        ItemStack meatIn = items.getStackInSlot(1);
        if (!meatIn.isEmpty() && isRawMeat(meatIn)) {
            ItemStack jerky = new ItemStack(ModItems.JERKY.get(), meatIn.getCount());
            items.setStackInSlot(1, jerky);
        } else if (!meatIn.isEmpty() && meatIn.is(ModItems.CLEAN_GUT.get())) {
            ItemStack cord = new ItemStack(ModItems.CORD.get(), meatIn.getCount());
            items.setStackInSlot(1, cord);
        }
        
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        dryTicks = 0;
    }

    public boolean isFinished() {
        boolean leatherFinished = items.getStackInSlot(0).is(ModItems.TANNED_LEATHER_STRETCHED.get());
        boolean jerkyFinished = items.getStackInSlot(1).is(ModItems.JERKY.get());
        boolean cordFinished = items.getStackInSlot(1).is(ModItems.CORD.get());
        return leatherFinished || jerkyFinished || cordFinished;
    }
    
    private boolean isRawMeat(ItemStack stack) {
        return stack.is(net.minecraft.world.item.Items.PORKCHOP) ||
               stack.is(net.minecraft.world.item.Items.BEEF) ||
               stack.is(net.minecraft.world.item.Items.MUTTON);
    }

    public void resetCook() {
        dryTicks = 0;
    }

    public ItemStack getRenderStack() {
        return items.getStackInSlot(0);
    }
    
    public ItemStack getMeatRenderStack() {
        return items.getStackInSlot(1);
    }

    private boolean hasLitCampfireNearby() {
        for (var dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
            BlockState st = level.getBlockState(worldPosition.relative(dir));
            if (st.is(Blocks.CAMPFIRE) && st.getValue(CampfireBlock.LIT))
                return true;
        }
        return false;
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
        dryTicks = tag.getInt("t");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inv", items.serializeNBT(provider));
        tag.putInt("t", dryTicks);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag t = new CompoundTag();
        saveAdditional(t, provider);
        return t;
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
