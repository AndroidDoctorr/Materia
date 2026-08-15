package com.torr.materia.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/** Side-mounted shield slots (left and right only). */
public enum CartWallSide {
    LEFT(1),
    RIGHT(2);

    public static final int ALL_MASK = LEFT.mask | RIGHT.mask;

    private final int mask;

    CartWallSide(int mask) {
        this.mask = mask;
    }

    public int mask() {
        return this.mask;
    }

    public static boolean hasSide(int mask, CartWallSide side) {
        return (mask & side.mask) != 0;
    }

    public static int withSide(int mask, CartWallSide side, boolean attached) {
        return attached ? mask | side.mask : mask & ~side.mask;
    }

    public static CartWallSide other(CartWallSide side) {
        return side == LEFT ? RIGHT : LEFT;
    }

    /** Hit position in cart-local blocks (matches {@link com.torr.materia.client.renderer.entity.CartRenderer} rotation). */
    public static Vec3 worldToLocal(Vec3 hit, double entityX, double entityY, double entityZ, float entityYaw) {
        double ox = hit.x - entityX;
        double oy = hit.y - entityY;
        double oz = hit.z - entityZ;
        float rad = (entityYaw - 180.0F) * Mth.DEG_TO_RAD;
        float cos = Mth.cos(rad);
        float sin = Mth.sin(rad);
        double localX = ox * cos + oz * sin;
        double localZ = -ox * sin + oz * cos;
        return new Vec3(localX, oy, localZ);
    }

    /** Which side the player is standing on relative to the cart. */
    @Nullable
    public static CartWallSide fromPlayerLocalX(double localX) {
        if (Math.abs(localX) < 0.2D) {
            return null;
        }
        return localX < 0.0D ? LEFT : RIGHT;
    }
}
