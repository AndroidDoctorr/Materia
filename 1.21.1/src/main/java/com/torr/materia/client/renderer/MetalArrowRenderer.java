package com.torr.materia.client.renderer;

import com.torr.materia.entity.MetalArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Simple renderer for MetalArrowEntity that uses the vanilla arrow texture.
 */
public class MetalArrowRenderer extends ArrowRenderer<MetalArrowEntity> {
    private static final ResourceLocation VANILLA_ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/projectiles/arrow.png");

    public MetalArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MetalArrowEntity entity) {
        return VANILLA_ARROW_TEXTURE;
    }
}

