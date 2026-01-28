package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;

public class FrameLoomBlockEntity extends BlockEntity {

    private ResourceLocation selectedOutputItemId;

    public FrameLoomBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FRAME_LOOM_BLOCK_ENTITY.get(), pos, state);
    }

    public void clearSelection() {
        selectedOutputItemId = null;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void setSelectedOutputItem(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id != null) {
            selectedOutputItemId = id;
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    @Nullable
    public Item getSelectedOutputItem() {
        if (selectedOutputItemId == null) return null;
        return ForgeRegistries.ITEMS.getValue(selectedOutputItemId);
    }

    @Nullable
    public ResourceLocation getSelectedOutputItemId() {
        return selectedOutputItemId;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("selected_item")) {
            selectedOutputItemId = ResourceLocation.parse(tag.getString("selected_item"));
        } else {
            selectedOutputItemId = null;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (selectedOutputItemId != null) {
            tag.putString("selected_item", selectedOutputItemId.toString());
        }
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


