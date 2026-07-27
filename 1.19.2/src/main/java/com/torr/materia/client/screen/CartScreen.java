package com.torr.materia.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.entity.CartEntity;
import com.torr.materia.materia;
import com.torr.materia.menu.CartMenu;
import com.torr.materia.network.CartSleepPacket;
import com.torr.materia.network.NetworkHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CartScreen extends AbstractContainerScreen<CartMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(materia.MOD_ID, "textures/gui/cart.png");

    private static final int SLEEP_BUTTON_X = 176;
    private static final int SLEEP_BUTTON_Y = 78;
    private static final int SLEEP_BUTTON_W = 54;
    private static final int SLEEP_BUTTON_H = 20;

    private Button sleepButton;

    public CartScreen(CartMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 200;
        this.inventoryLabelY = 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.sleepButton = this.addRenderableWidget(Button.builder(
                Component.translatable("gui.materia.cart.sleep"),
                btn -> NetworkHandler.INSTANCE.sendToServer(new CartSleepPacket(this.menu.getCart().getId())))
                .bounds(this.leftPos + SLEEP_BUTTON_X, this.topPos + SLEEP_BUTTON_Y, SLEEP_BUTTON_W, SLEEP_BUTTON_H)
                .build());
        this.updateSleepButton();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.updateSleepButton();
    }

    private void updateSleepButton() {
        if (this.sleepButton == null || this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        this.sleepButton.active = CartEntity.canSleepAt(this.minecraft.player.level());
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }
}
