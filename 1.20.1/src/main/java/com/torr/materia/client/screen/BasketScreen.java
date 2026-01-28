package com.torr.materia.client.screen;

import com.torr.materia.materia;
import com.torr.materia.menu.BasketMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Draw the main GUI texture
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
