package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.PacketSendEvent;

public interface PacketSendListener extends EventListener {
    void onPacketSend(final PacketSendEvent event);
}
