package com.ufo.cart.gui.components;

import com.ufo.cart.gui.components.settings.*;
import com.ufo.cart.module.Module;
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

    public ModuleButton(Window parent, Module module, int offset) {
        this.parent = parent;
        this.module = module;
        this.offset = offset;
        this.extended = false;

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
        Color backgroundColor = module.isEnabled() ? ThemeUtils.getMainColor(255) : new Color(35, 35, 35, 175);
        context.fill(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + parent.getHeight() + offset, backgroundColor.getRGB());
        TextRenderer.drawCenteredMinecraftText(module.getName(), context, parent.getX() + (parent.getWidth() / 2), parent.getY() + offset + 8, Color.WHITE.getRGB());

        if (isHovered(mouseX, mouseY)) {
            context.fill(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + parent.getHeight() + offset, new Color(255, 255, 255, 10).getRGB());
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
}
