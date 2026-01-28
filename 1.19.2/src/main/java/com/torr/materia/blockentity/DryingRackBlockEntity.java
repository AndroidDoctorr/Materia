package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;

import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
        boolean shouldDryMeat = isRawMeat(items.getStackInSlot(1));
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
        }
        
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        dryTicks = 0;
    }

    public boolean isFinished() {
        boolean leatherFinished = items.getStackInSlot(0).is(ModItems.TANNED_LEATHER_STRETCHED.get());
        boolean jerkyFinished = items.getStackInSlot(1).is(ModItems.JERKY.get());
        return leatherFinished || jerkyFinished;
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
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return optional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.deserializeNBT(tag.getCompound("inv"));
        dryTicks = tag.getInt("t");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inv", items.serializeNBT());
        tag.putInt("t", dryTicks);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag t = new CompoundTag();
        saveAdditional(t);
        return t;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}
