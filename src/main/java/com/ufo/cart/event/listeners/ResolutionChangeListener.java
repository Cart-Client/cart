package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.ResolutionChangedEvent;

public interface ResolutionChangeListener extends EventListener {
    void onResolutionChange(final ResolutionChangedEvent p0);
}
