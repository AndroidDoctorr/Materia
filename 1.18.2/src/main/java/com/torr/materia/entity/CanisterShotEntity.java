package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.Random;

public class CanisterShotEntity extends ThrowableItemProjectile {
    private int fuseTicks = 40;
    private boolean exploded;

    public CanisterShotEntity(EntityType<? extends CanisterShotEntity> type, Level level) {
        super(type, level);
    }

    public CanisterShotEntity(Level level) {
        super(ModEntities.CANISTER_SHOT_PROJECTILE.get(), level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CANISTER_SHOT.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide && !exploded) {
            fuseTicks--;
            if (fuseTicks <= 0) {
                explode();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level.isClientSide && !exploded) {
            explode();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!level.isClientSide && !exploded) {
            explode();
        }
    }

    private void explode() {
        if (exploded) return;
        exploded = true;

        // Small explosion effect, but no block damage
        level.explode(this, getX(), getY(), getZ(), 1.8F, Explosion.BlockInteraction.NONE);

        spawnShrapnel();
        discard();
    }

    private void spawnShrapnel() {
        Random r = random;
        int count = 28;
        for (int i = 0; i < count; i++) {
            ShrapnelEntity s = new ShrapnelEntity(ModEntities.SHRAPNEL_PROJECTILE.get(), level);
            s.setOwner(getOwner());
            s.setPos(getX(), getY() + 0.1, getZ());

            // Random direction on sphere, biased slightly upward
            double yaw = r.nextDouble() * Math.PI * 2.0;
            double u = r.nextDouble() * 2.0 - 1.0;
            double v = Math.sqrt(Math.max(0.0, 1.0 - u * u));
            Vec3 dir = new Vec3(Math.cos(yaw) * v, Math.abs(u) * 0.9 + 0.1, Math.sin(yaw) * v).normalize();

            s.shoot(dir.x, dir.y, dir.z, 1.2F, 6.0F);
            level.addFreshEntity(s);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

