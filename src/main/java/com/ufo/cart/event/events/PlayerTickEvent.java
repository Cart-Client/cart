package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.PlayerTickListener;

import java.util.ArrayList;

public class PlayerTickEvent extends Event {
    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof PlayerTickListener).map(listener -> (PlayerTickListener) listener).forEach(PlayerTickListener::onPlayerTick);
    }

    @Override
    public Class<?> getClazz() {
        return PlayerTickListener.class;
    }
}