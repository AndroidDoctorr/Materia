package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class ShrapnelEntity extends ThrowableItemProjectile {
    public ShrapnelEntity(EntityType<? extends ShrapnelEntity> type, Level level) {
        super(type, level);
    }

    public ShrapnelEntity(Level level) {
        super(ModEntities.SHRAPNEL_PROJECTILE.get(), level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.IRON_NUGGET;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hit = result.getEntity();
        hit.hurt(DamageSource.thrown(this, this.getOwner()), 10.0F);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!level.isClientSide) {
            discard();
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

