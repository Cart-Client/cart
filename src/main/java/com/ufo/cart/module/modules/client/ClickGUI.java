package com.ufo.cart.module.modules.client;

import com.ufo.cart.gui.ClickImgui;
import com.ufo.cart.imgui.ImGuiImpl;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.ModeSetting;
import org.lwjgl.glfw.GLFW;

public class ClickGUI extends Module {
    private boolean didInitImgui = false;
    public static final ModeSetting clickguiMode = new ModeSetting("Mode", "ImGUI", "ImGUI");

    public ClickGUI() {
        super("ClickGui", "Click Gui", GLFW.GLFW_KEY_RIGHT_SHIFT, Category.CLIENT);
        this.addSetting(clickguiMode);
    }

    @Override
    public void onEnable() {
        switch (clickguiMode.getMode()) {
            case "ImGUI":
                if (mc.getWindow() != null && !didInitImgui) {
                    ImGuiImpl.initialize(mc.getWindow().getHandle());
                    didInitImgui = true;
                }
                mc.setScreen(new ClickImgui());
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
