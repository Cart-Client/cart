package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.ResolutionChangeListener;
import net.minecraft.client.util.Window;

import java.util.ArrayList;

public class ResolutionChangedEvent extends Event {
    public Window window;

    public ResolutionChangedEvent(final Window window) {
        this.window = window;
    }

    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof ResolutionChangeListener).map(listener -> (ResolutionChangeListener) listener).forEach(listener -> listener.onResolutionChange(this));
    }

    @Override
    public Class<?> getClazz() {
        return ResolutionChangeListener.class;
    }
}