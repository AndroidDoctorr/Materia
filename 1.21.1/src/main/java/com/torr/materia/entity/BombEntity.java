package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BombEntity extends ThrowableItemProjectile {
    private static final float EXPLOSION_POWER = 2.0F; // TNT is 4.0F

    private int fuseTimer = 40; // 2 seconds at 20 ticks per second
    private boolean hasExploded = false;

    public BombEntity(EntityType<? extends BombEntity> type, Level level) {
        super(type, level);
    }

    public BombEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BOMB_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BOMB.get();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide && !hasExploded) {
            fuseTimer--;

            if (fuseTimer % 4 == 0) {
                level().addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE,
                    getX(), getY() + 0.1, getZ(),
                    0.0, 0.05, 0.0);
            }

            if (fuseTimer <= 0) {
                explode();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();

        if (hitEntity instanceof LivingEntity) {
            hitEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 6.0F);
        }

        if (!level().isClientSide && !hasExploded) {
            explode();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            setDeltaMovement(getDeltaMovement().multiply(0.1, 0.1, 0.1));
        }
    }

    private void explode() {
        if (!level().isClientSide && !hasExploded) {
            hasExploded = true;
            level().explode(this, getX(), getY(), getZ(), EXPLOSION_POWER, Level.ExplosionInteraction.TNT);
            discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }

    public int getFuseTimer() {
        return fuseTimer;
    }

    public void setFuseTimer(int timer) {
        this.fuseTimer = timer;
    }
}

