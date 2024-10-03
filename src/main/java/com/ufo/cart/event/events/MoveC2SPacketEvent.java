package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.MoveC2SPacketListener;

import java.util.ArrayList;

public class MoveC2SPacketEvent extends Event {
    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof MoveC2SPacketListener).map(listener -> (MoveC2SPacketListener) listener).forEach(MoveC2SPacketListener::onMoveC2SPacket);
    }

    @Override
    public Class<?> getClazz() {
        return MoveC2SPacketListener.class;
    }
}
