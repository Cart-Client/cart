package com.ufo.cart.module.modules.player;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;

public final class JumpReset extends Module implements TickListener {
    public JumpReset() {
        super("Jump Reset", "Jumps when hit to reduce incoming knockback.", 0, Category.PLAYER);
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
        if (mc.player != null) {
            if (mc.player.isOnGround() && mc.player.hurtTime > 0) {
                mc.player.jump();
            }
        }
    }
}