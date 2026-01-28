package com.torr.materia.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;

public class CustomSheepRenderer extends SheepRenderer {

    public CustomSheepRenderer(EntityRendererProvider.Context context) {
        super(context);
        // Replace only the fur layer, keep everything else vanilla
        this.layers.removeIf(layer -> layer instanceof SheepFurLayer);
        this.addLayer(new CustomSheepFurLayer(this, context.getModelSet()));
    }
}
