package com.ufo.cart.event.events;

import com.ufo.cart.event.CancellableEvent;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.MouseMoveListener;

import java.util.ArrayList;

public class MouseMoveEvent extends CancellableEvent {
    public long handle;
    public double x;
    public double y;

    public MouseMoveEvent(final long handle, final double x, final double y) {
        this.handle = handle;
        this.x = x;
        this.y = y;
    }

    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof MouseMoveListener).map(listener -> (MouseMoveListener) listener).forEach(listener -> listener.onMouseMove(this));
    }

    @Override
    public Class<?> getClazz() {
        return MouseMoveListener.class;
    }
}