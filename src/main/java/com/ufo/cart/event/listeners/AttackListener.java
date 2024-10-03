package com.ufo.cart.event.listeners;

import com.ufo.cart.event.EventListener;
import com.ufo.cart.event.events.AttackEvent;

public interface AttackListener extends EventListener {
    void onAttack(final AttackEvent event);
}
