package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.blockentity.WaterPotBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;

public class WaterPotRenderer implements BlockEntityRenderer<WaterPotBlockEntity> {

    private final ItemRenderer itemRenderer;

    public WaterPotRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(WaterPotBlockEntity pot, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack stack = pot.getRenderStack();
        if (stack.isEmpty())
            return;

        poseStack.pushPose();
        // Translate to inside the pot
        poseStack.translate(0.5D, 0.32D, 0.5D); // y slightly above water surface
        // Reduce size to fit
        poseStack.scale(0.5F, 0.5F, 0.5F);
        // Optional slow rotation for effect
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED,
                packedLight, packedOverlay, poseStack, buffer, pot.getLevel(), pot.getBlockPos().hashCode());

        poseStack.popPose();
    }
}