package com.torr.materia.mosaic;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * 16×16 pixel grid for the mosaic canvas. Values are {@link MosaicPalette} indices (0–26).
 */
public final class MosaicFaceData {
    public static final int SIZE = 16;
    public static final String CANVAS_KEY = "Canvas";

    private final byte[] pixels = new byte[SIZE * SIZE];

    public byte get(int x, int y) {
        return pixels[index(x, y)];
    }

    public void set(int x, int y, byte color) {
        pixels[index(x, y)] = color;
    }

    public void clear() {
        java.util.Arrays.fill(pixels, (byte) 0);
    }

    public void copyFrom(MosaicFaceData other) {
        System.arraycopy(other.pixels, 0, pixels, 0, pixels.length);
    }

    public byte[] copyPixels() {
        return pixels.clone();
    }

    public void read(CompoundTag tag) {
        if (tag.contains(CANVAS_KEY, Tag.TAG_BYTE_ARRAY)) {
            byte[] data = tag.getByteArray(CANVAS_KEY);
            int len = Math.min(data.length, pixels.length);
            System.arraycopy(data, 0, pixels, 0, len);
            return;
        }
        // Legacy six-face saves: use the first painted face found.
        for (Direction direction : Direction.values()) {
            String legacyKey = "Face" + direction.get3DDataValue();
            if (tag.contains(legacyKey, Tag.TAG_BYTE_ARRAY)) {
                byte[] data = tag.getByteArray(legacyKey);
                int len = Math.min(data.length, pixels.length);
                System.arraycopy(data, 0, pixels, 0, len);
                return;
            }
        }
    }

    public void write(CompoundTag tag) {
        tag.putByteArray(CANVAS_KEY, pixels.clone());
    }

    public static int index(int x, int y) {
        return y * SIZE + x;
    }
}
