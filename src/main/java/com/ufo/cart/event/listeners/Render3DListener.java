package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.Render3DEvent;

public interface Render3DListener extends EventListener {
    void onRender(final Render3DEvent event);
}
