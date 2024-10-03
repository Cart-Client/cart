package com.ufo.cart.event.events;

import com.ufo.cart.event.CancellableEvent;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.AttackListener;

import java.util.ArrayList;

public class AttackEvent extends CancellableEvent {
    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof AttackListener).map(listener -> (AttackListener) listener).forEach(listener -> listener.onAttack(this));
    }

    @Override
    public Class<?> getClazz() {
        return AttackListener.class;
    }
}