package com.torr.materia.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.torr.materia.materia;
import com.torr.materia.menu.OvenMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OvenScreen extends AbstractContainerScreen<OvenMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "textures/gui/oven.png");

    public OvenScreen(OvenMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(graphics, x, y);
        renderFuelFlame(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics graphics, int x, int y) {
        if(menu.isCrafting()) {
            graphics.blit(TEXTURE, x + 77, y + 18, 177, 0, menu.getScaledProgress(), 16);
        }
    }

    private void renderFuelFlame(GuiGraphics graphics, int x, int y) {
        if(menu.hasFuel()) {
            int k = menu.getScaledFuelProgress();
            graphics.blit(TEXTURE, x + 59, y + 38 + (14 - k), 201, (14 - k), 14, k);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
