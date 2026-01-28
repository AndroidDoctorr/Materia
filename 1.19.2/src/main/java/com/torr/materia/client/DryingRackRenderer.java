package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.DryingRackBlock;
import com.torr.materia.blockentity.DryingRackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Vector3f;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    private final ItemRenderer itemRenderer;

    public DryingRackRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(DryingRackBlockEntity rack, float partialTicks, PoseStack pose, MultiBufferSource buffer,
            int packedLight, int packedOverlay) {
        // Render leather (slot 0)
        ItemStack leatherStack = rack.getRenderStack();
        if (!leatherStack.isEmpty()) {
            pose.pushPose();
            pose.translate(0.5, 0.5, 0.5);
            // Match rotation of the block
            pose.mulPose(Vector3f.YP
                    .rotationDegrees(rack.getBlockState().getValue(DryingRackBlock.FACING).getOpposite().toYRot()));
            pose.scale(0.9f, 0.9f, 0.9f);
            itemRenderer.renderStatic(leatherStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, pose, buffer,
                    0);
            pose.popPose();
        }
        
        ItemStack meatStack = rack.getMeatRenderStack();
        if (!meatStack.isEmpty()) {
            // Get the block's facing direction
            net.minecraft.core.Direction facing = rack.getBlockState().getValue(DryingRackBlock.FACING);
            
            // Calculate meat positions based on block orientation
            double meatX1, meatZ1, meatX2, meatZ2;
            if (facing.getAxis() == net.minecraft.core.Direction.Axis.X) {
                // East/West facing rack - meat hangs along Z axis
                meatX1 = 0.5; meatZ1 = 0.3;
                meatX2 = 0.5; meatZ2 = 0.7;
            } else {
                // North/South facing rack - meat hangs along X axis
                meatX1 = 0.3; meatZ1 = 0.5;
                meatX2 = 0.7; meatZ2 = 0.5;
            }
            
            pose.pushPose();
            pose.translate(meatX1, 0.8, meatZ1);
            pose.mulPose(Vector3f.YP
                    .rotationDegrees(rack.getBlockState().getValue(DryingRackBlock.FACING).getOpposite().toYRot()));
            pose.mulPose(Vector3f.YP.rotationDegrees(30));
            pose.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderStatic(meatStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, pose, buffer,
                    0);
            pose.popPose();
        }

        if (!meatStack.isEmpty() && meatStack.getCount() > 1) {
            // Get the block's facing direction
            net.minecraft.core.Direction facing = rack.getBlockState().getValue(DryingRackBlock.FACING);
            
            // Calculate second meat position based on block orientation
            double meatX2, meatZ2;
            if (facing.getAxis() == net.minecraft.core.Direction.Axis.X) {
                // East/West facing rack - meat hangs along Z axis
                meatX2 = 0.5; meatZ2 = 0.7;
            } else {
                // North/South facing rack - meat hangs along X axis
                meatX2 = 0.7; meatZ2 = 0.5;
            }
            
            pose.pushPose();
            pose.translate(meatX2, 0.8, meatZ2);
            pose.mulPose(Vector3f.YP
                    .rotationDegrees(rack.getBlockState().getValue(DryingRackBlock.FACING).getOpposite().toYRot()));
            pose.mulPose(Vector3f.YP.rotationDegrees(30));
            pose.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderStatic(meatStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, pose, buffer,
                    0);
            pose.popPose();
        }
    }
}
