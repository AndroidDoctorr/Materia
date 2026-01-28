package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerEntity;

/**
 * Arrow entity that remembers the originating ItemStack so pickup returns the correct arrow item.
 *
 * Vanilla Arrow defaults pickup to minecraft:arrow, so modded ArrowItem types need a custom entity if they
 * want distinct pickup items in 1.18.2.
 */
public class MetalArrowEntity extends Arrow {
    private static final String TAG_ARROW_STACK = "ArrowStack";

    private ItemStack arrowStack = ItemStack.EMPTY;

    public MetalArrowEntity(EntityType<? extends MetalArrowEntity> type, Level level) {
        super(type, level);
    }

    public MetalArrowEntity(Level level, LivingEntity shooter, ItemStack arrowStack) {
        this(ModEntities.METAL_ARROW.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        // The ItemStack passed in is the player's ammo stack, which can be > 1.
        // An arrow entity should always pick up as exactly ONE arrow.
        this.arrowStack = arrowStack.copy();
        this.arrowStack.setCount(1);
    }

    @Override
    protected ItemStack getPickupItem() {
        if (!arrowStack.isEmpty()) {
            ItemStack stack = arrowStack.copy();
            stack.setCount(1);
            return stack;
        }
        return super.getPickupItem();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!arrowStack.isEmpty()) {
            tag.put(TAG_ARROW_STACK, arrowStack.save(this.level().registryAccess(), new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(TAG_ARROW_STACK)) {
            arrowStack = ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound(TAG_ARROW_STACK));
            if (!arrowStack.isEmpty()) {
                arrowStack.setCount(1);
            }
        } else {
            arrowStack = ItemStack.EMPTY;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }
}

