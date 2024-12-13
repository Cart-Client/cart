package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.RangeSetting;

import static com.ufo.cart.utils.math.RandomUtil.random;

public class Reach extends Module implements TickListener {

    private final RangeSetting extendLength = new RangeSetting("Extend Length", 0.1, 3, 0.1, 0.1, 0.1);

    public Reach() {
        super("Reach", "Gives you longer arms", 0, Category.COMBAT);
        addSettings(extendLength);
    }

    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        super.onEnable();
    }

    public void onDisable() {
        this.eventBus.unregister(TickListener.class, this);
        super.onDisable();
    }

    private double reach;

    @Override
    public void onTick() {
        if (mc.player != null) {
            int min = extendLength.getValueMinInt();
            int max = extendLength.getValueMaxInt();
            if (max > min) {
                reach = random.nextInt(max - min) + min;
            } else {
                reach = min;
            }
        }
    }

    public double getReach() {
        return reach;
    }
}
