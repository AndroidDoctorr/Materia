package com.torr.materia.block;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum PalmLeafFacing implements StringRepresentable {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    private final String name = name().toLowerCase(Locale.ROOT);

    @Override
    public String getSerializedName() {
        return name;
    }

    public static PalmLeafFacing fromOffset(int dx, int dz) {
        if (dx == 0 && dz < 0) {
            return NORTH;
        }
        if (dx > 0 && dz == 0) {
            return EAST;
        }
        if (dx == 0 && dz > 0) {
            return SOUTH;
        }
        if (dx < 0 && dz == 0) {
            return WEST;
        }
        return NORTH;
    }
}
