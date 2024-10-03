package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.Render2DEvent;

public interface Render2DListener extends EventListener {
    void onRender2D(final Render2DEvent p0);
}
