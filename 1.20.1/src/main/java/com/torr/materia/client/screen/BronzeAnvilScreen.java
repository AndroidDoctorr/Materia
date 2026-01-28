package com.torr.materia.client.screen;

import com.torr.materia.materia;
import com.torr.materia.menu.BronzeAnvilMenu;
import com.torr.materia.recipe.BronzeAnvilRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class BronzeAnvilScreen extends AbstractContainerScreen<BronzeAnvilMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(materia.MOD_ID, "textures/gui/bronze_anvil.png");
    private float scrollOffs;
    private int startIndex;
    private boolean scrolling;

    public BronzeAnvilScreen(BronzeAnvilMenu menu, Inventory inventory, Component component) {
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

        // Render recipe outputs (similar to stone anvil but for bronze anvil recipes)
        java.util.List<BronzeAnvilRecipe> recipes = this.menu.getAvailableRecipes();
        int visible = 6;
        int total = recipes.size();
        if (total > 0) {
            int baseX = x + 110;
            int baseY = y + 18;
            int idx = 0;
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    int absolute = startIndex + idx;
                    if (absolute >= total) break;
                    ItemStack icon = recipes.get(absolute).getResultItem();
                    int rx = baseX + col * 18;
                    int ry = baseY + row * 18;
                    guiGraphics.renderItem(icon, rx, ry);
                    guiGraphics.renderItemDecorations(this.font, icon, rx, ry);
                    // Hover highlight
                    if (mouseX >= rx && mouseX < rx + 16 && mouseY >= ry && mouseY < ry + 16) {
                        guiGraphics.fill(rx, ry, rx + 16, ry + 16, 0x80FFFFFF);
                    }
                    idx++;
                }
            }

            // Scrollbar
            if (total > visible) {
                int barX = x + 158;
                int barY = y + 18;
                int barH = 36; // spans two rows (2*18)
                int handleH = 12;
                int max = total - visible;
                int handleY = barY + (int)((barH - handleH) * scrollOffs);
                guiGraphics.fill(barX, barY, barX + 2, barY + barH, 0xFF666666);
                guiGraphics.fill(barX, handleY, barX + 2, handleY + handleH, 0xFFCCCCCC);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            int baseX = x + 110;
            int baseY = y + 18;

            java.util.List<BronzeAnvilRecipe> recipes = this.menu.getAvailableRecipes();
            int idx = 0;
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    int absolute = startIndex + idx;
                    if (absolute >= recipes.size()) break;
                    int rx = baseX + col * 18;
                    int ry = baseY + row * 18;
                    if (mouseX >= rx && mouseX < rx + 16 && mouseY >= ry && mouseY < ry + 16) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, absolute);
                        return true;
                    }
                    idx++;
                }
            }
            // Scrollbar drag start
            if (recipes.size() > 6) {
                int barX = x + 158;
                int barY = y + 18;
                int barH = 36;
                if (mouseX >= barX && mouseX < barX + 4 && mouseY >= barY && mouseY < barY + barH) {
                    scrolling = true;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) scrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (scrolling) {
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            int barY = y + 18;
            int barH = 36;
            float off = (float)((mouseY - barY - 6) / (double)(barH - 12));
            scrollOffs = net.minecraft.util.Mth.clamp(off, 0.0F, 1.0F);
            updateStartIndex();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        java.util.List<BronzeAnvilRecipe> recipes = this.menu.getAvailableRecipes();
        if (recipes.size() > 6) {
            int max = recipes.size() - 6;
            startIndex = net.minecraft.util.Mth.clamp(startIndex - (int)Math.signum(delta), 0, max);
            scrollOffs = (float)startIndex / (float)max;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void updateStartIndex() {
        java.util.List<BronzeAnvilRecipe> recipes = this.menu.getAvailableRecipes();
        int max = Math.max(0, recipes.size() - 6);
        startIndex = (int)(scrollOffs * max + 0.5F);
    }
}
