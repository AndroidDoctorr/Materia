package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import net.minecraft.core.BlockPos;
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
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("selected_item")) {
            selectedOutputItemId = new ResourceLocation(tag.getString("selected_item"));
        } else {
            selectedOutputItemId = null;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (selectedOutputItemId != null) {
            tag.putString("selected_item", selectedOutputItemId.toString());
        }
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


