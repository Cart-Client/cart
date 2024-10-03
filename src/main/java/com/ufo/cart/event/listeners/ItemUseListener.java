package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.ItemUseEvent;

public interface ItemUseListener extends EventListener {
    void onItemUse(final ItemUseEvent event);
}
