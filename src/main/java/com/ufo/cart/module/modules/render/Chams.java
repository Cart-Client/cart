package com.ufo.cart.module.modules.render;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;

public class Chams extends Module {
    public Chams() {
        super("Chams", "Render players through walls.", 0, Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
    }
}
