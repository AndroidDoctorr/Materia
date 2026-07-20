package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.block.PottedPlantRules;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

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

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (plant != Blocks.AIR) {
            tag.putString("Plant", ForgeRegistries.BLOCKS.getKey(plant).toString());
        } else {
            tag.remove("Plant");
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Plant")) {
            ResourceLocation id = new ResourceLocation(tag.getString("Plant"));
            Block block = ForgeRegistries.BLOCKS.getValue(id);
            plant = block != null ? block : Blocks.AIR;
        } else {
            plant = Blocks.AIR;
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    public static boolean accepts(Block block) {
        return PottedPlantRules.canPlantInUrn(block);
    }
}
