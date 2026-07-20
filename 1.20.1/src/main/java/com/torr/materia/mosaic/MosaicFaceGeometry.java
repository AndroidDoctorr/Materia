package com.torr.materia.mosaic;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

/**
 * Shared mapping between block-face UV space (16×16, origin bottom-left) and block-space quads.
 */
public final class MosaicFaceGeometry {
    private MosaicFaceGeometry() {
    }

    public static int[] localToPixel(Direction face, double localX, double localY, double localZ) {
        double u;
        double v;
        switch (face) {
            case DOWN -> {
                u = localX;
                v = localZ;
            }
            case UP -> {
                u = localX;
                v = 1.0D - localZ;
            }
            case NORTH -> {
                u = localX;
                v = localY;
            }
            case SOUTH -> {
                u = 1.0D - localX;
                v = localY;
            }
            case WEST -> {
                u = localZ;
                v = localY;
            }
            case EAST -> {
                u = 1.0D - localZ;
                v = localY;
            }
            default -> throw new IllegalStateException("Unexpected face: " + face);
        }
        int px = toPixel(u);
        int py = toPixel(v);
        return new int[]{px, py};
    }

    /**
     * Returns four block-space corners for a pixel quad in CCW order when viewed from outside the face.
     * Each corner is {x, y, z}.
     */
    public static float[][] pixelCorners(Direction face, int px, int py) {
        float u0 = px / 16F;
        float u1 = (px + 1) / 16F;
        float v0 = py / 16F;
        float v1 = (py + 1) / 16F;
        float[][] corners = switch (face) {
            case DOWN -> quad(
                    u0, 0F, v0,
                    u1, 0F, v0,
                    u1, 0F, v1,
                    u0, 0F, v1);
            case UP -> reverse(quad(
                    u0, 1F, 1F - v1,
                    u1, 1F, 1F - v1,
                    u1, 1F, 1F - v0,
                    u0, 1F, 1F - v0));
            case NORTH -> reverse(quad(
                    u0, v0, 0F,
                    u1, v0, 0F,
                    u1, v1, 0F,
                    u0, v1, 0F));
            case SOUTH -> quad(
                    1F - u1, v0, 1F,
                    1F - u0, v0, 1F,
                    1F - u0, v1, 1F,
                    1F - u1, v1, 1F);
            case WEST -> quad(
                    0F, v0, u0,
                    0F, v0, u1,
                    0F, v1, u1,
                    0F, v1, u0);
            case EAST -> reverse(quad(
                    1F, v0, 1F - u1,
                    1F, v0, 1F - u0,
                    1F, v1, 1F - u0,
                    1F, v1, 1F - u1));
        };
        float ox = face.getStepX() * 0.01F;
        float oy = face.getStepY() * 0.01F;
        float oz = face.getStepZ() * 0.01F;
        for (float[] corner : corners) {
            corner[0] += ox;
            corner[1] += oy;
            corner[2] += oz;
        }
        return corners;
    }

    private static float[][] reverse(float[][] corners) {
        return new float[][]{
                corners[3],
                corners[2],
                corners[1],
                corners[0]
        };
    }

    private static float[][] quad(float x0, float y0, float z0,
                                  float x1, float y1, float z1,
                                  float x2, float y2, float z2,
                                  float x3, float y3, float z3) {
        return new float[][]{
                {x0, y0, z0},
                {x1, y1, z1},
                {x2, y2, z2},
                {x3, y3, z3}
        };
    }

    private static int toPixel(double unit) {
        unit = Mth.clamp(unit, 0.0D, 0.999999D);
        return Mth.clamp((int) (unit * MosaicFaceData.SIZE), 0, MosaicFaceData.SIZE - 1);
    }
}
