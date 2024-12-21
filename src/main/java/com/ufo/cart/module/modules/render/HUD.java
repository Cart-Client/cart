package com.ufo.cart.module.modules.render;

import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.ModeSetting;

public class HUD extends Module {

    public final BooleanSetting arrayList = new BooleanSetting("Array List", true);
    public final ModeSetting whichSide = new ModeSetting("Side", "Left", "Left", "Right");

    public HUD() {
        super("HUD", "Various HUD Elements.", 0, Category.RENDER);
        addSettings(arrayList, whichSide, colorMode);
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
