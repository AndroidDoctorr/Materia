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
}

