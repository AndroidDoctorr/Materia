package com.torr.materia.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.torr.materia.materia;
import com.torr.materia.block.CannonBlock;
import com.torr.materia.blockentity.CannonBlockEntity;
import com.torr.materia.network.CannonFirePacket;
import com.torr.materia.network.CannonUpdateAimPacket;
import com.torr.materia.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class CannonAimScreen extends Screen {
    private static final ResourceLocation OVERLAY =
            new ResourceLocation(materia.MOD_ID, "textures/gui/cannon.png");

    private final BlockPos pos;

    private double lastMouseX;
    private double lastMouseY;

    private float yaw;
    private float pitch;

    private float originalPlayerYaw;
    private float originalPlayerPitch;

    public CannonAimScreen(BlockPos pos) {
        super(new TextComponent(""));
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();
        lastMouseX = minecraft.mouseHandler.xpos();
        lastMouseY = minecraft.mouseHandler.ypos();

        if (minecraft.player != null) {
            originalPlayerYaw = minecraft.player.getYRot();
            originalPlayerPitch = minecraft.player.getXRot();
        }

        var be = getCannon();
        if (be != null) {
            yaw = be.getYawDeg();
            pitch = be.getPitchDeg();
        }

        com.torr.materia.client.CannonAimClientCamera.begin(pos);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        // Don't darken the world; just draw overlay
        super.render(poseStack, mouseX, mouseY, partialTick);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, OVERLAY);
        RenderSystem.enableBlend();

        // Fullscreen overlay
        blit(poseStack, 0, 0, 0, 0, width, height, width, height);

        RenderSystem.disableBlend();
    }

    @Override
    public void tick() {
        super.tick();

        var be = getCannon();
        if (be == null) {
            minecraft.setScreen(null);
            return;
        }

        // Keep camera aligned with the cannon aim direction (camera entity is at the cannon)
        com.torr.materia.client.CannonAimClientCamera.updateAim(yaw, pitch);

        // Arrow key aiming (mouse aiming handled in mouseMoved)
        float step = 2f;
        boolean changed = false;
        if (isDown(GLFW.GLFW_KEY_LEFT)) {
            yaw -= step;
            changed = true;
        }
        if (isDown(GLFW.GLFW_KEY_RIGHT)) {
            yaw += step;
            changed = true;
        }
        if (isDown(GLFW.GLFW_KEY_UP)) {
            pitch += step;
            changed = true;
        }
        if (isDown(GLFW.GLFW_KEY_DOWN)) {
            pitch -= step;
            changed = true;
        }

        if (changed) {
            clampAngles();
            sendAimUpdate();
        }
    }

    private boolean isDown(int key) {
        if (minecraft.getWindow() == null) return false;
        long handle = minecraft.getWindow().getWindow();
        return GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        double dx = mouseX - lastMouseX;
        double dy = mouseY - lastMouseY;
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        float sensitivity = 0.25f;
        yaw += (float) dx * sensitivity;
        pitch += (float) dy * sensitivity;
        clampAngles();
        sendAimUpdate();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Left click fires and exits aim mode
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            NetworkHandler.INSTANCE.sendToServer(new CannonFirePacket(pos, yaw, pitch));
            cleanupAfterAim();
            minecraft.setScreen(null);
            return true;
        }

        // Right click exits aim mode
        if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
            cleanupAfterAim();
            minecraft.setScreen(null);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            cleanupAfterAim();
            minecraft.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        cleanupAfterAim();
        super.onClose();
    }

    @Override
    public void removed() {
        // This is called whenever the screen is closed via setScreen(null), ESC, etc.
        cleanupAfterAim();
        super.removed();
    }

    private void cleanupAfterAim() {
        com.torr.materia.client.CannonAimClientCamera.end();

        // Restore player rot (helps avoid odd post-exit orientation)
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.setYRot(originalPlayerYaw);
            minecraft.player.yRotO = originalPlayerYaw;
            minecraft.player.setXRot(originalPlayerPitch);
            minecraft.player.xRotO = originalPlayerPitch;
        }

        // Ensure the game re-grabs the mouse so movement works again
        if (minecraft != null) {
            minecraft.mouseHandler.grabMouse();
        }
    }

    private void clampAngles() {
        yaw = Mth.wrapDegrees(yaw);
        // 0 = straight up, 90 = horizon
        pitch = Mth.clamp(pitch, 20f, 90f);
    }

    private void sendAimUpdate() {
        NetworkHandler.INSTANCE.sendToServer(new CannonUpdateAimPacket(pos, yaw, pitch));
    }

    private CannonBlockEntity getCannon() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        var be = mc.level.getBlockEntity(pos);
        if (be instanceof CannonBlockEntity cannonBe) return cannonBe;
        return null;
    }

    // Camera positioning is now handled by a client render event (CannonAimClientCamera)
}

