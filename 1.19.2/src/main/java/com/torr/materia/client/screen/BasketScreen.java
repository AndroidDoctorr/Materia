package com.torr.materia.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.materia;
import com.torr.materia.menu.BasketMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BasketScreen extends AbstractContainerScreen<BasketMenu> {
    private static final ResourceLocation TEXTURE = 
        new ResourceLocation(materia.MOD_ID, "textures/gui/basket.png");

    public BasketScreen(BasketMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        // Adjust GUI size for 2x2 slots (smaller than amphora)
        this.imageHeight = 166; // Standard chest height
        this.inventoryLabelY = this.imageHeight - 94; // Adjust inventory label position
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Draw the main GUI texture
        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
