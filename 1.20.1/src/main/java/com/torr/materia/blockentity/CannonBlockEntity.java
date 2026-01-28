package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CannonBlockEntity extends BlockEntity {
    public static final int MAX_POWDER = 4;

    private float yawDeg;
    private float pitchDeg;

    private int powder;
    private ItemStack ammo = ItemStack.EMPTY;

    public CannonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CANNON_BLOCK_ENTITY.get(), pos, state);
        // By convention for this cannon: 0 pitch = straight up, 90 pitch = horizon
        // Start near-horizontal so aiming feels natural (mortar-like)
        pitchDeg = 85f;
    }

    public int getPowder() {
        return powder;
    }

    public boolean hasPowder() {
        return powder > 0;
    }

    public boolean isFullyCharged() {
        return powder >= MAX_POWDER;
    }

    public boolean hasAmmo() {
        return !ammo.isEmpty();
    }

    public ItemStack getAmmo() {
        return ammo;
    }

    public void setAmmo(ItemStack stack) {
        if (stack.isEmpty()) {
            ammo = ItemStack.EMPTY;
        } else {
            ammo = stack.copy();
            ammo.setCount(1);
        }
        setChanged();
    }

    public void addPowder(int amount) {
        powder = Mth.clamp(powder + amount, 0, MAX_POWDER);
        setChanged();
    }

    public float getYawDeg() {
        return yawDeg;
    }

    public float getPitchDeg() {
        return pitchDeg;
    }

    public void setAimDegrees(float yawDeg, float pitchDeg) {
        this.yawDeg = Mth.wrapDegrees(yawDeg);
        this.pitchDeg = pitchDeg;
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Powder", powder);
        if (!ammo.isEmpty()) {
            tag.put("Ammo", ammo.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        // These are transient and only present in update packets / update tags, not persisted to disk.
        if (tag.contains("YawDeg")) yawDeg = tag.getFloat("YawDeg");
        if (tag.contains("PitchDeg")) pitchDeg = tag.getFloat("PitchDeg");
        powder = tag.getInt("Powder");
        ammo = tag.contains("Ammo") ? ItemStack.of(tag.getCompound("Ammo")) : ItemStack.EMPTY;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        // Include transient aim for client rendering, but do NOT persist it via saveAdditional()
        tag.putFloat("YawDeg", yawDeg);
        tag.putFloat("PitchDeg", pitchDeg);

        // Include gameplay state
        tag.putInt("Powder", powder);
        if (!ammo.isEmpty()) {
            tag.put("Ammo", ammo.save(new CompoundTag()));
        }
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
}

