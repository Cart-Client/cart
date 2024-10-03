package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.PacketReceiveEvent;

public interface PacketReceiveListener extends EventListener {
    void onPacketReceive(final PacketReceiveEvent event);
}
