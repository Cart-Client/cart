package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.gui.components.settings.RenderableSetting;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.modules.client.Theme;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public final class Window {
    public List<ModuleButton> moduleButtons = new ArrayList<>();
    protected MinecraftClient mc = Client.mc;
    private int x, y;
    private final int width, height;
    private final Category category;
    public boolean dragging, extended;
    int dragX, dragY;
    private static final int BORDER_RADIUS = 10;
    private static final int CORNER_RADIUS = 10;
    Theme themeModule = Client.getInstance().getModuleManager().getModule(Theme.class);

    public Window(int x, int y, int width, int height, Category category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.dragging = false;
        this.extended = true;
        this.height = height;
        this.category = category;

        int offset = height;
        for (Module module : Client.INSTANCE.getModuleManager().getModulesInCategory(category)) {
            moduleButtons.add(new ModuleButton(this, module, offset));
            offset += height;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int totalHeight = height + height/2;
        if (extended) {
            for (ModuleButton moduleButton : moduleButtons) {
                totalHeight += moduleButton.parent.getHeight();
                if (moduleButton.extended) {
                    for (RenderableSetting renderableSetting : moduleButton.settings) {
                        totalHeight += height;
                    }
                }
            }
        }

        drawRoundedRect(context, x - 2, y - 2, width + 4, totalHeight + 4, BORDER_RADIUS, themeModule.getColor(0).getRGB());

        drawFilledRoundedRect(context, x, y, width, totalHeight, CORNER_RADIUS, new Color(18, 18, 18, 255).getRGB());

        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();

        float scale = 2.0f;
        matrixStack.scale(scale, scale, 1.0f);
        int scaledX = (int) ((x + (width / 2)) / scale);
        int scaledY = (int) ((y + 6) / scale);
        int scaledXRight = (int) ((x + getWidth() - 20) / scale);

        context.drawCenteredTextWithShadow(mc.textRenderer, category.name, scaledX, scaledY, ThemeUtils.getTextColor().getRGB());
        context.drawText(mc.textRenderer, extended ? "-" : "+", scaledXRight, scaledY, ThemeUtils.getTextColor().getRGB(), true);

        matrixStack.pop();

        if (!extended) return;

        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.render(context, mouseX, mouseY, delta);
        }
    }

    private void drawFilledRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        int subSamples = 4;
        double subPixelSize = 1.0 / subSamples;

        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + width, y + height - radius, color);
        
        for (int cornerX = 0; cornerX <= 1; cornerX++) {
            for (int cornerY = 0; cornerY <= 1; cornerY++) {
                int centerX = (cornerX == 0) ? x + radius : x + width - radius - 1;
                int centerY = (cornerY == 0) ? y + radius : y + height - radius - 1;

                for (int i = 0; i < radius; i++) {
                    for (int j = 0; j < radius; j++) {
                        int pixelX = centerX + (cornerX == 0 ? -radius + i : i);
                        int pixelY = centerY + (cornerY == 0 ? -radius + j : j);

                        int filledSubPixels = 0;
                        for (int subX = 0; subX < subSamples; subX++) {
                            for (int subY = 0; subY < subSamples; subY++) {
                                double subPixelCenterX = pixelX + subX * subPixelSize + subPixelSize / 2.0;
                                double subPixelCenterY = pixelY + subY * subPixelSize + subPixelSize / 2.0;

                                double distToCornerCenter = Math.sqrt(
                                        Math.pow(subPixelCenterX - centerX, 2) +
                                                Math.pow(subPixelCenterY - centerY, 2)
                                );

                                if (distToCornerCenter <= radius) {
                                    filledSubPixels++;
                                }
                            }
                        }

                        if (filledSubPixels > 0) {
                            int alpha = (int) (255 * ((double) filledSubPixels / (subSamples * subSamples)));
                            int subSampledColor = (color & 0x00FFFFFF) | (alpha << 24);
                            context.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, subSampledColor);
                        }
                    }
                }
            }
        }
    }

    private void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        for (int i = 0; i <= 1; i++) {
            drawFilledRoundedRect(context, x - i, y - i, width + (i * 2), height + (i * 2), radius + i, color);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            switch (button) {
                case 0: {
                    dragging = true;
                    dragX = (int) (mouseX - x);
                    dragY = (int) (mouseY - y);
                    break;
                }
                case 1: {
                    extended = !extended;
                    break;
                }
            }
        }

        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.mouseClicked(mouseX, mouseY, button);
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    public void updateButtons() {
        int offset = height;

        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.offset = offset;
            offset += height;

            if (moduleButton.extended) {
                for (RenderableSetting renderableSetting : moduleButton.settings) {
                    offset += height;
                }
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
        }

        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.mouseReleased(mouseX, mouseY, button);
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
    }

    public void updatePosition(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }
}