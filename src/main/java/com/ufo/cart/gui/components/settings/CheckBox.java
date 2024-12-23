package com.ufo.cart.gui.components.settings;

import com.ufo.cart.Client;
import com.ufo.cart.gui.components.ModuleButton;
import com.ufo.cart.module.modules.client.Theme;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.utils.render.TextRenderer;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public final class CheckBox extends RenderableSetting {

    private final BooleanSetting setting;
    private boolean targetState;
    private float sliderXAnim;
    private static final float ANIM_SPEED = 0.2f;
    Theme themeModule = Client.getInstance().getModuleManager().getModule(Theme.class);

    public CheckBox(ModuleButton parent, Setting setting, int offset) {
        super(parent, setting, offset);
        this.setting = (BooleanSetting) setting;
        this.targetState = ((BooleanSetting) setting).getValue();
        this.sliderXAnim = ((BooleanSetting) setting).getValue() ? 1.0f : 0.0f;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        TextRenderer.drawMinecraftText(
                setting.getName(),
                context,
                parentX() + 6,
                parentY() + parentOffset() + offset + 6,
                Color.WHITE.getRGB(),
                true
        );

        int switchWidth = 30;
        int switchHeight = 16;
        int switchX = (parentX() + parentWidth()) - 35;
        int switchY = (parentY() + parentOffset() + offset) + 6;

        int baseColor = Color.DARK_GRAY.getRGB();
        context.fill(switchX, switchY, switchX + switchWidth, switchY + switchHeight, baseColor);

        if(this.targetState != setting.getValue()){
            setting.setValue(targetState);
        }

        float targetX = targetState ? 1.0f : 0.0f;

        sliderXAnim = interpolate(sliderXAnim, targetX, ANIM_SPEED);


        int sliderX = switchX + (int) ( (switchWidth/2) * sliderXAnim);
        int sliderSize = switchWidth / 2;


        Color sliderColor = setting.getValue() ? themeModule.getColor(0)) : Color.LIGHT_GRAY;
        int glowColor = new Color(sliderColor.getRed(), sliderColor.getGreen(), sliderColor.getBlue(), 70).getRGB();
        int glowOffset = 2;

        context.fill(sliderX - glowOffset, switchY + 1 - glowOffset, sliderX + sliderSize - 1 + glowOffset, switchY + switchHeight - 1 + glowOffset, glowColor);
        context.fill(sliderX, switchY + 1, sliderX + sliderSize - 1, switchY + switchHeight - 1, sliderColor.getRGB());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        int switchWidth = 30;
        int switchHeight = 16;
        int switchX = (parentX() + parentWidth()) - 35;
        int switchY = (parentY() + parentOffset() + offset) + 6;

        if (mouseX >= switchX && mouseX <= switchX + switchWidth && mouseY >= switchY && mouseY <= switchY + switchHeight) {
            targetState = !setting.getValue();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }


    private float interpolate(float oldValue, float newValue, float interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }
}