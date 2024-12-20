package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.gui.components.settings.RenderableSetting;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
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
    private static final int BORDER_RADIUS = 8;

    private double animationScale;
    private static final double ANIM_SPEED = 0.1;



    public Window(int x, int y, int width, int height, Category category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.dragging = false;
        this.extended = true;
        this.height = height;
        this.category = category;
        this.animationScale = 0.0;

        int offset = height;
        for (Module module : Client.INSTANCE.getModuleManager().getModulesInCategory(category)) {
            moduleButtons.add(new ModuleButton(this, module, offset));
            offset += height;
        }
    }


    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // --- Animate Window Scale ---
        animationScale = Math.min(1, animationScale + ANIM_SPEED);
        double animX = x - (width * (1 - animationScale) / 2.0);
        double animY = y - (height * (1 - animationScale) / 2.0);
        double animWidth = width * animationScale;
        double animHeight = height * animationScale;

        int totalHeight = height;
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


        drawRoundedRect(context, (int)animX - 2, (int)animY - 2, (int)animWidth + 4, totalHeight + 4, BORDER_RADIUS, ThemeUtils.getBorderColor().getRGB());


        context.fill((int)animX, (int)animY, (int)(animX + animWidth), (int)(animY + animHeight), new Color(15, 15, 15, 220).getRGB());


        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();

        float scale = 2.0f;
        matrixStack.scale(scale, scale, 1.0f);
        int scaledX = (int) (((int)animX + (animWidth / 2)) / scale);
        int scaledY = (int) (((int)animY + 6) / scale);
        int scaledXRight = (int) (((int)animX + getWidth() - 20) / scale);


        context.drawCenteredTextWithShadow(mc.textRenderer, category.name, scaledX, scaledY, ThemeUtils.getTextColor().getRGB());
        context.drawText(mc.textRenderer, extended ? "-" : "+", scaledXRight, scaledY, ThemeUtils.getTextColor().getRGB(), true);


        matrixStack.pop();

        if (!extended) return;

        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.render(context, mouseX, mouseY, delta);
        }
    }


    private void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + width, y + height - radius, color);


        context.fill(x, y, x + radius, y + radius, color);
        context.fill(x + width - radius, y, x + width, y + radius, color);
        context.fill(x, y + height - radius, x + radius, y + height, color);
        context.fill(x + width - radius, y + height - radius, x + width, y + height, color);

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