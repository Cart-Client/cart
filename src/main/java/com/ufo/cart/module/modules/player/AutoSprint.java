package com.ufo.cart.module.modules.player;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.event.listeners.TickListener;

public final class AutoSprint extends Module implements TickListener {
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
        if (!omni.getValue()) {
            if (mc.player != null){
                if (mc.player.forwardSpeed > 0) {
                    mc.player.setSprinting(true);
                }
            }
        } else {
            if (mc.player != null) {
                if (mc.player.forwardSpeed > 0) {
                    mc.player.setSprinting(true);
                }
                if (mc.player.sidewaysSpeed > 0) {
                    mc.player.setSprinting(true);
                }
                if (mc.player.forwardSpeed < 0) {
                    mc.player.setSprinting(true);
                }
            }
        }
    }
    BooleanSetting omni = new BooleanSetting("Omni", false);
}