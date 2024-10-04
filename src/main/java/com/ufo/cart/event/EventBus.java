package com.ufo.cart.event;

import com.ufo.cart.Client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public final class EventBus {
    private final HashMap<Class<?>, ArrayList<PriorityListener>> listeners;

    public EventBus() {
        this.listeners = new HashMap<>();
    }

    public static void postEvent(final Event event) {
        final EventBus eventBus = Client.INSTANCE.getEVENT_BUS();
        if (eventBus != null) eventBus.callListeners(event);
    }

    private void callListeners(final Event event) {
        final ArrayList<PriorityListener> priorityListeners = this.listeners.get(event.getClazz());
        if (priorityListeners == null || priorityListeners.isEmpty()) return;

        final ArrayList<PriorityListener> sortedListeners = new ArrayList<>(priorityListeners);
        sortedListeners.removeIf(Objects::isNull);
        sortedListeners.sort(Comparator.comparingInt(PriorityListener::getPriority)); // Sort by priority

        // Extract the actual listeners and collect them into a list
        ArrayList<EventListener> actualListeners = new ArrayList<>();
        sortedListeners.forEach(priorityListener -> actualListeners.add(priorityListener.getListener()));

        // Now pass the list of actual listeners to the event
        event.callListeners(actualListeners);
    }

    public void registerPriorityListener(final Class<?> type, final EventListener listener) {
        this.register(type, listener, 0);
    }

    public void register(final Class<?> type, final EventListener listener, final int priority) {
        final ArrayList<PriorityListener> list = this.listeners.computeIfAbsent(type, k -> new ArrayList<>());
        list.add(new PriorityListener(listener, priority));
    }

    public void unregister(final Class<?> type, final EventListener listener) {
        final ArrayList<PriorityListener> list = this.listeners.get(type);
        if (list != null) {
            // Unregister the actual listener (wrapped in PriorityListener)
            list.removeIf(priorityListener -> priorityListener.getListener().equals(listener));
        }
    }
}
