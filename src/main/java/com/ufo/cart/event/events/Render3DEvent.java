package com.ufo.cart.event.events;

import com.ufo.cart.event.Event;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.listeners.Render3DListener;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.util.ArrayList;

public class Render3DEvent extends Event {
    public MatrixStack matrices;
    public float delta;

    public Render3DEvent(final MatrixStack matrices, final float delta, Matrix4f matrix4f) {
        this.matrices = matrices;
        this.delta = delta;
    }

    @Override
    public void callListeners(final ArrayList<EventListener> listeners) {
        listeners.stream().filter(listener -> listener instanceof Render3DListener).map(listener -> (Render3DListener) listener).forEach(listener -> listener.onRender(this));
    }

    @Override
    public Class<?> getClazz() {
        return Render3DListener.class;
    }
}