package com.torr.materia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.blockentity.TableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;
public class TableRenderer implements BlockEntityRenderer<TableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public TableRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(TableBlockEntity table, float partialTicks, PoseStack pose, MultiBufferSource buffer,
            int packedLight, int packedOverlay) {
        ItemStack stack = table.getRenderStack();
        if (stack.isEmpty())
            return;

        pose.pushPose();
        
        // Position item in the center of the table at the top surface
        pose.translate(0.5, 1.0, 0.5); // Center horizontally, place on top surface
        
        pose.mulPose(Axis.XP.rotationDegrees(90)); // Lay flat
        pose.scale(1.0f, 1.0f, 1.0f);
        
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, pose, buffer,
                table.getLevel(), table.getBlockPos().hashCode());
        
        pose.popPose();
    }
}
