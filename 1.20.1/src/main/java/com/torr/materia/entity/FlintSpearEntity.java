package com.torr.materia.entity;

// New class implementing a thrown spear that behaves like a trident

import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.damagesource.DamageSource;

/**
 * Entity that represents a thrown {@code flint_spear}. It is heavily based on the
 * vanilla {@link net.minecraft.world.entity.projectile.ThrownTrident} but cut down
 * to the bare minimum we need: flight physics, basic damage and pickup.
 */
public class FlintSpearEntity extends AbstractArrow implements ItemSupplier {

    /**
     * The actual spear item stack that will be rendered while this entity is flying / stuck
     * in the ground.
     */
    private ItemStack spearItem = new ItemStack(ModItems.FLINT_SPEAR.get());

    /* --------------------------------------------------------------------- */
    /* Constructors                                                          */
    /* --------------------------------------------------------------------- */

    public FlintSpearEntity(EntityType<? extends FlintSpearEntity> type, Level level) {
        super(type, level);
    }

    public FlintSpearEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(ModEntities.FLINT_SPEAR_PROJECTILE.get(), owner, level);
        this.spearItem = stack.copy();
    }

    /* --------------------------------------------------------------------- */
    /* Item rendering / pickup                                               */
    /* --------------------------------------------------------------------- */

    @Override
    public ItemStack getItem() {
        return spearItem.copy();
    }

    @Override
    protected ItemStack getPickupItem() {
        return spearItem.copy();
    }

    /* --------------------------------------------------------------------- */
    /* Damage                                                                 */
    /* --------------------------------------------------------------------- */

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
    }

    @Override
    protected void onHitEntity(net.minecraft.world.phys.EntityHitResult result) {
        super.onHitEntity(result);
        
        // Calculate damage based on spear type
        float damage = getSpearDamage();
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), damage);
        dropAndDiscard();
    }
    
    private float getSpearDamage() {
        if (spearItem.is(ModItems.NETHERITE_SPEAR.get())) {
            return 14.0F; // Highest damage
        } else if (spearItem.is(ModItems.DIAMOND_SPEAR.get())) {
            return 13.0F; // Very high damage
        } else if (spearItem.is(ModItems.STEEL_SPEAR.get())) {
            return 12.0F; // High damage
        } else if (spearItem.is(ModItems.IRON_SPEAR.get())) {
            return 10.0F; // High damage
        } else if (spearItem.is(ModItems.BRONZE_SPEAR.get())) {
            return 9.0F; // Medium damage
        } else {
            return 8.0F; // Flint spear base damage
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (hitResult.getType() == HitResult.Type.BLOCK && !this.level().isClientSide) {
            // Spear sticks in the block â€“ just mark some durability damage.
            damageSpearItem();
            // Allow pickup by anyone (similar to arrow)
            this.pickup = Pickup.ALLOWED;
        }
    }

    private void damageSpearItem() {
        if (spearItem.getItem().isDamageable(spearItem)) {
            spearItem.setDamageValue(Math.min(spearItem.getDamageValue() + 1, spearItem.getMaxDamage()));
        }
    }

    private void dropAndDiscard() {
        if (level().isClientSide) {
            discard();
            return;
        }

        ItemStack drop = spearItem.copy();
        // increase damage by 1
        if (drop.getItem().isDamageable(drop)) {
            drop.setDamageValue(Math.min(drop.getDamageValue() + 1, drop.getMaxDamage()));
        }

        if (!drop.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), drop);
            level().addFreshEntity(itemEntity);
        }

        discard();
    }

    /* --------------------------------------------------------------------- */
    /* Networking                                                             */
    /* --------------------------------------------------------------------- */

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Spear", spearItem.save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Spear")) {
            spearItem = ItemStack.of(tag.getCompound("Spear"));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
