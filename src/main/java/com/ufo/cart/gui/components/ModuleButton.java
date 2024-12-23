package com.ufo.cart.gui.components;

import com.ufo.cart.Client;
import com.ufo.cart.gui.components.settings.*;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.modules.client.Theme;
import com.ufo.cart.module.setting.*;
import com.ufo.cart.utils.render.TextRenderer;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;
import com.ufo.cart.gui.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class ModuleButton {
    public List<RenderableSetting> settings = new ArrayList<>();
    public Window parent;
    public Module module;
    public int offset;
    public boolean extended;
    public int settingOffset;
    private Color animatedColor;
    private static final double ANIM_SPEED = 1.0;
    Theme themeModule = Client.getInstance().getModuleManager().getModule(Theme.class);

    public ModuleButton(Window parent, Module module, int offset) {
        this.parent = parent;
        this.module = module;
        this.offset = offset;
        this.extended = false;
        this.animatedColor = module.isEnabled() ? themeModule.getColor(0) : new Color(25, 25, 25, 220); // Initial state

        setupSettingsAndstuff();
    }

    private void setupSettingsAndstuff() {
        settingOffset = parent.getHeight();
        settings.clear();
        for (Setting setting : module.getSettings()) {
            if (setting.getDependencyBoolSetting() != null) {
                Setting dependency = setting.getDependencyBoolSetting();
                if (dependency instanceof BooleanSetting && ((BooleanSetting) dependency).getValue() != setting.getDependencyBool()) {
                    continue;
                }
            }

            if (setting.dependencyModeSetting() != null) {
                Setting dependency = setting.dependencyModeSetting();
                if (!(dependency instanceof ModeSetting && ((ModeSetting) dependency).isMode(setting.dependencyMode()))) {
                    continue;
                }
            }

            if (setting instanceof BooleanSetting booleanSetting) {
                settings.add(new CheckBox(this, booleanSetting, settingOffset));
            } else if (setting instanceof NumberSetting numberSetting) {
                settings.add(new Slider(this, numberSetting, settingOffset));
            } else if (setting instanceof ModeSetting modeSetting) {
                settings.add(new ModeBox(this, modeSetting, settingOffset));
            } else if (setting instanceof RangeSetting rangeSetting) {
                settings.add(new RangeSlider(this, rangeSetting, settingOffset));
            }
            settingOffset += parent.getHeight();
        }
    }


    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = parent.getX();
        int y = parent.getY() + offset;
        int width = parent.getWidth();
        int height = parent.getHeight();

        Color targetColor = module.isEnabled() ? ThemeUtils.getMainColor(200) : new Color(25, 25, 25, 220);
        animatedColor = interpolateColor(animatedColor, targetColor, ANIM_SPEED);

        context.fill(x, y, x + width, y + height, animatedColor.getRGB());


        TextRenderer.drawCenteredMinecraftText(module.getName(), context, x + (width / 2), y + 8, ThemeUtils.getTextColor().getRGB(), true);

        if (isHovered(mouseX, mouseY)) {
            context.fill(x, y, x + width, y + height, new Color(255, 255, 255, 10).getRGB());
        }


        if (extended) {
            for (RenderableSetting renderableSetting : settings) {
                renderableSetting.render(context, mouseX, mouseY, delta);
            }
        }
    }


    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (extended) {
            for (RenderableSetting renderableSetting : settings) {
                renderableSetting.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            }
            if (button == 1) {
                extended = !extended;
                parent.updateButtons();
            }
        }
        if (extended) {
            for (RenderableSetting renderableSetting : settings) {
                renderableSetting.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        setupSettingsAndstuff();
        for (RenderableSetting renderableSetting : settings) {
            renderableSetting.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.getX()
                && mouseX < parent.getX() + parent.getWidth()
                && mouseY > parent.getY() + offset
                && mouseY < parent.getY() + offset + parent.getHeight();
    }

    private Color interpolateColor(Color startColor, Color endColor, double interpolationValue) {
        int r = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * interpolationValue);
        int g = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * interpolationValue);
        int b = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * interpolationValue);
        int a = (int) (startColor.getAlpha() + (endColor.getAlpha() - startColor.getAlpha()) * interpolationValue);
        return new Color(r, g, b, a);
    }
}