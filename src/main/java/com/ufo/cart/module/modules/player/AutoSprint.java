package com.ufo.cart.module.modules.player;

import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.event.listeners.TickListener;

public final class AutoSprint extends Module implements TickListener {

    private final BooleanSetting omni = new BooleanSetting("Omni", false);

    public AutoSprint() {
        super("Auto Sprint", "Makes your player sprint automatically.", 0, Category.PLAYER);
        addSetting(omni);
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventBus.unregister(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return; 
        }

        boolean shouldSprint = omni.getValue() 
            ? (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0) 
            : (mc.player.forwardSpeed > 0);

        if (shouldSprint) {
            mc.player.setSprinting(true);
        }
    }
}
