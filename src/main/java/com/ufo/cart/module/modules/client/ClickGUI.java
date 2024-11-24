package com.ufo.cart.module.modules.client;

import com.ufo.cart.gui.ClickGui;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.ModeSetting;
import org.lwjgl.glfw.GLFW;

public class ClickGUI extends Module {
    public static final ModeSetting clickguiMode = new ModeSetting("Mode", "Default", "Default");

    public ClickGUI() {
        super("ClickGui", "Click Gui", GLFW.GLFW_KEY_RIGHT_SHIFT, Category.CLIENT);
        this.addSetting(clickguiMode);
    }

    @Override
    public void onEnable() {
        switch (clickguiMode.getMode()) {
            case "Default":
                mc.setScreen(new ClickGui());
                break;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.setScreen(null);
        super.onDisable();
    }
}