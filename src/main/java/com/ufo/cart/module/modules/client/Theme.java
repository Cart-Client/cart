package com.ufo.cart.module.modules.client;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.ModeSetting;
import com.ufo.cart.module.setting.NumberSetting;

import java.awt.*;

public class Theme extends Module {
    public static final NumberSetting red = new NumberSetting("Red", 0, 255, 193, 1);
    public static final NumberSetting green = new NumberSetting("Green", 0, 255, 193, 1);
    public static final NumberSetting blue = new NumberSetting("Blue", 0, 255, 193, 1);
    public static final NumberSetting alpha = new NumberSetting("Alpha", 0, 255, 255, 1);
    public final ModeSetting colorMode = new ModeSetting("Theme", "Custom", "Custom", "Rainbow");

    public Theme() {
        super("Theme", "Changes the client's theme", 0, Category.CLIENT);
        addSettings(red, green, blue, alpha, colorMode);
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        super.onEnable();
    }

    public Color getColorForModule(int index) {
        if (colorMode.getMode().equals("Rainbow")) {
            float hue = (System.currentTimeMillis() % 3600 + (index * 50)) / 3600f;
            return Color.getHSBColor(hue, 1.0f, 1.0f);
        }
        if (colorMode.getMode().equals("Custom")) {
            return new Color(red.getValueInt(), green.getValueInt(), blue.getValueInt(), alpha.getValueInt());
        }
        return Color.white;
    }
}