package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.MouseMoveEvent;

public interface MouseMoveListener extends EventListener {
    void onMouseMove(final MouseMoveEvent event);
}
