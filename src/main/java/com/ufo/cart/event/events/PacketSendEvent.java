package com.ufo.cart.event.events;

import com.ufo.cart.event.CancellableEvent;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.PacketSendListener;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;

public class PacketSendEvent extends CancellableEvent {
    public Packet<?> packet;

    public PacketSendEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof PacketSendListener).map(listener -> (PacketSendListener) listener).forEach(listener -> listener.onPacketSend(this));
    }

    @Override
    public Class<?> getClazz() {
        return PacketSendListener.class;
    }
}