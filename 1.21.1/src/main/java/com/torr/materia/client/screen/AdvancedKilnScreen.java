package com.torr.materia.client.screen;

import com.torr.materia.materia;
import com.torr.materia.menu.AdvancedKilnMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedKilnScreen extends AbstractContainerScreen<AdvancedKilnMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "textures/gui/kiln_tier2.png");

    public AdvancedKilnScreen(AdvancedKilnMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
        renderFuelFlame(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 86, y + 18, 177, 0, menu.getScaledProgress(), 16);
        }
    }

    private void renderFuelFlame(GuiGraphics guiGraphics, int x, int y) {
        if(menu.hasFuel()) {
            int k = menu.getScaledFuelProgress();
            guiGraphics.blit(TEXTURE, x + 59, y + 38 + (14 - k), 201, (14 - k), 14, k);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
} 