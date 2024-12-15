package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.Render2DListener;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;

public class Render2DEvent extends Event {
    public DrawContext ctx;
    public RenderTickCounter delta;

    public Render2DEvent(final DrawContext context, final RenderTickCounter delta) {
        this.ctx = context;
        this.delta = delta;
    }

    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof Render2DListener).map(listener -> (Render2DListener) listener).forEach(listener -> listener.onRender2D(this));
    }

    @Override
    public Class<?> getClazz() {
        return Render2DListener.class;
    }
}
