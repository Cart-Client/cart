package com.ufo.cart.gui.components.settings;

import com.ufo.cart.Client;
import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.module.modules.client.Theme;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.utils.render.TextRenderer;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public final class Slider extends RenderableSetting {
    public boolean dragging;
    private final NumberSetting setting;
    private double targetValue;
    private static final int TRACK_HEIGHT = 6;
    private static final int HANDLE_SIZE = 8;
    private static final double SMOOTH_FACTOR = 0.3;
    private static final int HITBOX_EXPAND = 6;
    Theme themeModule = Client.getInstance().getModuleManager().getModule(Theme.class);

    public Slider(ModuleButton parent, Setting setting, int offset) {
        super(parent, setting, offset);
        this.setting = (NumberSetting) setting;
        this.targetValue = ((NumberSetting) setting).getValue();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if(dragging){
            double currentValue = setting.getValue();
            double smoothedValue = interpolate(currentValue, targetValue, SMOOTH_FACTOR);
            setting.setValue(smoothedValue);
        }

        float widthPercentage = (float) ((setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin()));

        int switchX = parentX() + 5;
        int switchY = parentY() + offset + parentOffset() + 22;
        int switchWidth = parentWidth() - 10;
        int handleX = switchX + (int) (switchWidth * widthPercentage);
        int handleY = switchY;

        Color sliderColor = themeModule.getColor(0);

        context.fill(switchX, switchY, switchX + switchWidth, switchY + TRACK_HEIGHT, new Color(50, 50, 50).getRGB());

        context.fill(switchX, switchY, handleX, switchY + TRACK_HEIGHT, sliderColor.getRGB());

        drawSquareHandle(context, handleX - HANDLE_SIZE / 2, handleY + TRACK_HEIGHT / 2 - HANDLE_SIZE / 2, HANDLE_SIZE, sliderColor);

        int textX = parentX() + parentWidth() / 2;
        int textY = parentY() + offset + parentOffset() + 4;
        TextRenderer.drawCenteredMinecraftText(setting.getName() + ": " + setting.getValue(), context, textX, textY, Color.WHITE.getRGB(), true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        int switchX = parentX() + 5;
        int switchY = parentY() + offset + parentOffset() + 22;
        int switchWidth = parentWidth() - 10;
        int handleX = switchX + (int) (switchWidth * ((setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin())));
        int handleY = switchY;

        int hitboxX1 = handleX - (HANDLE_SIZE / 2) - HITBOX_EXPAND;
        int hitboxX2 = handleX + (HANDLE_SIZE / 2) + HITBOX_EXPAND;
        int hitboxY1 = handleY - HITBOX_EXPAND;
        int hitboxY2 = handleY + TRACK_HEIGHT + HITBOX_EXPAND;

        if (mouseX >= hitboxX1 && mouseX <= hitboxX2 && mouseY >= hitboxY1 && mouseY <= hitboxY2 && button == 0) {
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
            this.targetValue = propertyValue;
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