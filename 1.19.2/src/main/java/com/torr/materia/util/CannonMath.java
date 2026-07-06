package com.torr.materia.util;

import net.minecraft.core.Direction;

public class CannonMath {
    private CannonMath() {
    }

    // Blockstate model convention: north=0, east=90, south=180, west=270
    public static float facingToModelY(Direction facing) {
        return switch (facing) {
            case NORTH -> 0f;
            case EAST -> 90f;
            case SOUTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };
    }

    /**
     * Barrel BER horizontal rotation (render only — firing uses {@code facingToModelY + aimYaw}).
     * Aim yaw is negated on N/S facings and positive on E/W so mouse/arrow adjustments turn the
     * model the same way relative to the shot line on every facing.
     *
     * @param modelForwardOffsetDeg constant mesh correction (+180 on 1.18/1.19, 0 on 1.20+)
     */
    public static float barrelRenderYawDeg(Direction facing, float aimYawDeg, float modelForwardOffsetDeg) {
        float aimSign = facing.getAxis() == Direction.Axis.Z ? -1f : 1f;
        return facingToModelY(facing) + aimSign * aimYawDeg + modelForwardOffsetDeg;
    }
}

