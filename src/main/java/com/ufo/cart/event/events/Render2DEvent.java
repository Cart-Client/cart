package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.Render2DListener;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class Render2DEvent extends Event {
    private DrawContext context;
    private int width;
    private int height;

    public Render2DEvent(DrawContext context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
    }

    public DrawContext getContext() {
        return context;
    }

    public void setContext(DrawContext context) {
        this.context = context;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream()
            .filter(listener -> listener instanceof Render2DListener)
            .map(listener -> (Render2DListener) listener)
            .forEach(listener -> listener.onRender2D(this));
    }

    @Override
    public Class<?> getClazz() {
        return Render2DListener.class;
    }
}