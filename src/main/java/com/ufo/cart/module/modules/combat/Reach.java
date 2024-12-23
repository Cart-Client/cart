package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.RangeSetting;

import static com.ufo.cart.utils.math.RandomUtil.random;

public class Reach extends Module implements TickListener {

    public double reach = 3.0;
    public final RangeSetting range = new RangeSetting( "Range", 3.0, 6.0, 3.0, 3.5, 0.1);

    public Reach() {
        super("Reach", "Gives you very long arms.", 0, Category.COMBAT);
        addSettings(range);
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
            double max = range.getValueMax();
            double min = range.getValueMin();

            if (max <= min) {
                double temp = max;
                max = min;
                min = temp;
            }

            reach = random.nextDouble(min, max);
        }
    }
}
