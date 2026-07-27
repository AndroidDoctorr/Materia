package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.block.PottedPlantRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class UrnBlockEntity extends BlockEntity {
    private Block plant = Blocks.AIR;

    public UrnBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.URN_BLOCK_ENTITY.get(), pos, state);
    }

    public Block getPlant() {
        return plant;
    }

    public void setPlant(Block plant) {
        this.plant = plant;
        setChanged();
    }

    public boolean hasTallPlant() {
        return PottedPlantRules.isTallPlant(plant);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (plant != Blocks.AIR) {
            tag.putString("Plant", BuiltInRegistries.BLOCK.getKey(plant).toString());
        } else {
            tag.remove("Plant");
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Plant")) {
            ResourceLocation id = ResourceLocation.parse(tag.getString("Plant"));
            plant = BuiltInRegistries.BLOCK.getOptional(id).orElse(Blocks.AIR);
        } else {
            plant = Blocks.AIR;
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        loadAdditional(tag, provider);
    }

    public static boolean accepts(Block block) {
        return PottedPlantRules.canPlantInUrn(block);
    }
}
