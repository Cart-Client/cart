package com.ufo.cart.module.modules.client;

import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.module.setting.BooleanSetting;

public class Theme extends Module {
    public static final NumberSetting red = new NumberSetting("Red", 0, 255, 193, 1);
    public static final NumberSetting green = new NumberSetting("Green", 0, 255, 193, 1);
    public static final NumberSetting blue = new NumberSetting("Blue", 0, 255, 193, 1);
    public static final NumberSetting alpha = new NumberSetting("Alpha", 0, 255, 255, 1);



    public Theme() {
        super("Theme", "Changes the client's theme", 0, Category.CLIENT);
        addSettings(red, green, blue, alpha);
    }

    @Override
    public void onEnable() {
        this.toggle();
        super.onEnable();
    }
}
