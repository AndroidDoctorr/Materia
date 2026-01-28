package com.torr.materia.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.materia;
import com.torr.materia.menu.BlastFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlastFurnaceScreen extends AbstractContainerScreen<BlastFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(materia.MOD_ID, "textures/gui/blast_furnace.png");

    public BlastFurnaceScreen(BlastFurnaceMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(poseStack, x, y);
        renderFuelFlame(poseStack, x, y);
    }

    private void renderProgressArrow(PoseStack poseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(poseStack, x + 86, y + 18, 177, 0, menu.getScaledProgress(), 16);
        }
    }

    private void renderFuelFlame(PoseStack poseStack, int x, int y) {
        if(menu.hasFuel()) {
            int k = menu.getScaledFuelProgress();
            blit(poseStack, x + 59, y + 38 + (14 - k), 201, (14 - k), 14, k);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
