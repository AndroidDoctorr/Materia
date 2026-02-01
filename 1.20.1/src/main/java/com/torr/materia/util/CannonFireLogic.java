package com.torr.materia.util;

import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import com.torr.materia.block.CannonBlock;
import com.torr.materia.blockentity.CannonBlockEntity;
import com.torr.materia.entity.CannonTntEntity;
import com.torr.materia.entity.CanisterShotEntity;
import com.torr.materia.entity.CannonballEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Shared cannon firing logic (used by packets / automation).
 *
 * Ported from 1.18.2 to 1.19.2.
 */
public class CannonFireLogic {
    private CannonFireLogic() {
    }

    public static boolean fire(Level level, BlockPos pos, BlockState state, CannonBlockEntity cannonBe, @Nullable LivingEntity owner, float yaw, float pitch) {
        if (level.isClientSide) return false;
        if (!state.hasProperty(CannonBlock.FACING)) return false;
        if (!cannonBe.hasPowder() || !cannonBe.hasAmmo()) return false;

        int powder = cannonBe.getPowder();
        ItemStack ammo = cannonBe.getAmmo().copy();
        if (ammo.isEmpty()) return false;

        Direction facing = state.getValue(CannonBlock.FACING);
        float baseModelY = CannonMath.facingToModelY(facing);
        float yawWorldDeg = baseModelY + yaw;

        Vec3 dir = directionFromAngles(yawWorldDeg, pitch);

        // Spawn outside the block collision to avoid self-hit
        Vec3 muzzle = new Vec3(pos.getX() + 0.5, pos.getY() + 1.05, pos.getZ() + 0.5).add(dir.scale(0.65));

        // Tuned so partial charges are still useful, but full charge reaches farther.
        float p = Mth.clamp(powder, 0, CannonBlockEntity.MAX_POWDER);
        float speed = 0.52f + 0.17f * p + 0.02f * p * p;
        float inaccuracy = getInaccuracyForAmmo(ammo);

        if (ammo.is(Items.TNT)) {
            CannonTntEntity tnt = new CannonTntEntity(ModEntities.CANNON_TNT_PROJECTILE.get(), level);
            tnt.setPos(muzzle.x, muzzle.y, muzzle.z);
            tnt.setItem(ammo);
            if (owner != null) tnt.setOwner(owner);
            tnt.shoot(dir.x, dir.y, dir.z, speed, inaccuracy);
            level.addFreshEntity(tnt);
        } else if (ammo.is(ModItems.CANISTER_SHOT.get())) {
            CanisterShotEntity can = new CanisterShotEntity(ModEntities.CANISTER_SHOT_PROJECTILE.get(), level);
            can.setPos(muzzle.x, muzzle.y, muzzle.z);
            can.setItem(ammo);
            if (owner != null) can.setOwner(owner);
            can.shoot(dir.x, dir.y, dir.z, speed, inaccuracy);
            level.addFreshEntity(can);
        } else {
            CannonballEntity projectile = new CannonballEntity(ModEntities.CANNONBALL_PROJECTILE.get(), level);
            projectile.setPos(muzzle.x, muzzle.y, muzzle.z);
            projectile.setItem(ammo);
            if (owner != null) projectile.setOwner(owner);
            projectile.shoot(dir.x, dir.y, dir.z, speed, inaccuracy);
            level.addFreshEntity(projectile);
        }

        // Consume payload
        cannonBe.setAmmo(ItemStack.EMPTY);
        cannonBe.addPowder(-powder);

        // Preserve last-fired angles for redstone automation / display
        cannonBe.setAimDegrees(yaw, pitch);
        level.sendBlockUpdated(pos, state, state, 3);

        level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
        return true;
    }

    // Cannon convention:
    // - pitchDeg: 0 = straight up (+Y), 90 = horizon
    // - yawWorldDeg: 0 = north (-Z), 90 = east (+X), 180 = south (+Z), 270 = west (-X)
    private static Vec3 directionFromAngles(float yawWorldDeg, float pitchDeg) {
        double theta = pitchDeg * Mth.DEG_TO_RAD;
        double phi = yawWorldDeg * Mth.DEG_TO_RAD;

        double sinT = Math.sin(theta);
        double x = sinT * Math.sin(phi);
        double y = Math.cos(theta);
        double z = -sinT * Math.cos(phi);

        return new Vec3(x, y, z).normalize();
    }

    private static float getInaccuracyForAmmo(ItemStack ammo) {
        // These values are intentionally small: just enough to make shots feel a bit less laser-precise.
        if (ammo.is(ModItems.IRON_CANNONBALL.get())) return 0.85f;
        if (ammo.is(ModItems.STONE_CANNONBALL.get())) return 1.0f;
        if (ammo.is(ModItems.CANISTER_SHOT.get())) return 1.15f;
        if (ammo.is(Items.TNT)) return 1.20f;
        return 1.0f;
    }
}

