package com.ufo.cart.gui.components.settings;

import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.utils.render.TextRenderer;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public final class CheckBox extends RenderableSetting {
    private final BooleanSetting setting;

    public CheckBox(ModuleButton parent, Setting setting, int offset) {
        super(parent, setting, offset);
        this.setting = (BooleanSetting) setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        TextRenderer.drawMinecraftText(
                setting.getName(),
                context,
                parentX() + 6,
                parentY() + parentOffset() + offset + 6,
                Color.WHITE.getRGB()
        );


        int boxX1 = (parentX() + parentWidth()) - 30;
        int boxY1 = (parentY() + parentOffset() + offset) + 5;
        int boxX2 = (parentX() + parentWidth() - 10);
        int boxY2 = (parentY() + parentOffset() + offset + parentHeight()) - 5;


        context.fill(boxX1, boxY1, boxX2, boxY2, Color.darkGray.getRGB());


        int centerX = boxX1 + (boxX2 - boxX1) / 2;
        int centerY = boxY1 + (boxY2 - boxY1) / 2;


        int checkmarkOffsetX = centerX - 5;
        int checkmarkOffsetY = centerY - 6;


        if (setting.getValue()) {
            TextRenderer.drawMinecraftText(
                    "✔",
                    context,
                    checkmarkOffsetX,
                    checkmarkOffsetY,
                    ThemeUtils.getMainColor(255).getRGB()
            );
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            setting.toggle();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
}
