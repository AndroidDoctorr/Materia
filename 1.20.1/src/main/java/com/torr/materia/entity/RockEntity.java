package com.torr.materia.entity;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class RockEntity extends ThrowableItemProjectile {

    public RockEntity(EntityType<? extends RockEntity> type, Level level) {
        super(type, level);
    }

    public RockEntity(Level level, LivingEntity shooter) {
        super(ModEntities.ROCK_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModBlocks.ROCK.get().asItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 6.0F);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!level().isClientSide) {
            // If we didn't hit an entity, try to place a rock block where it lands
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                net.minecraft.world.phys.BlockHitResult bhr = (net.minecraft.world.phys.BlockHitResult) hitResult;
                net.minecraft.core.BlockPos impactPos = bhr.getBlockPos().relative(bhr.getDirection());
                if (level().getBlockState(impactPos).isAir()) {
                    level().setBlock(impactPos, ModBlocks.ROCK.get().defaultBlockState(), 3);
                }
            }
            discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}


