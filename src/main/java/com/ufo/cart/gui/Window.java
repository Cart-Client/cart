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
    public Window(int x, int y, int width, int height, Category category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.dragging = false;
        this.extended = true;
        this.height = height;
        this.category = category;

        int offset = height;
        for(Module module : Client.INSTANCE.getModuleManager().getModulesInCategory(category)) {
            moduleButtons.add(new ModuleButton(this, module, offset));
            offset += height;
        }

    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(x, y, x + width, y + height, ThemeUtils.getMainColor(255).darker().getRGB());
        MatrixStack matrixStack = context.getMatrices();

        matrixStack.push();

        float scale = 2.0f;
        matrixStack.scale(scale, scale, 1.0f);
        int scaledX = (int) ((x + (width / 2)) / scale);
        int scaledY = (int) ((y + 6) / scale);
        int scaledXRight = (int) ((x + getWidth() - 20) / scale);

        context.drawCenteredTextWithShadow(mc.textRenderer, category.name, scaledX, scaledY, Color.WHITE.getRGB());

        context.drawText(mc.textRenderer, extended ? "+" : "-", scaledXRight, scaledY, Color.white.getRGB(), true);

        matrixStack.pop();
        if(!extended) return;

        for(ModuleButton moduleButton : moduleButtons) {
            moduleButton.render(context, mouseX, mouseY, delta);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if(isHovered(mouseX, mouseY)) {
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

        for(ModuleButton moduleButton : moduleButtons) {
            moduleButton.mouseClicked(mouseX, mouseY, button);
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for(ModuleButton moduleButton : moduleButtons) {
            moduleButton.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    public void updateButtons() {
        int offset = height;

        for(ModuleButton moduleButton : moduleButtons) {
            moduleButton.offset = offset;
            offset += height;

            if(moduleButton.extended) {
                for(RenderableSetting renderableSetting : moduleButton.settings) {
                    offset += height;
                }
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if(button == 0 && dragging) {
            dragging = false;
        }

        for(ModuleButton moduleButton : moduleButtons) {
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
        if(dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }
}