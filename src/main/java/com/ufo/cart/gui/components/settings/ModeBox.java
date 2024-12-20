package com.ufo.cart.gui.components.settings;

import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.module.setting.ModeSetting;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.utils.render.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public final class ModeBox extends RenderableSetting {
    public final ModeSetting setting;

    public ModeBox(ModuleButton parent, Setting setting, int offset) {
        super(parent, setting, offset);
        this.setting = (ModeSetting) setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        TextRenderer.drawMinecraftText(setting.getName() + ": " + setting.getMode(), context, parentX() + 6, (parentY() + parentOffset() + offset) + 6, Color.WHITE.getRGB(), true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            setting.cycle();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
}