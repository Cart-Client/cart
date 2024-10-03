// 
// Decompiled by the rizzer xd
// 

package com.ufo.cart.event.events;

import com.ufo.cart.event.CancellableEvent;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.BreakBlockListener;

import java.util.ArrayList;

public class BreakBlockEvent extends CancellableEvent {
    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof BreakBlockListener).map(listener -> (BreakBlockListener) listener).forEach(listener -> listener.onBlockBreak(this));
    }

    @Override
    public Class<?> getClazz() {
        return BreakBlockListener.class;
    }
}
