package com.torr.materia.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

/** Render layer for mosaic pixels — must not sample the block atlas or every tint looks black. */
public final class MosaicRenderTypes {
    public static final RenderType PIXEL = RenderType.create(
            "materia_mosaic_pixel",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                    .setCullState(new RenderStateShard.CullStateShard(false))
                    .createCompositeState(false)
    );

    private MosaicRenderTypes() {
    }
}
