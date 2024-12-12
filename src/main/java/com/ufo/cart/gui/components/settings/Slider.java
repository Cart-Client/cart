package com.ufo.cart.gui.components.settings;

import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.utils.math.MathUtils;
import com.ufo.cart.utils.render.TextRenderer;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public final class Slider extends RenderableSetting {
    public boolean dragging;
    private final NumberSetting setting;

    public Slider(ModuleButton parent, Setting setting, int offset) {
        super(parent, setting, offset);
        this.setting = (NumberSetting) setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        float widthPercentage = (float) ((setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin()));

        if (dragging) {
            float percent = Math.min(1, Math.max(0, (mouseX - parentX()) / (float) parentWidth()));
            double propertyValue = interpolate(setting.getMin(), setting.getMax(), percent);
            propertyValue = snapToIncrement(propertyValue, (float) setting.getIncrement());
            setting.setValue(propertyValue);
        }

        int sliderY = parentY() + offset + parentOffset() + 28; // Adjust the Y-coordinate to lower the slider even more
        Color sliderColor = ThemeUtils.getMainColor(255); // Use ThemeUtils for slider color

        // Draw the background of the slider
        context.fill(parentX(), sliderY, parentX() + parentWidth(), sliderY + 4, new Color(50, 50, 50).getRGB());

        // Draw the filled part of the slider
        context.fill(parentX(), sliderY, parentX() + (int) (parentWidth() * widthPercentage), sliderY + 4, sliderColor.getRGB());

        // Draw the handle
        int circleX = parentX() + (int) (parentWidth() * widthPercentage) - 5; // Increase the size of the handle
        drawSquareHandle(context, circleX, parentY() + offset + parentOffset() + 25, 10, sliderColor);

        // Draw the text centered above the slider
        int textX = parentX() + parentWidth() / 2;
        int textY = parentY() + offset + parentOffset() + 10; // Move the text lower
        TextRenderer.drawCenteredMinecraftText(setting.getName() + ": " + setting.getValue(), context, textX, textY, Color.WHITE.getRGB());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            dragging = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (dragging && button == 0) {
            dragging = false;
        }
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            float percent = Math.min(1, Math.max(0, (float) (mouseX - parentX()) / parentWidth()));
            double propertyValue = interpolate(setting.getMin(), setting.getMax(), percent);
            propertyValue = snapToIncrement(propertyValue, (float) setting.getIncrement());
            setting.setValue(propertyValue);
        }
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    private double snapToIncrement(double value, float increment) {
        if (increment == 0) return value;
        return Math.round(value / increment) * increment;
    }

    private void drawSquareHandle(DrawContext context, int x, int y, int size, Color color) {
        context.fill(x, y, x + size, y + size, color.getRGB());
    }
}
