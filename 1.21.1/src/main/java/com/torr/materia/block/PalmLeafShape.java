package com.torr.materia.block;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum PalmLeafShape implements StringRepresentable {
    FLAT,
    SLOPED;

    private final String name = name().toLowerCase(Locale.ROOT);

    @Override
    public String getSerializedName() {
        return name;
    }
}
