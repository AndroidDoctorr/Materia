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

    /**
     * West-facing wheel disc on a {@link ModelPart} pivot. Geometry matches 1.20+ baked wheels: 1-block template
     * ({@code templateDiameterPx}) scaled on the pose stack (like {@code ModelPart} x/y/z scale), then a west face.
     *
     * @param cubeAtlasLayout when true, V uses the full cube unwrap ({@code tv + sizeZ …}); when false, V spans only
     *                        the disc ({@code tv … tv + sizeY}) for a dedicated 16×16 wheel texture.
     */
    public static void westDiscOnPart(PoseStack poseStack, VertexConsumer buffer, ModelPart part, int packedLight,
            int packedOverlay, float thicknessPx, float templateDiameterPx, float geometryScale, int texU, int texV,
            int atlasW, int atlasH, boolean cubeAtlasLayout) {
        if (!part.visible) {
            return;
        }
        poseStack.pushPose();
        part.translateAndRotate(poseStack);
        poseStack.scale(geometryScale, geometryScale, geometryScale);
        float half = templateDiameterPx * 0.5F;
        float minX = -thicknessPx * 0.5F;
        westFace(poseStack.last(), buffer, minX, -half, -half, thicknessPx, templateDiameterPx, templateDiameterPx,
                texU, texV, atlasW, atlasH, cubeAtlasLayout, packedLight, packedOverlay);
        poseStack.popPose();
    }

    public static void boxFace(PoseStack.Pose pose, VertexConsumer buffer, Direction face, float minX, float minY,
            float minZ, float sizeX, float sizeY, float sizeZ, int texU, int texV, int atlasW, int atlasH,
            int packedLight, int packedOverlay) {
        boxFace(pose, buffer, face, minX, minY, minZ, sizeX, sizeY, sizeZ, texU, texV, atlasW, atlasH, true,
                packedLight, packedOverlay);
    }

    public static void boxFace(PoseStack.Pose pose, VertexConsumer buffer, Direction face, float minX, float minY,
            float minZ, float sizeX, float sizeY, float sizeZ, int texU, int texV, int atlasW, int atlasH,
            boolean flipTextureV, int packedLight, int packedOverlay) {
        float maxX = minX + sizeX;
        float maxY = minY + sizeY;
        float maxZ = minZ + sizeZ;
        float f4 = texU;
        float f5 = texU + sizeZ;
        float f6 = texU + sizeZ + sizeX;
        float f7 = texU + sizeZ + sizeX + sizeX;
        float f8 = texU + sizeZ + sizeX + sizeZ;
        float f9 = texU + sizeZ + sizeX + sizeZ + sizeX;
        float f10 = texV;
        float f11 = texV + sizeZ;
        float f12 = texV + sizeZ + sizeY;

        float invW = 1.0F / atlasW;
        float invH = 1.0F / atlasH;
        switch (face) {
            case WEST -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, flipTextureV, minX,
                    minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, f4, f12, f5, f11);
            case EAST -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, flipTextureV, maxX,
                    minY, maxZ, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, f6, f12, f8, f11);
            case NORTH -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, flipTextureV, maxX,
                    minY, minZ, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, f5, f12, f6, f11);
            case SOUTH -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, flipTextureV, minX,
                    minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, f8, f12, f9, f11);
            case UP -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, flipTextureV, minX, maxY,
                    maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, f6, f11, f7, f10);
            case DOWN -> emitPolygon(pose, buffer, face, packedLight, packedOverlay, invW, invH, flipTextureV, maxX,
                    minY, maxZ, minX, minY, maxZ, minX, minY, minZ, maxX, minY, minZ, f5, f10, f6, f11);
            default -> {
            }
        }
    }

    private static void westFace(PoseStack.Pose pose, VertexConsumer buffer, float minX, float minY, float minZ,
            float sizeX, float sizeY, float sizeZ, int texU, int texV, int atlasW, int atlasH,
            boolean cubeAtlasLayout, int packedLight, int packedOverlay) {
        float maxY = minY + sizeY;
        float maxZ = minZ + sizeZ;
        float f4 = texU;
        float f5 = texU + sizeZ;
        float f11 = cubeAtlasLayout ? texV + sizeZ : texV;
        float f12 = cubeAtlasLayout ? texV + sizeZ + sizeY : texV + sizeY;
        float invW = 1.0F / atlasW;
        float invH = 1.0F / atlasH;
        emitPolygon(pose, buffer, Direction.WEST, packedLight, packedOverlay, invW, invH, false, minX, minY, minZ,
                minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, f4, f12, f5, f11);
    }

    private static void emitPolygon(PoseStack.Pose pose, VertexConsumer buffer, Direction face, int light, int overlay,
            float invAtlasW, float invAtlasH, boolean flipTextureV, float x1, float y1, float z1, float x2, float y2,
            float z2, float x3, float y3, float z3, float x4, float y4, float z4, float uMin, float vMax, float uMax,
            float vMin) {
        Vector3f normal = face.step();
        normal.transform(pose.normal());
        float nx = normal.x();
        float ny = normal.y();
        float nz = normal.z();
        if (flipTextureV) {
            modelVertex(pose, buffer, x1, y1, z1, uMax * invAtlasW, vMin * invAtlasH, light, overlay, nx, ny, nz);
            modelVertex(pose, buffer, x2, y2, z2, uMin * invAtlasW, vMin * invAtlasH, light, overlay, nx, ny, nz);
            modelVertex(pose, buffer, x3, y3, z3, uMin * invAtlasW, vMax * invAtlasH, light, overlay, nx, ny, nz);
            modelVertex(pose, buffer, x4, y4, z4, uMax * invAtlasW, vMax * invAtlasH, light, overlay, nx, ny, nz);
        } else {
            modelVertex(pose, buffer, x1, y1, z1, uMax * invAtlasW, vMax * invAtlasH, light, overlay, nx, ny, nz);
            modelVertex(pose, buffer, x2, y2, z2, uMin * invAtlasW, vMax * invAtlasH, light, overlay, nx, ny, nz);
            modelVertex(pose, buffer, x3, y3, z3, uMin * invAtlasW, vMin * invAtlasH, light, overlay, nx, ny, nz);
            modelVertex(pose, buffer, x4, y4, z4, uMax * invAtlasW, vMin * invAtlasH, light, overlay, nx, ny, nz);
        }
    }

    private static void modelVertex(PoseStack.Pose pose, VertexConsumer buffer, float px, float py, float pz, float u,
            float v, int light, int overlay, float nx, float ny, float nz) {
        Vector4f pos = new Vector4f(px / 16.0F, py / 16.0F, pz / 16.0F, 1.0F);
        pos.transform(pose.pose());
        buffer.vertex(pos.x(), pos.y(), pos.z(), WHITE, WHITE, WHITE, WHITE, u, v, overlay, light, nx, ny, nz);
    }
}
