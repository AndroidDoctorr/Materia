package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class DynamiteEntity extends ThrowableItemProjectile {
    private int fuseTimer = 40; // 2 seconds at 20 ticks per second
    private boolean hasExploded = false;

    public DynamiteEntity(EntityType<? extends DynamiteEntity> type, Level level) {
        super(type, level);
    }

    public DynamiteEntity(Level level, LivingEntity shooter) {
        super(ModEntities.DYNAMITE_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DYNAMITE.get();
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!level.isClientSide && !hasExploded) {
            fuseTimer--;
            
            // Create smoke particles every few ticks
            if (fuseTimer % 4 == 0) {
                level.addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE,
                    getX(), getY() + 0.1, getZ(),
                    0.0, 0.05, 0.0);
            }
            
            // Explode when fuse runs out
            if (fuseTimer <= 0) {
                explode();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        
        // Deal some impact damage
        if (hitEntity instanceof LivingEntity) {
            hitEntity.hurt(DamageSource.thrown(this, this.getOwner()), 5.0F);
        }
        
        // Explode on impact with entities
        if (!level.isClientSide && !hasExploded) {
            explode();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        
        // Stop the projectile but don't explode immediately on block hit
        // Let the fuse continue burning
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            setDeltaMovement(getDeltaMovement().multiply(0.1, 0.1, 0.1));
        }
    }

    private void explode() {
        if (!level.isClientSide && !hasExploded) {
            hasExploded = true;
            
            level.explode(this, getX(), getY(), getZ(), 1.0F, Explosion.BlockInteraction.BREAK);
            
            discard();
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    public int getFuseTimer() {
        return fuseTimer;
    }
    
    public void setFuseTimer(int timer) {
        this.fuseTimer = timer;
    }
}
