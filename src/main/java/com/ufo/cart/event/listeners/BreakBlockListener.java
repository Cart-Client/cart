

package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.BreakBlockEvent;

public interface BreakBlockListener extends EventListener {
    void onBlockBreak(final BreakBlockEvent p0);
}
