package com.ufo.cart.event.events;

import com.ufo.cart.event.CancellableEvent;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.ItemUseListener;

import java.util.ArrayList;

public class ItemUseEvent extends CancellableEvent {
    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof ItemUseListener).map(listener -> (ItemUseListener) listener).forEach(listener -> listener.onItemUse(this));
    }

    @Override
    public Class<?> getClazz() {
        return ItemUseListener.class;
    }
}
