package com.torr.materia.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;

/**
 * Single-face quads for 1.18.x (no {@code CubeListBuilder} face mask). Matches {@link ModelPart.Cube#compile}
 * for one polygon: model-pixel positions (÷16), UVs remapped like {@link ModelPart.Polygon} (pixel ÷ atlas size),
 * and {@link Direction#step()} normals.
 */
public final class EntityPlaneRenderer {

    private static final float WHITE = 1.0F;

    private EntityPlaneRenderer() {
    }

    public static void westDiscOnPart(PoseStack poseStack, VertexConsumer buffer, ModelPart part, int packedLight,
            int packedOverlay, float thicknessPx, float radiusPx, int texU, int texV, int atlasW, int atlasH) {
        if (!part.visible) {
            return;
        }
        poseStack.pushPose();
        part.translateAndRotate(poseStack);
        float minX = -thicknessPx * 0.5F;
        float minY = -radiusPx;
        float minZ = -radiusPx;
        boxFace(poseStack.last(), buffer, Direction.WEST, minX, minY, minZ, thicknessPx, radiusPx * 2.0F,
                radiusPx * 2.0F, texU, texV, atlasW, atlasH, packedLight, packedOverlay);
        poseStack.popPose();
    }

    public static void boxFace(PoseStack.Pose pose, VertexConsumer buffer, Direction face, float minX, float minY,
            float minZ, float sizeX, float sizeY, float sizeZ, int texU, int texV, int atlasW, int atlasH,
            int packedLight, int packedOverlay) {
        float maxX = minX + sizeX;
        float maxY = minY + sizeY;
        float maxZ = minZ + sizeZ;
        int tu = texU;
        int tv = texV;
        int d = Math.round(sizeZ);
        int w = Math.round(sizeX);
        int h = Math.round(sizeY);
        int f4 = tu;
        int f5 = tu + d;
        int f6 = tu + d + w;
        int f7 = tu + d + w + w;
        int f8 = tu + d + w + d;
        int f9 = tu + d + w + d + w;
        int f10 = tv;
        int f11 = tv + d;
        int f12 = tv + d + h;

        float invW = 1.0F / atlasW;
        float invH = 1.0F / atlasH;
        switch (face) {
            case WEST -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, minX, minY, minZ, minX,
                    minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, f4, f12, f5, f11);
            case EAST -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, maxX, minY, maxZ, maxX,
                    minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, f6, f12, f8, f11);
            case NORTH -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, maxX, minY, minZ,
                    minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, f5, f12, f6, f11);
            case SOUTH -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, minX, minY, maxZ,
                    maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, f8, f12, f9, f11);
            case UP -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, minX, maxY, maxZ, maxX,
                    maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, f6, f11, f7, f10);
            case DOWN -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, maxX, minY, maxZ,
                    minX, minY, maxZ, minX, minY, minZ, maxX, minY, minZ, f5, f10, f6, f11);
            default -> {
            }
        }
    }

    private static void emitPolygon(PoseStack.Pose pose, VertexConsumer buffer, Direction face, int light, int overlay,
            float invAtlasW, float invAtlasH, float x1, float y1, float z1, float x2, float y2, float z2, float x3,
            float y3, float z3, float x4, float y4, float z4, float uMin, float vMax, float uMax, float vMin) {
        Vector3f normal = face.step();
        normal.transform(pose.normal());
        float nx = normal.x();
        float ny = normal.y();
        float nz = normal.z();
        modelVertex(pose, buffer, x1, y1, z1, uMax * invAtlasW, vMax * invAtlasH, light, overlay, nx, ny, nz);
        modelVertex(pose, buffer, x2, y2, z2, uMin * invAtlasW, vMax * invAtlasH, light, overlay, nx, ny, nz);
        modelVertex(pose, buffer, x3, y3, z3, uMin * invAtlasW, vMin * invAtlasH, light, overlay, nx, ny, nz);
        modelVertex(pose, buffer, x4, y4, z4, uMax * invAtlasW, vMin * invAtlasH, light, overlay, nx, ny, nz);
    }

    private static void modelVertex(PoseStack.Pose pose, VertexConsumer buffer, float px, float py, float pz, float u,
            float v, int light, int overlay, float nx, float ny, float nz) {
        Vector4f pos = new Vector4f(px / 16.0F, py / 16.0F, pz / 16.0F, 1.0F);
        pos.transform(pose.pose());
        buffer.vertex(pos.x(), pos.y(), pos.z(), WHITE, WHITE, WHITE, WHITE, u, v, overlay, light, nx, ny, nz);
    }
}
