package com.ufo.cart.module.modules.client;

import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.NumberSetting;

public class Theme extends Module {
    public static final NumberSetting red = new NumberSetting("Red", 0, 255, 164, 1);
    public static final NumberSetting green = new NumberSetting("Green", 0, 255, 0, 1);
    public static final NumberSetting blue = new NumberSetting("Blue", 0, 255, 0, 1);

    public Theme() {
        super("Theme", "Changes the clients theme", 0, Category.CLIENT);
        addSettings(red, green, blue);
    }

    @Override
    public void onEnable() {
        this.toggle();
        super.onEnable();
    }
}
