package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerEntity;

public class CannonTntEntity extends ThrowableItemProjectile {
    private int fuseTicks = 60;
    private boolean exploded;

    public CannonTntEntity(EntityType<? extends CannonTntEntity> type, Level level) {
        super(type, level);
    }

    public CannonTntEntity(Level level) {
        super(ModEntities.CANNON_TNT_PROJECTILE.get(), level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TNT;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && !exploded) {
            fuseTicks--;
            if (fuseTicks <= 0) {
                explode();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide && !exploded) {
            explode();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!level().isClientSide && !exploded) {
            explode();
        }
    }

    private void explode() {
        if (exploded) return;
        exploded = true;
        level().explode(this, getX(), getY(), getZ(), 3.5F, Level.ExplosionInteraction.TNT);
        discard();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }
}

