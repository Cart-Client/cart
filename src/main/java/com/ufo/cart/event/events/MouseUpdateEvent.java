package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.MouseUpdateListener;

import java.util.ArrayList;

public class MouseUpdateEvent extends Event {
    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof MouseUpdateListener).map(listener -> (MouseUpdateListener) listener).forEach(MouseUpdateListener::onMouseUpdate);
    }

    @Override
    public Class<?> getClazz() {
        return MouseUpdateListener.class;
    }
}