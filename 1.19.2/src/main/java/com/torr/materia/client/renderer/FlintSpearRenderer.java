package com.torr.materia.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.entity.FlintSpearEntity;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.block.model.ItemTransforms;

/**
 * Client-side renderer for {@link FlintSpearEntity}. Merely reuses the built-in
 * {@link ThrownTridentRenderer} so that the spear is rendered in full 3-D and
 * spins correctly while in flight.
 */
public class FlintSpearRenderer extends EntityRenderer<FlintSpearEntity> {

    public FlintSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FlintSpearEntity spear, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Rotate spear to match flight direction
        float yaw = Mth.lerp(partialTicks, spear.yRotO, spear.getYRot()) - 90.0F;
        float pitch = Mth.lerp(partialTicks, spear.xRotO, spear.getXRot()) + 90.0F;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(yaw));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(pitch));

        // Render the item model in 3-D
        net.minecraft.world.item.ItemStack renderStack = spear.getItem().copy();
        renderStack.getOrCreateTag().putInt("CustomModelData", 1);

        Minecraft.getInstance().getItemRenderer().renderStatic(renderStack, ItemTransforms.TransformType.GROUND, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, bufferSource, spear.getId());

        poseStack.popPose();
        super.render(spear, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(FlintSpearEntity entity) {
        // The item renderer handles textures, so return null
        return null;
    }
}
