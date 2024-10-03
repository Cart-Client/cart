package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.CameraEvent;

public interface CameraListener extends EventListener {
    void onCamera(final CameraEvent event);
}
