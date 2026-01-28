package com.torr.materia.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;

public class CustomSheepFurLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {
    private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final SheepFurModel<Sheep> model;

    public CustomSheepFurLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> renderer, net.minecraft.client.model.geom.EntityModelSet modelSet) {
        super(renderer);
        this.model = new SheepFurModel<>(modelSet.bakeLayer(ModelLayers.SHEEP_FUR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Sheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (sheep.isSheared()) {
            return; // Don't render wool if sheep is sheared
        }

        // Check for custom color capability first
        var customColorOpt = sheep.getCapability(CustomSheepColorCapability.CUSTOM_SHEEP_COLOR);
        if (customColorOpt.isPresent()) {
            var capability = customColorOpt.resolve().get();
            CustomSheepColor customColor = capability.getCustomColor();
            
            if (customColor != null && customColor.isCustomColor()) {
                // Use custom color tinting
                int hexColor = customColor.getColor();
                float red = ((hexColor >> 16) & 0xFF) / 255.0F;
                float green = ((hexColor >> 8) & 0xFF) / 255.0F;
                float blue = (hexColor & 0xFF) / 255.0F;
                
                // Copy pose and animations from parent model
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(sheep, limbSwing, limbSwingAmount, partialTicks);
                this.model.setupAnim(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                
                // Render the wool layer with custom tint
                renderColoredCutoutModel(this.model, SHEEP_FUR_LOCATION, poseStack, buffer, packedLight, sheep, red, green, blue);
                return;
            }
        }

        // Fall back to vanilla behavior - use vanilla sheep color
        float[] colors = sheep.getColor().getTextureDiffuseColors();
        
        // Copy pose and animations from parent model
        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(sheep, limbSwing, limbSwingAmount, partialTicks);
        this.model.setupAnim(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        
        renderColoredCutoutModel(this.model, SHEEP_FUR_LOCATION, poseStack, buffer, packedLight, sheep, colors[0], colors[1], colors[2]);
    }
}
