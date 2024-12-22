package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.TickListener;

import java.util.ArrayList;

public class TickEvent extends Event {
    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream()
                .filter(listener -> listener instanceof TickListener)
                .map(listener -> (TickListener) listener)
                .forEach(TickListener::onTick);
    }

    @Override
    public Class<?> getClazz() {
        return TickListener.class;
    }
}
