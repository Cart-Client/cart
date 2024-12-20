package com.ufo.cart.gui.components.settings;

import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.module.setting.RangeSetting;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.utils.math.MathUtils;
import com.ufo.cart.utils.render.TextRenderer;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class RangeSlider extends RenderableSetting {
    private boolean draggingMin;
    private boolean draggingMax;
    private final RangeSetting setting;
    private double targetMin;
    private double targetMax;
    private static final int TRACK_HEIGHT = 6;
    private static final int HANDLE_SIZE = 8;
    private static final double SMOOTH_FACTOR = 0.3;
    private static final int HITBOX_EXPAND = 6;

    public RangeSlider(ModuleButton parent, Setting setting, int offset) {
        super(parent, setting, offset);
        this.setting = (RangeSetting) setting;
        this.targetMin = ((RangeSetting) setting).getValueMin();
        this.targetMax = ((RangeSetting) setting).getValueMax();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // --- Smooth transition logic ---
        if (draggingMin || draggingMax) {
            double currentMin = setting.getValueMin();
            double currentMax = setting.getValueMax();
            double smoothedMin = interpolate(currentMin, targetMin, SMOOTH_FACTOR);
            double smoothedMax = interpolate(currentMax, targetMax, SMOOTH_FACTOR);
            setting.setValueMin(smoothedMin);
            setting.setValueMax(smoothedMax);
        }

        double range = setting.getMax() - setting.getMin();
        double offsetXMin = (setting.getValueMin() - setting.getMin()) / range;
        double offsetXMax = (setting.getValueMax() - setting.getMin()) / range;


        int switchX = parentX() + 5;
        int switchY = parentY() + offset + parentOffset() + 22;
        int switchWidth = parentWidth() - 10;

        int minHandleX = switchX + (int) (switchWidth * offsetXMin);
        int maxHandleX = switchX + (int) (switchWidth * offsetXMax);

        Color sliderColor = ThemeUtils.getMainColor(255);

        context.fill(switchX, switchY, switchX + switchWidth, switchY + TRACK_HEIGHT, new Color(50, 50, 50).getRGB());

        context.fill(minHandleX, switchY, maxHandleX, switchY + TRACK_HEIGHT, sliderColor.getRGB());


        drawSquareHandle(context, minHandleX - HANDLE_SIZE / 2, switchY + TRACK_HEIGHT / 2 - HANDLE_SIZE / 2, HANDLE_SIZE, sliderColor);
        drawSquareHandle(context, maxHandleX - HANDLE_SIZE / 2, switchY + TRACK_HEIGHT / 2 - HANDLE_SIZE / 2, HANDLE_SIZE, sliderColor);


        int textX = parentX() + parentWidth() / 2;
        int textY = parentY() + offset + parentOffset() + 4;
        TextRenderer.drawCenteredMinecraftText(setting.getName() + ": " + setting.getValueMin() + " - " + setting.getValueMax(), context, textX, textY, Color.WHITE.getRGB(), true);
    }

    private void slide(double mouseX) {
        double switchX = parentX() + 5;
        int switchWidth = parentWidth() - 10;
        double relativeX = mouseX - switchX;
        double range = setting.getMax() - setting.getMin();
        double relativeMin = MathHelper.clamp(relativeX / switchWidth, 0, 1);
        double relativeMax = MathHelper.clamp((relativeX + setting.getIncrement()) / switchWidth, 0, 1);

        double valueMin = MathUtils.roundToDecimal(relativeMin * range + setting.getMin(), setting.getIncrement());
        double valueMax = MathUtils.roundToDecimal(relativeMax * range + setting.getMin(), setting.getIncrement());

        if (draggingMin && !draggingMax) {
            targetMin = valueMin;
        } else if (draggingMax && !draggingMin) {
            targetMax = valueMax;
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        int switchX = parentX() + 5;
        int switchY = parentY() + offset + parentOffset() + 22;
        int switchWidth = parentWidth() - 10;

        if (mouseX >= switchX && mouseX <= switchX + switchWidth && mouseY >= switchY && mouseY <= switchY + TRACK_HEIGHT && button == 0) {
            double relativeX = mouseX - switchX;
            double range = setting.getMax() - setting.getMin();
            double minHandleX = switchWidth * (setting.getValueMin() - setting.getMin()) / range;
            double maxHandleX = switchWidth * (setting.getValueMax() - setting.getMin()) / range;

            double hitboxMinX1 = minHandleX - HANDLE_SIZE / 2 - HITBOX_EXPAND;
            double hitboxMinX2 = minHandleX + HANDLE_SIZE / 2 + HITBOX_EXPAND;
            double hitboxMaxX1 = maxHandleX - HANDLE_SIZE / 2 - HITBOX_EXPAND;
            double hitboxMaxX2 = maxHandleX + HANDLE_SIZE / 2 + HITBOX_EXPAND;
            double hitboxY1 = switchY - HITBOX_EXPAND;
            double hitboxY2 = switchY + TRACK_HEIGHT + HITBOX_EXPAND;


            if (mouseX >= switchX + hitboxMinX1 && mouseX <= switchX + hitboxMinX2 && mouseY >= hitboxY1 && mouseY <= hitboxY2) {
                draggingMin = true;
            } else if (mouseX >= switchX + hitboxMaxX1 && mouseX <= switchX + hitboxMaxX2 && mouseY >= hitboxY1 && mouseY <= hitboxY2) {
                draggingMax = true;
            }
            slide(mouseX);
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingMin || draggingMax) {
            draggingMin = false;
            draggingMax = false;
        }
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingMin || draggingMax) {
            slide(mouseX);
        }
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void drawSquareHandle(DrawContext context, int x, int y, int size, Color color) {
        context.fill(x, y, x + size, y + size, color.getRGB());
    }

    private double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }
}