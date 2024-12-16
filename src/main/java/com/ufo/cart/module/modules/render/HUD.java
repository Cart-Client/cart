package com.ufo.cart.module.modules.render;

import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;

public class HUD extends Module {

    public final BooleanSetting ArrayList = new BooleanSetting("Array List", true);

    public HUD() {
        super("HUD", "Various HUD Elements.", 0, Category.RENDER);
        addSettings(ArrayList);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
