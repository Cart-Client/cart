package com.ufo.cart.module.modules.player;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.NumberSetting;

import static com.ufo.cart.utils.math.MathUtils.random;

public final class JumpReset extends Module implements TickListener {

    public final NumberSetting chance = new NumberSetting("Chance", 0, 100, 50, 1);

    public JumpReset() {
        super("Jump Reset", "Jumps when hit to reduce incoming knock back.", 0, Category.PLAYER);

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
        assert mc.player != null;
        if (random.nextInt(1, 100) <= chance.getValueInt()) {
            if (mc.currentScreen != null)
                return;

            if (mc.player.isUsingItem())
                return;

            if (mc.player.hurtTime == 0)
                return;

            if (mc.player.hurtTime == mc.player.maxHurtTime)
                return;

            if (!mc.player.isOnGround())
                return;

            if (mc.player.hurtTime == 9 && random.nextInt(1, 100) <= chance.getValueInt()) {
                mc.player.jump();
            }
        }
    }
}