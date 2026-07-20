package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.torr.materia.block.MosaicBlock;
import com.torr.materia.blockentity.MosaicBlockEntity;
import com.torr.materia.mosaic.MosaicFaceData;
import com.torr.materia.mosaic.MosaicFaceGeometry;
import com.torr.materia.mosaic.MosaicPalette;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

public class MosaicRenderer implements BlockEntityRenderer<MosaicBlockEntity> {

    public MosaicRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MosaicBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        Direction paintFace = blockEntity.getBlockState().getValue(MosaicBlock.FACING);
        VertexConsumer consumer = bufferSource.getBuffer(MosaicRenderTypes.PIXEL);
        Matrix4f matrix = poseStack.last().pose();
        MosaicFaceData canvas = blockEntity.canvas();
        for (int y = 0; y < MosaicFaceData.SIZE; y++) {
            for (int x = 0; x < MosaicFaceData.SIZE; x++) {
                int colorIndex = canvas.get(x, y) & 0xFF;
                if (colorIndex == MosaicPalette.UNPAINTED) {
                    continue;
                }
                drawPixel(matrix, consumer, paintFace, x, y, MosaicPalette.colorArgb(colorIndex));
            }
        }
    }

    private static void drawPixel(Matrix4f matrix, VertexConsumer consumer, Direction direction,
                                  int px, int py, int argb) {
        float r = ((argb >> 16) & 0xFF) / 255F;
        float g = ((argb >> 8) & 0xFF) / 255F;
        float b = (argb & 0xFF) / 255F;
        float a = ((argb >> 24) & 0xFF) / 255F;
        float[][] corners = MosaicFaceGeometry.pixelCorners(direction, px, py);
        for (float[] corner : corners) {
            consumer.vertex(matrix, corner[0], corner[1], corner[2])
                    .color(r, g, b, a)
                    .endVertex();
        }
    }
}
